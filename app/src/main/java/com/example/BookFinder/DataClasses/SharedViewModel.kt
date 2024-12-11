package com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookFinder.ui.theme.Book
import com.example.BookFinder.ui.theme.DataSource
import kotlinx.coroutines.launch
class SharedViewModel : ViewModel() {
    private val _likedBooks = mutableStateListOf<Book>()
    val likedBooks: SnapshotStateList<Book> = _likedBooks
    val isLoading = mutableStateOf(true) // Track loading state

    init {
        fetchLikedBooks()
    }

    private fun fetchLikedBooks() {
        viewModelScope.launch {
            val db = DataSource()
            db.getLikedBooksForUser(
                onSuccess = { books ->
                    _likedBooks.clear()
                    _likedBooks.addAll(books)
                    Log.d("SharedViewModel", "Fetched liked books: ${_likedBooks.map { it.name }}")
                    isLoading.value = false // Mark loading as complete
                },
                onFailure = { error ->
                    Log.e("SharedViewModel", "Error fetching books: ${error.message}")
                    isLoading.value = false // Mark loading as complete even on failure
                }
            )
        }
    }

    fun addBook(book: Book) {
        if (!_likedBooks.any { it.name == book.name }) {
            _likedBooks.add(book)
            Log.d("SharedViewModel", "Added book: ${book.name}")
        }
    }

    fun removeBook(book: Book) {
        if (_likedBooks.removeIf { it.name == book.name }) {
            Log.d("SharedViewModel", "Removed book: ${book.name}")
        }
    }

    fun contains(book: Book): Boolean {
        return _likedBooks.any { it.name == book.name }
    }

}
