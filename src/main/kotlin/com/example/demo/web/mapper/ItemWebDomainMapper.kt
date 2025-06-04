package com.example.demo.web.mapper

import com.example.demo.domain.model.ItemDomain
import com.example.demo.web.model.ItemWebRequest
import com.example.demo.web.model.ItemWebResponse

object ItemWebDomainMapper {
    fun toItemDomain(itemWebRequest: ItemWebRequest) = ItemDomain(
        name = itemWebRequest.name,
        price = itemWebRequest.price,
        category = itemWebRequest.category
    )
    fun toItemWebResponse(itemDomain: ItemDomain) = ItemWebResponse(
        id = itemDomain.id,
        name = itemDomain.name,
        price = itemDomain.price,
        category = itemDomain.category
    )
}