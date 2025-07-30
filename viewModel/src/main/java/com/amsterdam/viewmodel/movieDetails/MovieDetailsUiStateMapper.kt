package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import kotlinx.datetime.LocalDate
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class MovieDetailsUiStateMapper @Inject constructor() {

    fun toUiState(domain: MovieDetails): MovieDetailsUiState = with(domain) {
        MovieDetailsUiState(
            movieId = movie.id,
            rating = ratingToRatingString(movie.rating),
            movieTitle = movie.name,
            categories = movie.categories,
            moviePostersUrl = moviePosters,
            releaseDate = movie.releaseDate.toString(),
            movieLength = movieLengthToHourMinuteString(movie.runTimeInMinutes),
            originCountry = movie.originCountry,
            description = movie.description,
            hasVideo = movie.hasVideo,
            actors = actors.map {
                ActorUiState(
                    photo = it.imageUrl,
                    name = it.name
                )
            },
            extraItem = listOf(
                Selectable(isSelected = true, MovieExtras.MORE_LIKE_THIS),
                Selectable(isSelected = false, MovieExtras.REVIEWS),
                Selectable(isSelected = false, MovieExtras.GALLERY),
                Selectable(isSelected = false, MovieExtras.COMPANY_PRODUCTION)
            ),
            similarMovies = similarMovies.map {
                SimilarMovieUiState(
                    rate = ratingToRatingString(it.rating),
                    name = it.name,
                    productionYear = it.releaseDate.year.toString(),
                    posterUrl = it.posterUrl
                )
            },
            productionCompany = movie.productionCompanies.map { company ->
                ProductionCompanyUiState(
                    image = company.imageUrl,
                    name = company.name,
                    country = company.country
                )
            },
            gallery = movieGallery.map { it },
            reviews = reviews.map {
                ReviewUiState(
                    author = it.reviewerName,
                    username = it.reviewerUsername,
                    rating = ratingToRatingString(it.rating),
                    content = it.content,
                    date = dateToString(it.date),
                    imageUrl = it.imageUrl.takeIf { it.isNotBlank() }
                )
            }
        )
    }

    fun movieLengthToHourMinuteString(movieLength: Int): String {
        val hours = movieLength / 60
        val minutes = movieLength % 60
        return "${hours}h ${minutes}m"
    }

    fun dateToString(date: LocalDate?): String {
        if (date == null) {
            return ""
        }
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year.toString()
        return "$year-$month-$day"
    }

    fun ratingToRatingString(rating: Float): String {
        val clamped = rating.coerceIn(0f, 10f)
        val rounded = (clamped * 10).roundToInt() / 10f
        return String.format(Locale.US, "%.1f", rounded)
    }
}
