package com.example.domain.utils

open class AflamiException : Exception()

open class QueryValidationException() : AflamiException()

class QueryTooShortException : QueryValidationException()
class QueryTooLongException : QueryValidationException()
class InvalidCharactersException : QueryValidationException()
class BlankQueryException : QueryValidationException()