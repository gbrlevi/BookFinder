package com.example.BookFinder.ui.theme.com.example.BookFinder

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.BookFinder.ui.theme.Book
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()

    suspend fun saveRecentBooks(userId: String, books: List<Book>) {
        val key = stringSetPreferencesKey("${userId}_recent_books")
        val booksJson = books.map { gson.toJson(it) }.toSet()
        context.dataStore.edit { preferences ->
            preferences[key] = booksJson
        }
    }

    suspend fun getRecentBooks(userId: String): List<Book> {
        val key = stringSetPreferencesKey("${userId}_recent_books")
        val preferences = context.dataStore.data.first()
        return preferences[key]?.map { gson.fromJson(it, Book::class.java) } ?: emptyList()
    }

    suspend fun saveRecentLibraries(userId: String, libraries: List<Library>) {
        val key = stringSetPreferencesKey("${userId}_recent_libraries")
        val librariesJson = libraries.map { gson.toJson(it) }.toSet()
        context.dataStore.edit { preferences ->
            preferences[key] = librariesJson
        }
    }

    suspend fun getRecentLibraries(userId: String): List<Library> {
        val key = stringSetPreferencesKey("${userId}_recent_libraries")
        val preferences = context.dataStore.data.first()
        return preferences[key]?.map { gson.fromJson(it, Library::class.java) } ?: emptyList()
    }

    suspend fun addRecentBook(userId: String, book: Book) {
        val recentBooks = getRecentBooks(userId).toMutableList()
        recentBooks.removeIf { it.name == book.name } // Remove duplicatas pelo nome do livro
        recentBooks.add(0, book) // Adiciona o livro no início
        if (recentBooks.size > 3) recentBooks.removeLast() // Mantém no máximo 3 livros
        saveRecentBooks(userId, recentBooks)
    }

    suspend fun addRecentLibrary(userId: String, library: Library) {
        val recentLibraries = getRecentLibraries(userId).toMutableList()
        recentLibraries.removeIf { it.name == library.name } // Remove duplicatas pelo nome da biblioteca
        recentLibraries.add(0, library) // Adiciona a biblioteca no início
        if (recentLibraries.size > 3) recentLibraries.removeLast() // Mantém no máximo 3 bibliotecas
        saveRecentLibraries(userId, recentLibraries)
    }


}
