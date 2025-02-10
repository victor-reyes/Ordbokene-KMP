import kotlinx.serialization.json.Json
import no.ordbokene.model.json.autocomplete.AutocompleteResponse
import no.ordbokene.model.json.autocomplete.Dictionary
import no.ordbokene.model.json.autocomplete.Suggestion
import kotlin.test.Test
import kotlin.test.assertEquals

class AutocompleteTest {

  companion object {
    const val TEST_RESPONSE =
      """{"q": "tomte", "cnt": 9, "cmatch": 1, "a": {"exact": [["tomte", 3], ["tomtefelt", 2], ["tomtekall", 2]]}}"""

    val json = Json { ignoreUnknownKeys = false }
  }

  @Test
  fun shouldParseAutocompleteResponse() {
    val parsed = json.decodeFromString<AutocompleteResponse>(TEST_RESPONSE)

    assertEquals("tomte", parsed.query)
    assertEquals(
      listOf(
        Suggestion("tomte", Dictionary.BOTH),
        Suggestion("tomtefelt", Dictionary.NN),
        Suggestion("tomtekall", Dictionary.NN),
      ),
      parsed.suggestions.exact,
    )
  }
}
