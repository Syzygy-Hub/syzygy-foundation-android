package com.syzygyhub.foundation.primitives.pagination

/**
 * A single page of results returned from a paginated data source.
 *
 * @param T The element type contained in this page.
 * @property items The elements on this page.
 * @property totalCount The total number of elements across all pages.
 * @property pageNumber The 1-based index of this page.
 * @property pageSize The maximum number of elements per page.
 */
data class Page<T>(
    val items: List<T>,
    val totalCount: Int,
    val pageNumber: Int,
    val pageSize: Int,
) {
    /** `true` if there are more pages after this one. */
    val hasNextPage: Boolean get() = pageNumber * pageSize < totalCount

    /** `true` if there are pages before this one. */
    val hasPreviousPage: Boolean get() = pageNumber > 1

    /** `true` if this page contains no items. */
    val isEmpty: Boolean get() = items.isEmpty()

    /** The total number of pages given [totalCount] and [pageSize]. */
    val totalPages: Int get() = if (pageSize == 0) 0 else (totalCount + pageSize - 1) / pageSize
}
