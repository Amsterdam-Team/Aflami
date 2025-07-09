package com.example.entity

import kotlinx.datetime.Instant

// val id: Long
data class SearchHistory(val query : String , val date: Instant)
