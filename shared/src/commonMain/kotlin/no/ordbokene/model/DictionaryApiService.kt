package no.ordbokene.model

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
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
import no.ordbokene.model.json.concept.ConceptResponse
import no.ordbokene.model.json.lookup.ArticleResponse
import no.ordbokene.model.json.search.SearchResponse

class DictionaryApiService(val client: HttpClient) {

  suspend fun fetchAutocomplete(query: String, dict: String = "bm", scope: String = "eifs") =
    client
      .get("api/suggest") {
        url {
          parameters.apply {
            append("q", query)
            append("dict", dict)
            append("scope", scope)
            append("dform", "int")
          }
        }
      }
      .body<AutocompleteResponse>()

  suspend fun search(word: String, dict: String = "bm,nn", scope: String = "eif") =
    client
      .get("api/articles") {
        url {
          parameters.apply {
            append("w", word)
            append("dict", dict)
            append("scope", scope)
            append("n", 50.toString())
          }
        }
      }
      .body<SearchResponse>()

  suspend fun fetchArticle(id: Int, dict: String) = client.get("$dict/article/$id.json").body<ArticleResponse>()

  suspend fun fetchConcepts(dict: String) = client.get("$dict/concepts.json").body<ConceptResponse>().concepts

  suspend fun fetchWordClassAbbreviations(dict: String) =
    client.get("$dict/word_class.json").body<Map<String, String>>()

  suspend fun fetchSubWordClassAbbreviations(dict: String) =
    client.get("$dict/sub_word_class.json").body<Map<String, String>>()
}

expect val engine: HttpClientEngineFactory<HttpClientEngineConfig>

val httpClient =
  HttpClient(engine) {
      install(ContentNegotiation) { json(Json) }
      install(HttpCache) { publicStorage(SharedCacheStorage()) }
      install(DefaultRequest) {
        url("https://ord.uib.no/")
        contentType(ContentType.Application.Json)
      }
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
