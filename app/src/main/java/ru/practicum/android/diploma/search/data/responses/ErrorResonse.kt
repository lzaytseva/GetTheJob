package ru.practicum.android.diploma.search.data.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.ErrorDto

data class ErrorResonse(
    @SerializedName("request_id")
    val requestId: String,
    val errors: List<ErrorDto>
)
