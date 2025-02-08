package no.ordbokene.model.json.lookup.ids

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TemporalId {
  @SerialName("foreld") FORELD,
  @SerialName("e") E,
  @SerialName("tidl") TIDL,
  @SerialName("fKr") FKR,
  @SerialName("gl") GL,
  @SerialName("eKr") EKR,
  @SerialName("eldre") ELDRE,
  @SerialName("glt") GLT,
}
