package com.kgbier.kgbmd.data.imdb.graphql

import com.kgbier.kgbmd.data.imdb.model.transformImageUrl
import com.kgbier.kgbmd.domain.model.NameDetails
import kotlinx.serialization.Serializable

class NameDetailsQuery : GraphqlQuery<NameDetailsQuery.Params, NameDetailsQuery.Result> {

    @Serializable
    data class Params(val id: String)

    @Serializable
    data class Result(val name: Name) {
        @Serializable
        data class Name(
            val nameText: NameText,
            val bio: Bio,
            val primaryImage: PrimaryImage?,
            val knownFor: KnownFor,
        ) {
            @Serializable
            data class NameText(val text: String)

            @Serializable
            data class Bio(val text: Text) {
                @Serializable
                data class Text(val plainText: String)
            }

            @Serializable
            data class PrimaryImage(val url: String?)

            @Serializable
            data class KnownFor(val edges: List<Edge>) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(val title: Title, val summary: Summary) {
                        @Serializable
                        data class Title(val titleText: TitleText) {
                            @Serializable
                            data class TitleText(val text: String)
                        }

                        @Serializable
                        data class Summary(val principalCategory: PrincipalCategory) {
                            @Serializable
                            data class PrincipalCategory(val text: String)
                        }
                    }
                }
            }
        }
    }

    override val document: String = """
query NameDetails(${'$'}id: ID!) {
  name(id: ${'$'}id) {
    nameText {
      text
    }
    bio {
      text {
        plainText
      }
    }
    primaryImage {
      url
    }
    knownFor(first: 3) {
      edges {
        node {
          title {
            titleText {
              text
            }
          }
          summary {
            principalCategory {
              text
            }
          }
        }
      }
    }
  }
}
"""
}

fun NameDetailsQuery.Result.Name.toNameDetails() = NameDetails(
    name = nameText.text,
    headshot = primaryImage?.url?.let(::transformImageUrl),
    description = bio.text.plainText,
    filmography = emptyMap(), // TODO: Lazily load filmography
)