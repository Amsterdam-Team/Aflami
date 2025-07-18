package com.example.repository.mapper

class DateParserImpl : DateParser {
    override fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }
}