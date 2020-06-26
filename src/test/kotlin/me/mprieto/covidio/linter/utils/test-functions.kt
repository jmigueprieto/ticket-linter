package me.mprieto.covidio.linter.utils

import org.mockito.Mockito

fun <T> whenever(x: T) = Mockito.`when`(x)