@file:OptIn(ExperimentalSerializationApi::class)

package no.ordbokene.model.json.lookup

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.JsonPrimitive
import no.ordbokene.model.json.lookup.ids.DomainId
import no.ordbokene.model.json.lookup.ids.EntityId
import no.ordbokene.model.json.lookup.ids.GrammarId
import no.ordbokene.model.json.lookup.ids.LanguageId
import no.ordbokene.model.json.lookup.ids.RelationId
import no.ordbokene.model.json.lookup.ids.RhetoricId
import no.ordbokene.model.json.lookup.ids.TemporalId

@JsonClassDiscriminator("type_") @Polymorphic @Serializable sealed interface TextItem

@JsonIgnoreUnknownKeys @Serializable @SerialName("usage") data class Usage(val text: String) : TextItem

@JsonIgnoreUnknownKeys @Serializable @SerialName("superscript") data class Superscript(val text: String) : TextItem

@JsonIgnoreUnknownKeys @Serializable @SerialName("subscript") data class Subscript(val text: String) : TextItem

@Polymorphic
@Serializable
sealed interface UniqueItem : TextItem {
  val id: Enum<*>
}

@Serializable @SerialName("grammar") data class GrammarItem(override val id: GrammarId) : UniqueItem

@Serializable @SerialName("domain") data class DomainItem(override val id: DomainId) : UniqueItem

@Serializable @SerialName("rhetoric") data class RhetoricItem(override val id: RhetoricId) : UniqueItem

@Serializable @SerialName("entity") data class EntityItem(override val id: EntityId) : UniqueItem

@Serializable @SerialName("relation") data class RelationItem(override val id: RelationId) : UniqueItem

@Serializable @SerialName("language") data class LanguageItem(override val id: LanguageId) : UniqueItem

@Serializable @SerialName("temporal") data class TemporalItem(override val id: TemporalId) : UniqueItem

@Serializable
@SerialName("quote_inset")
data class QuoteInset(override val content: String, override val items: List<TextItem> = emptyList()) :
  TextItem, ContentNode

@Serializable
@SerialName("pronunciation_guide")
data class PronunciationGuide(override val content: String, override val items: List<TextItem> = emptyList()) :
  TextItem, ContentNode

@Serializable
@SerialName("fraction")
data class FractionItem(
  val items: List<Nothing> = emptyList(),
  @Serializable(with = PrimitiveAsStringSerializer::class) val numerator: String,
  @Serializable(with = PrimitiveAsStringSerializer::class) val denominator: String,
) : TextItem

object PrimitiveAsStringSerializer : KSerializer<String> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("PrimitiveAsStringSerializer", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): String {
    val jsonDecoder = (decoder as JsonDecoder)
    val element = jsonDecoder.decodeJsonElement()
    return when (element) {
      is JsonPrimitive -> element.content
      else -> throw RuntimeException()
    }
  }

  override fun serialize(encoder: Encoder, value: String) = encoder.encodeString(value)
}
