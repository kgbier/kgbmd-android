package com.kgbier.kgbmd.data.imdb.operation

import com.kgbier.kgbmd.data.imdb.model.HotListItem
import okio.BufferedSource

class HotListParser(private val source: BufferedSource) {

    private companion object {
        const val RATING_LOWER = "data-value=\""
        const val RATING_UPPER = "\""

        const val TTID_LOWER = "title/"
        const val TTID_UPPER = "/"

        const val POSTER_IMAGE_LOWER = "<img src=\""
        const val POSTER_IMAGE_UPPER = "\""

        const val NAME_LOWER = ">"
        const val NAME_UPPER = "<"
    }

    fun getList(): List<HotListItem> = mutableListOf<HotListItem>().apply {
        while (seekPosterSection() != null) {
            getNextItem()?.let(::add)
        }
    }

    private fun getNextItem(): HotListItem? {
        findPosterSection()
        val ratingLine = findPosterSectionRating() ?: return null
        val rating = extractFromBounds(ratingLine, RATING_LOWER, RATING_UPPER)

        val posterImageLine = findImage() ?: return null
        val imageUrl = extractFromBounds(posterImageLine, POSTER_IMAGE_LOWER, POSTER_IMAGE_UPPER)

        findTitleSection()
        val titleLinkLine = findAnchor() ?: return null
        val ttid = extractFromBounds(titleLinkLine, TTID_LOWER, TTID_UPPER)

        val titleTextLine = findEndAnchor() ?: return null
        val name = extractFromBounds(titleTextLine, NAME_LOWER, NAME_UPPER)

        return HotListItem(
            ttid,
            name,
            rating,
            imageUrl
        )
    }

    private var matchBuffer: String? = null

    private fun seek(token: String, startWith: String? = matchBuffer): String? {
        startWith?.let { if (it.contains(token)) return it }
        while (!source.exhausted()) {
            with(source.readUtf8Line()) {
                if (this != null && contains(token)) {
                    matchBuffer = this
                    return this
                }
            }
        }
        return null
    }

    private fun findAnchor() = seek("""<a """)
    private fun findEndAnchor() = seek("""</a""")
    private fun findImage() = seek("""<img """)

    private fun seekPosterSection() = seek("""<td class="posterColumn"""", null)
    private fun findPosterSection() = seek("""<td class="posterColumn"""")
    private fun findPosterSectionRating() = seek("""<span name="ir"""")

    private fun findTitleSection() = seek("""<td class="titleColumn">""")

    private fun extractFromBounds(source: String, lower: String, upper: String): String {
        val lowerBound = source.indexOf(lower) + lower.length
        val upperBound = source.indexOf(upper, lowerBound)
        return source.substring(lowerBound, upperBound)
    }
}
