package com.illio.adapters.eodhistoricaldata

import com.illio.domain.domain.Stock
import com.illio.domain.services.EodHistoricalData
import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.auto
import java.time.LocalDate

class EodHistoricalDataApi(
    private val apiToken: String,
    private val baseUrl: String = "https://eodhistoricaldata.com/api/eod",
    val client: HttpHandler = ApacheClient()
) : EodHistoricalData {

    val eodHistoricalStockLens = Body.auto<List<Stock>>().toLens()

    override fun getStockPrices(ticker: String, from: LocalDate, to: LocalDate): List<Stock> {
        val request = Request(GET, "$baseUrl/$ticker?api_token=$apiToken&period=d&from=$from&to=$to&fmt=json")
        val response = client(request)

        return when (response.status) {
            OK -> eodHistoricalStockLens(response)
            else -> emptyList()
        }
    }

}