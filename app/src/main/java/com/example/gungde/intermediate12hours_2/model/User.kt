package com.example.gungde.intermediate12hours_2.model

/**
 * Created by gungdeaditya on 2/26/18.
 */

data class User(
		val status: Boolean, //true
		val message: String, //User exist
		val data: Data
)

data class Data(
		val _id: String, //5a93dbe5bf29600014c2974a
		val email: String, //gunkdep@gmail.com
		val password: String, //$2a$10$fPXNBWqLvwjiiQT35Tcps.Fva0A1nzu.EO72lV7D/.O3M9EDqs07i
		val name: String, //Gungde Aditya
		val phoneNumber: String, //085772136367
		val __v: Int //0
)