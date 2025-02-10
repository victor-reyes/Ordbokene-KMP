import kotlinx.serialization.json.Json
import no.ordbokene.model.json.concept.ConceptResponse
import kotlin.test.Test

class ConceptResponseTest {

  companion object {
    val json = Json { ignoreUnknownKeys = false }
  }

  @Test
  fun shouldParseConceptResponse() {
    listOf("bm", "nn").forEach {
      val rawJson = readResources("concepts_$it.json")
      json.decodeFromString<ConceptResponse>(rawJson)
    }
  }
}
