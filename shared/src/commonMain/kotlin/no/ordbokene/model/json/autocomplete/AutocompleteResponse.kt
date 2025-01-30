package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutocompleteResponse(
  @SerialName("q") val query: String,
  @SerialName("cnt") val count: Int,
  @SerialName("cmatch") val matchCount: Int,
  @SerialName("a") val suggestions: Suggestions,
)
