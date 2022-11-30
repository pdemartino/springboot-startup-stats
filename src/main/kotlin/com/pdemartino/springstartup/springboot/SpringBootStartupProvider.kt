package com.pdemartino.springstartup.springboot

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class SpringBootStartupProvider {

    private val TIMEOUT_SECS = 20L

    fun getFromFile(filepath: String): StartupActuatorInformation =
        Json.decodeFromString(
            File(filepath).readText(Charsets.UTF_8))

    fun getFromUrl(url: String): StartupActuatorInformation? {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT_SECS))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return Json.decodeFromString(response.body())
    }
}
