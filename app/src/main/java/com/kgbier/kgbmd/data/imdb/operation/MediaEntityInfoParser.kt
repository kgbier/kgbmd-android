package com.kgbier.kgbmd.data.imdb.operation

import com.kgbier.kgbmd.data.imdb.model.NameInfo
import com.kgbier.kgbmd.data.imdb.model.TitleInfo
import okio.BufferedSource
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.parser.Parser

class MediaEntityInfoParser(private val source: BufferedSource) {

    companion object {
        val START_START_TAG by lazy { "<".encodeUtf8() }
        val END_START_TAG by lazy { ">".encodeUtf8() }
        val START_END_TAG by lazy { "</".encodeUtf8() }
        val END_END_TAG by lazy { END_START_TAG }

        val H1 by lazy { "<h1".encodeUtf8() }
        val TD by lazy { "<td".encodeUtf8() }
        val H4 by lazy { "<h4".encodeUtf8() }
        val ANCHOR by lazy { "<a".encodeUtf8() }
        val BOLD by lazy { "<b".encodeUtf8() }
        val BREAK by lazy { "<br".encodeUtf8() }
        val IMG by lazy { "<img".encodeUtf8() }
        val SPAN by lazy { "<span".encodeUtf8() }
        val ATTRIBUTE_VALUE_DELIMETER by lazy { "\"".encodeUtf8() }

        val ATTRIBUTE_SRC by lazy { "src=".encodeUtf8() }
        val ATTRIBUTE_ID by lazy { "id=".encodeUtf8() }

        // Name
        val OVERVIEW_SECTION_SEEK by lazy { "id=\"name-overview-widget-layout".encodeUtf8() }
        val OVERVIEW_NAME_SPAN_SEEK by lazy { "<span".encodeUtf8() }
        val NAME_POSTER_SECTION_SEEK by lazy { "id=\"name-poster".encodeUtf8() }

        val FILMOGRAPHY_SECTION_SEEK by lazy { "id=\"filmography\"".encodeUtf8() }
        val FILMOGRAPHY_DATA_CATEGORY by lazy { "data-category=".encodeUtf8() }
        val FILMOGRAPHY_ROW_SEEK by lazy { "class=\"filmo-row".encodeUtf8() }
        val FILMOGRAPHY_ROW_START by lazy { END_START_TAG }
        val FILMOGRAPHY_ROW_END by lazy { "</div".encodeUtf8() }
        val FILMOGRAPHY_YEAR_SPAN_SEEK by lazy { "class=\"year_column".encodeUtf8() }

        // Title
        val TITLE_SECTION_SEEK by lazy { "class=\"TitleHeader__TitleText".encodeUtf8() }
        val TITLE_YEAR_SEEK by lazy { "/releaseinfo".encodeUtf8() }

        val RATING_VALUE_SEEK by lazy { "AggregateRatingButton__RatingScore".encodeUtf8() }

        val RATING_COUNT_SEEK by lazy { "AggregateRatingButton__TotalRatingAmount".encodeUtf8() }
        val TITLE_DURATION_SEEK by lazy { "<time datetime".encodeUtf8() }

        val TITLE_RELEASE_DATE_SEEK by lazy { "releaseinfo?ref_=tt_ov_inf".encodeUtf8() }
        val TITLE_POSTER_SECTION_SEEK by lazy { "Media__MediaParent".encodeUtf8() }

        val SUMMARY_SECTION_SEEK by lazy { "GenresAndPlot__TextContainerBreakpointXL".encodeUtf8() }
        val SUMMARY_TEXT_SEEK by lazy { "class=\"summary_text".encodeUtf8() }
        val SUMMARY_TEXT_START by lazy { "<div class=\"summary_text\">".encodeUtf8() }
        val SUMMARY_TEXT_END by lazy { "</div>".encodeUtf8() }
        val SUMMARY_CREDIT_START by lazy { "<div class=\"credit_summary_item\">".encodeUtf8() }
        val SUMMARY_CREDIT_END by lazy { "</div>".encodeUtf8() }

        val CAST_SECTION_SEEK by lazy { "<div class=\"article\" id=\"titleCast\">".encodeUtf8() }
        val CAST_SECTION_START by lazy { "<table class=\"cast_list\">".encodeUtf8() }
        val CAST_SECTION_END by lazy { "</table>".encodeUtf8() }
    }

    fun getTitleInfo(): TitleInfo? {

        skipOver(TITLE_SECTION_SEEK) ?: return null

        val titleName = getBetween(END_START_TAG, START_START_TAG)
            ?.trim()
            ?.replaceNbsp()
            ?.stripMarkup()
            ?: return null

        val titleYear = skipOver(TITLE_YEAR_SEEK)?.let {
            getElementValue()
        }

        val duration = null
//        val duration = skipOver(TITLE_DURATION_SEEK)?.let {
//            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER)
//        }

        val ratingValue = skipOver(RATING_VALUE_SEEK)?.let { getElementValue() }
        val ratingBest = skipOver(SPAN)?.let { getElementValue()?.filter { it.isDigit() } }
        val ratingCount = skipOver(RATING_COUNT_SEEK)?.let { getElementValue() }

        val posterUrl = skipOver(TITLE_POSTER_SECTION_SEEK)?.let {
            skipOver(IMG)
        }?.let {
            skipOver(ATTRIBUTE_SRC)
        }?.let {
            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER)
        }

