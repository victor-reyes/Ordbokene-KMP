package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Suggestions(
  val exact: List<Suggestion> = emptyList(),
  @SerialName("inflect") val inflection: List<Suggestion> = emptyList(),
  val similar: List<Suggestion> = emptyList(),
  @SerialName("freetext") val freeText: List<Suggestion> = emptyList(),
  val translate: List<Suggestion> = emptyList(),
)
