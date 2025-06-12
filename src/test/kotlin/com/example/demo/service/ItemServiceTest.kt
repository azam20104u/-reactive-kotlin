package com.example.demo.service

import com.example.demo.domain.model.ItemDomain
import com.example.demo.usecase.ItemUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal

class ItemServiceTest :
    DescribeSpec({

        val itemUseCase = mockk<ItemUseCase>()
        val itemService = ItemService(itemUseCase)

        val id = "101"
        val item =
            ItemDomain(
                id = id,
                name = "Pen",
                price = BigDecimal.valueOf(15.0),
                category = "Stationery"
            )

        describe("ItemService") {

            it("should create item") {
                every { itemUseCase.create(item) } returns Mono.just(item)

                StepVerifier
                    .create(itemService.create(item))
                    .expectNext(item)
                    .verifyComplete()
            }

            it("should return all items") {
                every { itemUseCase.getAll() } returns Flux.just(item)

                StepVerifier
                    .create(itemService.getAll())
                    .expectNext(item)
                    .verifyComplete()
            }

            it("should return by ID") {
                every { itemUseCase.getById(id) } returns Mono.just(item)

                StepVerifier
                    .create(itemService.getById(id))
                    .expectNext(item)
                    .verifyComplete()
            }

            it("should update item") {
                val updatedItem = item.copy(price = BigDecimal.valueOf(20.0))

                every { itemUseCase.update(id, updatedItem) } returns Mono.just(updatedItem)

                StepVerifier
                    .create(itemService.update(id, updatedItem))
                    .expectNext(item)
                    .verifyComplete()
            }

            it("should delete item") {
                every { itemUseCase.delete(id) } returns Mono.empty()

                StepVerifier
                    .create(itemService.delete(id))
                    .verifyComplete()
            }
        }
    })
