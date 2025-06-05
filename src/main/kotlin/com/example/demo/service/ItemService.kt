package com.example.demo.service

import com.example.demo.domain.model.ItemDomain
import com.example.demo.usecase.ItemUseCase
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ItemService(
    private val useCase: ItemUseCase
) {
    fun create(itemDomain: ItemDomain): Mono<ItemDomain> = useCase.create(itemDomain)
    fun getAll(): Flux<ItemDomain> = useCase.getAll()
    fun getById(id: String): Mono<ItemDomain> = useCase.getById(id)
    fun update(id: String, itemDomain: ItemDomain): Mono<ItemDomain> = useCase.update(id, itemDomain)
    fun delete(id: String): Mono<Void> = useCase.delete(id)
}
