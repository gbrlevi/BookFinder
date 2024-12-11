package com.example.BookFinder.ui.theme

import com.google.firebase.firestore.PropertyName


data class Book(
    @PropertyName("name") val name: String = "",
    @PropertyName("author") val author: String = "",
    @PropertyName("date") val date: String = "", // For now, keep as String
    @PropertyName("imageUrl") val imageUrl: String = "",
    @PropertyName("likes") var likes: Int = 0,
    @PropertyName("sinopsis") val sinopsis: String = "",
    @PropertyName("quantity") var quantity: Int = 0, //
    @PropertyName("quantity") var isLikedYet: Boolean = false //

)