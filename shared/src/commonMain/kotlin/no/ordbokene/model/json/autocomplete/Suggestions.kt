package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Suggestions(
  val exact: Set<Suggestion> = emptySet(),
  @SerialName("inflect") val inflection: Set<Suggestion> = emptySet(),
  val similar: Set<Suggestion> = emptySet(),
  @SerialName("freetext") val freeText: Set<Suggestion> = emptySet(),
)
