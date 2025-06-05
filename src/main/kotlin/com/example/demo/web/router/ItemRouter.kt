package com.example.demo.web.router

import com.example.demo.web.handler.ItemHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class ItemRouter {
    @Bean
    fun routes(handler: ItemHandler) = router {
        "items".nest {
            POST("/", handler::create)
            GET("/", handler::getAll)
            GET("/{id}", handler::getById)
            PUT("{id}", handler::update)
            DELETE("/{id}", handler::delete)
        }
    }
}
