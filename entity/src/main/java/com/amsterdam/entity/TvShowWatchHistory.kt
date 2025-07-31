package com.amsterdam.entity

import kotlinx.datetime.Instant

data class TvShowWatchHistory(
    val tvShow: TvShow,
    val lastWatchedTime: Instant
)