package com.alex.domain.model


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "breed")
data class Breed(
    @Id val id: Long? = null,
    @Column("name") val name: String,
)
