package com.archesky.auth.server.types

interface CSV<T> {
    fun getCSVData() : List<T>
}
