package com.example.domain.exceptions

open class AflamiException : Exception()

open class KeywordValidationException() : AflamiException()
open class NetworkException : AflamiException()
class UnknownException : AflamiException()

class KeywordTooLongException : KeywordValidationException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()
