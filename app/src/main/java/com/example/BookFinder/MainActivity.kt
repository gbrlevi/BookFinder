package com.example.BookFinder

import android.annotation.SuppressLint
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import android.location.Location
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.VisualTransformation
import com.google.android.gms.location.LocationServices
import com.example.BookFinder.ui.theme.Book
import com.example.BookFinder.ui.theme.BookFinderTheme
import com.example.BookFinder.ui.theme.BookList
import com.example.BookFinder.ui.theme.DataSource
import com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses.BookViewModel
import com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses.CrudViewModel
import com.example.BookFinder.ui.theme.com.example.BookFinder.DataStoreManager
import com.example.BookFinder.ui.theme.com.example.BookFinder.Library
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.firestore.SetOptions
import androidx.compose.ui.unit.TextUnit
import androidx.core.content.ContextCompat
import com.example.BookFinder.ui.theme.com.example.BookFinder.DataClasses.SharedViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay


import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            var isLogged by rememberSaveable() {
                mutableStateOf(false)
            }
            val auth = FirebaseAuth.getInstance()

            DisposableEffect(Unit) {
                val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                    isLogged = auth.currentUser != null
                }
                auth.addAuthStateListener(authStateListener)

                onDispose {
                    auth.removeAuthStateListener(authStateListener)
                }
            }
            var booklist = BookList(navController)
            BookFinderTheme {
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") { app(booklist,isLogged,navController) }
                    composable("libDetailScreen/{libraryJson}") { backStackEntry ->
                        val gson = Gson()
                        val libraryJson = backStackEntry.arguments?.getString("libraryJson") ?: ""
                        val library = try {
                            gson.fromJson(libraryJson, Library::class.java)
                        } catch (e: Exception) {
                            Log.e("Navigation", "Failed to parse library JSON", e)
                            null
                        }
                        if (library != null) {
                            LibDetailScreen(library, navController)
                        } else {
                            // Mostra uma tela de erro ou navega de volta
                            Text("Erro ao carregar os dados da biblioteca")
                        }
                    }

                    composable("searchScreen") { SearchScreen(navController) }
                    composable("searchScreenCrud/{libraryId}") {  backStackEntry ->
                        val libId = backStackEntry.arguments?.getString("libraryId") ?: ""
                        SearchScreenCrud(navController,libId) }
                    composable("bookDetailScreen/{bookName}/{bookAuthor}/{bookDate}/{bookImage}/{bookLikes}/{bookSinopsis}") { backStackEntry ->
                        val bookName = backStackEntry.arguments?.getString("bookName") ?: ""
                        val bookAuthor = backStackEntry.arguments?.getString("bookAuthor") ?: ""
                        val bookDate = backStackEntry.arguments?.getString("bookDate") ?: ""

                        // Convert the image string back to Int
                        val bookImage = backStackEntry.arguments?.getString("bookImage") ?: ""

                        val bookLikes = backStackEntry.arguments?.getString("bookLikes")?.toIntOrNull() ?: 0
                        val bookSinopsis = backStackEntry.arguments?.getString("bookSinopsis") ?: ""

                        BookDetailScreen(
                            book = Book(bookName, bookAuthor, bookDate, bookImage, bookLikes, bookSinopsis),
                            navController = navController
                        )
                    }
                    composable("LibDetailCrudScreen/{libraryId}") { backStackEntry ->
                        val libraryId = backStackEntry.arguments?.getString("libraryId") ?: ""
                        val crudViewModel: CrudViewModel = viewModel()
                        LibDetailCrudScreen(libraryId, navController, crudViewModel)
                    }


                    composable("loginScreen") { LoginScreen(navController = navController, isLogged = remember { mutableStateOf(isLogged) }) }
                    composable("registerScreen") { RegisterScreen(navController = navController) }
                    composable("successScreen") { SuccessScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                }
            }
        }
    }
}


