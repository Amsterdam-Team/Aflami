package com.example.domain.exceptions

open class AflamiException : Exception()

open class QueryValidationException() : AflamiException()
open class NetworkException : AflamiException()
class UnknownException : AflamiException()

class QueryTooShortException : QueryValidationException()
class QueryTooLongException : QueryValidationException()
class InvalidCharactersException : QueryValidationException()
class BlankQueryException : QueryValidationException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()

class NoSuggestedCountriesException : AflamiException()

class NoSearchByKeywordResultFoundException : AflamiException()
class NoSearchByActorResultFoundException : AflamiException()

class CountryIsEmptyException : AflamiException()