package com.example.domain.exceptions

open class AflamiException : Exception()

open class NetworkException : AflamiException()
class UnknownException : AflamiException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()

class NoSuggestedCountriesException : AflamiException()

class NoSearchByKeywordResultFoundException : AflamiException()
class NoSearchByActorResultFoundException : AflamiException()

class CountryIsEmptyException : AflamiException()