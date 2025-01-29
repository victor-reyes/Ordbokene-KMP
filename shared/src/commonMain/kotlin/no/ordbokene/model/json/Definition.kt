package no.ordbokene.model.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Definition(
  val id: Int?,
  @SerialName("type_") val type: String = "definition",
  val elements: List<DefinitionElement> = emptyList(),
  @SerialName("sub_definition") val subDefinition: Boolean? = null,
)
