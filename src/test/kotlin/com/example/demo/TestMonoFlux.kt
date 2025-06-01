package com.example.demo

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.test.Test

class TestMonoFlux {
    @Test
    fun testMonoFlux() {
        val mono = Mono.just("Hello Kotlin Reactive")
            .then<String>(Mono.error(RuntimeException("error")))
            .log()
        mono.subscribe(::println)
    }

    @Test
    fun fluxTest() {
        val flux = Flux.just("Hello", "Hi")
            .concatWith(Flux.error(RuntimeException("just error")))
            .log()
        flux.subscribe(::println)
    }
}