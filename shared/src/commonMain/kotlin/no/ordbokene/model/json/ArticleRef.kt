@file:OptIn(ExperimentalSerializationApi::class)

package no.ordbokene.model.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@SerialName("article_ref")
data class ArticleRef(
  @SerialName("type_") val type: String = "article_ref",
  @SerialName("article_id") val articleId: Int,
  @SerialName("definition_id") val definitionId: Int? = null,
  @SerialName("definition_order") val definitionOrder: Int? = null,
  val lemmas: List<RefLemma>,
  @SerialName("word_form") val wordForm: String? = null,
) : TextItem

@JsonIgnoreUnknownKeys
@Serializable
data class RefLemma(
  val id: Int? = null,
  val hgno: Int? = null,
  @SerialName("lemma") val lemma: String,
  @SerialName("markdown_lemma") val markdownLemma: String? = null,
  @SerialName("annotated_lemma") val annotatedLemma: AnnotatedLemma? = null,
)
