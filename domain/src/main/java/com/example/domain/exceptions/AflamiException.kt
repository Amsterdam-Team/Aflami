package com.example.domain.exceptions

open class AflamiException : Exception()

open class QueryValidationException() : AflamiException()

class QueryTooShortException : QueryValidationException()
class QueryTooLongException : QueryValidationException()
class InvalidCharactersException : QueryValidationException()
class BlankQueryException : QueryValidationException()

class UnknownException : AflamiException()

class InternetConnectionException: AflamiException()
class NoSuggestedCountriesException: AflamiException()
class NoMoviesForCountryException: AflamiException()
class CountryTooShortException: AflamiException()
class CountryIsEmptyException: AflamiException()