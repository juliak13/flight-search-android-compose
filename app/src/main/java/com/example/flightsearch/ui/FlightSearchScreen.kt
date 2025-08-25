package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flightsearch.data.AppDatabase
import com.example.flightsearch.data.Favorite

@Composable
fun FlightSearchScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: FlightViewModel = viewModel(factory = FlightViewModelFactory(db))

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Enter Airport") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // No search — show favorites
            if (searchQuery.isBlank()) {
                items(favorites) { favorite ->
                    FavoriteItem(favorite, viewModel)
                }
                // Searching — show airport results
            } else {
                items(searchResults) { airport ->
                    AirportItem(airport) {
                        navController.navigate("flights/${airport.iata_code}")
                    }
                }
            }
        }
    }
}

@Composable
fun AirportItem(airport: com.example.flightsearch.data.Airport, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${airport.name} (${airport.iata_code})")
        }
    }
}

@Composable
fun FlightItem(
    flight: Favorite,
    viewModel: FlightViewModel,
    savedFavorites: List<Favorite>
) {
    val isFavorited = savedFavorites.any {
        it.departure_code == flight.departure_code && it.destination_code == flight.destination_code
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Depart: ${flight.departure_code}")
                Text("Arrive: ${flight.destination_code}")
            }

            IconButton(onClick = {
                if (!isFavorited) {
                    viewModel.addFavorite(flight.departure_code, flight.destination_code)
                } else {
                    viewModel.removeFavorite(flight.departure_code, flight.destination_code)
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = if (isFavorited) "Unfavorite" else "Favorite",
                    tint = if (isFavorited) Color.Yellow else Color.Gray
                )
            }
        }
    }
}
