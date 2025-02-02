//
//  SearchScreen.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-01-29.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SearchScreen: View {
    @EnvironmentObject private var model: SearchViewModel

    var body: some View {
        VStack(spacing: 20) {
            SearchField(query: $model.query, suggestions: model.suggestions) {
                model.search(word: $0)
            }
            Articles(articleUiState: model.articleUiState)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

private struct Articles: View {
    let articleUiState: ArticleUiState

    var body: some View {
        ScrollView {
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
}

private struct SearchField: View {
    @Binding var query: String
    let suggestions: [String]
    let onSelect: (String) -> Void
    @FocusState private var isFocused

    var body: some View {
        VStack {
            TextField("Search", text: $query)
                .onSubmit {
                    withAnimation {
                        isFocused = false
                        onSelect(query)
                    }
                }
                .focused($isFocused, equals: true)
                .padding()
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue))

            if isFocused {
                ScrollView {
                    Suggestions(suggestions: suggestions) { word in
                        withAnimation {
                            isFocused = false
                            onSelect(word)
                        }
                    }
                }
            }
        }
    }
}

private struct Suggestions: View {
    let suggestions: [String]
    let onSelect: (String) -> Void

    var body: some View {
        LazyVStack {
            ForEach(Array(suggestions.enumerated()), id: \.offset) { index, suggestion in
                VStack {
                    HStack {
                        Text(suggestion)
                        Spacer()
                    }
                    .padding()
                    .onTapGesture {
                        print("click on suggestion \(suggestion)")
                        onSelect(suggestion)
                    }
                    if index < suggestions.count - 1 { Divider() }
                }
            }
        }
        .background(Rectangle().fill(Color.white))
        .cornerRadius(10)
        .shadow(color: .gray, radius: 3, x: 2, y: 2)
        .padding(.horizontal)

    }
}

#Preview {
    let viewModel = SearchViewModel()
    SearchScreen().environmentObject(viewModel)
}
