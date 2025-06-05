package com.example.demo.web.handler

import com.example.demo.service.ItemService
import com.example.demo.web.mapper.ItemWebDomainMapper
import com.example.demo.web.model.ItemWebRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ItemHandler(
    private val itemService: ItemService
) {
    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(ItemWebRequest::class.java)
            .map(ItemWebDomainMapper::toItemDomain)
            .flatMap(itemService::create)
            .map(ItemWebDomainMapper::toItemWebResponse)
            .flatMap { ServerResponse.ok().bodyValue(it) }
    fun getAll(request: ServerRequest): Mono<ServerResponse> =
        itemService.getAll()
            .map(ItemWebDomainMapper::toItemWebResponse)
            .collectList()
            .flatMap { ServerResponse.ok().bodyValue(it) }
    fun getById(request: ServerRequest): Mono<ServerResponse> =
        itemService.getById(request.pathVariable("id"))
            .map(ItemWebDomainMapper::toItemWebResponse)
            .flatMap { ServerResponse.ok().bodyValue(it) }
            .switchIfEmpty(ServerResponse.notFound().build())
    fun update(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(ItemWebRequest::class.java)
            .map(ItemWebDomainMapper::toItemDomain)
            .flatMap { itemService.update(request.pathVariable("id"), it) }
            .map(ItemWebDomainMapper::toItemWebResponse)
            .flatMap { ServerResponse.ok().bodyValue(it) }
    fun delete(request: ServerRequest): Mono<ServerResponse> =
        itemService.delete(request.pathVariable("id"))
            .then(ServerResponse.noContent().build())
}
