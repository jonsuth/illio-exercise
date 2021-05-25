package com.illio.domain.services

import com.illio.domain.domain.Stock
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.time.LocalDate

internal class EodHistoricalDataServiceTest {

    private val eodHistoricalDataApi = mockk<EodHistoricalData>()
    private val eodHistoricalDataService = EodHistoricalDataService(eodHistoricalDataApi)

    @Test
    fun `should return null when stocks are not present`() {
        //given
        every { eodHistoricalDataApi.getStockPrices(any(), any(), any()) } returns emptyList()

        //when
        val average = eodHistoricalDataService.getAveragePriceInPennies("MSD.US", LocalDate.now(), LocalDate.now())

        //then
        expectThat(average).isNull()
    }

    @Test
    fun `should return average price of stock`() {
        //given
        every { eodHistoricalDataApi.getStockPrices(any(), any(), any()) } returns listOf(Stock(208.934), Stock(209.7097), Stock(207.225))

        //when
        val average = eodHistoricalDataService.getAveragePriceInPennies("MSD.US", LocalDate.now(), LocalDate.now())

        //then
        expectThat(average) isEqualTo 20862
    }

    @Test
    fun `should return average price of single stock`() {
        //given
        every { eodHistoricalDataApi.getStockPrices(any(), any(), any()) } returns listOf(Stock(208.934))

        //when
        val average = eodHistoricalDataService.getAveragePriceInPennies("MSD.US", LocalDate.now(), LocalDate.now())

        //then
        expectThat(average) isEqualTo 20893
    }
}