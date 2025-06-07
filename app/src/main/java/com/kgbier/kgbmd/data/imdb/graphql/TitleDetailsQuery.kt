package com.kgbier.kgbmd.data.imdb.graphql

import com.kgbier.kgbmd.data.imdb.model.transformImageUrl
import com.kgbier.kgbmd.domain.model.TitleDetails
import kotlinx.serialization.Serializable

class TitleDetailsQuery : GraphqlQuery<TitleDetailsQuery.Params, TitleDetailsQuery.Result> {

    @Serializable
    data class Params(val id: String)

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(
            val titleText: TitleText,
            val originalTitleText: OriginalTitleText,
            val titleType: TitleType,
            val primaryImage: PrimaryImage?,
            val releaseYear: ReleaseYear,
            val ratingsSummary: RatingsSummary,
            val runtime: Runtime,
            val titleGenres: TitleGenres,
            val creators: List<PrincipalCreditsForCategory>,
            val directors: List<PrincipalCreditsForCategory>,
            val writers: List<PrincipalCreditsForCategory>,
            val outline: Plot,
        ) {
            @Serializable
            data class TitleText(val text: String)

            @Serializable
            data class OriginalTitleText(val text: String)

            @Serializable
            data class TitleType(val id: String)

            @Serializable
            data class PrimaryImage(val url: String?)

            @Serializable
            data class ReleaseYear(val year: Int?, val endYear: Int?)

            @Serializable
            data class RatingsSummary(val aggregateRating: Double?, val voteCount: Int?)

            @Serializable
            data class Runtime(val displayableProperty: DisplayableProperty) {
                @Serializable
                data class DisplayableProperty(val value: Value) {
                    @Serializable
                    data class Value(val plainText: String)
                }
            }

            @Serializable
            data class TitleGenres(val genres: List<Genre>) {
                @Serializable
                data class Genre(val genre: GenreText) {
                    @Serializable
                    data class GenreText(val text: String)
                }
            }

            @Serializable
            data class PrincipalCreditsForCategory(val credits: List<Credit>) {
                @Serializable
                data class Credit(val name: Name) {
                    @Serializable
                    data class Name(val id: String, val nameText: NameText) {
                        @Serializable
                        data class NameText(val text: String)
                    }
                }
            }

            @Serializable
            data class Plot(val edges: List<Edge>) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(val plotText: PlotText) {
                        @Serializable
                        data class PlotText(val plaidHtml: String)
                    }
                }
            }
        }
    }

    override val document: String = """
query TitleDetails(${'$'}id: ID!) {
  title(id: ${'$'}id) {
    titleText {
      text
    }
    originalTitleText {
      text
    }
    titleType {
      id
    }
    primaryImage {
      url
    }
    releaseYear {
      year
      endYear
    }
    ratingsSummary {
      aggregateRating
      voteCount
    }
    runtime {
      displayableProperty {
        value {
          plainText
        }
      }
    }
    titleGenres {
      genres {
        genre {
          text
        }
      }
    }
    creators: principalCredits(filter: { categories: ["creator"] }) {
      ...CreditNameAndId
    }
    directors: principalCredits(filter: { categories: ["director"] }) {
      ...CreditNameAndId
    }
    writers: principalCredits(filter: { categories: ["writer"] }) {
      ...CreditNameAndId
    }
    outline: plots(first: 1, filter: { type: OUTLINE }) {
      edges {
        node {
          ...PlotData
        }
      }
    }
  }
}

fragment CreditNameAndId on PrincipalCreditsForCategory {
  credits {
    name {
      id
      nameText {
        text
      }
    }
  }
}

fragment PlotData on Plot {
  plotText {
    plaidHtml
  }
}
"""
}

fun TitleDetailsQuery.Result.Title.toTitleDetails(): TitleDetails {
    return TitleDetails(
        name = titleText.text,
        poster = primaryImage?.url?.let(::transformImageUrl),
        contentRating = "",
        genre = titleGenres.genres.joinToString { it.genre.text },
        directedBy = directors.toCommaSeparatedList(),
        writtenBy = writers.toCommaSeparatedList(),
        createdBy = creators.toCommaSeparatedList(),
        description = outline.edges.firstOrNull()?.node?.plotText?.plaidHtml,
        yearReleased = releaseYear.year?.toString(),
        rating = ratingsSummary.aggregateRating?.toString()?.let { aggregate ->
            TitleDetails.Rating(
                value = aggregate,
                best = "10",
                count = ratingsSummary.voteCount?.toString(),
            )
        },
        duration = runtime.displayableProperty.value.plainText,
        castMembers = emptyList(), // Load lazily
    )
}

private fun List<TitleDetailsQuery.Result.Title.PrincipalCreditsForCategory>.toCommaSeparatedList() =
    firstOrNull()?.credits
        ?.joinToString { it.name.nameText.text }