        val summaryText = skipOver(SUMMARY_SECTION_SEEK)?.let {
            getElementValue()
        }?.stripMarkup()?.trim()

        val creditSummary = mutableListOf<TitleInfo.Credit>()
        while (findIndex(SUMMARY_CREDIT_START) != null) {
            getBetween(SUMMARY_CREDIT_START, SUMMARY_CREDIT_END)
                ?.let(::parseCreditSummary)
                ?.let {
                    creditSummary.add(it)
                }
        }

        val cast = skipOver(CAST_SECTION_SEEK)?.let {
            getBetween(CAST_SECTION_START, CAST_SECTION_END)
        }?.let(::parseCastList) ?: emptyList()

        return TitleInfo(
            ratingValue,
            ratingBest,
            ratingCount,
            titleName,
            titleYear,
            duration,
            posterUrl,
            summaryText,
            creditSummary,
            cast,
        )
    }

    private fun parseCastList(fragment: String): List<TitleInfo.CastMember>? {
        val nodes: MutableList<Node> = Parser.parseXmlFragment(fragment, "") ?: return null
        return nodes.filterIsInstance<Element>()
            .map { it.getElementsByTag("td") }
            .filter { it.size == 4 }
            .map {
                TitleInfo.CastMember(
                    it[0].getElementsByTag("img").firstOrNull()?.attr("loadlate") ?: "",
                    it[1].text() ?: "",
                    it[3].text() ?: "",
                    it[1].getElementsByTag("a").attr("href"),
                )
            }
    }

    private fun parseCreditSummary(fragment: String): TitleInfo.Credit? {
        var str = fragment.stripMarkup()
        val pipeIndex = str.indexOf("|")
        if (pipeIndex > 0) str = str.dropLast(str.length - pipeIndex)
        val typedList = str.split(":")
        val creditList = typedList[1].split(",")

        return TitleInfo.Credit(typedList.first().trim(), creditList.map { it.trim() })
    }

    fun getNameInfo(): NameInfo? {

        skipOver(OVERVIEW_SECTION_SEEK) ?: return null
        skipOver(H1) ?: return null
        skipOver(OVERVIEW_NAME_SPAN_SEEK) ?: return null

        val name = getBetween(END_START_TAG, START_START_TAG)?.trim()
            ?: return null

        val headshotUrl = skipOver(NAME_POSTER_SECTION_SEEK)?.let {
            skipOver(ATTRIBUTE_SRC)
        }?.let {
            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER)
        }

        skipOver(FILMOGRAPHY_SECTION_SEEK)

        val filmography = mutableListOf<NameInfo.Title>()
        while (findIndex(FILMOGRAPHY_ROW_SEEK) != null) {
            parseNameFilmographyRow()?.let {
                filmography.add(it)
            }
        }

        return NameInfo(
            name,
            headshotUrl,
            null,
            filmography,
        )
    }

    private fun parseNameFilmographyRow(): NameInfo.Title? {
        skipOver(FILMOGRAPHY_ROW_SEEK) ?: return null
        skipOver(ATTRIBUTE_ID) ?: return null
        val rowMeta =
            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER) ?: return null
        val (category, titleId) = runCatching { rowMeta.split("-") }
            .mapCatching { Pair(it[0], it[1]) }.getOrNull()
            ?: return null

        skipOver(FILMOGRAPHY_YEAR_SPAN_SEEK) ?: return null
        val year = getElementValue()?.trim()?.replaceNbsp()?.takeIf { it.isNotEmpty() }

        skipOver(BOLD) ?: return null
        skipOver(ANCHOR) ?: return null
        val name = getElementValue() ?: return null

        skipOver(BREAK) ?: return null
        val role = getElementValue()?.trim()?.takeIf { it.isNotEmpty() }

        return NameInfo.Title(
            category,
            titleId,
            name,
            year,
            role,
        )
    }


    private fun findIndex(byteString: ByteString, fromIndex: Long = 0L): Long? {
        val result = source.indexOf(byteString, fromIndex)
        return if (result == -1L) {
            null
        } else {
            result
        }
    }

    private fun skipOver(byteString: ByteString): Unit? {
        val index = findIndex(byteString) ?: return null
        source.skip(index + byteString.size)
        return Unit
    }

    private fun getBetween(lowerString: ByteString, upperString: ByteString): String? {
        val lower = findIndex(lowerString) ?: return null
        val skipBy = lower + lowerString.size
        val upper = findIndex(upperString, skipBy) ?: return null
        source.skip(skipBy)
        return source.readUtf8(upper - skipBy)
    }

    private fun getElementValue(): String? {
        val lower = source.indexOf(END_START_TAG)
        val lowerEnd = lower + END_START_TAG.size
        val upper = findIndex(START_END_TAG, lowerEnd) ?: return null
        var skipBy = 0L
        if (lower >= 0) {
            skipBy = lowerEnd
            source.skip(skipBy)
        }
        return source.readUtf8(upper - skipBy)
    }

    private fun String.stripMarkup(): String = Jsoup.parseBodyFragment(this).text()

    private fun String.replaceNbsp(): String {
        if (indexOf("&n") == -1) return this
        return replace("&nbsp;", " ")
    }
}
