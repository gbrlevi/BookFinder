package com.example.BookFinder.ui.theme

import android.annotation.SuppressLint
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.util.Log
import com.example.BookFinder.ui.theme.com.example.BookFinder.Library
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.net.URLEncoder

class DataSource {

    private val db = FirebaseFirestore.getInstance()


    fun like(book: Book, value: Int) {
        val booksRef = db.collection("likes")

        booksRef.document(book.name).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val bookRef = booksRef.document(book.name)
                    bookRef.update("likes", FieldValue.increment(value.toLong()))
                } else {
                    val newBook = mapOf(
                        "name" to book.name,
                        "author" to book.author,
                        "date" to book.date,
                        "imageUrl" to book.imageUrl,
                        "likes" to 1,
                        "sinopsis" to book.sinopsis,
                        "quantity" to book.quantity
                    )
                    booksRef.document(book.name).set(newBook)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun fetchLikedBooks(
        onSuccess: (List<Book>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val booksRef = db.collection("likes")

        booksRef.get()
            .addOnSuccessListener { querySnapshot ->
                val books = querySnapshot.documents.mapNotNull { document ->
                    try {
                        // Map the Firestore document to a Book object
                        Book(
                            name = document.getString("name") ?: "Unknown",
                            author = document.getString("author") ?: "Unknown",
                            date = document.getString("date") ?: "Unknown",
                            imageUrl = document.getString("imageUrl") ?: "",
                            likes = document.getLong("likes")?.toInt() ?: 0,
                            sinopsis = document.getString("sinopsis") ?: "No description",
                            quantity = document.getLong("quantity")?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null // Skip invalid documents
                    }
                }
                onSuccess(books) // Return the list of books
            }
            .addOnFailureListener { e ->
                onFailure(e) // Handle failure case
            }
    }


    fun getLikes(bookName: String, onResult: (Int?) -> Unit) {

        val safeBookName = bookName.replace("/", "_") // Replace slashes with underscores
        val booksRef = db.collection("likes")

        booksRef.document(safeBookName).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val likes = documentSnapshot.getLong("likes")?.toInt()
                    println("Book with name $safeBookName found: $likes likes")
                    onResult(likes)
                } else {
                    onResult(0)
                    println("Book with name $safeBookName not found")
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                onResult(0)
            }
    }


    fun addBooksToLibrary(
        libraryId: String,
        books: List<Book>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val db = FirebaseFirestore.getInstance()


        val booksData = books.map { book ->
            mapOf(
                "name" to book.name,
                "author" to book.author,
                "date" to book.date,
                "imageUrl" to book.imageUrl,
                "likes" to book.likes,
                "sinopsis" to book.sinopsis,
                "quantity" to book.quantity
            )
        }

        db.collection("libraries").document(libraryId)
            .update("books", FieldValue.arrayUnion(*booksData.toTypedArray()))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun addLibrary(
        libraryName: String,
        location: GeoPoint?,
        books: List<String>,
        accessibility: Boolean
    ) {
        val libraryData = hashMapOf(
            "name" to libraryName,
            "location" to location,
            "books" to books,
            "accessibility" to accessibility
        )


        db.collection("libraries").document(libraryName)
            .set(libraryData)
            .addOnSuccessListener {
                println("Library added with name as ID: $libraryName")
            }
            .addOnFailureListener { e ->
                println("Error adding library: ${e.message}")
            }
    }

    fun updateLibrary(
        CurrentLibraryName: String,
        libraryName: String,
        location: GeoPoint?,
        accessibility: Boolean
    ) {
        val updates = mapOf(
            "name" to libraryName,
            "location" to location,
            "accessibility" to accessibility
        )

        db.collection("libraries").document(CurrentLibraryName)
            .update(updates)
            .addOnSuccessListener {
                println("Library updated with name as ID: $libraryName")
            }
            .addOnFailureListener { e ->
                println("Error updating library: ${e.message}")
            }
    }

    fun getLibrariesWithBook(
        bookName: String,
        onSuccess: (List<Library>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("libraries")
            .get()
            .addOnSuccessListener { documents ->
                val libraries = documents.mapNotNull { document ->
                    val library = document.toObject(Library::class.java).copy(id = document.id)

                    if (library.books.any { it.name == bookName }) {
                        library
                    } else {
                        null
                    }
                }
                onSuccess(libraries)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }


    fun getAllLibraries(onSuccess: (List<Library>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("libraries")
            .get()
            .addOnSuccessListener { documents ->
                val libraries = documents.mapNotNull { document ->
                    document.toObject(Library::class.java).copy(id = document.reference.id)
                }
                onSuccess(libraries)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun deleteLibrary(libraryId: String) {
        db.collection("libraries").document(libraryId)
            .delete()

    }

    fun addBookToLikedBooks(book: Book, ) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val bookData = mapOf(
                "name" to book.name,
                "author" to book.author,
                "date" to book.date,
                "imageUrl" to book.imageUrl,
                "likes" to book.likes,
                "sinopsis" to book.sinopsis
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .update("likedBooks", FieldValue.arrayUnion(bookData))
                .addOnSuccessListener {

                }
                .addOnFailureListener { exception ->

                }
        } else {

        }
    }

    fun removeBookFromLikedBooks(book: Book, ) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val bookData = mapOf(
                "name" to book.name,
                "author" to book.author,
                "date" to book.date,
                "imageUrl" to book.imageUrl,
                "likes" to book.likes,
                "sinopsis" to book.sinopsis
            )

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .update("likedBooks", FieldValue.arrayRemove(bookData))
                .addOnSuccessListener {

                }
                .addOnFailureListener { exception ->
                }
        } else {
        }
    }

    fun getLikedBooksForUser(onSuccess: (List<Book>) -> Unit, onFailure: (Exception) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("likedBooks")) {
                        val likedBooksArray =
                            document["likedBooks"] as? List<Map<String, Any>> ?: emptyList()
                        val likedBooks = likedBooksArray.map { bookMap ->
                            Book(
                                name = bookMap["name"] as? String ?: "",
                                author = bookMap["author"] as? String ?: "",
                                date = bookMap["date"] as? String ?: "",
                                imageUrl = bookMap["imageUrl"] as? String ?: "",
                                likes = (bookMap["likes"] as? Long)?.toInt() ?: 0,
                                sinopsis = bookMap["sinopsis"] as? String ?: ""
                            )
                        }
                        onSuccess(likedBooks)
                    } else {
                        onSuccess(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(Exception("User ID is null"))
        }
    }


    fun getBookFromLibrary(
        libraryId: String,
        bookName: String,
        onSuccess: (Book?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val tag = "BookFinder"
        db.collection("libraries").document(libraryId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    val library = document.toObject(Library::class.java)

                    // Find the book in the library's books list
                    val book = library?.books?.find { it.name == bookName }

                    // Log the book details
                    Log.d(tag, "Found book '$bookName' in library '$libraryId': $book")

                    // Pass the book or null (if not found) to onSuccess
                    onSuccess(book)
                } else {
                    // Log when the library document does not exist
                    Log.d(tag, "Library document with ID '$libraryId' does not exist.")
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                // Log the exception message for debugging
                Log.d(tag, "Error retrieving data: ${exception.message}")
                // Pass the exception to the onFailure callback
                onFailure(exception)
            }
    }

    fun deleteBook(
        libraryId: String,
        bookId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val libraryRef = db.collection("libraries").document(libraryId)

        libraryRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val books = document.toObject(Library::class.java)?.books ?: emptyList()

                    val updatedBooks = books.filterNot { it.name == bookId }

                    libraryRef.update("books", updatedBooks)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                } else {
                    onFailure(Exception("Library document does not exist"))
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateBookQuantity(
        libraryId: String,
        bookId: String,
        newQuantity: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val libraryRef = db.collection("libraries").document(libraryId)

        libraryRef.get()
            .addOnSuccessListener { document ->
                val books = document.toObject(Library::class.java)?.books ?: emptyList()
                val updatedBooks = books.map { book ->
                    if (book.name == bookId) book.copy(quantity = newQuantity) else book
                }

                libraryRef.update("books", updatedBooks)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getLibraryById(
        libraryId: String,
        onSuccess: (Library) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("libraries").document(libraryId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val library = document.toObject(Library::class.java)?.copy(id = libraryId)
                    if (library != null) {
                        onSuccess(library)
                    } else {
                        onFailure(Exception("Biblioteca não encontrada"))
                    }
                } else {
                    onFailure(Exception("Biblioteca não encontrada"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
    fun geoPointToAddress(geoPoint: GeoPoint?): String
    {
        return if (geoPoint != null) {
            "Lat: ${geoPoint.latitude}, Lng: ${geoPoint.longitude}"
        } else {
            "Location not available"
        }
    }

    fun getUserLocation(
        fusedLocationClient: FusedLocationProviderClient,
        library: Library,
        userLocation: Location?,
        onDistanceCalculated: (String) -> Unit
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null && library.location != null) {
                val libraryGeoPoint = library.location
                val distance = calculateDistance(location, libraryGeoPoint)
                val distanceInKm = distance / 1000 // Convert to kilometers
                onDistanceCalculated(distanceInKm.toString())
            } else {
                onDistanceCalculated("Location not available")
            }
        }
    }

    // Function to calculate distance in meters
    fun calculateDistance(userLocation: Location, libraryLocation: GeoPoint): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            userLocation.latitude,
            userLocation.longitude,
            libraryLocation.latitude,
            libraryLocation.longitude,
            results
        )
        return results[0].toDouble() // Return distance in meters
    }
    fun getLibraryLocation(library: Library): Location {
        val libraryLocation = Location("library")
        libraryLocation.latitude = library.location?.latitude ?: 0.0
        libraryLocation.longitude = library.location?.longitude ?: 0.0
        return libraryLocation
    }
    fun calculateDistance2(userLocation: Location, library: Library): Float {
        val libraryLocation = getLibraryLocation(library)
        return userLocation.distanceTo(libraryLocation)
    }
}