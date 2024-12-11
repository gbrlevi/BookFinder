package com.example.BookFinder.ui.theme.com.example.BookFinder

import com.example.BookFinder.ui.theme.Book
import com.google.firebase.firestore.GeoPoint

data class Library(
    val id: String = "",
    val name: String = "",
    val location: GeoPoint? = null, // Alterado para GeoPoint
    val books: List<Book> = emptyList(),
    val accessibility: Boolean = false
) {
    constructor() : this("", "", null, emptyList(), false)
}