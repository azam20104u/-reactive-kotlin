package com.example.demo.infrastructure.repository

import com.example.demo.repository.dto.ItemDTO
import java.util.UUID

object DBStores {
    private val store = mutableMapOf<String, ItemDTO>()
    fun save(itemDTO: ItemDTO): ItemDTO {
        val generatedId = UUID.randomUUID().toString()
        val itemWith = itemDTO.copy(id = generatedId)
        store[generatedId] = itemWith
        return itemWith
    }
    fun findById(id: String): ItemDTO? = store[id]
    fun findAll(): List<ItemDTO> = store.values.toList()
    fun update(id: String, updatedItem: ItemDTO): ItemDTO? {
        return if(store.containsKey(id)){
            store[id] = updatedItem
            updatedItem
        } else null
    }
    fun delete(id: String): Boolean = store.remove(id) != null
}