// these are the navItem, the navItem class is a class to display the bottomBar icons
sealed class navItem(
    val icon: ImageVector,
    val label: String,
    var color: Color,
    var page: Int
) {
    data object main : navItem(icon = Icons.Default.Home, "Home", Color(0xFF00BCD4), 0)
    data object map : navItem(icon = Icons.Default.Map, "Map", Color(0xFF4CAF50), 1)
    data object favorites : navItem(icon = Icons.Default.Favorite, "Favorites", Color(0xFFFF5252), 2)
    data object community :
        navItem(icon = Icons.Default.PeopleAlt, "Community", Color(0xFFFF9800), 3)
    data object crud : navItem(icon = Icons.Default.Create, "Crud", Color(0xFFDC3545), 4)


}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun app(
    bookList: BookList,
    isLogged: Boolean,
    navController: NavHostController
) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    val isAdmin = auth.currentUser?.email == "blabla@exemplo.com" // Verifica se o usuário é admin
    var libraries by remember { mutableStateOf<List<Library>>(emptyList()) }

    // Carregar as bibliotecas do Firebase
    LaunchedEffect(Unit) {
        val db = DataSource()
        db.getAllLibraries(
            onSuccess = { fetchedLibraries ->
                libraries = fetchedLibraries
            },
            onFailure = { error ->
                Log.e("App", "Erro ao buscar bibliotecas: ${error.message}")
            }
        )
    }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<Location?>(null) }

    // Obter a localização do usuário
    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                userLocation = location
            }.addOnFailureListener { exception ->
                Log.e("App", "Error fetching location: ${exception.message}")
            }
        } catch (e: SecurityException) {
            Log.e("App", "Permission not granted for location access")
        }
    }


    // Define os itens de navegação com base no status do admin
    val navItems = listOfNotNull(
        navItem.main,
        navItem.map,
        navItem.favorites,
        navItem.community,
        if (isAdmin) navItem.crud else null // Adiciona "Crud" apenas se for admin
    )

    var selectedItem by remember { mutableStateOf(navItems.first()) }
    val pagerState = rememberPagerState { navItems.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        if (isLogged) {
                            navController.navigate("profile")
                        } else {
                            navController.navigate("loginScreen") {
                                popUpTo("mainScreen") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "BookFinder", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("searchScreen")
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White,
                modifier = Modifier.height(70.dp)
            ) {
                for (item in navItems) {
                    NavigationBarItem(
                        selected = item == selectedItem,
                        onClick = {
                            selectedItem = item
                            coroutineScope.launch { pagerState.scrollToPage(item.page) }
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = "",
                                tint = if (item == selectedItem) item.color else Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = {
                            Text(text = item.label, color = Color.White)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(state = pagerState, Modifier.padding(innerPadding),     userScrollEnabled = selectedItem != navItem.map
        ) { page ->
            val item = navItems[page]

            when (item) {
                navItem.main -> {
                    selectedItem = navItems[pagerState.currentPage]
                    MainScreen(navController)
                }

                navItem.map -> {
                    selectedItem = navItems[pagerState.currentPage]
                    MapScreen(navController, libraries = libraries, userLocation = userLocation)
                }

                navItem.favorites -> {
                    selectedItem = navItems[pagerState.currentPage]
                    FavoritesScreen(navController, bookList)
                }

                navItem.community -> {
                    selectedItem = navItems[pagerState.currentPage]
                    CommunityScreenWrapper(navController = navController, bookList = bookList)
                }

                navItem.crud -> {
                    selectedItem = navItems[pagerState.currentPage]
                    CrudScreen(navController)
                }
            }
        }
    }
}

// do not include this
@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    var sharedViewModel = SharedViewModel()

    val recentBooks = remember { mutableStateListOf<Book>() }
    val recentLibraries = remember { mutableStateListOf<Library>() }
    val booksLoading = remember { mutableStateOf(true) }
    val librariesLoading = remember { mutableStateOf(true) }

    val booksMessage =
        remember { mutableStateOf("Log in to view your recently accessed books") }
    val librariesMessage =
        remember { mutableStateOf("Log in to view your recently accessed libraries") }

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            booksLoading.value = true
            librariesLoading.value = true

            val books = dataStore.getRecentBooks(userId)
            if (books.isNotEmpty()) {
                recentBooks.clear()
                recentBooks.addAll(books)
            } else {
                booksMessage.value = "No recent books"
            }
            booksLoading.value = false

            val libraries = dataStore.getRecentLibraries(userId)
            if (libraries.isNotEmpty()) {
                recentLibraries.clear()
                recentLibraries.addAll(libraries)
            } else {
                librariesMessage.value = "No recent libraries"
            }
            librariesLoading.value = false
        } else {
            booksMessage.value = "Login to check recent books"
            librariesMessage.value = "Login to check recent libraries"
            booksLoading.value = false
            librariesLoading.value = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Consistent padding around the entire screen
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Reduced spacing between elements
    ) {
        // Search Bar
        SearchBar(navController, false, true)

        // Últimos Livros Section
        Text(
            "Recent books",
            style = MaterialTheme.typography.headlineSmall,

        )
        if (booksLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)

            )
        } else if (recentBooks.isEmpty()) {
            Text(
                booksMessage.value,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()

            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp), // Reduced spacing between book cards
                modifier = Modifier.fillMaxWidth()
            ) {
                recentBooks.forEach { book ->
                    BookCard(book = book, sharedViewModel, navController = navController)
                }
            }
        }


        // Bibliotecas Recentes Section
        Text(
            "Recent libraries",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp) // Padding below the section header
        )
        if (librariesLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp) // Padding above the loading indicator
            )
        } else if (recentLibraries.isEmpty()) {
            Text(
                librariesMessage.value,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp) // Padding below the empty message
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), // Reduced spacing between library cards
                modifier = Modifier.fillMaxWidth()
            ) {
                recentLibraries.filterNotNull().forEach { library ->
                    LibraryCard(
                        lib = library,
                        book = Book("", "", "", "", 0, ""),
                        navController = navController
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    libraries: List<Library>,
    userLocation: Location?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var librariesWithDistances by remember { mutableStateOf(emptyList<Pair<Library, Float>>()) }
    val db = DataSource()

    // Obter a localização atual do usuário
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // State for permission
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    // Solicitar permissão de localização se necessário
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            // Se a permissão foi concedida, obtenha a localização
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Atualiza a localização do usuário no mapa
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        } else {
            // Solicitar permissão
            permissionState.launchPermissionRequest()
        }
    }

    // Dynamic camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-23.5505, -46.6333), 12f) // Default to São Paulo
    }

    // Update camera position when user location is available
    LaunchedEffect(userLocation) {
        userLocation?.let { latLng ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 12f)
        }
    }

    LaunchedEffect(userLocation, libraries) {
        userLocation?.let { latLng ->
            // Converter LatLng para Location
            val userLocationAsLocation = Location("").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            val distances = libraries.map { library ->
                val distance = db.calculateDistance2(userLocationAsLocation, library) // Chamada da função
                library to distance // Associa a biblioteca à sua distância
            }
            librariesWithDistances = distances.sortedBy { it.second } // Ordena por distância
        }
    }

    // Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchBar(
            navController = navController,
            true,
            true,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .pointerInput(Unit) {
                    detectTapGestures {}
                }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // Marcador do usuário
                userLocation?.let { position ->
                    Marker(
                        state = MarkerState(position = position),
                        title = "Você está aqui",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    )
                }

                // Marcadores das bibliotecas
                libraries.forEach { library ->
                    library.location?.let { geoPoint ->
                        Marker(
                            state = MarkerState(position = LatLng(geoPoint.latitude, geoPoint.longitude)),
                            title = library.name,
                            snippet = "Biblioteca"
                        )
                    }
                }
            }
        }

        Text(
            text = "Closest Libraries",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 10.dp),

        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            librariesWithDistances.take(5).forEach { (library, distance) ->
                LibraryCard(
                    lib = library,
                    book = Book("Sample Book"),
                    navController = navController,

                )
            }
        }
    }
}


fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double, onResult: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val latLng = LatLng(latitude, longitude)

    // Tente obter o endereço
    try {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        // Verifique se a lista de endereços não é nula e contém algum item
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // Exiba o endereço no formato desejado
            onResult(address.getAddressLine(0) ?: "Endereço desconhecido")
        } else {
            onResult("Endereço não encontrado")
        }
    } catch (e: IOException) {
        // Em caso de falha, retorne um endereço de erro
        onResult("Erro ao obter endereço")
        e.printStackTrace()
    }
}

@Composable
fun FavoritesScreen(navController: NavHostController, bookList: BookList, modifier: Modifier = Modifier) {
    var numberOfBooks by remember {
        mutableStateOf(0)
    }
    val bookListState = remember { mutableStateListOf<Book>() } // Observable list for recomposition
    var selectedSort by remember { mutableStateOf("Oldest") } // Inicializa com "Date"
    var expanded by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) } // Controla a ordem para Date

    // Fetch books from Firestore
    LaunchedEffect(Unit) {
        val db = DataSource()
        db.getLikedBooksForUser(
            onSuccess = { books ->
                bookListState.clear()
                bookListState.addAll(books) // Add new data
                numberOfBooks = books.size // Update book count
                bookListState.sortBy { it.date }
                Log.d("SortOrder", "Sorted by Most Recent: ${bookListState.map { it.date }}")
            },
            onFailure = { error ->
                println("Erro ao buscar livros: ${error.message}")
            }
        )
    }
    // UI Content
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Favorites",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "$numberOfBooks books",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box {
                Text(
                    text = "Sorted by: $selectedSort",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Ordenação por Data
                    DropdownMenuItem(
                        text = { Text(if (selectedSort == "Oldest") "Most Recent" else "Oldest") },
                        onClick = {
                            // Alterna entre as ordens
                            selectedSort = if (selectedSort == "Oldest") "Most Recent" else "Oldest"

                            // Atualiza a lista com base na ordem selecionada
                            if (selectedSort == "Oldest") {
                                bookListState.sortBy { it.date } // Mais antigos primeiro
                                Log.d("SortOrder", "Sorted by Oldest: ${bookListState.map { it.date }}")
                            } else {
                                bookListState.sortByDescending { it.date } // Mais recentes primeiro
                                Log.d("SortOrder", "Sorted by Most Recent: ${bookListState.map { it.date }}")
                            }

                            expanded = false
                        }
                    )

                    // Ordenação por Nome
                    DropdownMenuItem(
                        text = { Text("Name") },
                        onClick = {
                            selectedSort = "Name"
                            expanded = false
                            val sortedBooks = bookListState.sortedBy { it.name }
                            bookListState.clear()
                            bookListState.addAll(sortedBooks)
                        }
                    )

                    // Ordenação por Likes
                    DropdownMenuItem(
                        text = { Text("Likes") },
                        onClick = {
                            selectedSort = "Likes"
                            expanded = false

                            // Atualize os likes e aguarde até que todos estejam atualizados
                            val updatedBooks = mutableListOf<Book>()
                            bookListState.forEach { book ->
                                DataSource().getLikes(book.name) { result ->
                                    book.likes = result ?: 0
                                    updatedBooks.add(book)

                                    // Quando todos os livros forem processados
                                    if (updatedBooks.size == bookListState.size) {
                                        // Ordenar por likes
                                        bookListState.clear()
                                        bookListState.addAll(updatedBooks.sortedByDescending { it.likes })
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(bookListState) { book ->
                cardMeme(book, navController) // Pass book directly
            }
        }
    }
}

@Composable
fun SearchBar(navController: NavHostController, state: Boolean, icon: Boolean, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }




    val leadingIcon: @Composable (() -> Unit)? = if (icon) {
        {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    } else {
        null // No icon when "icon" is false
    }
    val trailingIcon: @Composable (() -> Unit)? = {
        IconButton(onClick = {

        }) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice Search",
                modifier = Modifier.size(20.dp)
            )
        }
    }


    TextField(
        value = text,
        onValueChange = { text = it },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedContainerColor = if (!icon) Color(0xFF1E88E5) else Color.Unspecified,
            focusedContainerColor = if (!icon) Color(0xFF1E88E5) else Color.Unspecified,
            unfocusedLeadingIconColor = Color.White,
            focusedLeadingIconColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                stringResource(R.string.place_Holder_Search_Bar),
                style = TextStyle(fontSize = 14.sp)
            )
        },
        textStyle = TextStyle(fontSize = 14.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { navController.navigate("searchScreen") },
        enabled = state,
        singleLine = true
    )
}
@Composable
fun CrudScreen(
    navController: NavHostController,
    viewModel: CrudViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val libraryList by viewModel.libraryList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showPopup by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.updateList()
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (showPopup) {
            BookRegistrationPopup(
                onDismiss = { showPopup = false
                    viewModel.updateList()
                            },

            )
        }




        SearchBar(
            navController = navController,
            true,
            true,
            modifier.padding(bottom = 0.dp),
            onQueryChanged = { query ->
                viewModel.searchLibraries(query)

            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Card(){
                IconButton(onClick = { showPopup = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Register library",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(libraryList) { library ->
                    LibraryCardCrud(
                        lib = library,
                        onUpdate = { viewModel.updateList() },navController
                    )
                }
            }
        }
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: BookViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var sharedViewModel = SharedViewModel()
    val books by viewModel.books.collectAsState()
    var likes by remember { mutableStateOf(0) }
    var db = DataSource()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        navController = navController,
                        state = true,
                        icon = false,
                        onQueryChanged = { query ->
                            viewModel.searchBooks(query, "AIzaSyBAYtQglFE225WW_aoGg9NOnV0GmGIX28g") // Trigger search with query
                        }
                    )
                },
                navigationIcon = {
                    Row(modifier = Modifier.width(30.dp)) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(books) { book ->

                BookCard(navController = navController, sharedViewModel = sharedViewModel, book = Book(book.name,book.author,book.date,book.imageUrl,book.likes,book.sinopsis))
                Log.d("ImageURL", "URL: ${book.imageUrl}")

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenCrud(
    navController: NavHostController,libId:String,
    viewModel: BookViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val books by viewModel.books.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        navController = navController,
                        state = true,
                        icon = false,
                        onQueryChanged = { query ->
                            viewModel.searchBooks(query, "AIzaSyBAYtQglFE225WW_aoGg9NOnV0GmGIX28g") // Trigger search with query
                        }
                    )
                },
                navigationIcon = {
                    Row(modifier = Modifier.width(30.dp)) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(books) { book ->
                BookCardCrud(navController = navController, book = Book(book.name,book.author,book.date,book.imageUrl,100,book.sinopsis),libId)
                Log.d("ImageURL", "URL: ${book.imageUrl}")

            }
        }
    }
}



