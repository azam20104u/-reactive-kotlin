package com.example.demo.infrastructure.repository.service

import com.example.demo.infrastructure.repository.mapper.ItemDomainRepositoryMapper
import com.example.demo.repository.dto.ItemDTO
import com.mongodb.client.result.DeleteResult
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.remove
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ItemRepositoryImplTest :
    DescribeSpec({
        val mongoTemplate = mockk<ReactiveMongoTemplate>()
        val repository = ItemRepositoryImpl(mongoTemplate)
        describe("ItemRepositoryImpl") {
            it("should insert a new item when ID is null") {
                val itemDTO =
                    ItemDTO(
                        id = null,
                        name = "Pen",
                        category = "Stationary",
                        price = 15.0.toBigDecimal()
                    )
                val itemDomain = ItemDomainRepositoryMapper.toItemDomain(itemDTO)
                every {
                    mongoTemplate.insert(any<ItemDTO>(), "items")
                } returns Mono.just(itemDTO)
                StepVerifier
                    .create(repository.save(itemDomain))
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should update an item when ID is present") {
                val itemDTO =
                    ItemDTO(
                        id = "101",
                        name = "Pen",
                        category = "Stationary",
                        price = 15.0.toBigDecimal()
                    )
                val itemDomain = ItemDomainRepositoryMapper.toItemDomain(itemDTO)
                val query = Query(Criteria.where("id").`is`("101"))
                every {
                    mongoTemplate.findAndReplace(query, any<ItemDTO>(), "items")
                } returns Mono.just(itemDTO)
                StepVerifier
                    .create(repository.save(itemDomain))
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should return item by ID") {
                val itemDTO =
                    ItemDTO(
                        id = "101",
                        name = "Pen",
                        category = "Stationary",
                        price = 15.0.toBigDecimal()
                    )
                val itemDomain = ItemDomainRepositoryMapper.toItemDomain(itemDTO)
                every {
                    mongoTemplate.findOne(any(), ItemDTO::class.java, "items")
                } returns Mono.just(itemDTO)
                StepVerifier
                    .create(repository.findById("101"))
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should return error when item not found by ID") {
                val id = "100"
                every {
                    mongoTemplate.findOne(any(), ItemDTO::class.java, "items")
                } returns Mono.empty()
                StepVerifier
                    .create(repository.findById(id))
                    .expectErrorMatches { it is IllegalArgumentException && it.message == "Item with id: $id not found" }
            }
            it("should delete item by ID") {
                every {
                    mongoTemplate.remove(
                        any(),
                        ItemDTO::class.java,
                        "items"
                    )
                } returns Mono.just(DeleteResult.acknowledged(1))
                StepVerifier
                    .create(repository.deletedBy("101"))
                    .verifyComplete()
            }
            it("should return error when delete fails") {
                val id = "101"
                every {
                    mongoTemplate.remove(
                        any(),
                        ItemDTO::class.java,
                        "items"
                    )
                } returns Mono.just(DeleteResult.acknowledged(0))
                StepVerifier
                    .create(repository.deletedBy(id))
                    .expectErrorMatches { it is IllegalArgumentException && it.message == "Item with id: $id not found" }
            }
        }
    })
