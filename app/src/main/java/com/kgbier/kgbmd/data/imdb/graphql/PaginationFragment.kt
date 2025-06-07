package com.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

@Serializable
data class PaginationFragment(
    val endCursor: String,
    val hasNextPage: Boolean,
) {
    companion object {
        const val name = "Pagination"
        const val fragment = """
fragment Pagination on PageInfo {
  hasNextPage
  endCursor
}
"""
    }
}