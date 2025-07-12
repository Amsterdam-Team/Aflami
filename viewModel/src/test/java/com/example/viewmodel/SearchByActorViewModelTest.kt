package com.example.viewmodel

import com.example.entity.Movie
import com.example.viewmodel.searchByActor.SearchByActorEffect
import com.example.viewmodel.searchByActor.SearchByActorViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

//
//class SearchByActorViewModelTest {
//    //lateinit var getSearchByActorUseCase: GetSearchByActorUseCase
//    private lateinit var viewModel: SearchByActorViewModel
//
//    private val fakeMovies = listOf(
//        Movie(1, "Movie 1", "", "", 2023, emptyList(), 8.0f),
//        Movie(2, "Movie 2", "", "", 2023, emptyList(), 9.0f)
//    )
//    @Before
//    fun setUp() {
//        //  getSearchByActorUseCase = mockk()
//        //viewModel = SearchByActorViewModel(getSearchByActorUseCase)
//
//    }
//
//    @Test
//    fun `should update query state when onQueryChange is called`() {
//        val query = "Tom Hanks"
//        viewModel.onQueryChange(query)
//        assert(viewModel.state.value.query == query)
//    }
//
//    @Test
//    fun `should navigate back when onBackClicked is called`() {
//        viewModel.onBackClicked()
//        assert(viewModel.effect == SearchByActorEffect.NavigateBack)
//    }
//
//    @Test
//    fun `should get movies by actor when query is not blank `() {
//        val query = "Tom Hanks"
//        viewModel.onQueryChange(query)
//      //  viewModel.getMoviesByActor(query)
//        assert(viewModel.state.value.movies.isNotEmpty())
//    }
//
//    @Test
//    fun `should not get movies by actor when query is blank `() {
//        val query = ""
//        viewModel.onQueryChange(query)
//      //  viewModel.getMoviesByActor(query)
//        assert(viewModel.state.value.movies.isEmpty())
//    }
//
//    @Test
//    fun `should update search by actor result when getMoviesByActor is called`() {
//        val movies = listOf(
//            Movie(1, " ", " ", " ", 2023, emptyList(), 9.9f),
//            Movie(2, " ", " ", " ", 2023, emptyList(), 9.9f)
//        )
//       // viewModel.getMoviesByActor("Tom Hanks")
//        assert(viewModel.state.value.movies == movies)
//
//    }
//
//    @Test
//    fun `should debounce search input before calling use case`()  {
//        var callCount = 0
//
//        val debouncedUseCase: suspend (String) -> List<Movie> = {
//            callCount++
//            fakeMovies
//        }
//
//     //   val viewModel = SearchByActorViewModel(debouncedUseCase)
//
//        // simulate typing
//        viewModel.onQueryChange("Tom")
//        viewModel.onQueryChange("Tom H")
//        viewModel.onQueryChange("Tom Ha")
//        viewModel.onQueryChange("Tom Han")
//        viewModel.onQueryChange("Tom Hank")
//        viewModel.onQueryChange("Tom Hanks")
//
////        advanceTimeBy(300)
////
////        advanceUntilIdle()
//
//        assertEquals(1, callCount)
//    }
//
//
//
//}