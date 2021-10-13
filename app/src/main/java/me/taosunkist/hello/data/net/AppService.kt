package me.taosunkist.hello.data.net

import io.reactivex.Single

class AppService {
    companion object {
        val shared = AppService()
    }

    fun fetchMultiListDataList(pageSize: Int, pageIndex: Int): Single<MultiListResponse> {
        return Single.create {
            Thread.sleep(2000)
            it.onSuccess(MultiListResponse.fake(pageSize, pageIndex, 3))
        }
    }
}