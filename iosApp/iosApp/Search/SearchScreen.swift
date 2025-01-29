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
            TextField("Search", text: $model.query)
                .padding()
                .overlay(RoundedRectangle(cornerRadius: 20).stroke(.blue))

            LazyVStack {
                ForEach(model.articles, id: \.self) { article in
                    Text(article)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

#Preview {
    let viewModel = SearchViewModel()
    SearchScreen().environmentObject(viewModel)
}
