package com.example.domain

open class RemoteDataSourceException : Exception()

class NoInternetException : RemoteDataSourceException()

class ServerErrorException : RemoteDataSourceException()
