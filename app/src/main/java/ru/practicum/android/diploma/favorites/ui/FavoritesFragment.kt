package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {
        (requireActivity() as? RootActivity)?.run {
            toolbar.title = ContextCompat.getString(this, R.string.favorite)
            toolbar.navigationIcon = null
        }
    }
}