@Composable
fun SearchBar(
    navController: NavHostController,
    state: Boolean,
    icon: Boolean,
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var hasPermission by remember { mutableStateOf(false) }
    RequestMicrophonePermission { hasPermission = true }

    val voiceRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            text = spokenText
            onQueryChanged(spokenText)
            Log.d("VoiceSearch", "Texto falado: $spokenText")
        } else {
            Log.d("VoiceSearch", "Erro no reconhecimento de voz")
        }
    }

    fun startVoiceRecognition() {
        if (hasPermission) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora")
            }
            voiceRecognitionLauncher.launch(intent)
        } else {
            Log.d("VoiceSearch", "Permissão de microfone não concedida")
        }
    }
    val leadingIcon: @Composable (() -> Unit)? = if (icon) {
        {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    } else {
        null
    }
    val trailingIcon: @Composable (() -> Unit)? = {
        IconButton(onClick = {startVoiceRecognition()
        }) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice Search",
                modifier = Modifier.size(20.dp)
            )
        }
    }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedContainerColor = if (!icon) Color(0xFF1E88E5) else Color.Unspecified,
            focusedContainerColor = if (!icon) Color(0xFF1E88E5) else Color.Unspecified,
            unfocusedLeadingIconColor = Color.White,
            focusedLeadingIconColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Search...",
                style = TextStyle(fontSize = 14.sp)
            )
        },
        textStyle = TextStyle(fontSize = 14.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { navController.navigate("searchScreen") },
        enabled = state,
        singleLine = true
    )
}


@Composable
fun cardMeme(book: Book,navController: NavHostController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(0.65f)
            .padding(8.dp)
            .clickable {
                val encodedUrl = Uri.encode(book.imageUrl)
                navController.navigate("bookDetailScreen/${book.name}/${book.author}/${book.date}/${encodedUrl}/${book.likes}/${book.sinopsis}") }
                ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        SubcomposeAsyncImage(
            model = (book.imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}


// this is for test
@Composable

fun cardWithName(name: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hello ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { /* TODO */ }
            ) {
                Text("Show more")
            }
        }
    }
}

// this is for test

