//
//  SearchViewModel.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-01-29.
//  Copyright © 2025 orgName. All rights reserved.
//

import Combine

class SearchViewModel: ObservableObject {

    @Published var query: String = ""
    @Published var articles: [String] = []

    init() {
        $query.map { query in
            ["Article 1", "Article 2", "Article 3", "Article 4"].map {
                "\($0) - \(query)"
            }
        }
        .assign(to: &$articles)
    }
}
