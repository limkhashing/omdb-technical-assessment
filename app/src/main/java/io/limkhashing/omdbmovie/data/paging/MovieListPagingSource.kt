package io.limkhashing.omdbmovie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository

//class MovieListPagingSource(
//    private val moviesRepository: MoviesRepository,
//) : PagingSource<Int, Movie>() {
//
//    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        return try {
//            val nextPageNumber = params.key ?: 1
//            val response = moviesRepository.fetchMovieList(page = nextPageNumber)
//            LoadResult.Page(
//                data = response.jobs ?: emptyList(),
//                prevKey = null,
//                nextKey = if (!response.jobs.isNullOrEmpty())
//                    response.page + 1
//                else
//                    null
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//}