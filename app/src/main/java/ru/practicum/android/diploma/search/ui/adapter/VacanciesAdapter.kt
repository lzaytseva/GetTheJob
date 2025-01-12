package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyCardBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.getSalaryDescription

class VacanciesAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<VacanciesAdapter.VacancyViewHolder>() {

    private val vacanciesList: MutableList<Vacancy> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VacancyViewHolder(VacancyCardBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = vacanciesList.size

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClick(vacanciesList[position].id)
        }
        holder.bind(vacanciesList[position])
    }

    fun setContent(newList: List<Vacancy>) {
        val diffCallback = VacanciesDiffCallback(vacanciesList, newList)
        val diffVacancies = DiffUtil.calculateDiff(diffCallback)
        vacanciesList.clear()
        vacanciesList.addAll(newList)
        diffVacancies.dispatchUpdatesTo(this)
    }

    fun getItemByPosition(position: Int) =
        vacanciesList[position]


    inner class VacancyViewHolder(private val binding: VacancyCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            with(binding) {
                setImage(vacancy.logo)
                val name = vacancy.name + ", " + vacancy.areaName
                vacancyNameTextView.text = name
                employerNameTextView.text = vacancy.employerName
                salaryInfoTextView.text = getSalaryDescription(
                    itemView.resources,
                    vacancy.salaryFrom,
                    vacancy.salaryTo,
                    vacancy.salaryCurrency
                )
            }
        }

        private fun setImage(imageUrl: String?) {
            val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.search_field_corner_radius)
            Glide.with(itemView)
                .load(imageUrl)
                .transform(FitCenter(), RoundedCorners(cornerRadius))
                .placeholder(R.drawable.ic_vacancy_placeholder)
                .into(binding.vacancyIconImageView)
        }
    }
}
