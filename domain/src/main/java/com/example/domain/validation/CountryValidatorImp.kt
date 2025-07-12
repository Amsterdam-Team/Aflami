package com.example.domain.validation

import com.example.domain.exceptions.CountryIsEmptyException
import com.example.domain.exceptions.CountryTooShortException

class CountryValidatorImp : CountryValidator {

    override fun validateCountry(countryName: String) {
        if (countryName.isBlank()) throw CountryIsEmptyException()
    }
}