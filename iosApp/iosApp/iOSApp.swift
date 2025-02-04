import Shared
import SwiftUI

@main
struct iOSApp: App {
    @StateObject private var searchViewModel: SearchViewModel = initSearchViewModel()

    var body: some Scene {
        WindowGroup {
            ContentView().environmentObject(searchViewModel)
        }
    }
}

extension iOSApp {
    static func initSearchViewModel() -> SearchViewModel {
        let service = DictionaryApiService()
        let repository: ArticleRepository = ArticleRepositoryImpl(service: service)
        let viewModel = SearchViewModel(repository: repository)
        return viewModel
    }
}
