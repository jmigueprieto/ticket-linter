package me.mprieto.covidio.linter.utils

import org.mockito.Mockito
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.ClassPathResource

fun <T> whenever(x: T) = Mockito.`when`(x)!!

fun getResourceAsString(path: String) = ClassPathResource(path).url.readText()

inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)!!

// https://stackoverflow.com/questions/39679180/kotlin-call-java-method-with-classt-argument
inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}
