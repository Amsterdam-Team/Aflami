package com.example.domain.exceptions

open class AflamiException : Exception()

open class NetworkException : AflamiException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()
