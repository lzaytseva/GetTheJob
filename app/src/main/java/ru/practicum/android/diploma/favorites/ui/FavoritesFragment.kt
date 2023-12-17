package ru.practicum.android.diploma.favorites.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
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
        vacanciesAdapter = VacanciesAdapter { vacancyId ->
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToVacancyDetailsFragment(vacancyId)
            findNavController().navigate(action)
        }
        binding.rvVacancies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvVacancies.adapter = vacanciesAdapter

        val itemTouchHelper = ItemTouchHelper(getSwipeCallback())
        itemTouchHelper.attachToRecyclerView(binding.rvVacancies)
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

    private fun getSwipeCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val vacancyId = vacanciesAdapter?.getItemByPosition(viewHolder.adapterPosition)?.id
                if (!vacancyId.isNullOrBlank()) {
                    viewModel.deleteFromFavorite(vacancyId)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemViewWidthDivider = 3
                val itemViewHeightDivider = 2.8
                val trashBinIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete)

                c.clipRect(
                    0f,
                    viewHolder.itemView.top.toFloat(),
                    dX,
                    viewHolder.itemView.bottom.toFloat()
                )

                if (dX < viewHolder.itemView.width / itemViewWidthDivider) {
                    c.drawColor(ContextCompat.getColor(requireContext(), R.color.favorite_delete_bg))
                } else {
                    c.drawColor(ContextCompat.getColor(requireContext(), R.color.soft_red))
                }

                val listItemHeight = (viewHolder.itemView.height / itemViewHeightDivider).toInt()

                if (trashBinIcon != null) {
                    trashBinIcon.bounds = Rect(
                        listItemHeight,
                        viewHolder.itemView.top + listItemHeight,
                        listItemHeight + trashBinIcon.intrinsicWidth,
                        viewHolder.itemView.top + trashBinIcon.intrinsicHeight
                            + listItemHeight
                    )
                }

                trashBinIcon?.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
    }
}
