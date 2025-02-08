package no.ordbokene.model.json.lookup.ids

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RhetoricId {
  @SerialName("overf") OVERF,
  @SerialName("iron") IRON,
  @SerialName("spoekt") SPOEKT,
  @SerialName("neds") NEDS,
  @SerialName("arkais") ARKAIS,
  @SerialName("poet_s") POET_S,
  @SerialName("skjemt") SKJEMT,
  @SerialName("litt") LITT,
}
