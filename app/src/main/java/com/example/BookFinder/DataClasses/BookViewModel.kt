package com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.BookFinder.ui.theme.Book
import com.example.BookFinder.ui.theme.DataSource

class BookViewModel : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books
    private val _mostRelevantBooks = MutableStateFlow<List<Book>>(emptyList())
    val mostRelevantBooks: StateFlow<List<Book>> get() = _mostRelevantBooks
    var db = DataSource()

    // aq é o pedido obviamente que mapeia as informações na minha classe querida chamda books : )
    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchBooks(query, "relevance",apiKey)
                val bookList = response.items.map {
                    item ->
                    val volumeInfo = item.volumeInfo
                    val imageUrl = volumeInfo.imageLinks?.let { imageLinks ->
                        // Use the highest-resolution image available
                        imageLinks.large ?: imageLinks.medium ?: imageLinks.small ?: imageLinks.thumbnail
                    } ?: "https://picsum.photos/id/237/200/300" // Fallback to placeholder

                    Book(
                        name = item.volumeInfo.title,
                        author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        date = item.volumeInfo.publishedDate ?: "Unknown",
                        imageUrl = imageUrl,
                        likes = 0,
                        sinopsis = item.volumeInfo.description ?: "No description"
                    )
                }
                _books.value = bookList
            } catch (e: Exception) {
                e.printStackTrace()
                _books.value = emptyList()
            }
        }
    }
    fun fetchRecentBooks(query: String, apiKey: String, maxResults: Int = 5) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.fetchBooks(
                    query = query,
                    orderBy = "newest",
                    maxResults = maxResults,
                    key = apiKey
                )
                if (response.items.isNullOrEmpty()) {
                    Log.w("fetchRecentBooks", "No books found for query: $query")
                    _books.value = emptyList()
                    return@launch
                }

                val bookList = response.items.map { item ->
                    val volumeInfo = item.volumeInfo
                    val imageUrl = volumeInfo.imageLinks?.let { imageLinks ->
                        // Use the highest-resolution image available
                        imageLinks.large ?: imageLinks.medium ?: imageLinks.small ?: imageLinks.thumbnail
                    } ?: "https://picsum.photos/id/237/200/300" // Fallback to placeholder

                    Book(
                        name = volumeInfo.title ?: "Unknown",
                        author = volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        date = volumeInfo.publishedDate ?: "Unknown",
                        imageUrl = imageUrl,
                        likes = 0,
                        sinopsis = volumeInfo.description ?: "No description"
                    )
                }
                _books.value = bookList
            } catch (e: Exception) {
                Log.e("fetchRecentBooks", "Exception fetching books", e)
                _books.value = emptyList()
            }
        }
    }
    fun fetchMostRelevantBooks(query: String, apiKey: String, maxResults: Int = 5) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.fetchBooks(
                    query = query,
                    orderBy = "relevance",
                    maxResults = maxResults,
                    key = apiKey
                )
                if (response.items.isNullOrEmpty()) {
                    Log.w("fetchRecentBooks", "No books found for query: $query")
                    _mostRelevantBooks.value = emptyList()
                    return@launch
                }

                val mostRelevantBook = response.items.map { item ->
                    val volumeInfo = item.volumeInfo
                    val imageUrl = volumeInfo.imageLinks?.let { imageLinks ->
                        // Use the highest-resolution image available
                        imageLinks.large ?: imageLinks.medium ?: imageLinks.small ?: imageLinks.thumbnail
                    } ?: "https://picsum.photos/id/237/200/300" // Fallback to placeholder

                    Book(
                        name = volumeInfo.title ?: "Unknown",
                        author = volumeInfo.authors?.joinToString(", ") ?: "Unknown",
                        date = volumeInfo.publishedDate ?: "Unknown",
                        imageUrl = imageUrl,
                        likes = 0,
                        sinopsis = volumeInfo.description ?: "No description"
                    )
                }
                _mostRelevantBooks.value = mostRelevantBook
            } catch (e: Exception) {
                Log.e("fetchRecentBooks", "Exception fetching books", e)
                _books.value = emptyList()
            }
        }
    }
    }

