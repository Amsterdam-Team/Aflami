package com.example.viewmodel.movieDetails

import com.example.viewmodel.common.Selectable

data class MovieDetailsUiState(
    val movieId: Long = 0,
    val posterUrl: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
    val rating: String = "9.9",
    val movieTitle: String = "The Green Mile ",
    val categoriesNames: List<String> = listOf("Drama", "Family", "Comedy", "Fantasy"),
    val releaseDate: String = "2016-10-12",
    val movieLength: String = "1h 32m",
    val originCountry: String = "US",
    val description: String = "In 1935, corrections officer Paul Edgecomb oversees \"The Green Mile,\" the death row section of Cold Mountain Penitentiary, alongside officers Brutus Howell, Dean Stanton, Harry Terwilliger, and the sadistic and despised Percy Wetmore, whose connections to the state governor shield him from punishment. ",
    val actors: List<ActorUiState> = listOf(
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState(),
        ActorUiState()
    ),
    val extraItem: List<Selectable<MovieExtras>> = listOf(
        Selectable(isSelected = true, MovieExtras.MORE_LIKE_THIS),
        Selectable(isSelected = false, MovieExtras.REVIEWS),
        Selectable(isSelected = false, MovieExtras.GALLERY),
        Selectable(isSelected = false, MovieExtras.COMPANY_PRODUCTION)
    ),
    val similarMovies: List<SimilarMovie> = listOf(
        SimilarMovie(),
        SimilarMovie(),
        SimilarMovie(),
        SimilarMovie(),
        SimilarMovie(),
        SimilarMovie()
    ),
    val productionCompany: List<ProductionCompany> = listOf(
        ProductionCompany(),
        ProductionCompany(),
        ProductionCompany(),
        ProductionCompany(),
        ProductionCompany(),
        ProductionCompany()
    ), val gallery: List<String> = listOf(
        "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
        "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
        "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
        "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
        "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg"
    ),
val reviews : List<Review> = listOf(Review(),Review(),Review(),Review(),Review(),Review())
)


enum class MovieExtras {
    MORE_LIKE_THIS,
    REVIEWS,
    GALLERY,
    COMPANY_PRODUCTION
}

data class ActorUiState(
    val photo: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
    val name: String = "Tom Hanks"
)

data class SimilarMovie(
    val rate: String = "9.9",
    val name: String = "Pan’s Labyrinth",
    val productionYear: String = "1990",
    val posterUrl: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg"
)

data class ProductionCompany(
    val image: String = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
    val name: String = "Paramount Pictures",
    val country: String = "US"
)

data class Review(
    val author: String = "Manuel São Bento",
    val username: String = "msbreviews",
    val rating: String = "9.9",
    val content: String = "Hmmm! I wasn’t sure if I was watching a sentimental edition of “Hawaii Five-O” here or a Hmmm! I wasn’t sure if I was watching a sentimental edition of “Hawaii Five-O” here or a Hmmm! I wasn’t sure if I was watching a sentimental edition of “Hawaii Five-O” here or a collection of outtakes from a “Sonic” movie as this rather disappointingly trundles along for the guts of two hours. It’s starts Read more It’s starts Read more",
    val date: String = "10-09-2016",
    val imageUrl: String? = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg",
)