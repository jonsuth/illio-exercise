package com.illio.adapters.rest

import com.illio.domain.services.EodHistoricalDataService
import io.mockk.every
import io.mockk.mockk
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class StockPriceApiTest {
    private val eodHistoricalDataService = mockk<EodHistoricalDataService>()
    private val endpoints = StockPriceApi(eodHistoricalDataService).endpoints()

    @Test
    fun `should respond with 400 for missing query parameters`() {
        //given
        every { eodHistoricalDataService.getAveragePriceInPennies(any(), any(), any()) } returns 1000
        val request = Request(GET, "?ticker=MCD.US&from=2021-05-01")

        //when
        val response = endpoints(request)

        //then
        expectThat(response.status) isEqualTo BAD_REQUEST
    }

    @Test
    fun `should respond with 400 for malformed query parameters`() {
        //given
        every { eodHistoricalDataService.getAveragePriceInPennies(any(), any(), any()) } returns 1000
        val request = Request(GET, "?ticker=MCD.US&from=2021-05-01&to=02-05-2021")

        //when
        val response = endpoints(request)

        //then
        expectThat(response.status) isEqualTo BAD_REQUEST
    }

    @Test
    fun `should respond with 200 for valid request`() {
        //given
        every { eodHistoricalDataService.getAveragePriceInPennies(any(), any(), any()) } returns 1000
        val request = Request(GET, "?ticker=MCD.US&from=2021-05-01&to=2021-05-02")

        //when
        val response = endpoints(request)

        //then
        expectThat(response) {
            get { status } isEqualTo OK
            get { body.toString() } isEqualTo "1000"
        }
    }

    @Test
    fun `should respond with 200 and error message for valid request`() {
        //given
        every { eodHistoricalDataService.getAveragePriceInPennies(any(), any(), any()) } returns null
        val request = Request(GET, "?ticker=MCD.US&from=2021-05-01&to=2021-05-02")

        //when
        val response = endpoints(request)

        //then
        expectThat(response) {
            get { status } isEqualTo OK
            get { body.toString() } isEqualTo "Unable to calculate average"
        }
    }

}