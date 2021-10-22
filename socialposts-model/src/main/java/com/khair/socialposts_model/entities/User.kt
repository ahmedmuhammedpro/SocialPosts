package com.khair.socialposts_model.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val userId: Int, val username: String ,val name: String,
    val email: String, val address: Address, val phone: String, val website: String, val company: Company
)

data class Address(
    val street: String, val suite: String, val zipcode: String, val city: String, val geo: Geo
)

data class Geo (val lat: String, val lng: String)

data class Company (
    val name: String, val catchPhrase: String, val bs: String
)
