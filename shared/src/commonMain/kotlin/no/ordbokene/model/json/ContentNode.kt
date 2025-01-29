@file:OptIn(ExperimentalSerializationApi::class)

package no.ordbokene.model.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Polymorphic
@Serializable
sealed interface ContentNode {
  val content: String
  val items: List<TextItem>
}

@Serializable
data class Intro(override val content: String, override val items: List<TextItem> = emptyList()) : ContentNode

@Serializable
data class Quote(override val content: String, override val items: List<TextItem> = emptyList()) : ContentNode

@Serializable
data class Explanation(override val content: String, override val items: List<TextItem> = emptyList()) : ContentNode

@Serializable
data class Tydingstekst(override val content: String, override val items: List<TextItem> = emptyList()) : ContentNode

@JsonIgnoreUnknownKeys
@Serializable
data class AnnotatedLemma(override val content: String, override val items: List<TextItem> = emptyList()) : ContentNode

@JsonIgnoreUnknownKeys
@Serializable
data class PronunciationNode(override val content: String, override val items: List<TextItem>) : ContentNode
