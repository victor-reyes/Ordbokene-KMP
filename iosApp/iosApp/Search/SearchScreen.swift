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
        ScrollView {
            Spacer(minLength: 64)
            Articles(articleUiState: model.articleUiState).frame(maxWidth: .infinity, maxHeight: .infinity)
            Spacer(minLength: 32)
        }
        .overlay(alignment: .top) {
            SearchField(query: $model.query, suggestions: model.suggestions) {
                model.search(word: $0)
            }
        }
        .ignoresSafeArea(.container, edges: .bottom)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding([.top, .horizontal])
    }
}

private struct Articles: View {
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

private struct SearchField: View {
    @Binding var query: String
    let suggestions: [String]
    let onSelect: (String) -> Void
    @FocusState private var isFocused

    var body: some View {
        VStack {
            TextField("Tap to search", text: $query)
                .submitLabel(.search)
                .autocorrectionDisabled(true)
                .textInputAutocapitalization(.never)
                .onSubmit {
                    withAnimation {
                        isFocused = false
                        onSelect(query)
                    }
                }
                .focused($isFocused, equals: true)
                .padding()

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
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
        .background(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .stroke(.gray, lineWidth: 1)
                .fill(.background)
                .shadow(color: .gray, radius: 2, x: 2, y: 2)
        )
        .frame(maxHeight: 400, alignment: .top)
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
    }
}

#Preview {
    let viewModel = SearchViewModel()
    SearchScreen().environmentObject(viewModel)
}
