package com.example.viewmodel.movieDetails

import com.example.domain.useCase.GetMovieDetailsUseCase
import com.example.viewmodel.movieDetails.MovieDetailsUiState.ActorUiState
import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.example.viewmodel.movieDetails.MovieDetailsUiState.ProductionCompanyUiState
import com.example.viewmodel.movieDetails.MovieDetailsUiState.ReviewUiState
import com.example.viewmodel.movieDetails.MovieDetailsUiState.SimilarMovieUiState
import com.example.viewmodel.shared.Selectable
import kotlinx.datetime.LocalDate
import java.util.Locale
import kotlin.math.roundToInt

class MovieDetailsUiStateMapper {

    fun toUiState(domain: GetMovieDetailsUseCase.MovieDetails): MovieDetailsUiState = with(domain) {
        MovieDetailsUiState(
            movieId = movie.id,
            posterUrl = movie.poster,
            rating = ratingToRatingString(movie.rating),
            movieTitle = movie.name,
            categories = categories,
            releaseDate = productionYearToDate(movie.productionYear),
            movieLength = movieLengthToHourMinuteString(movie.movieLength),
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
                    productionYear = it.productionYear.toString(),
                    posterUrl = it.poster
                )
            },
            productionCompany = productionsCompanies.map { company ->
                ProductionCompanyUiState(
                    image = company.imageUrl,
                    name = company.name,
                    country = company.country
                )
            },
            gallery = movieGallery.map { it},
            reviews = reviews.map {
                ReviewUiState(
                    author = it.author,
                    username = it.username,
                    rating = ratingToRatingString(it.rating),
                    content = it.content,
                    date = dateToString(it.date),
                    imageUrl = it.imageUrl.orEmpty().takeIf { it.isNotBlank() }
                )
            }
        )
    }
    private fun productionYearToDate(year: Int): String = "$year-01-01"

    fun movieLengthToHourMinuteString(movieLength: Int): String {
        val hours = movieLength / 60
        val minutes = movieLength % 60
        return "${hours}h ${minutes}m"
    }

    fun dateToString(date: LocalDate): String {
        val day   = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year  = date.year.toString()
        return "$year-$month-$day"
    }

    fun ratingToRatingString(rating: Float): String {
        val clamped = rating.coerceIn(0f, 10f)
        val rounded = (clamped * 10).roundToInt() / 10f
        return String.format(Locale.US, "%.1f", rounded)
    }
}


