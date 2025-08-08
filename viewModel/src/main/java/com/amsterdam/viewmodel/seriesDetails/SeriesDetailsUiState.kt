package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType

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
    val cast: List<ActorTvShowUiState> = emptyList(),
    val isRateDialogVisible: Boolean = false,
    val isAddToListDialogVisible: Boolean = false,
    val extraItem: List<Selectable<SeriesExtras>> = defaultSeriesExtrasItems,
    val seasons: List<SeasonUiState> = emptyList(),
    val similarSeries: List<SimilarTvShowUiState> = emptyList(),
    val reviews: List<ReviewTvShowUiState> = emptyList(),
    val gallery: List<String> = emptyList(),
    val postersUrls: List<String> = emptyList(),
    val productionCompanies: List<ProductionTvShowCompanyUiState> = emptyList(),
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
    data class SimilarTvShowUiState(
        val movieId: Long,
        val rate: String = "",
        val name: String = "",
        val productionYear: String = "",
        val posterUrl: String = ""
    )
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
    data class ActorTvShowUiState(
        val photo: String = "",
        val name: String = ""
    )

    data class ReviewTvShowUiState(
        val author: String = "",
        val username: String = "",
        val rating: String = "",
        val content: String = "",
        val date: String = "",
        val imageUrl: String? = "",
        val isExpanded: Boolean = false,
    )

    data class ProductionTvShowCompanyUiState(
        val image: String = "",
        val name: String = "",
        val country: String = ""
    )

    companion object {
        val defaultSeriesExtrasItems = listOf(
            Selectable(isSelected = true, SeriesExtras.SEASONS),
            Selectable(isSelected = false, SeriesExtras.MORE_LIKE_THIS),
            Selectable(isSelected = false, SeriesExtras.REVIEWS),
            Selectable(isSelected = false, SeriesExtras.GALLERY),
            Selectable(isSelected = false, SeriesExtras.COMPANY_PRODUCTION)
        )
    }
}
