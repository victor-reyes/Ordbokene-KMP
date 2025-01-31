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
    @Published var word: String = ""
    @Published var suggestions: [String] = []
    @Published var articleUiState: ArticleUiState = .loading

    let repository: ArticleRepository = ArticleRepositoryImpl(service: DictionaryApiService())

    init() {
        $query.flatMap { createFuture(for: self.repository.fetchAutocomplete(query: $0)) }
            .map { $0.suggestions }
            .map { $0.exact.union($0.inflection).union($0.freeText).union($0.similar).map { $0.word } }
            .catch { _ in Just([]) }
            .receive(on: DispatchQueue.main)
            .assign(to: &$suggestions)

        $word
            .flatMap { word in
                Just(.loading).merge(
                    with: createFuture(for: self.repository.fetchArticles(word: word, dictionary: "bm"))
                        .map { ArticleUiState.success(articles: $0) }
                        .catch { error in Just(ArticleUiState.error(message: error.localizedDescription)) })
            }
            .receive(on: DispatchQueue.main)
            .assign(to: &$articleUiState)
    }
}

extension SearchViewModel {
    func search(word: String) {
        self.query = word
        self.word = word
    }
}

enum ArticleUiState {
    case loading
    case error(message: String)
    case success(articles: [ArticleResponse])
}
