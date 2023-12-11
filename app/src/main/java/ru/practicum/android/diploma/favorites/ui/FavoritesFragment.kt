package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.RootActivity
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import ru.practicum.android.diploma.util.BindingFragment

@AndroidEntryPoint
class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()
    private var vacanciesAdapter: VacanciesAdapter? = null

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        vacanciesAdapter = VacanciesAdapter(resources) { vacancyId ->
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToVacancyDetailsFragment(vacancyId)
            findNavController().navigate(action)
        }
        binding.rvVacancies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvVacancies.adapter = vacanciesAdapter
    }

    override fun onDestroyView() {
        binding.rvVacancies.adapter = null
        super.onDestroyView()
        vacanciesAdapter = null
    }

    private fun configureToolbar() {
        (requireActivity() as? RootActivity)?.run {
            toolbar.title = ContextCompat.getString(this, R.string.favorite)
            toolbar.navigationIcon = null
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> renderContent(state)
            FavoritesState.DbError -> renderDbError()
            FavoritesState.Empty -> renderEmpty()
        }
    }

    private fun renderContent(state: FavoritesState) {
        vacanciesAdapter?.setContent((state as FavoritesState.Content).vacancies)
        binding.groupEmpty.visibility = View.GONE
        binding.groupError.visibility = View.GONE
        binding.rvVacancies.visibility = View.VISIBLE
    }

    private fun renderDbError() {
        binding.groupEmpty.visibility = View.GONE
        binding.groupError.visibility = View.VISIBLE
        binding.rvVacancies.visibility = View.GONE
    }

    private fun renderEmpty() {
        binding.groupEmpty.visibility = View.VISIBLE
        binding.groupError.visibility = View.GONE
        binding.rvVacancies.visibility = View.GONE
    }
}
