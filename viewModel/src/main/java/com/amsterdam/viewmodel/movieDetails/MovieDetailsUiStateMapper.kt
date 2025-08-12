package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Movie
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ActorMovieUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ProductionMovieCompanyUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ReviewMovieUiState
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.SimilarMovieUiState
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.utils.movieLengthToHourMinuteString
import com.amsterdam.viewmodel.utils.toFormattedString

fun MovieDetails.toUiState(): MovieDetailsUiState {
    return MovieDetailsUiState(
        movieId = movie.id,
        rating = movie.rating.toFormattedRating(),
        movieTitle = movie.name,
        categories = movie.categories,
        moviePostersUrl = moviePosters,
        releaseDate = movie.releaseDate.toFormattedString(),
        movieLength = movieLengthToHourMinuteString(movie.runTimeInMinutes),
        originCountry = movie.originCountry,
        description = movie.description,
        videoUrl = movie.videoUrl,
        actors = actors.toActorsMovieUiState(),
        extraItem = MovieDetailsUiState.defaultMovieExtras,
        similarMovies = similarMovies.toSimilarMoviesUiState(),
        productionCompany = productionCompanies.toProductionMovieCompaniesUiState(),
        gallery = movieGallery,
        reviews = reviews.map(Review::toUiState),
        rateDialogUiState = RateDialogUiState(selectedStarIndex = userRate)
    )
}
fun Actor.toActorMovieUiState(): ActorMovieUiState = ActorMovieUiState(photo = imageUrl, name = name)

fun List<Actor>.toActorsMovieUiState() : List<ActorMovieUiState> = map { it.toActorMovieUiState() }

private fun Movie.toSimilarMovieUiState(): SimilarMovieUiState {
    return SimilarMovieUiState(
        movieId = id,
        rate = rating.toFormattedRating(),
        name = name,
        productionYear = releaseDate?.year?.toString() ?: "",
        posterUrl = posterUrl
    )
}

fun List<Movie>.toSimilarMoviesUiState() = map { it.toSimilarMovieUiState() }

fun Review.toUiState(): ReviewMovieUiState {
    return ReviewMovieUiState(
        author = reviewerName,
        username = reviewerUsername,
        rating = rating.toFormattedRating(),
        content = content,
        date = date.toFormattedString(),
        imageUrl = imageUrl.takeIf { it.isNotBlank() }
    )
}

fun ProductionCompany.toProductionMovieCompanyUiState(): ProductionMovieCompanyUiState {
    return ProductionMovieCompanyUiState(
        image = imageUrl,
        name = name,
        country = country
    )
}

fun List<ProductionCompany>.toProductionMovieCompaniesUiState() = map { it.toProductionMovieCompanyUiState() }