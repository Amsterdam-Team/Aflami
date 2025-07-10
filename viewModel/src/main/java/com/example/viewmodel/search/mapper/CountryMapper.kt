package com.example.viewmodel.search.mapper

import com.example.entity.Country

fun List<Country>.toListOfString(): List<String> {
    return this.map { it.countryName }
}