package ru.practicum.android.diploma.core.domain.models

data class Filters(
    val regionId: String?,
    val regionName: String?,
    val countryId: String?,
    val countryName: String?,
    val salary: String?,
    val salaryFlag: Boolean?,
    val industryId: String?,
    val industryName: String?,
    val currency: String?
) {
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return regionId == null
                && regionName == null
                && countryId == null
                && countryName == null
                && salary == null
                && salaryFlag == null
                && industryId == null
                && industryName == null
                && currency == null
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = regionId?.hashCode() ?: 0
        result = 31 * result + (regionName?.hashCode() ?: 0)
        result = 31 * result + (countryId?.hashCode() ?: 0)
        result = 31 * result + (countryName?.hashCode() ?: 0)
        result = 31 * result + (salary?.hashCode() ?: 0)
        result = 31 * result + (salaryFlag?.hashCode() ?: 0)
        result = 31 * result + (industryId?.hashCode() ?: 0)
        result = 31 * result + (industryName?.hashCode() ?: 0)
        result = 31 * result + (currency?.hashCode() ?: 0)
        return result
    }
}
