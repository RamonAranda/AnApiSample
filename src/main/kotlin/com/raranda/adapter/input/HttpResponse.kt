package com.raranda.adapter.input

data class HttpError(val code: Int, val message: String)

data class HttpResponse(val data: Any? = null, val error: HttpError? = null) {
    companion object {
        fun ok(body: Any) = HttpResponse(data = body)
        fun badRequest(message: String) = HttpResponse(error = HttpError(400, message))
    }
}
