package com.amsterdam.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PagingSource<T : Any>(
    private val fetch: suspend (page: Int) -> List<T>,
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val data = fetch(page)
            LoadResult.Page(
                data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}

// Temp, until replace all paging sources in all viewmodels
abstract class BasePagingSource<T: Any>() : PagingSource<Int, T>() {

    protected abstract suspend fun fetch(page: Int): List<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val data = fetch(page)
            LoadResult.Page(
                data = data,
                prevKey = (page - 1).takeIf { page != 1 },
                nextKey = (page + 1).takeIf { data.isNotEmpty() }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }
}
