package no.ordbokene.model.json.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(@SerialName("meta") val info: Info, @SerialName("articles") val articlesIds: ArticlesIds)

@Serializable data class Info(val bm: CountInfo? = null, val nn: CountInfo? = null)

@Serializable data class CountInfo(val total: Int)

@Serializable data class ArticlesIds(val bm: Set<Int> = emptySet(), val nn: Set<Int> = emptySet())
