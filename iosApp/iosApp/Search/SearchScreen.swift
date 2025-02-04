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

#Preview {
    let viewModel = iOSApp.initSearchViewModel()
    SearchScreen().environmentObject(viewModel)
}
