package no.ordbokene.model

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.ordbokene.initLogger
import no.ordbokene.model.json.autocomplete.AutocompleteResponse

class DictionaryApiService {

  companion object {
    val client =
      HttpClient {
          install(ContentNegotiation) { json(Json) }
          install(DefaultRequest) { url("https://ord.uib.no/api/") }
          install(Logging) {
            level = LogLevel.HEADERS
            logger =
              object : Logger {
                override fun log(message: String) {
                  Napier.v(message, tag = "HTTP Client")
                }
              }
          }
        }
        .also { initLogger() }
  }

  suspend fun fetchAutocomplete(query: String) =
    client
      .get("suggest") {
        contentType(ContentType.Application.Json)
        url {
          parameters.apply {
            append("q", query)
            append("dict", "bm,nn")
            append("include", "eifs")
            append("dform", "int")
          }
        }
      }
      .body<AutocompleteResponse>()
}
