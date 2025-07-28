package com.amsterdam.entity

import kotlinx.datetime.Instant

data class TvShowWatchHistory(
    val tvShowId: Long,
    val lastWatchedTime: Instant
)