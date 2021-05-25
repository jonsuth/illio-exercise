package com.illio.domain.services

import com.illio.domain.domain.Stock
import java.time.LocalDate

interface EodHistoricalData {
    fun getStockPrices(ticker: String, from: LocalDate, to: LocalDate): List<Stock>
}