package no.ordbokene.di

import no.ordbokene.model.ArticleRepository
import no.ordbokene.model.ArticleRepositoryImpl
import no.ordbokene.model.DictionaryApiService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
  singleOf(::DictionaryApiService)
  singleOf(::ArticleRepositoryImpl) { bind<ArticleRepository>() }
}
