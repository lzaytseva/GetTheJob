package ru.practicum.android.diploma.core.data.network

import ru.practicum.android.diploma.core.data.dto.responses.Response

interface NetworkClient {

    suspend fun doRequest(request: Any): Response
}
