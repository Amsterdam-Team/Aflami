package com.amsterdam.domain.validation

import com.amsterdam.domain.exceptions.CountryIsEmptyException

class CountryValidatorImp : CountryValidator {

    override fun validateCountry(countryName: String) {
        if (countryName.isBlank()) throw CountryIsEmptyException()
    }
}