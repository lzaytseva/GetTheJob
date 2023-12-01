package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFavouritesBinding
import ru.practicum.android.diploma.util.BindingFragment

class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>() {
    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavouritesBinding =
        FragmentFavouritesBinding.inflate(inflater, container, false)
}
