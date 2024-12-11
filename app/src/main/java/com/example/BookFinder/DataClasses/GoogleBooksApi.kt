package com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses

import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleBooksApi {
    // comentando para tu lembrar depois
    @GET("volumes") // isso aq é endpoint que tu quer chegar
    suspend fun searchBooks(
        @Query("q") query: String, // obviamente é a query
        @Query("orderBy") orderBy: String = "relevance", // Ordenar por relevância por padrão
        @Query("key") apiKey: String // aq é tua chave
    ): BookResponse // isso aq é tua resposta no formato JSON e tu ta dizendo q retorna esse tipo

    @GET("volumes")
    suspend fun fetchBooks(
        @Query("q") query: String,
        @Query("orderBy") orderBy: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") key: String
    ): BookResponse
}
