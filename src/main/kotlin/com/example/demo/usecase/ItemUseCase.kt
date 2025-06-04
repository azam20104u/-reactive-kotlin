package com.example.demo.usecase

import com.example.demo.domain.model.ItemDomain
import com.example.demo.entity.interfaces.repository.ItemRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ItemUseCase(private val itemRepository: ItemRepository) {
    fun create(itemDomain: ItemDomain) : Mono<ItemDomain> = itemRepository.save(itemDomain)
    fun getAll(): Flux<ItemDomain> = itemRepository.findAll()
    fun getById(id: String) : Mono<ItemDomain> = itemRepository.findById(id)
    fun update(id: String, updated: ItemDomain) : Mono<ItemDomain> {
       return itemRepository.findById(id)
            .flatMap{
                itemRepository.save(updated.copy(id = id))
            }
    }
    fun delete(id: String) : Mono<Void> = itemRepository.deletedBy(id)
}