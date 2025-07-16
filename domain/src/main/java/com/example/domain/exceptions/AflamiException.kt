package com.example.domain.exceptions

open class AflamiException : Exception()

open class QueryValidationException() : AflamiException()
open class NetworkException : AflamiException()
class UnknownException : AflamiException()

class QueryTooLongException : QueryValidationException()
class BlankQueryException : QueryValidationException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()

class NoSuggestedCountriesException : AflamiException()

class NoSearchByKeywordResultFoundException : AflamiException()
class NoSearchByActorResultFoundException : AflamiException()

class CountryTooShortException: AflamiException()
class CountryIsEmptyException: AflamiException()

class NoCastFoundException: AflamiException()
class CountryIsEmptyException : AflamiException()