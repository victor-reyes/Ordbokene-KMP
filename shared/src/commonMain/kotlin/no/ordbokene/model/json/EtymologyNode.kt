package no.ordbokene.model.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class EtymologyType {
  @SerialName("etymology_litt") LITT,
  @SerialName("etymology_language") LANGUAGE,
  @SerialName("etymology_reference") REFERENCE,
}

@Serializable
data class EtymologyNode(
  @SerialName("type_") val type: EtymologyType,
  override val content: String,
  override val items: List<TextItem>,
) : ContentNode
