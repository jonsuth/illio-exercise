package com.illio.adapters.rest

import com.illio.domain.AverageStockPriceRequest
import com.illio.domain.services.EodHistoricalDataService
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_PLAIN
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.filter.ServerFilters
import org.http4k.lens.Query
import org.http4k.lens.composite
import org.http4k.lens.localDate
import org.http4k.lens.string
import org.http4k.routing.bind


class StockPriceApi(private val eodHistoricalDataService: EodHistoricalDataService) {

    val averageStockPriceLens = Query.composite {
        AverageStockPriceRequest(
            string().required("ticker")(it),
            localDate().required("from")(it),
            localDate().required("to")(it)
        )
    }

    fun endpoints() = ("/" bind GET to ::getAverageStockPrice)
        .withFilter(ServerFilters.CatchLensFailure)

    private fun getAverageStockPrice(request: Request): Response {
        val averageStockPriceRequest = averageStockPriceLens(request)
        val response = eodHistoricalDataService.getAveragePriceInPennies(averageStockPriceRequest.key, averageStockPriceRequest.from, averageStockPriceRequest.to)

        return Response(OK).with(Body.string(TEXT_PLAIN).toLens() of evaluateResponse(response))
    }

    private fun evaluateResponse(response: Int?) = when (response == null) {
        true -> "Unable to calculate average"
        false -> response.toString()
    }
}

