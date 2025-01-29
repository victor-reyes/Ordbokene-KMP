@file:OptIn(ExperimentalSerializationApi::class)

package no.ordbokene.model.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.JsonObject

@JsonClassDiscriminator("type_") @Polymorphic @Serializable sealed interface DefinitionElement

@Serializable
@SerialName("definition")
data class SubDefinitionElement(
  val id: Int,
  val elements: List<DefinitionElement>,
  @SerialName("sub_definition") val subDefinition: Boolean? = null,
  val status: String? = null,
) : DefinitionElement

@Serializable
@SerialName("explanation")
data class ExplanationElement(
  val content: String,
  val items: List<TextItem> = emptyList(),
  val attest: List<Nothing> = emptyList(),
  val usage: List<JsonObject> = emptyList(),
) : DefinitionElement

@JsonIgnoreUnknownKeys
@Serializable
@SerialName("example")
data class ExampleElement(val quote: Quote, val explanation: Explanation? = null) : DefinitionElement

@Serializable
@SerialName("sub_article")
data class SubArticleElement(
  @SerialName("article_id") val articleId: Int,
  val lemmas: List<String> = emptyList(),
  val intro: Intro,
  val article: SubArticle,
  val status: String? = null,
) : DefinitionElement

@JsonIgnoreUnknownKeys
@Serializable
@SerialName("compound_list")
data class CompoundListElement(val intro: Intro, val elements: List<ArticleRef> = emptyList()) : DefinitionElement
