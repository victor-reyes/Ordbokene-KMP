package no.ordbokene.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(@SerialName("article_id") val articleId: Long)
