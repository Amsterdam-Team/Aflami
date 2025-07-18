package com.example.viewmodel.seriesDetails

import com.example.entity.category.TvShowGenre
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

data class SeriesDetailsUiState(
    val tvShowId: Long = 0,
    val posterUrl: String = "",
    val rating: String = "",
    val title: String = "",
    val categories: List<TvShowGenre> = emptyList(),
    val airDate: String = "",
    val seasonCount: String = "",
    val originCountry: String = "",
    val description: String = "",
    val cast: List<ActorUiState> = emptyList(),
    val isRateDialogVisible: Boolean = false,
    val isAddToListDialogVisible: Boolean = false,
    val extraItem: List<Selectable<SeriesExtras>> = listOf(
        Selectable(isSelected = true, SeriesExtras.SEASONS),
        Selectable(isSelected = false, SeriesExtras.MORE_LIKE_THIS),
        Selectable(isSelected = false, SeriesExtras.REVIEWS),
        Selectable(isSelected = false, SeriesExtras.GALLERY),
        Selectable(isSelected = false, SeriesExtras.COMPANY_PRODUCTION)
    ),
    val seasons: List<SeasonUiState> = emptyList(),
    val similarSeries: List<SimilarMovieUiState> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val productionCompanies: List<ProductionCompanyUiState> = emptyList(),
    val isLoading: Boolean = false,
    val networkError: Boolean = false,
    val hasVideo: Boolean = false,
) {
    enum class SeriesExtras {
        SEASONS,
        MORE_LIKE_THIS,
        REVIEWS,
        GALLERY,
        COMPANY_PRODUCTION
    }

    data class SeasonUiState(
        val id: Long = 0,
        val title: String = "",
        val seasonNumber: Int = 0,
        val episodeCount: String = "",
        val episodes: List<EpisodeUiState> = emptyList()
    ) {
        data class EpisodeUiState(
            val id: Long = 0,
            val number: String = "",
            val title: String = "",
            val rating: String = "",
            val imageUrl: String = "",
            val imageNumber: String = "",
            val description: String = "",
            val duration: String = "",
            val airDate: String = "",
        )
    }
}
