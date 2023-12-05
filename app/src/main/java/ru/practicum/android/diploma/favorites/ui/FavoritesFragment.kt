package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)
}
