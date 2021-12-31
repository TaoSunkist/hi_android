package me.taosunkist.hello.data.net

import me.taosunkist.hello.data.net.model.MultiListItem
import kotlin.math.min

data class MultiListResponse(
    val list: List<MultiListItem>,
    val pageIndex: Int,
    val pageSize: Int,
    val pages: Int,
    val totalItems: Int,
) {
    companion object {

        fun fake(pageIndex: Int, pageSize: Int, maxPages: Int): MultiListResponse {
            if (pageIndex <= maxPages) {
                return MultiListResponse(
                    list = (0 until pageSize).map { MultiListItem.fake() },
                    pageIndex = pageIndex,
                    pageSize = pageSize,
                    pages = min(maxPages, pageIndex + 1),
                    totalItems = 10000
                )
            } else {
                return MultiListResponse(
                    list = arrayListOf(),
                    pageIndex = pageIndex,
                    pageSize = pageSize,
                    pages = min(maxPages, pageIndex + 1),
                    totalItems = 10000)
            }
        }
    }
}
