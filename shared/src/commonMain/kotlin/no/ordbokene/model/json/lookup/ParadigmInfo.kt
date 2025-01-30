package no.ordbokene.model.json.lookup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParadigmInfo(
  val from: String,
  val tags: List<String>,
  val inflection: List<InflectionForm>,
  @SerialName("paradigm_id") val paradigmId: Int,
  val standardisation: String,
  @SerialName("inflection_group") val inflectionGroup: String,
  val to: String? = null,
)

@Serializable
data class InflectionForm(
  val tags: List<String> = emptyList(),
  @SerialName("word_form") val wordForm: String?,
  @SerialName("markdown_word_form") val markdownWordForm: String? = null,
)
