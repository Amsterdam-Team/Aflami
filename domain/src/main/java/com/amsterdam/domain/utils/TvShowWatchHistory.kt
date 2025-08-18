package com.amsterdam.domain.utils

import com.amsterdam.entity.TvShow
import kotlinx.datetime.Instant

data class TvShowWatchHistory(
    val tvShow: TvShow,
    val lastWatchedTime: Instant
)