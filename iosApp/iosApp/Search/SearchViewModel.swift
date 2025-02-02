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
        $query.flatMap {
            createFuture(for: self.repository.fetchAutocomplete(query: $0)).map(\.uniqueSuggestionArray).replaceError(with: [])
        }
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


extension AutocompleteResponse {
    var uniqueSuggestionArray: [String] {
        let suggestions = self.suggestions
        return (suggestions.exact + suggestions.inflection + suggestions.freeText + suggestions.similar).map(\.word).distinct()
    }
}

extension Array where Element: Hashable {
    func distinct() -> [Element] {
        var seen: Set<Element> = []
        return self.filter { seen.insert($0).inserted }
    }
}

enum ArticleUiState {
    case loading
    case error(message: String)
    case success(articles: [ArticleResponse])
}
