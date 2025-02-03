//
//  Articles.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-02-03.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct Articles: View {
    let articleUiState: ArticleUiState

    var body: some View {
        switch articleUiState {
        case .loading:
            ProgressView()
        case .error(let message):
            Text("Error: \(message)")
        case .success(let articles):
            LazyVStack {
                ForEach(articles, id: \.self) { article in
                    Text(article.lemmas.first!.lemma)
                }
            }
        }
    }
}
