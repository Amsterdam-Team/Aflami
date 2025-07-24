package com.example.domain.exceptions

open class AflamiException : Exception()

open class NetworkException : AflamiException()

class UnknownException : NetworkException()

class NoInternetException : NetworkException()
class ServerErrorException : NetworkException()

open class AuthenticationException : NetworkException()

class UnauthorizedException : AuthenticationException()

class AccountDisabledException : AuthenticationException()

class InvalidCredentialsException : AuthenticationException()

class VerificationRequiredException : AuthenticationException()

class InvalidSessionException : AuthenticationException()

class AccessDeniedException : AuthenticationException()

class AccessRestrictedException : AuthenticationException()
