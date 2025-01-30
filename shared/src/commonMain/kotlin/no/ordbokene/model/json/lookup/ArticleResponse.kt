package no.ordbokene.model.json.lookup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
  val body: ArticleBody,
  val author: String,
  val lemmas: List<ArticleLemma>,
  val status: Int,
  val suggest: List<String>,
  val updated: String,
  val referers: List<Referer>,
  val submitted: String,
  @SerialName("article_id") val articleId: Int,
  @SerialName("edit_state") val editState: String? = null,
  @SerialName("to_index") val toIndex: List<String> = emptyList(),
)

@Serializable
data class ArticleLemma(
  val id: Int,
  val hgno: Int,
  @SerialName("lemma") val lemma: String,
  @SerialName("paradigm_info") val paradigmInfo: List<ParadigmInfo>,
  @SerialName("split_inf") val splitInf: Boolean,
  @SerialName("final_lexeme") val finalLexeme: String,
  @SerialName("initial_lexeme") val initialLexeme: String? = null,
  @SerialName("inflection_class") val inflectionClass: String? = null,
  @SerialName("markdown_lemma") val markdownLemma: String? = null,
  @SerialName("annotated_lemma") val annotatedLemma: AnnotatedLemma? = null,
  val junction: String? = null,
  @SerialName("neg_junction") val negJunction: String? = null,
  @SerialName("added_norm") val addedNorm: Boolean? = null,
)
