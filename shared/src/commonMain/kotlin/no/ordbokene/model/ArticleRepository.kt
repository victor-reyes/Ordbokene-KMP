package no.ordbokene.model

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import no.ordbokene.model.json.autocomplete.AutocompleteResponse
import no.ordbokene.model.json.lookup.ArticleResponse

interface ArticleRepository {
  @NativeCoroutines suspend fun fetchAutocomplete(query: String): AutocompleteResponse

  @NativeCoroutines suspend fun fetchIds(word: String, dictionary: String): List<Int>

  @NativeCoroutines suspend fun fetchArticleFlow(ids: List<Int>, dictionary: String): Flow<ArticleResponse>
}

class ArticleRepositoryImpl(val service: DictionaryApiService) : ArticleRepository {
  override suspend fun fetchAutocomplete(query: String) =
    withContext(Dispatchers.IO) { service.fetchAutocomplete(query) }

  override suspend fun fetchIds(word: String, dictionary: String): List<Int> =
    withContext(Dispatchers.IO) { service.search(word, dictionary).articlesIds.bm }.toList()

  override suspend fun fetchArticleFlow(ids: List<Int>, dictionary: String) = channelFlow {
    val concurrencyLimit = 5
    val semaphore = Semaphore(concurrencyLimit)
    ids
      .map { async(Dispatchers.IO) { semaphore.withPermit { tryFetch { service.fetchArticle(it, dictionary) } } } }
      .forEach { send(it.await()) }
  }
}

suspend fun <T> tryFetch(n: Int = 5, block: suspend () -> T): T {
  repeat(n) {
    try {
      return block()
    } catch (e: Exception) {
      Napier.e(message = e.message ?: "Unknown error", tag = "ArticleRepositoryImpl")
    }
  }
  throw (Exception("Failed to fetch"))
}
