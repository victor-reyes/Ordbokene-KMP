@file:OptIn(ExperimentalSerializationApi::class)

package no.ordbokene.model.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class SubArticle(
    val body: ArticleBody,
    val author: String,
    val lemmas: List<SubLemma>,
    val updated: String? = null,
    val referers: List<Referer> = emptyList(),
    @SerialName("article_id") val articleId: Int,
    val owner: Int,
    @SerialName("dict_id") val dictId: String,
    val version: Int,
    val frontpage: Boolean,
    @SerialName("word_class") val wordClass: String? = null,
    @SerialName("latest_status") val latestStatus: Int,
    @SerialName("referenced_by") val referencedBy: List<ReferencedBy>,
)

@JsonIgnoreUnknownKeys
@Serializable
data class SubLemma(val id: Int, val hgno: Int, val lemma: String)

@Serializable
data class ReferencedBy(
    @SerialName("art_id") val artId: Int,
    val hgno: Int,
    @SerialName("word_form") val wordForm: String,
)
