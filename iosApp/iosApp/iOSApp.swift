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
        let helper: KoinHelper = KoinHelper()
        helper.doInitKoin()
        let repository = helper.repository
        let viewModel = SearchViewModel(repository: repository)
        return viewModel
    }
}
