package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.repository.dto.remote.RemoteMovieDetailsResponse
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieDetailRemoteMapper @Inject constructor(
    private val movieRemoteMapper: MovieRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val posterRemoteMapper: PostersRemoteMapper,
): EntityMapper<RemoteMovieDetailsResponse, MovieDetails> {
    override fun toEntity(response: RemoteMovieDetailsResponse): MovieDetails {
        val remoteMovieItemDto = with(response){
            RemoteMovieItemDto(
                adult = adult,
                backdropPath = backdropPath,
                genreIds = genreIds,
                id = id,
                originalLanguage = originalLanguage,
                originalTitle = originalTitle,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                productionCompanies = productionCompanies,
                releaseDate = releaseDate,
                title = title,
                video = video,
                voteAverage = voteAverage,
                voteCount = voteCount,
                originCountry = originCountry,
                runtime = runtime,
                genres = genres
            )
        }
        return MovieDetails(
            movie = movieRemoteMapper.toEntity(remoteMovieItemDto),
            reviews = reviewRemoteMapper.toEntityList(response.reviews.results),
            actors = castRemoteMapper.toEntityList(response.credits.cast),
            similarMovies = movieRemoteMapper.toEntityList(response.similar.results,isPoster = false),
            movieGallery = galleryRemoteMapper.toEntity(response.images),
            moviePosters = posterRemoteMapper.toEntity(response.images),
        )
    }

    fun mapMovieDetailsToMovieItemDto(remoteMovieDetailsResponse: RemoteMovieDetailsResponse): RemoteMovieItemDto {
        return with(remoteMovieDetailsResponse) {
            RemoteMovieItemDto(
                adult = adult,
                backdropPath = backdropPath,
                genreIds = genreIds,
                id = id,
                originalLanguage = originalLanguage,
                originalTitle = originalTitle,
                overview = overview,
                popularity = popularity,
                posterPath = posterPath,
                productionCompanies = productionCompanies,
                releaseDate = releaseDate,
                title = title,
                video = video,
                voteAverage = voteAverage,
                voteCount = voteCount,
                originCountry = originCountry,
                runtime = runtime,
                genres = genres
            )
        }
    }

}