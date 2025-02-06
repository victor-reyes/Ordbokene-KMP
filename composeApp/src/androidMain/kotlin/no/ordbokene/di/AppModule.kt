package no.ordbokene.di

import no.ordbokene.ui.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module { viewModelOf(::SearchViewModel) }
