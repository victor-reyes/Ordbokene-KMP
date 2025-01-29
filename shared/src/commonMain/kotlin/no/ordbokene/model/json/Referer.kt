package no.ordbokene.model.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Referer(
    val hgno: Int? = null,
    val lemma: String? = null,
    @SerialName("article_id") val articleId: Int
)
