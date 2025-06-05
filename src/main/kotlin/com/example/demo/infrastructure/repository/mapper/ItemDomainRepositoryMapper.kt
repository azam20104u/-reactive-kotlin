package com.example.demo.infrastructure.repository.mapper

import com.example.demo.domain.model.ItemDomain
import com.example.demo.repository.dto.ItemDTO

object ItemDomainRepositoryMapper {
    fun toItemDTO(itemDomain: ItemDomain) = ItemDTO(
        id = itemDomain.id,
        name = itemDomain.name,
        price = itemDomain.price,
        category = itemDomain.category
    )

    fun toItemDomain(itemDTO: ItemDTO) = ItemDomain(
        id = itemDTO.id,
        name = itemDTO.name,
        price = itemDTO.price,
        category = itemDTO.category
    )
}
