package me.mprieto.covidio.linter.utils

import org.mockito.Mockito
import org.springframework.core.io.ClassPathResource

fun <T> whenever(x: T) = Mockito.`when`(x)!!

fun getResourceAsString(path: String) = ClassPathResource(path).url.readText()
