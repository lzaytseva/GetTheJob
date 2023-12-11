package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyCardBinding
import ru.practicum.android.diploma.search.domain.model.VacancyInList

class VacanciesAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<VacanciesAdapter.VacancyViewHolder>() {

    private val vacanciesList: MutableList<VacancyInList> = ArrayList()

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

    fun setContent(newList: List<VacancyInList>) {
        val diffCallback = VacanciesDiffCallback(vacanciesList, newList)
        val diffVacancies = DiffUtil.calculateDiff(diffCallback)
        vacanciesList.clear()
        vacanciesList.addAll(newList)
        diffVacancies.dispatchUpdatesTo(this)
    }

    inner class VacancyViewHolder(private val binding: VacancyCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: VacancyInList) {
            with(binding) {
                setImage(vacancy.logo)
                vacancyNameTextView.text = vacancy.name
                employerNameTextView.text = vacancy.employerName
                salaryInfoTextView.text = vacancy.salaryFrom.toString() // get correct string
            }
        }

        private fun setImage(imageUrl: String?) {
            val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.search_field_corner_radius)
            Glide.with(itemView)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .placeholder(R.drawable.ic_vacancy_placeholder)
                .into(binding.vacancyIconImageView)
        }
    }
}
