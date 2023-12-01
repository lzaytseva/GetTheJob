package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.util.BindingFragment

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)
}
