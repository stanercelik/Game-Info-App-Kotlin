package com.example.midtermproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.midtermproject.model.GameClass

/* ViewModel - FavoriteFragment Class */
class FavoriteViewModel : ViewModel() {
    companion object FavList {
        val favorites = ArrayList<GameClass>()

        fun refreshData(game: GameClass) {
            favorites.add(game)
        }

        fun deleteItem(game: GameClass) {
            favorites.remove(game)
        }
    }

    // Oyun bilgileri listede tutuluyor

}