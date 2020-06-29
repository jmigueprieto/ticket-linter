package me.mprieto.covidio.linter.exceptions

class RestClientException : LinterException {
    constructor(msg: String) : super(msg)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
