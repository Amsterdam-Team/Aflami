package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

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
    val videoUrl: String = "",
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
    val postersUrls: List<String> = emptyList(),
    val productionCompanies: List<ProductionCompanyUiState> = emptyList(),
    val isLoading: Boolean = false,
    val networkError: Boolean = false,
    val hasVideo: Boolean = false,
    val isLoginDialogVisible: Boolean = false,
    val dialogType: MovieAndSeriesDetailsDialogType? = null,
    val rateDialogUiState: RateDialogUiState = RateDialogUiState(),
    val isDescriptionExpanded: Boolean = false
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
        val episodeCount: Int = 0,
        val episodes: List<EpisodeUiState> = emptyList(),
        var isExpanded: Boolean = false,
        val isLoading: Boolean = false
    ) {
        data class EpisodeUiState(
            val id: Long = 0,
            val number: Int = 0,
            val title: String = "",
            val rating: String = "",
            val imageUrl: String = "",
            val imageNumber: Int = 0,
            val description: String = "",
            val duration: String = "",
            val airDate: String = "",
            val videoUrl: String = ""
        )
    }
}
