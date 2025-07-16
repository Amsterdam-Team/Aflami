package com.example.domain.common.CountryExtensions

import com.example.entity.Country

fun List<Country>.filterByName(keyword: String): List<Country> {
    return this.filter {
        it.countryName.contains(keyword, ignoreCase = true)
    }
}