@Composable
fun BookCard(book: Book,sharedViewModel: SharedViewModel, navController: NavHostController) {
    var likes by rememberSaveable {
        mutableStateOf(book.likes)
    }
    var isLiked by rememberSaveable {
        mutableStateOf(false) // Procura o livro na lista de livros do usario e ao retorna se obtiver sucesso e for encontrado return true,
    }
    var db = DataSource()
    fun updateLikes() {
        DataSource().getLikes(book.name) { result ->
            likes = result ?: 0
        }
    }
    LaunchedEffect(book.name) {
        DataSource().getLikes(book.name) { result ->
            likes = result ?: 0
        }
        while (sharedViewModel.isLoading.value) {
            delay(100) // Wait for data to load
        }
        isLiked = sharedViewModel.contains(book)

    }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                val encodedUrl = Uri.encode(book.imageUrl)

                navController.navigate("bookDetailScreen/${book.name}/${book.author}/${book.date}/${encodedUrl}/${book.likes}/${book.sinopsis}")


            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = (book.imageUrl),
                    contentDescription = book.name,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .size(60.dp),
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator()
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    book.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    book.author,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorites",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            isLiked = !isLiked
                            // se ja tiver likado, remove do favoritos do usuario, se não tiver, adiciona
                            val incrementValue = if (isLiked) 1 else -1
                            if (isLiked) sharedViewModel.addBook(book) else sharedViewModel.removeBook(book)
                            db.like(book,incrementValue)
                            likes = likes + incrementValue
                            if (isLiked) db.addBookToLikedBooks(book) else db.removeBookFromLikedBooks(book)

                        }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likes.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }
    }
}
@Composable
fun LibraryCardCrud(lib: Library, onUpdate: () -> Unit, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val libname = rememberUpdatedState(lib.name)
    val location = rememberUpdatedState(lib.location)
    val accessibility = rememberUpdatedState(lib.accessibility)
    val db = DataSource()
    var address by remember { mutableStateOf("Loading...") }

    LaunchedEffect(lib.location) {
        lib.location?.let { geoPoint ->
            getAddressFromLatLng(context, geoPoint.latitude, geoPoint.longitude) { result ->
                address = result
            }
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("libDetailCrudScreen/${lib.id}")
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Library Icon",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = libname.value,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.AccessibilityNew,
                    contentDescription = "Accessibility Icon",
                    modifier = Modifier.size(20.dp),
                    tint = if (accessibility.value) Color(0xFF1E88E5) else Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { navController.navigate("SearchScreenCrud/${lib.id}") }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Edit Icon Button
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = {
                    val db = DataSource()
                    db.deleteLibrary(lib.id)
                    onUpdate()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Column {
                    var editLibName by remember { mutableStateOf(lib.name) }
                    var editLocation by remember { mutableStateOf(lib.location?.let { "${it.latitude},${it.longitude}" } ?: "") }
                    var editAccessibility by remember { mutableStateOf(lib.accessibility) }

                    TextField(
                        value = editLibName,
                        onValueChange = { editLibName = it },
                        label = { Text("Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    TextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("Location (lat,long)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        visualTransformation = VisualTransformation.None
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = editAccessibility,
                            onCheckedChange = { editAccessibility = it }
                        )
                        Text(
                            text = "Accessible",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        ElevatedButton(
                            onClick = { expanded = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }
                        ElevatedButton(
                            onClick = {
                                val db = DataSource()
                                val geoPoint: GeoPoint? = lib.location
                                db.updateLibrary(lib.id, editLibName, geoPoint, editAccessibility)
                                onUpdate()
                                expanded = false
                            }
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun BookCardCrud(navController: NavHostController, book: Book,libId:String) {
    var likes by rememberSaveable { mutableStateOf(book.likes) }
    var isLiked by rememberSaveable { mutableStateOf(true) }

    var name by rememberSaveable { mutableStateOf(book.name) }
    var author by rememberSaveable { mutableStateOf(book.author) }
    var date by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var quantity by rememberSaveable { mutableStateOf(1) }
    book.quantity = quantity
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("bookDetailScreen/${book.name}/${book.author}/${book.date}/${book.imageUrl}/${book.likes}/${book.sinopsis}")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(book.imageUrl),
                        contentDescription = "Book Cover",
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        book.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        book.author,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp)
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > 0) quantity -= 1
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Quantity",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )

                        IconButton(
                            onClick = {
                                quantity += 1
                            },
                            modifier = Modifier.size(30.dp) // Consistent size for buttons
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Quantity",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp) // Slightly larger for better visibility
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    var db = DataSource()
                    db.addBooksToLibrary(libId, listOf(book),
                        onSuccess = {
                            var toast = Toast.makeText(
                                context,
                                "Sucess adding the book",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = { error ->
                            var toast = Toast.makeText(
                                context,
                                "Fail adding the book",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )

                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun LibraryCard(
    lib: Library,
    book: Book,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = DataSource()
    var address by remember { mutableStateOf("Loading...") }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var quantityBooks by remember { mutableStateOf(0) }
    var distanceText by remember { mutableStateOf("Loading...") }

    // Fetch the quantity of books asynchronously
    LaunchedEffect(lib.id) {
        db.getBookFromLibrary(
            libraryId = lib.id,
            bookName = book.name,
            onSuccess = { book ->
                quantityBooks = book?.quantity ?: 0
            },
            onFailure = { exception ->
                Log.e("BookFinder", "Error retrieving book: ${exception.message}")
            }
        )
    }

    // Transform LatLng to address
    LaunchedEffect(lib.location) {
        lib.location?.let { geoPoint ->
            getAddressFromLatLng(context, geoPoint.latitude, geoPoint.longitude) { result ->
                address = result
            }
        }
    }
    val permissionState = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            db.getUserLocation(fusedLocationClient, lib, userLocation) { distance ->
                distanceText = distance
            }
        } else {
            distanceText = "Permission denied"
        }
    }

    LaunchedEffect(Unit) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            db.getUserLocation(fusedLocationClient, lib, userLocation) { distance ->
                distanceText = distance
            }
        } else {
            permissionState.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Display the library card
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp) // Reduced padding around the card
            .clickable {
                val libraryJson = Uri.encode(Gson().toJson(lib))
                navController.navigate("libDetailScreen/$libraryJson")
            },
        shape = RoundedCornerShape(6.dp), // Slightly smaller corner radius
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // Reduced padding inside the card
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Less spacing between items
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "Library Icon",
                modifier = Modifier.size(36.dp) // Slightly smaller icon size
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lib.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 2.dp) // Reduced spacing below the name
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Distance: ${distanceText.take(3)} km",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
            Icon(
                imageVector = Icons.Default.AccessibilityNew,
                contentDescription = "Accessibility Icon",
                modifier = Modifier.size(18.dp), // Smaller accessibility icon
                tint = if (lib.accessibility) Color(0xFF1E88E5) else Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(book: Book, navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }
    var isPlaying by remember { mutableStateOf(false) }
    val libraryList = remember { mutableStateListOf<Library>() }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(book.name) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val dataStore = DataStoreManager(context)
            dataStore.addRecentBook(userId, book)
        }
    }



    LaunchedEffect(Unit) {
        val db = DataSource()
        db.getLibrariesWithBook(book.name,
            onSuccess = { libraries ->
                libraryList.clear()
                libraryList.addAll(libraries)
                loading = false
            },
            onFailure = { e ->
                Log.e("LibraryInfo", "Error fetching libraries: ${e.message}")
                loading = false
            }
        )
    }
    DisposableEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech?.language = Locale.getDefault()

                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        isPlaying = false
                    }

                    override fun onError(utteranceId: String?) {}
                })
            } else {
                Log.e("BookDetailScreen", "Erro ao inicializar TextToSpeech")
            }
        }

        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    val bookInfo = """
        ${book.name}, 
        by ${book.author}, 
        published in ${book.date}.
        sinopsis: ${book.sinopsis}.
    """.trimIndent()

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            textToSpeech?.stop()
        }
    }








    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(

                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding).verticalScroll(rememberScrollState())

        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Image(
                    painter = rememberImagePainter(book.imageUrl),
                    contentDescription = book.name,
                    modifier = Modifier
                        .size(width = 120.dp, height = 218.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = book.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "by ${book.author}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Published: ${book.date}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "${book.sinopsis}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(), // Ensures the Row takes up the full width
                horizontalArrangement = Arrangement.End


            ) {

                if (isPlaying) {
                    IconButton(
                        onClick = {
                            textToSpeech?.stop()
                            isPlaying = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause, // Use a pause icon for stopping speech
                            contentDescription = "Pause Speech",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            textToSpeech?.speak(bookInfo, TextToSpeech.QUEUE_FLUSH, null, null)
                            isPlaying = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp, // Use a volume icon for starting speech
                            contentDescription = "Play Speech",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }



            Text("Available in Libraries", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))
            if (loading) {

                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading libraries...", style = MaterialTheme.typography.bodyMedium)
            } else {

                libraryList.forEach { library ->
                    LibraryCard(library,book,navController)
                    Spacer(modifier = Modifier.height(16.dp))

                }


            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean> // Passando isLogged como um estado mutável
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf<String?>(null) }

    // Exibe o Toast quando a mensagem é definida
    showMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        showMessage = null
    }

    // Configure o Google Sign-In
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("875532978785-94hh42vaqj3b1s0vht0ujdmcvkcftrkd.apps.googleusercontent.com") // Insira o `client_id` do Firebase aqui
                .requestEmail()
                .build()
        )
    }

    // Registrar o ActivityResultLauncher para Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account, isLogged, navController)
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In falhou: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(

                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("mainScreen") {}
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",

                        )
                    }

                },
                title = {
                    Text(text = "Login", fontWeight = FontWeight.Bold, color = Color.White)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone do aplicativo
            Icon(
                painter = painterResource(id = R.drawable.appicone),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 2.dp),
                tint = Color.Unspecified
            )

            // Texto de boas-vindas
            Text(
                text = "Welcome to\nBookFinder",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Campo de login
            TextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                placeholder = { Text("Digite seu login") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp)
            )

            // Campo de senha
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Digite sua senha") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            // Botão de login por email e senha
            Button(
                onClick = {
                    loginUser(login, password) { success ->
                        if (success) {
                            isLogged.value = true // Atualizando o estado global
                            navController.navigate("mainScreen")
                        } else {
                            showMessage = "Login failed"
                        }
                    }
                },
                modifier = Modifier
                    .height(70.dp)
                    .width(280.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 20.sp
                )
            }

            // Divider com "Login com"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(
                    text = " Login with ",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }

            // Botão de login com Google
            IconButton(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Login with Google",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )
            }

            // Texto para "Não tem conta?"
            Text(
                text = "No account? Register here!",
                fontSize = 19.sp,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .clickable {
                        navController.navigate("registerScreen")
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

fun firebaseAuthWithGoogle(account: GoogleSignInAccount?, isLogged: MutableState<Boolean>, navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userId = user?.uid

                if (userId != null) {
                    val userRef = db.collection("users").document(userId)

                    // Verifica se o documento do usuário já existe
                    userRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            // Documento existe, atualiza apenas os campos necessários
                            val updateData = hashMapOf<String, Any>(
                                "email" to (user.email ?: ""),
                                "name" to (user.displayName ?: "Usuário Google") // Atualiza o nome da conta Google
                            )
                            userRef.update(updateData)
                                .addOnSuccessListener {
                                    isLogged.value = true
                                    navController.navigate("mainScreen")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseAuth", "Failed to update user data: ${e.message}")
                                }
                        } else {
                            // Documento não existe, cria um novo
                            val userData = hashMapOf(
                                "email" to (user.email ?: ""),
                                "name" to (user.displayName ?: "Usuário Google"), // Adiciona o nome da conta Google
                                "createdAt" to System.currentTimeMillis(),
                                "likedBooks" to emptyList<Map<String, Any>>() // Lista vazia para livros favoritos
                            )
                            userRef.set(userData)
                                .addOnSuccessListener {
                                    isLogged.value = true
                                    navController.navigate("mainScreen")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseAuth", "Failed to save user data: ${e.message}")
                                }
                        }
                    }.addOnFailureListener { e ->
                        Log.e("FirebaseAuth", "Failed to check user existence: ${e.message}")
                    }
                }
            } else {
                Log.e("FirebaseAuth", "Authentication failed: ${task.exception?.message}")
            }
        }
}





fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRegistrationPopup(
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var acessesbility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = DataSource()

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LibraryAdd,
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),

                    )

                Text(
                    text = "Register your library",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    placeholder = { Text("Enter your name") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    placeholder = { Text("Enter your location") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Checkbox(
                        checked = acessesbility,
                        onCheckedChange = { acessesbility = it }
                    )
                    Text(
                        text = "Accessible",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = {
                            onDismiss()
                        },

                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp
                        )
                    }


                    Button(
                        onClick = {
                            var db = DataSource()
                            val geoPoint = getGeoPointFromAddress(context, location)
                            db.addLibrary(name, geoPoint, emptyList(),acessesbility )
                            var toast = Toast.makeText(
                                context,
                                "Sucess adding the book",
                                Toast.LENGTH_LONG
                            ).show()
                            onDismiss()
                        },


                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
fun getGeoPointFromAddress(context: Context, address: String): GeoPoint? {
    val geocoder = Geocoder(context)
    val addressList = geocoder.getFromLocationName(address, 1)
    return if (addressList != null && addressList.isNotEmpty()) {
        val location = addressList[0]
        GeoPoint(location.latitude, location.longitude)
    } else {
        null // Retorna null se o endereço não for encontrado
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(

                ),
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack() // Volta para a tela anterior
                        } else {
                            navController.navigate("mainScreen") {
                                popUpTo("mainScreen") { inclusive = true } // Retorna Ã  tela principal
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",

                        )
                    }
                },
                title = { Text("Register") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.appicone),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 20.dp),
                tint = Color.Unspecified
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your name") },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Remove underline when unfocused
                ),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your email") },
                shape = RoundedCornerShape(16.dp),colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Remove underline when unfocused
                ),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Create a password") },
                shape = RoundedCornerShape(16.dp),colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Remove underline when unfocused
                ),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter your password") },
                shape = RoundedCornerShape(16.dp),colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Remove underline when unfocused
                ),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        registerUser(name, email, password) { success ->
                            if (success) {
                                navController.navigate("successScreen")
                            } else {
                                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .height(70.dp)
                    .width(280.dp)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Register", fontSize = 20.sp)
            }
        }
    }
}

