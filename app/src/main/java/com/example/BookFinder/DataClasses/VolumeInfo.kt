package com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val publishedDate: String?

) {
}