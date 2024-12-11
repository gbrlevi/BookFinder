package com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookFinder.ui.theme.DataSource
import com.example.BookFinder.ui.theme.com.example.BookFinder.Library
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrudViewModel : ViewModel() {
    private val originalLibraryList = mutableListOf<Library>()
    private val _libraryList = MutableStateFlow<List<Library>>(emptyList())
    val libraryList: StateFlow<List<Library>> get() = _libraryList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun updateList() {
        _isLoading.value = true
        val db = DataSource()
        db.getAllLibraries(
            onSuccess = { libraries ->
                Log.d("UpdateList", "Fetched Libraries: $libraries")
                originalLibraryList.clear()
                originalLibraryList.addAll(libraries)
                _libraryList.value = libraries.toList()
                _isLoading.value = false
            },
            onFailure = { e ->
                Log.e("LibraryInfo", "Error fetching libraries: ${e.message}")
                _isLoading.value = false
            }
        )
    }


    fun searchLibraries(query: String) {
        viewModelScope.launch {
            delay(10)
            _libraryList.value = if (query.isBlank()) {
                originalLibraryList.toList()
            } else {
                val filteredList = originalLibraryList
                    .filter { library ->
                        library.name.contains(query, ignoreCase = true)
                    }
                    .sortedWith(compareBy(
                        { library ->
                            if (library.name.startsWith(query, ignoreCase = true)) 0 else 1
                        },
                        { library ->
                            library.name.indexOf(query, ignoreCase = true)
                        }
                    ))

                Log.d("SearchLibraries", "Filtered List: $filteredList")

                filteredList
            }


        }
    }
    fun deleteBook(libId: String, bookId: String) {
        val db = DataSource()
        db.deleteBook(libId, bookId,
            onSuccess = {
                Log.d("DeleteBook", "Book deleted successfully")

                _libraryList.value = _libraryList.value.map { library ->
                    if (library.id == libId) {
                        library.copy(books = library.books.filterNot { it.name == bookId })
                    } else library
                }
            },
            onFailure = { e ->
                Log.e("DeleteBook", "Error deleting book: ${e.message}")
            }
        )
    }

    fun setQuantity(libId: String, bookId: String, quantity: Int) {
        val db = DataSource()
        db.updateBookQuantity(libId, bookId, quantity,
            onSuccess = {
                Log.d("SetQuantity", "Successful")
                _libraryList.value = _libraryList.value.map { library ->
                    if (library.id == libId) {
                        library.copy(books = library.books.map { book ->
                            if (book.name == bookId) book.copy(quantity = quantity) else book
                        })
                    } else library
                }
            },
            onFailure = { e ->
                Log.e("SetQuantity", "Error setting quantity: ${e.message}")
            }
        )
    }





}


