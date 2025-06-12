package com.example.demo.usecase

import com.example.demo.domain.model.ItemDomain
import com.example.demo.entity.interfaces.repository.ItemRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ItemUseCaseTest :
    DescribeSpec({
        val itemRepository = mockk<ItemRepository>()
        val itemUseCase = ItemUseCase(itemRepository)
        describe("ItemUseCase") {
            val itemDomain =
                ItemDomain(
                    name = "iPhone",
                    price = 10.0.toBigDecimal(),
                    category = "Phone",
                )
            it("it should create item") {
                every { itemRepository.save(any()) } returns Mono.just(itemDomain)
                StepVerifier
                    .create(itemUseCase.create(itemDomain))
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should get all items") {
                every { itemRepository.findAll() } returns Flux.just(itemDomain)
                StepVerifier
                    .create(itemUseCase.getAll())
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should get item by id") {
                every { itemRepository.findById("123") } returns Mono.just(itemDomain)
                StepVerifier
                    .create(itemUseCase.getById("123"))
                    .expectNext(itemDomain)
                    .verifyComplete()
            }
            it("should update item when ID exists") {
                val updatedItem = itemDomain.copy(name = "Updated Pen")

                every { itemRepository.findById("123") } returns Mono.just(itemDomain)
                every { itemRepository.save(any()) } returns Mono.just(updatedItem)

                StepVerifier
                    .create(itemUseCase.update("123", updatedItem))
                    .expectNext(updatedItem)
                    .verifyComplete()
            }

            it("should delete item by ID") {
                every { itemRepository.deletedBy("123") } returns Mono.empty()

                StepVerifier
                    .create(itemUseCase.delete("123"))
                    .verifyComplete()
            }
        }
    })
