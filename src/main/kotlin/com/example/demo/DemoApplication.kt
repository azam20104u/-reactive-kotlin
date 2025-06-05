package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication
//startup of the application
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
