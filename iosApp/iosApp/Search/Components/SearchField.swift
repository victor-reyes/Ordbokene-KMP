//
//  SearchField.swift
//  iosApp
//
//  Created by Victor Reyes on 2025-02-03.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SearchField: View {
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
