package com.illio.domain

import java.time.LocalDate

data class AverageStockPriceRequest(
    val key: String,
    val from: LocalDate,
    val to: LocalDate,
)