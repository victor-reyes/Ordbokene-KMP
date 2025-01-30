//
//  SearchViewModel.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-01-29.
//  Copyright © 2025 orgName. All rights reserved.
//

import Combine
import Shared

class SearchViewModel: ObservableObject {

    @Published var query: String = ""
    @Published var articles: [String] = []

    let repository = ArticleRepositoryImpl(service: DictionaryApiService())

    init() {
        $query.flatMap { query in
            Future { promise in
                Task {
                    let suggestions = try! await self.repository
                        .fetchAutocomplete(query: query)
                        .suggestions
                    let result = suggestions.exact.union(suggestions.inflection)
                        .union(
                            suggestions.similar
                        ).union(suggestions.freeText).map {
                            $0.word
                        }
                    promise(.success(result))
                }
            }

        }
        .assign(to: &$articles)
    }
}
