package com.kgbier.kgbmd.domain.operation

import com.kgbier.kgbmd.data.imdb.model.jsonld.Movie
import com.kgbier.kgbmd.service.Services
import okio.BufferedSource
import okio.ByteString.Companion.encodeUtf8

/**
<script type="application/ld+json">{
"@context": "http://schema.org",
"@type": "Movie",
"url": "/title/tt2527336/",
"name": "Star Wars: Episode VIII - The Last Jedi",
"image": "https://m.media-amazon.com/images/M/MV5BMjQ1MzcxNjg4N15BMl5BanBnXkFtZTgwNzgwMjY4MzI@._V1_.jpg",
"genre": [
"Action",
"Adventure",
"Fantasy",
"Sci-Fi"
],
"contentRating": "PG-13",
"actor": [
{
"@type": "Person",
"url": "/name/nm5397459/",
"name": "Daisy Ridley"
},
{
"@type": "Person",
"url": "/name/nm3915784/",
"name": "John Boyega"
},
{
"@type": "Person",
"url": "/name/nm0000434/",
"name": "Mark Hamill"
},
{
"@type": "Person",
"url": "/name/nm0000402/",
"name": "Carrie Fisher"
}
],
"director": {
"@type": "Person",
"url": "/name/nm0426059/",
"name": "Rian Johnson"
},
"creator": [
{
"@type": "Person",
"url": "/name/nm0426059/",
"name": "Rian Johnson"
},
{
"@type": "Person",
"url": "/name/nm0000184/",
"name": "George Lucas"
},
{
"@type": "Organization",
"url": "/company/co0008970/"
},
{
"@type": "Organization",
"url": "/company/co0071326/"
},
{
"@type": "Organization",
"url": "/company/co0236721/"
},
{
"@type": "Organization",
"url": "/company/co0021593/"
}
],
"description": "Star Wars: Episode VIII - The Last Jedi is a movie starring Daisy Ridley, John Boyega, and Mark Hamill. Rey develops her newly discovered abilities with the guidance of Luke Skywalker, who is unsettled by the strength of her powers....",
"datePublished": "2017-12-13",
"keywords": "wisecrack humor,one liner,sabotage,asiatic,chubby",
"aggregateRating": {
"@type": "AggregateRating",
"ratingCount": 533011,
"bestRating": "10.0",
"worstRating": "1.0",
"ratingValue": "7.0"
},
"review": {
"@type": "Review",
"itemReviewed": {
"@type": "CreativeWork",
"url": "/title/tt2527336/"
},
"author": {
"@type": "Person",
"name": "xufqzxgm-45078"
},
"dateCreated": "2018-07-15",
"inLanguage": "English",
"name": "I Think It Was Boring",
"reviewBody": "They are making too many movies, I think. The story is uninteresting, the acting is somehow weak. It seems a little unsure or perhaps the actors are reacting to a poor script.  I have watched all the new Star Wars so far and I don\u0027t feel like watching anymore because to be honest with myself they are uninteresting. There are better movies out there or I can just do other stuff."
},
"duration": "PT2H32M",
"trailer": {
"@type": "VideoObject",
"name": "\"Back\" TV Spot",
"embedUrl": "/video/imdb/vi983676953",
"thumbnail": {
"@type": "ImageObject",
"contentUrl": "https://m.media-amazon.com/images/M/MV5BMjMwMzMwMDg1NF5BMl5BanBnXkFtZTgwNzg1NTYxNDI@._V1_.jpg"
},
"thumbnailUrl": "https://m.media-amazon.com/images/M/MV5BMjMwMzMwMDg1NF5BMl5BanBnXkFtZTgwNzg1NTYxNDI@._V1_.jpg",
"description": "Having taken her first steps into the Jedi world, Rey joins Luke Skywalker on an adventure with Leia, Finn and Poe that unlocks mysteries of the Force and secrets of the past.",
"uploadDate": "2017-11-24T04:08:47Z"
}
}</script>
 */

class ImdbDetails(private val source: BufferedSource) {

    private companion object {
        const val LINK_DATA_LOWER = "<script type=\"application/ld+json\">"
        const val LINK_DATA_UPPER = "</script>"
    }

    fun getMovieDetails(): Movie? {
        val lowerBytes = LINK_DATA_LOWER.encodeUtf8()
        val lower = source.indexOf(lowerBytes)
        source.skip(lower + lowerBytes.size)
        val upper = source.indexOf(LINK_DATA_UPPER.encodeUtf8())
        val out = source.readUtf8(upper)

        return Services.moshi.adapter(Movie::class.java).fromJson(out)
    }
}