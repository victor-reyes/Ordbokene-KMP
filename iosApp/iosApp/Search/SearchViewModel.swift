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

    func fetchAsFlow(ids: [KotlinInt], dictionary: String) -> NativeSuspend<NativeFlow<ArticleResponse, Error, KotlinUnit>, Error, KotlinUnit> {
        return ArticleRepositoryNativeKt.fetchArticleFlow(self, ids: ids, dictionary: dictionary)
    }

    func fetchIds(word: String, dictionary: String) -> NativeSuspend<[KotlinInt], Error, KotlinUnit> {
        return ArticleRepositoryNativeKt.fetchIds(self, word: word, dictionary: dictionary)
    }
}

class SearchViewModel: ObservableObject {

    @Published var query: String = ""
    @Published var word: String = ""
    @Published var suggestions: [String] = []
    @Published var articleUiState: ArticleUiState = .loading

    init(repository: ArticleRepository) {

        $query.flatMap {
            Just([]).merge(
                with:
                    createFuture(for: repository.fetchAutocomplete(query: $0))
                    .map(\.uniqueSuggestionArray)
                    .replaceError(with: []))
        }
        .receive(on: DispatchQueue.main)
        .assign(to: &$suggestions)

        $word
            .map { word in
                let loading = Just(ArticleUiState.loading).eraseToAnyPublisher()
                let articles = createFuture(for: repository.fetchIds(word: word, dictionary: "bm"))
                    .replaceError(with: [])
                    .flatMap { ids -> AnyPublisher<ArticleUiState, Never> in
                        if ids.isEmpty {
                            return Just(ArticleUiState.success(articles: []))
                                .eraseToAnyPublisher()
                        } else {
                            return createPublisher(for: repository.fetchAsFlow(ids: ids, dictionary: "bm"))
                                .scan([]) { $0 + [$1] }
                                .dropFirst()
                                .map { articles in ArticleUiState.success(articles: articles) }
                                .replaceError(with: ArticleUiState.error(message: ""))
                                .eraseToAnyPublisher()
                        }
                    }
                    .eraseToAnyPublisher()
                return loading.merge(with: articles).eraseToAnyPublisher()
            }
            .switchToLatest()
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
