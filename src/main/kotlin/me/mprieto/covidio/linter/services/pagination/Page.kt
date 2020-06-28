package me.mprieto.covidio.linter.services.pagination

data class Page<T>(val data: T, val total: Int)