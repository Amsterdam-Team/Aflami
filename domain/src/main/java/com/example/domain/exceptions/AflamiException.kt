package com.example.domain.exceptions

open class AflamiException : Exception()

open class QueryValidationException() : AflamiException()
open class NetworkException : AflamiException()
class UnknownException : AflamiException()

class QueryTooLongException : QueryValidationException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()
