package com.example.demo.web.handler

import com.example.demo.service.ItemService
import com.example.demo.web.mapper.ItemWebDomainMapper
import com.example.demo.web.model.ItemWebRequest
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ItemHandlerTest :
    DescribeSpec({
        val itemService = mockk<ItemService>()
        val itemHandler = ItemHandler(itemService)
        val id = "101"
        val itemWebRequest =
            ItemWebRequest(
                name = "Pen",
                price = 10.0.toBigDecimal(),
                category = "Stationery",
            )
        val itemDomain = ItemWebDomainMapper.toItemDomain(itemWebRequest)
        describe("itemHandler") {
            it("should create item") {
                val itemWebRequest =
                    MockServerRequest
                        .builder()
                        .body(Mono.just(itemWebRequest))
                every { itemService.create(any()) } returns Mono.just(itemDomain)
                val response: Mono<ServerResponse> = itemHandler.create(itemWebRequest)
                StepVerifier
                    .create(response)
                    .expectNextMatches { it.statusCode().is2xxSuccessful }
                    .verifyComplete()
            }
            it("should get all items") {
                val request = MockServerRequest.builder().build()
                every { itemService.getAll() } returns Flux.just(itemDomain)
                val response = itemHandler.getAll(request)
                StepVerifier
                    .create(response)
                    .expectNextMatches { it.statusCode().is2xxSuccessful }
                    .verifyComplete()
            }
            it("should get item by ID") {
                val request =
                    MockServerRequest
                        .builder()
                        .pathVariable("id", id)
                        .build()
                every { itemService.getById(id) } returns Mono.just(itemDomain)
                val response = itemHandler.getById(request)
                StepVerifier
                    .create(response)
                    .expectNextMatches { it.statusCode().is2xxSuccessful }
                    .verifyComplete()
            }
            it("should update item") {
                val updated = itemWebRequest.copy(price = 20.0.toBigDecimal())
                val updatedDomain = ItemWebDomainMapper.toItemDomain(updated)
                val request =
                    MockServerRequest
                        .builder()
                        .pathVariable("id", id)
                        .body(Mono.just(updated))
                every { itemService.update(id, updatedDomain) } returns Mono.just(updatedDomain)
                StepVerifier
                    .create(itemHandler.update(request))
                    .expectNextMatches { it.statusCode().is2xxSuccessful }
                    .verifyComplete()
            }
            it("should delete item") {
                val request =
                    MockServerRequest
                        .builder()
                        .pathVariable("id", id)
                        .build()
                every { itemService.delete(id) } returns Mono.empty()
                StepVerifier
                    .create(itemHandler.delete(request))
                    .expectNextMatches { it.statusCode().is2xxSuccessful }
                    .verifyComplete()
            }
        }
    })
