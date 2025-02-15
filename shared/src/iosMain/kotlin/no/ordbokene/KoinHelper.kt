package no.ordbokene

import no.ordbokene.di.sharedModule
import no.ordbokene.model.ArticleRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class KoinHelper : KoinComponent {

  val repository: ArticleRepository by inject()

  fun initKoin() {
    startKoin { modules(sharedModule) }
  }
}