// A função `registerUser` deve estar fora do escopo do Composable
fun registerUser(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                val user = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("users").document(userId).set(user)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            } else {
                onResult(false)
            }
        }
}



@Composable
fun SuccessScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply inner padding provided by Scaffold
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SUCCESS!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Welcome!",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.success),
                contentDescription = "Success Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                tint = Color.Unspecified
            )

            Text(
                text = "Registration Successful",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Time to Book!",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = {
                    navController.navigate("mainScreen")
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(200.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 20.sp
                )
            }
        }
    }
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommunityScreenWrapper(
    viewModel: BookViewModel = viewModel(),
    bookList: BookList,
    navController: NavHostController
) {
    val newBooks by viewModel.books.collectAsState()
    val mostRelevantBooks by viewModel.mostRelevantBooks.collectAsState()
    val mostLiked = remember { mutableStateListOf<Book>() }

    var isDataLoaded by remember { mutableStateOf(false) } // Track data loading state

    LaunchedEffect(Unit) {
        viewModel.fetchRecentBooks("a", "AIzaSyBAYtQglFE225WW_aoGg9NOnV0GmGIX28g", 5)
        viewModel.fetchMostRelevantBooks("a", "AIzaSyBAYtQglFE225WW_aoGg9NOnV0GmGIX28g", 5)

        val db = DataSource()
        db.fetchLikedBooks(
            onSuccess = { likedBooks ->
                mostLiked.clear()
                mostLiked.addAll(
                    likedBooks.sortedByDescending { it.likes }.take(5)
                )
                isDataLoaded = true // Mark as loaded when all data is fetched
            },
            onFailure = { exception ->
                Log.e("fetchLikedBooks", "Error fetching liked books", exception)
                isDataLoaded = true // Still mark as loaded to avoid infinite loading state
            }
        )
    }

    // Show loading until data is ready
    if (isDataLoaded) {
        CommunityScreen(
            bookList = mostLiked,
            newBooks = newBooks,
            mostRelevantBooks = mostRelevantBooks,
            navController = navController
        )
    } else {
        LoadingScreen() // Show a loading indicator
    }
}


