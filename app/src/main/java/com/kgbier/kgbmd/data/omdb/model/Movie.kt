package com.kgbier.kgbmd.data.omdb.model

import com.kgbier.kgbmd.domain.model.MovieDetails
import com.squareup.moshi.Json

/**
{
"Title": "The Dark Knight",
"Year": "2008",
"Rated": "PG-13",
"Released": "18 Jul 2008",
"Runtime": "152 min",
"Genre": "Action, Crime, Drama, Thriller",
"Director": "Christopher Nolan",
"Writer": "Jonathan Nolan (screenplay), Christopher Nolan (screenplay), Christopher Nolan (story), David S. Goyer (story), Bob Kane (characters)",
"Actors": "Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine",
"Plot": "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
"Language": "English, Mandarin",
"Country": "USA, UK",
"Awards": "Won 2 Oscars. Another 152 wins & 155 nominations.",
"Poster": "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg",
"Ratings": [
{
"Source": "Internet Movie Database",
"Value": "9.0/10"
},
{
"Source": "Rotten Tomatoes",
"Value": "94%"
},
{
"Source": "Metacritic",
"Value": "84/100"
}
],
"Metascore": "84",
"imdbRating": "9.0",
"imdbVotes": "2,124,722",
"imdbID": "tt0468569",
"Type": "movie",
"DVD": "09 Dec 2008",
"BoxOffice": "$533,316,061",
"Production": "Warner Bros. Pictures/Legendary",
"Website": "N/A",
"Response": "True"
}
 */

data class Movie(
    @Json(name = "Title")
    val title: String,
    @Json(name = "Year")
    val year: String,
    @Json(name = "Rated")
    val rated: String,
    @Json(name = "Plot")
    val plot: String,
    @Json(name = "Poster")
    val poster: String,
    @Json(name = "Runtime")
    val runtime: String,
    @Json(name = "Genre")
    val genre: String,
    @Json(name = "Director")
    val directedBy: String,
    @Json(name = "Writer")
    val writtenBy: String,
    val imdbRating: String,
    val imdbVotes: String
)

fun transformMovieResponse(movie: Movie): MovieDetails? = with(movie) {
    MovieDetails(
        title,
        poster, // TODO
        poster, // TODO
        rated,
        genre,
        directedBy,
        writtenBy,
        plot,
        year, // TODO
        MovieDetails.Rating(
            imdbVotes,
            imdbRating
        ),
        runtime
    )
}
