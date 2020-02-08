package com.kgbier.kgbmd.data.imdb.model

/**
<script type="application/ld+json">
{
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
"url": "/company/co0341125/"
},
{
"@type": "Organization",
"url": "/company/co0179037/"
},
{
"@type": "Organization",
"url": "/company/co0655801/"
},
{
"@type": "Organization",
"url": "/company/co0655801/"
}
],
"description": "Star Wars: Episode VIII - The Last Jedi is a movie starring Daisy Ridley, John Boyega, and Mark Hamill. Rey develops her newly discovered abilities with the guidance of Luke Skywalker, who is unsettled by the strength of her powers....",
"datePublished": "2017-12-13",
"keywords": "wisecrack humor,one liner,chubby,big boned woman,chubby woman",
"aggregateRating": {
"@type": "AggregateRating",
"ratingCount": 487220,
"bestRating": "10.0",
"worstRating": "1.0",
"ratingValue": "7.1"
},
"duration": "PT2H32M",
"review": {
"@type": "Review",
"itemReviewed": {
"@type": "CreativeWork",
"url": "/title/tt2527336/"
},
"author": {
"@type": "Person",
"name": "andrew_cusack"
},
"dateCreated": "2019-08-26",
"inLanguage": "English",
"name": "disappointment around every corner",
"reviewBody": "I tried really hard to like the movie. I failed.\n\nand then i remembered do or do not, there is no try.\n\nI do not like this movie.",
"reviewRating": {
"@type": "Rating",
"worstRating": "1",
"bestRating": "10",
"ratingValue": "3"
}
},
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
}
</script>
 */

data class Movie(
    val url: String,
    val name: String,
    val image: String,
    val thumbnailUrl: String,
    val description: String,
    val datePublished: String, // Date released
    val aggregateRating: Rating,
    val duration: String
) {
    data class Rating(
        val ratingCount: Int,
        val ratingValue: String
    )
}