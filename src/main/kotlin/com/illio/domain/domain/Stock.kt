package com.illio.domain.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Stock(@JsonProperty("adjusted_close") val adjustedClose: Double)