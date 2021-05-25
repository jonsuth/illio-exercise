package com.illio

import com.illio.adapters.eodhistoricaldata.EodHistoricalDataApi
import com.illio.adapters.rest.StockPriceApi
import com.illio.domain.services.EodHistoricalDataService
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {
    val eodHistoricalDataApi = EodHistoricalDataApi(apiToken = "OeAFFmMliFG5orCUuwAKQ8l4WWFQ67YX")
    val eodHistoricalDataService = EodHistoricalDataService(eodHistoricalDataApi)

    val app = StockPriceApi(eodHistoricalDataService)

    app.endpoints().asServer(Jetty(8080)).start()
}