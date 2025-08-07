package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

data class MovieDetailsUiState(
    val movieId: Long = 0,
    val rating: String = "",
    val movieTitle: String = "",
    val categories: List<MovieGenre> = emptyList(),
    val releaseDate: String = "",
    val movieLength: String = "",
    val originCountry: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val isVideoLauncherFailed : Boolean = false,
    val moviePostersUrl: List<String> = emptyList(),
    val actors: List<ActorUiState> = emptyList(),
    val extraItem: List<Selectable<MovieExtras>> = defaultMovieExtras,
    val similarMovies: List<SimilarMovieUiState> = emptyList(),
    val productionCompany: List<ProductionCompanyUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val rateDialogUiState: RateDialogUiState = RateDialogUiState(),
    val isLoading: Boolean = false,
    val networkError: Boolean = false,
    val isLoginDialogVisible: Boolean = false,
    val dialogType: MovieAndSeriesDetailsDialogType? = null,
    val isDescriptionExpanded: Boolean = false,
    val isAddToListDialogVisible: Boolean = false,
    val isCreateNewListDialogVisible: Boolean = false,
    val userLists: List<UserListUiState> = emptyList(),
    val listName: String = "",
    val isCreateListLoading: Boolean = false,
    val isAddMovieToListLoading: Boolean = false,
    val selectedList: UserListUiState? = null,
) {

    enum class MovieExtras {
        MORE_LIKE_THIS,
        REVIEWS,
        GALLERY,
        COMPANY_PRODUCTION
    }

    companion object {
        val defaultMovieExtras = listOf(
            Selectable(isSelected = true, MovieExtras.MORE_LIKE_THIS),
            Selectable(isSelected = false, MovieExtras.REVIEWS),
            Selectable(isSelected = false, MovieExtras.GALLERY),
            Selectable(isSelected = false, MovieExtras.COMPANY_PRODUCTION)
        )
    }
}