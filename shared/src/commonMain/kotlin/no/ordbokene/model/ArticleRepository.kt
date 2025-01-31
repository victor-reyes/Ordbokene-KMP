package no.ordbokene.model

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import no.ordbokene.model.json.autocomplete.AutocompleteResponse
import no.ordbokene.model.json.lookup.ArticleResponse

interface ArticleRepository {
  @NativeCoroutines suspend fun fetchAutocomplete(query: String): AutocompleteResponse

  @NativeCoroutines suspend fun fetchArticles(word: String, dictionary: String): List<ArticleResponse>
}

class ArticleRepositoryImpl(val service: DictionaryApiService) : ArticleRepository {
  override suspend fun fetchAutocomplete(query: String) =
    withContext(Dispatchers.IO) { service.fetchAutocomplete(query) }

  override suspend fun fetchArticles(word: String, dictionary: String) =
    withContext(Dispatchers.IO) {
      val ids = service.search(word, dictionary).articlesIds.bm
      ids.map { id -> async { service.fetchArticle(id, dictionary) } }.awaitAll()
    }
}
