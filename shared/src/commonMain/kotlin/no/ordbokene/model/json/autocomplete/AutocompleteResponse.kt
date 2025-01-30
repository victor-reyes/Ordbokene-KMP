package no.ordbokene.model.json.autocomplete

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
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

@Serializable
data class Suggestions(
  val exact: Set<Suggestion> = emptySet(),
  @SerialName("inflect") val inflection: Set<Suggestion> = emptySet(),
  val similar: Set<Suggestion> = emptySet(),
  @SerialName("freetext") val freeText: Set<Suggestion> = emptySet(),
)

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

enum class Dictionary {
  BM,
  NN,
  BOTH;

  companion object {
    fun fromCode(code: Int) =
      when (code) {
        1 -> BM
        2 -> NN
        3 -> BOTH
        else -> throw IllegalArgumentException("Invalid dictionary code: $code")
      }
  }
}
