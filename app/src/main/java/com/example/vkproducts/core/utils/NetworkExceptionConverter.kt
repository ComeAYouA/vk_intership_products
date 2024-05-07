package com.example.vkproducts.core.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkExceptionConverter {
    fun Exception.convertToString(): String =
        when(this){
            is UnknownHostException -> "Отсутствует подключение к интернету"
            is SocketTimeoutException -> "Сервер не отвечает"
            is ConnectException -> "Сервер не найден"
            else -> "Неизвестная ошибка"
        }
}