package com.illio.adapters.eodhistoricaldata

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.withElementAt
import java.time.LocalDate

internal class EodHistoricalDataApiTest {

    @Test
    fun `should construct url for querying stocks`() {
        //given
        val capturingHandler = RequestCapturingHandler()
        val api = EodHistoricalDataApi("apiKey", "baseUrl", capturingHandler.then { Response(OK).body("""[]""") })

        //when
        api.getStockPrices("MSD.US", LocalDate.of(2021, 5, 1), LocalDate.of(2021, 5, 2))

        //then
        val lastRequest = capturingHandler.requests.last()

        expectThat(lastRequest) {
            get { method } isEqualTo GET
            get { uri } and {
                get { path } isEqualTo "baseUrl/MSD.US"
                get { query } isEqualTo "api_token=apiKey&period=d&from=2021-05-01&to=2021-05-02&fmt=json"
            }
        }
    }

    @Test
    fun `should return stocks when response is 200`() {
        //given
        val api = EodHistoricalDataApi("apiKey") { Response(OK).body(okResponse) }

        //when
        val response = api.getStockPrices("MSD.US", LocalDate.now(), LocalDate.now())

        //then
        expectThat(response) hasSize 2 and {
            withElementAt(0) {
                get { adjustedClose } isEqualTo 208.9345
            }
            withElementAt(1) {
                get { adjustedClose } isEqualTo 210.1868
            }
        }
    }

    @Test
    fun `should return empty list when response is not 200`() {
        //given
        val api = EodHistoricalDataApi("apiKey") { Response(NOT_FOUND).body("not_found") }

        //when
        val response = api.getStockPrices("MSD.US", LocalDate.now(), LocalDate.now())

        //then
        expectThat(response).isEmpty()
    }

    private val okResponse = """
        [
            {
                "date": "2021-01-04",
                "open": 214,
                "high": 214.72,
                "low": 208.22,
                "close": 210.22,
                "adjusted_close": 208.9345,
                "volume": 4055409
            },
            {
                "date": "2021-01-05",
                "open": 210.18,
                "high": 211.95,
                "low": 209.62,
                "close": 211.48,
                "adjusted_close": 210.1868,
                "volume": 2576065
            }
        ]
    """.trimIndent()

    class RequestCapturingHandler : Filter {
        val requests = mutableListOf<Request>()
        override fun invoke(next: HttpHandler): HttpHandler {
            return { request ->
                requests.add(request)
                next(request)
            }
        }
    }
}