package no.fdk.records_of_processing_activities.utils

import org.springframework.http.HttpHeaders

fun locationHeaderForCreated(location: String): HttpHeaders =
    HttpHeaders().apply {
        add(HttpHeaders.LOCATION, location)
        add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION)
    }