@Composable
fun CommunityScreen(modifier: Modifier = Modifier,  newBooks: List<Book>,mostRelevantBooks: List<Book>, bookList: List<Book>, navController: NavHostController) {
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())

    ) {

        Text(
            text = "Most Famous",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            items(bookList) { it ->
                BookCardWithLikes(
                    book = it,navController

                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "New for 2024",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            items(newBooks) { it ->
                BookCardWithLikes(
                    book = it,navController

                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Most relevant",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {

            items(mostRelevantBooks) { it ->
                BookCardWithLikes(
                    book = it,navController

                )
            }
        }
    }
}

@Composable
fun BookCardWithLikes(book: Book, navController: NavHostController) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .width(150.dp) // Diminuído a largura
            .clickable {
                val encodedUrl = Uri.encode(book.imageUrl)
                navController.navigate(
                    "bookDetailScreen/${book.name}/${book.author}/${book.date}/${encodedUrl}/${book.likes}/${book.sinopsis}"
                )
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(220.dp) // Diminuído a altura
                .width(150.dp) // Mantido o ajuste de largura
        ) {
            Image(
                painter = rememberImagePainter(
                    data = book.imageUrl,
                    builder = {
                        crossfade(true)
                        size(600, 600) // Diminuído o tamanho da imagem para uma resolução menor
                    }
                ),
                contentDescription = book.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Cropar a imagem para preencher a Box
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f) // Transparência leve para a legibilidade
                            ),
                            startY = 150f,
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), // Menos padding para reduzir o espaço
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = if (book.likes == 1) "like" else "Likes",
                        tint = Color.Red,
                        modifier = Modifier.size(14.dp) // Diminuído o tamanho do ícone
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text =if (book.likes == 1 || book.likes == 0) "${book.likes} like" else "${book.likes} Likes",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontSize = 10.sp // Menor tamanho de fonte
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    val db = FirebaseFirestore.getInstance()

    var email by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("User Name") } // Default name
    var favoriteBook by remember { mutableStateOf("") }
    var favoritesCount by remember { mutableStateOf(0) }
    val photoUrl = auth.currentUser?.photoUrl

    var isEditingFavoriteBook by remember { mutableStateOf(false) }

    // Fetch Firestore data
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            val userRef = db.collection("users").document(userId)
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    email = document.getString("email") ?: ""
                    createdAt = document.getLong("createdAt")?.let {
                        java.text.SimpleDateFormat("dd/MM/yyyy").format(it)
                    } ?: ""
                    displayName = document.getString("name") ?: "User Name" // Fetching "name" field
                    favoriteBook = document.getString("favoriteBook") ?: ""
                    favoritesCount = (document.get("likedBooks") as? List<*>)?.size ?: 0
                }
            }.addOnFailureListener {
                Log.e("ProfileScreen", "Failed to fetch user data: ${it.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5),
                    titleContentColor = Color.White
                ),
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF1E88E5), Color.White)))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(Color.White, shape = CircleShape)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFF1E88E5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Display Name
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // User Info Card
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProfileInfoItem(label = "Since", value = createdAt, fontSize = 18.sp)
                    ProfileInfoItem(label = "Email", value = email, fontSize = 18.sp)
                    ProfileInfoItem(label = "Favorites", value = "$favoritesCount books", fontSize = 18.sp)
                    if (isEditingFavoriteBook) {
                        TextField(
                            value = favoriteBook,
                            onValueChange = { favoriteBook = it },
                            label = { Text("Favorite Book") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    } else {
                        ProfileInfoItem(label = "Favorite Book", value = favoriteBook, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button "Edit"
            Button(
                onClick = {
                    isEditingFavoriteBook = !isEditingFavoriteBook
                    if (!isEditingFavoriteBook) {
                        val userRef = db.collection("users").document(userId)
                        userRef.update("favoriteBook", favoriteBook)
                            .addOnSuccessListener {
                                Log.d("ProfileScreen", "Favorite book updated successfully")
                            }
                            .addOnFailureListener {
                                Log.e("ProfileScreen", "Failed to update favorite book: ${it.message}")
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(

                    if (isEditingFavoriteBook) "Save" else "Change favorite book",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button "Logout"
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("loginScreen") {
                        popUpTo("mainScreen") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF225582)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "Logout",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun ProfileInfoItem(label: String, value: String, fontSize: TextUnit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            fontSize = fontSize
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = fontSize,
            color = Color.Gray
        )
    }
}







@Composable
fun RequestMicrophonePermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Permissão de microfone negada", Toast.LENGTH_SHORT).show()
        }
    }

    SideEffect {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibDetailScreen(lib: Library, navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val db = DataSource()
    val context = LocalContext.current
    var sharedViewModel = SharedViewModel()
    var address by remember { mutableStateOf("Loading...") }
    LaunchedEffect(lib.name) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val dataStore = DataStoreManager(context)
            dataStore.addRecentLibrary(userId, lib)
        }
    }
    LaunchedEffect(lib.location) {
        lib.location?.let { geoPoint ->
            getAddressFromLatLng(context, geoPoint.latitude, geoPoint.longitude) { result ->
                address = result
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exibe o ícone da biblioteca com nome e endereço
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, vertical = 8.dp), // Consistent padding around the row
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Balanced spacing between icon and text
            ) {
                // Icon
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Library Icon",
                    modifier = Modifier.size(60.dp), // Appropriately sized icon
                )

                // Text content
                Column(
                    modifier = Modifier.weight(1f) // Take up the remaining space for text alignment
                ) {
                    // Library name
                    Text(
                        text = lib.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 2.dp) // Small spacing below the title
                    )
                    // Library address
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        maxLines = 2, // Limit to 2 lines for compactness
                        overflow = TextOverflow.Ellipsis // Truncate if the text is too long
                    )
                }
            }

            // Barra de pesquisa para filtrar livros
            SearchBar(
                navController = navController,
                true,
                true,
                onQueryChanged = { query ->
                    searchQuery = query
                }
            )

            // Texto "Available books"
            Text(
                text = "Available books",
                style = MaterialTheme.typography.headlineSmall,

            )

            // Lista de livros filtrada pelo nome
            val filteredBooks = lib.books.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
            if (filteredBooks.isEmpty()) {
                // Exibe uma mensagem se não houver livros correspondentes
                Text(
                    text = "No books available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    items(filteredBooks) { book ->
                        BookCard(book,sharedViewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BookQuantityCard(
    navController: NavHostController,
    book: Book,
    libId: String,
    onConfirmQuantity: (Int) -> Unit,
    onDelete: () -> Unit
) {
    var quantity by remember { mutableStateOf(book.quantity) }

    // Decodificar a URL
    val decodedImageUrl = URLDecoder.decode(book.imageUrl, StandardCharsets.UTF_8.toString())

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // Altura fixa do card
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem do livro com margem ao redor
            Image(
                painter = rememberImagePainter(decodedImageUrl),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .padding(8.dp) // Margem ao redor da imagem
                    .fillMaxHeight() // Faz a imagem ocupar a altura restante do card
                    .width(80.dp) // Largura fixa para a imagem
                    .clip(RoundedCornerShape(8.dp)), // Cantos arredondados
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp)) // Espaço entre imagem e conteúdo

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp) // Margem direita do conteúdo
            ) {
                // Nome do livro
                Text(
                    text = book.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Linha de controle de quantidade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (quantity > 0) quantity-- }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Diminuir Quantidade")
                    }

                    TextField(
                        value = quantity.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { quantity = it }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(60.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        singleLine = true
                    )

                    IconButton(onClick = { quantity++ }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Aumentar Quantidade")
                    }

                    IconButton(onClick = { onConfirmQuantity(quantity) }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Confirmar Quantidade")
                    }

                    IconButton(onClick = { onDelete() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir Livro")
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibDetailCrudScreen(
    libraryId: String,
    navController: NavHostController,
    crudViewModel: CrudViewModel
) {
    val libraryState = remember { mutableStateOf<Library?>(null) }
    val db = DataSource()
    var searchQuery by remember { mutableStateOf("") }


    LaunchedEffect(libraryId) {
        db.getLibraryById(
            libraryId = libraryId,
            onSuccess = { library ->
                libraryState.value = library
            },
            onFailure = { exception ->
                Log.e("LibDetailCrudScreen", "Erro ao buscar biblioteca: ${exception.message}")
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        libraryState.value?.let { lib ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Detalhes da Biblioteca
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Library Icon",
                        modifier = Modifier.size(48.dp)
                    )
                    Column {
                        Text(
                            text = lib.name,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = db.geoPointToAddress(lib.location),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }

                // Barra de pesquisa
                SearchBar(
                    navController = navController,
                    true,
                    true,
                    onQueryChanged = { query ->
                        searchQuery = query
                    }
                )

                // Texto "Available books"
                Text(
                    text = "Available books",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Lista de livros com filtros
                val filteredBooks = lib.books.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                if (filteredBooks.isEmpty()) {
                    Text(
                        text = "No books available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(filteredBooks) { book ->
                            BookQuantityCard(
                                book = book,
                                navController = navController,
                                libId = lib.id,
                                onDelete = {
                                    crudViewModel.deleteBook(lib.id, book.name) // Função de exclusão
                                },
                                onConfirmQuantity = { newQuantity ->
                                    crudViewModel.setQuantity(lib.id, book.name, newQuantity) // Função para atualizar a quantidade
                                },


                            )
                        }
                    }
                }
            }
        } ?: run {
            // Carregando ou exibindo erro
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Carregando biblioteca...")
            }
        }
    }
}


@Preview
@Composable
private fun BookCardprev() {
    //cardMeme(R.drawable.harryp);
}

@Preview
@Composable
private fun appPreview() {
}

@Preview
@Composable
private fun LibraryCardprev() {

}