package com.expediagroup.startup.springboot

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class SpringBootStartupProvider {

    fun getFromFile(filepath: String): StartupActuatorInformation =
        Json.decodeFromString(
            File(filepath).readText(Charsets.UTF_8))

    fun getFromUrl(url: String): StartupActuatorInformation? {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return Json.decodeFromString(response.body())
    }
}