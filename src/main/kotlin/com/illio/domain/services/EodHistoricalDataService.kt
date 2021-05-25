package com.illio.domain.services

import java.time.LocalDate
import kotlin.math.roundToInt

class EodHistoricalDataService(private val eodHistoricalData: EodHistoricalData) {

    fun getAveragePriceInPennies(ticker: String, from: LocalDate, to: LocalDate): Int? {
        val stocks = eodHistoricalData.getStockPrices(ticker, from, to)

        if (stocks.isEmpty()) return null

        return stocks
            .sumOf { it.adjustedClose }
            .div(stocks.size)
            .toPennies()
            .roundToInt()
    }

    private fun Double.toPennies() = this.times(100)
}