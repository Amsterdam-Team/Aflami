package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import com.amsterdam.viewmodel.utils.dateToString
import com.amsterdam.viewmodel.utils.movieLengthToHourMinuteString
import com.amsterdam.viewmodel.utils.ratingToRatingString
import javax.inject.Inject

class MovieDetailsUiStateMapper @Inject constructor() {

    fun toUiState(domain: MovieDetails): MovieDetailsUiState = with(domain) {
        MovieDetailsUiState(
            movieId = movie.id,
            rating = ratingToRatingString(movie.rating),
            movieTitle = movie.name,
            categories = movie.categories,
            moviePostersUrl = moviePosters,
            releaseDate = dateToString(movie.releaseDate),
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
                    movieId = it.id,
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
    fun ratingToRatingString(rating: Float): String {
        return if  (rating % 1 == 0.0f) "${rating.toInt()}" else "%.1f".format(rating)
    }
}