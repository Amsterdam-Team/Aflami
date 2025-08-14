package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType

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
    val actors: List<ActorMovieUiState> = emptyList(),
    val extraItem: List<Selectable<MovieExtras>> = defaultMovieExtras,
    val similarMovies: List<SimilarMovieUiState> = emptyList(),
    val productionCompany: List<ProductionMovieCompanyUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val reviews: List<ReviewMovieUiState> = emptyList(),
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
    val selectedLists: List<UserListUiState> = emptyList(),
) {
    data class SimilarMovieUiState(
        val movieId: Long,
        val rate: String = "",
        val name: String = "",
        val productionYear: String = "",
        val posterUrl: String = ""
    )

    data class ReviewMovieUiState(
        val author: String = "",
        val username: String = "",
        val rating: String = "",
        val content: String = "",
        val date: String = "",
        val imageUrl: String? = "",
        val isExpanded: Boolean = false,
    )

    data class ProductionMovieCompanyUiState(
        val image: String = "",
        val name: String = "",
        val country: String = ""
    )
    data class ActorMovieUiState(
        val photo: String = "",
        val name: String = ""
    )
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