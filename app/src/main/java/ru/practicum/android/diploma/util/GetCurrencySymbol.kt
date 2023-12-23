package ru.practicum.android.diploma.util

private val currencySymbols = mutableMapOf<String, String>(
    "RUR" to "₽",
    "EUR" to "€",
    "KZT" to "₸",
    "AZN" to "\u20BC",
    "USD" to "$",
    "BYR" to "\u0042\u0072",
    "GEL" to "\u20BE",
    "UAH" to "\u20b4",
)

fun getCurrencySymbol(currency: String): String {
    return currencySymbols[currency] ?: currency
}
