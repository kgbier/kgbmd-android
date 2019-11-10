package com.kgbier.kgbmd.data.parse

import okio.BufferedSource

/**
<tr>

<td class="posterColumn">
<span name="rk" data-value="1"></span>
<span name="ir" data-value="8.8"></span>
<span name="us" data-value="1.5672096E12"></span>
<span name="nv" data-value="432428"></span>
<span name="ur" data-value="-2.1999999999999993"></span>
<a href="/title/tt7286456/?pf_rd_m=A2FGELUUNOQJNL&amp;pf_rd_p=ea4e08e1-c8a3-47b5-ac3a-75026647c16e&amp;pf_rd_r=WRCYJ3D4CYSB0QRRDWBP&amp;pf_rd_s=center-1&amp;pf_rd_t=15506&amp;pf_rd_i=moviemeter&amp;ref_=chtmvm_tt_1"> <img src="https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_UY67_CR0,0,45,67_AL_.jpg" width="45" height="67" alt="Joker">
</a>

</td>
<td class="titleColumn">
<a href="/title/tt7286456/?pf_rd_m=A2FGELUUNOQJNL&amp;pf_rd_p=ea4e08e1-c8a3-47b5-ac3a-75026647c16e&amp;pf_rd_r=WRCYJ3D4CYSB0QRRDWBP&amp;pf_rd_s=center-1&amp;pf_rd_t=15506&amp;pf_rd_i=moviemeter&amp;ref_=chtmvm_tt_1" title="Todd Phillips (dir.), Joaquin Phoenix, Robert De Niro">Joker</a>
<span class="secondaryInfo">(2019)</span>
<div class="velocity">1
(no change)
</div>
</td>

<td class="ratingColumn imdbRating">
<strong title="8.8 based on 432,428 user ratings">8.8</strong>
</td>

<td class="ratingColumn">
<div class="seen-widget seen-widget-tt7286456" data-titleid="tt7286456">
<div class="boundary">
<div class="popover">
<span class="delete">&nbsp;</span><ol><li>1</li><li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>10</li></ol>            </div>
</div>
<div class="inline">
<div class="pending"></div>
<div class="unseeable">NOT YET RELEASED</div>
<div class="unseen"> </div>
<div class="rating"></div>
<div class="seen">Seen</div>
</div>
</div>
</td>

<td class="watchlistColumn">
<div class="wlb_ribbon" data-tconst="tt7286456" data-recordmetrics="true" style="position: relative;"><div class="wl-ribbon standalone not-inWL" title="Click to add to watchlist"></div></div>
</td>

</tr>
 */

data class HotListItem(
    val ttId: String,
    val name: String,
    val rating: String?,
    val posterUrl: String,
    val thumbnailUrl: String
)

class HotListParser(private val source: BufferedSource) {

    companion object {
        // <span name="ir" data-value="8.8"></span>
        const val RATING_LOWER = "data-value=\""
        const val RATING_UPPER = "\""

        // <a href="/title/tt7286456/?pf_r...
        const val TTID_LOWER = "title/"
        const val TTID_UPPER = "/"

        // <a href="/title/tt7286456/?pf_r[...]_tt_1"> <img src="https://m.media-amazon.com/images/M/M[...]L_.jpg" width="45" height="67" alt="Joker">
        const val POSTER_IMAGE_LOWER = "<img src=\""
        const val POSTER_IMAGE_UPPER = "\""

        // ...er" >Terminator: Dark Fate</a>
        const val NAME_LOWER = ">"
        const val NAME_UPPER = "<"

        const val DYNAMIC_IMAGE_SIZE_DELIMITER = "@._V1"
        const val DYNAMIC_IMAGE_THUMBNAIL = "._SX40_CR0,0,40,54_.jpg"
        const val DYNAMIC_IMAGE_MEDIUM = "_UX182_CR0,0,182,268_AL_.jpg"
        const val DYNAMIC_IMAGE_LARGE = "_SX640_CR0,0,640,999_AL_.jpg"
    }

    fun getListItems(): List<HotListItem> {
        val list = mutableListOf<HotListItem?>()
        while (seekPosterSection() != null) {
            list.add(getNextItem())
        }
        return list.filterNotNull()
    }

    private fun getNextItem(): HotListItem? {
        findPosterSection()
        val ratingLine = findPosterSectionRating() ?: return null
        val rating = extractFromBounds(ratingLine, RATING_LOWER, RATING_UPPER)

        val posterImageLine = findImage() ?: return null
        val thumbnailUrl =
            extractFromBounds(posterImageLine, POSTER_IMAGE_LOWER, POSTER_IMAGE_UPPER)

        findTitleSection()
        val titleLinkLine = findAnchor() ?: return null
        val ttid = extractFromBounds(titleLinkLine, TTID_LOWER, TTID_UPPER)

        val titleTextLine = findEndAnchor() ?: return null
        val name = extractFromBounds(titleTextLine, NAME_LOWER, NAME_UPPER)

        val validatedRating = rating.takeUnless { it == "0.0" }
        val posterUrl = generatePosterUrl(thumbnailUrl)
        return HotListItem(ttid, name, validatedRating, posterUrl, thumbnailUrl)
    }

    private fun generatePosterUrl(thumbnailUrl: String): String {
        return thumbnailUrl.replaceAfter(DYNAMIC_IMAGE_SIZE_DELIMITER, DYNAMIC_IMAGE_MEDIUM)
    }

    private var matchBuffer: String? = null

    private fun seek(token: String, include: String? = matchBuffer): String? {
        include?.let { if (it.contains(token)) return it }
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

    private fun seekPosterSection() = seek("""<td class="posterColumn">""", null)
    private fun findPosterSection() = seek("""<td class="posterColumn">""")
    private fun findPosterSectionRating() = seek("""<span name="ir"""")

    private fun findTitleSection() = seek("""<td class="titleColumn">""")

    private fun extractFromBounds(source: String, lower: String, upper: String): String {
        val lowerBound = source.indexOf(lower) + lower.length
        val upperBound = source.indexOf(upper, lowerBound)
        return source.substring(lowerBound, upperBound)
    }
}