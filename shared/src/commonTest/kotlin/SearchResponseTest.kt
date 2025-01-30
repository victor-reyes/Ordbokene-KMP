import kotlinx.serialization.json.Json
import no.ordbokene.model.json.search.ArticlesIds
import no.ordbokene.model.json.search.CountInfo
import no.ordbokene.model.json.search.Info
import no.ordbokene.model.json.search.SearchResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchResponseTest {

  companion object {
    const val TEST_RESPONSE =
      """{"meta": {"bm": {"total": 3}, "nn": {"total": 3}}, "articles": {"bm": [61458, 61460, 62780], "nn": [79916, 82114, 79920]}}"""

    const val TEST_NULLABLE_RESPONSE = """{"meta": {"bm": {"total": 0}}, "articles": {"bm": []}}"""
    val json = Json { ignoreUnknownKeys = false }
  }

  @Test
  fun shouldParseSearchResponse() {
    val parsed = json.decodeFromString<SearchResponse>(TEST_RESPONSE)

    val expected =
      SearchResponse(
        Info(CountInfo(3), CountInfo(3)),
        ArticlesIds(setOf(61458, 61460, 62780), setOf(79916, 82114, 79920)),
      )

    assertEquals(expected, parsed)
  }

  @Test
  fun shouldParseNullableSearchResponse() {
    json.decodeFromString<SearchResponse>(TEST_NULLABLE_RESPONSE)
  }
}
