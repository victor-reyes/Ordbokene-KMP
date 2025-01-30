package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = SuggestionAsArraySerializer::class)
data class Suggestion(val word: String, val dictionary: Dictionary)

object SuggestionAsArraySerializer : KSerializer<Suggestion> {
  override val descriptor: SerialDescriptor =
    buildClassSerialDescriptor("SuggestionAsArray") {
      element("word", PrimitiveSerialDescriptor("word", PrimitiveKind.STRING))
      element("dictionary", PrimitiveSerialDescriptor("dictionary", PrimitiveKind.INT))
    }

  override fun serialize(encoder: Encoder, value: Suggestion) = Unit

  override fun deserialize(decoder: Decoder): Suggestion {
    val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("This class can only be loaded from JSON")

    val element = jsonDecoder.decodeJsonElement()
    if (element !is JsonArray || element.size != 2) {
      throw IllegalArgumentException("Expected a 2-element JSON array, got: $element")
    }

    val word = element[0].jsonPrimitive.content
    val code = element[1].jsonPrimitive.int
    return Suggestion(word, Dictionary.fromCode(code))
  }
}
