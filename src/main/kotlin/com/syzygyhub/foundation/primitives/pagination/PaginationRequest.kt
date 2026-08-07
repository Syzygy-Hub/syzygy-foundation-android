package com.syzygyhub.foundation.primitives.pagination

/**
 * Describes a request for a specific page of results.
 *
 * @property pageNumber 1-based page index. Defaults to `1`.
 * @property pageSize Maximum number of results per page. Defaults to `20`.
 * @property cursor Optional opaque cursor for cursor-based pagination.
 */
data class PaginationRequest(
    val pageNumber: Int = 1,
    val pageSize: Int = 20,
    val cursor: String? = null,
)
