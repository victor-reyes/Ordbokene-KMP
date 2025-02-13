package no.ordbokene.model.json.lookup

import kotlinx.serialization.Serializable

@Serializable
data class ArticleBody(
  val definitions: List<Definition>,
  val etymology: List<EtymologyNode> = emptyList(),
  val pronunciation: List<PronunciationNode> = emptyList(),
)
