package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AppDatabase
import com.example.flightsearch.data.Favorite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlightViewModel(private val database: AppDatabase) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Airport>>(emptyList())
    val searchResults: StateFlow<List<Airport>> = _searchResults.asStateFlow()

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchAirports(query)
    }

    private fun searchAirports(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
            } else {
                _searchResults.value = database.flightDao().searchAirports(query)
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = database.flightDao().getFavorites()
        }
    }

    fun addFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            database.flightDao().insertFavorite(
                Favorite(
                    departure_code = departureCode,
                    destination_code = destinationCode
                )
            )
            loadFavorites()
        }
    }

    suspend fun getFlightsFromDeparture(departureCode: String): List<Favorite> {
        return database.flightDao().getFlightsFromDepartureSuspend(departureCode)
    }

    fun removeFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            database.flightDao().deleteFavorite(departureCode, destinationCode)
            loadFavorites()
        }
    }
}
