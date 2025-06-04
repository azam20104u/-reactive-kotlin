package com.example.demo.infrastructure.repository.service

import com.example.demo.domain.model.ItemDomain
import com.example.demo.entity.interfaces.repository.ItemRepository
import com.example.demo.infrastructure.repository.mapper.ItemDomainRepositoryMapper
import com.example.demo.repository.dto.ItemDTO
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ItemRepositoryImpl(
    private val mongoTemplate: ReactiveMongoTemplate,
) : ItemRepository {
    companion object {
        const val COLLECTION_ITEM = "items"
    }

    override fun save(itemDomain: ItemDomain): Mono<ItemDomain> {
        val itemDto = ItemDomainRepositoryMapper.toItemDTO(itemDomain)
        return if (itemDto.id.isNullOrBlank()) {
            mongoTemplate
                .insert(itemDto, COLLECTION_ITEM)
                .map(ItemDomainRepositoryMapper::toItemDomain)
        } else {
            val query = Query(Criteria.where("id").`is`(itemDto.id))
            mongoTemplate
                .findAndReplace(query, itemDto, COLLECTION_ITEM)
                .flatMap { Mono.just(ItemDomainRepositoryMapper.toItemDomain(itemDto)) }
                .switchIfEmpty(Mono.error(IllegalArgumentException("Item with id: ${itemDto.id} not found")))
        }
    }

    override fun findAll(): Flux<ItemDomain> =
        mongoTemplate
            .findAll(ItemDTO::class.java, COLLECTION_ITEM)
            .map(ItemDomainRepositoryMapper::toItemDomain)

    override fun findById(id: String): Mono<ItemDomain> {
        val query = Query(Criteria.where("id").`is`(id))
        return mongoTemplate
            .findOne(query, ItemDTO::class.java, COLLECTION_ITEM)
            .map(ItemDomainRepositoryMapper::toItemDomain)
            .switchIfEmpty(Mono.error(IllegalArgumentException("Item with id: $id not found")))
    }

    override fun deletedBy(id: String): Mono<Void> {
        val query = Query(Criteria.where("id").`is`(id))
        return mongoTemplate
            .remove(query, ItemDTO::class.java, COLLECTION_ITEM)
            .flatMap { result ->
                if (result.deletedCount > 0) {
                    Mono.empty()
                } else {
                    Mono.error(IllegalArgumentException("Item with id: $id not found"))
                }
            }
    }
}
