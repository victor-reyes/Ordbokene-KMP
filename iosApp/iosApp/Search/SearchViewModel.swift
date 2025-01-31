//
//  SearchViewModel.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-01-29.
//  Copyright © 2025 orgName. All rights reserved.
//

import Combine
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesCore
import Shared

extension ArticleRepository {
    func fetchAutocomplete(query: String) -> NativeSuspend<AutocompleteResponse, Error, KotlinUnit> {
        ArticleRepositoryNativeKt.fetchAutocomplete(self, query: query)
    }

    func fetchArticles(word: String, dictionary: String) -> NativeSuspend<[ArticleResponse], Error, KotlinUnit> {
        ArticleRepositoryNativeKt.fetchArticles(self, word: word, dictionary: dictionary)
    }
}

class SearchViewModel: ObservableObject {

    @Published var query: String = ""
    @Published var articles: [String] = []

    let repository: ArticleRepository = ArticleRepositoryImpl(service: DictionaryApiService())

    init() {
        $query.flatMap { createFuture(for: self.repository.fetchAutocomplete(query: $0)) }
            .map { $0.suggestions }
            .map { $0.exact.union($0.inflection).union($0.inflection).union($0.freeText).map { $0.word } }
            .catch { _ in Just([]) }
            .receive(on: DispatchQueue.main)
            .assign(to: &$articles)
    }
}
