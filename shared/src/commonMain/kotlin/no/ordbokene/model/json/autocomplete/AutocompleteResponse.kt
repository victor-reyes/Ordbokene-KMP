package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class AutocompleteResponse(
  @SerialName("q") val query: String,
  @SerialName("cnt") val count: Int,
  @Serializable(with = IntAsBooleanSerializer::class) @SerialName("cmatch") val match: Boolean,
  @SerialName("a") val suggestions: Suggestions,
)

object IntAsBooleanSerializer : KSerializer<Boolean> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntAsBoolean", PrimitiveKind.INT)

  override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeInt(if (value) 1 else 0)

  override fun deserialize(decoder: Decoder) =
    when (decoder.decodeInt()) {
      1 -> true
      0 -> false
      else -> throw IllegalArgumentException("Invalid value for boolean: ${decoder.decodeInt()}")
    }
}
