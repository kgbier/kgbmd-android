package com.kgbier.kgbmd.data.imdb.operation

import com.kgbier.kgbmd.data.imdb.model.TitleInfo
import okio.BufferedSource
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import org.jsoup.Jsoup

class TitleInfoParser(private val source: BufferedSource) {

    companion object {
        val START_START_TAG by lazy { "<".encodeUtf8() }
        val END_START_TAG by lazy { ">".encodeUtf8() }
        val START_END_TAG by lazy { "</".encodeUtf8() }
        val END_END_TAG by lazy { END_START_TAG }

        val H1 by lazy { "<h1".encodeUtf8() }
        val H4 by lazy { "<h4".encodeUtf8() }
        val ANCHOR by lazy { "<a".encodeUtf8() }
        val IMG by lazy { "<img".encodeUtf8() }
        val ATTRIBUTE_VALUE_DELIMETER by lazy { "\"".encodeUtf8() }

        val RATING_SECTION_SEEK by lazy { "class=\"ratingValue".encodeUtf8() }
        val RATING_VALUE_SEEK by lazy { "itemprop=\"ratingValue".encodeUtf8() }
        val RATING_BEST_SEEK by lazy { "itemprop=\"bestRating".encodeUtf8() }
        val RATING_COUNT_SEEK by lazy { "itemprop=\"ratingCount".encodeUtf8() }

        val TITLE_SECTION_SEEK by lazy { "class=\"title_wrapper".encodeUtf8() }
        val TITLE_YEAR_SEEK by lazy { "id=\"titleYear".encodeUtf8() }
        val TITLE_DURATION_SEEK by lazy { "<time datetime".encodeUtf8() }
        val TITLE_RELEASE_DATE_SEEK by lazy { "releaseinfo?ref_=tt_ov_inf".encodeUtf8() }

        val POSTER_SECTION_SEEK by lazy { "class=\"poster".encodeUtf8() }
        val POSTER_ATTRIBUTE_SRC by lazy { "src=".encodeUtf8() }

        val SUMMARY_SECTION_SEEK by lazy { "class=\"plot_summary".encodeUtf8() }
        val SUMMARY_TEXT_SEEK by lazy { "class=\"summary_text".encodeUtf8() }
        val SUMMARY_TEXT_START by lazy { "<div class=\"summary_text\">".encodeUtf8() }
        val SUMMARY_TEXT_END by lazy { "</div>".encodeUtf8() }
        val SUMMARY_CREDIT_START by lazy { "<div class=\"credit_summary_item\">".encodeUtf8() }
        val SUMMARY_CREDIT_END by lazy { "</div>".encodeUtf8() }
    }

    fun getTitleInfo(): TitleInfo? {

        skipOver(RATING_SECTION_SEEK)
        val ratingValue = skipOver(RATING_VALUE_SEEK)?.let { getElementValue() }
        val ratingBest = skipOver(RATING_BEST_SEEK)?.let { getElementValue() }
        val ratingCount = skipOver(RATING_COUNT_SEEK)?.let { getElementValue() }

        skipOver(TITLE_SECTION_SEEK) ?: return null
        skipOver(H1) ?: return null

        val titleName = getBetween(END_START_TAG, START_START_TAG)?.let {
            it.trim().trimTrailingNbsp()
        } ?: return null

        val titleYear = skipOver(TITLE_YEAR_SEEK)?.let {
            skipOver(ANCHOR)
        }?.let {
            getElementValue()
        }

        val duration = skipOver(TITLE_DURATION_SEEK)?.let {
            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER)
        }

        val posterUrl = skipOver(POSTER_SECTION_SEEK)?.let {
            skipOver(IMG)
        }?.let {
            skipOver(POSTER_ATTRIBUTE_SRC)
        }?.let {
            getBetween(ATTRIBUTE_VALUE_DELIMETER, ATTRIBUTE_VALUE_DELIMETER)
        }

        val summaryText = skipOver(SUMMARY_SECTION_SEEK)?.let {
            findIndex(SUMMARY_TEXT_SEEK)
        }?.let {
            getBetween(SUMMARY_TEXT_START, SUMMARY_TEXT_END)
        }?.let(::stripMarkup)?.trim()

        val creditSummary = mutableListOf<TitleInfo.Credit>()
        while (findIndex(SUMMARY_CREDIT_START) != null) {
            getBetween(SUMMARY_CREDIT_START, SUMMARY_CREDIT_END)
                ?.let(::parseCreditSummary)
                ?.let {
                    creditSummary.add(it)
                }
        }

        return TitleInfo(
            ratingValue,
            ratingBest,
            ratingCount,
            titleName,
            titleYear,
            duration,
            posterUrl,
            summaryText,
            creditSummary
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

    private fun stripMarkup(fragment: String): String = Jsoup.parse(fragment).text()

    private fun parseCreditSummary(fragment: String): TitleInfo.Credit? {
        var str = stripMarkup(fragment)
        val pipeIndex = str.indexOf("|")
        if (pipeIndex > 0) str = str.dropLast(str.length - pipeIndex)
        val typedList = str.split(":")
        val creditList = typedList[1].split(",")

        return TitleInfo.Credit(typedList.first().trim(), creditList.map { it.trim() })
    }

    private fun String.trimTrailingNbsp(): String {
        if (indexOf("&n") == -1) return this
        return replace("&nbsp;", "")
    }
}
