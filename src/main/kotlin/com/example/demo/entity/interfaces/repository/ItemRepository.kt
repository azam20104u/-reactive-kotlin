package com.example.demo.entity.interfaces.repository

import com.example.demo.domain.model.ItemDomain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ItemRepository {
    fun save(itemDomain: ItemDomain): Mono<ItemDomain>
    fun findAll(): Flux<ItemDomain>
    fun findById(id: String): Mono<ItemDomain>
    fun deletedBy(id: String): Mono<Void>
}
