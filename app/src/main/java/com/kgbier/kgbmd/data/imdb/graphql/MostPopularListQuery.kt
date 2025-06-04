package com.kgbier.kgbmd.data.imdb.graphql

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.MoviePoster
import kotlinx.serialization.Serializable

class MostPopularListQuery : GraphqlQuery<MostPopularListQuery.Params, MostPopularListQuery.Result> {

    @Serializable
    data class Params(val count: Int, val type: ChartTitleType) {
        @Serializable
        enum class ChartTitleType {
            MOST_POPULAR_TV_SHOWS,
            MOST_POPULAR_MOVIES,
        }
    }

    @Serializable
    data class Result(val chartTitles: ChartTitles) {
        @Serializable
        data class ChartTitles(val pageInfo: PaginationFragment, val edges: List<Edge>) {
            @Serializable
            data class Edge(val currentRank: Int, val node: Node) {
                @Serializable
                data class Node(
                    val id: String,
                    val titleText: TitleText,
                    val primaryImage: PrimaryImage,
                    val ratingsSummary: RatingsSummary,
                ) {
                    @Serializable
                    data class TitleText(val text: String)

                    @Serializable
                    data class PrimaryImage(val url: String)

                    @Serializable
                    data class RatingsSummary(val aggregateRating: Double?)
                }
            }
        }
    }

    override val document: String = """
query MostPopularList(${'$'}count: Int!, ${'$'}type: ChartTitleType!) {
  chartTitles(first: ${'$'}count, chart: { chartType: ${'$'}type }) {
    pageInfo {
      ...${PaginationFragment.name}
    }

    edges {
      currentRank
      node {
        id
        titleText {
          text
        }
        primaryImage {
          url
        }
        ratingsSummary {
          aggregateRating
        }
      }
    }
  }
}
${PaginationFragment.fragment}
"""
}

fun MostPopularListQuery.Result.ChartTitles.Edge.toMoviePoster() = MoviePoster(
    ttId = node.id,
    title = node.titleText.text,
    rating = node.ratingsSummary.aggregateRating
        ?.takeUnless { it == 0.0 }?.toString(),
    thumbnailUrl = ImageResizer.resize(
        imageUrl = node.primaryImage.url,
        size = ImageResizer.SIZE_WIDTH_THUMBNAIL,
    ),
    posterUrlSmall = ImageResizer.resize(
        imageUrl = node.primaryImage.url,
        size = ImageResizer.SIZE_WIDTH_MEDIUM,
    ),
    posterUrlLarge = ImageResizer.resize(
        imageUrl = node.primaryImage.url,
        size = ImageResizer.SIZE_WIDTH_LARGE,
    )
)