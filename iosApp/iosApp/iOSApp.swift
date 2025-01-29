import SwiftUI

@main
struct iOSApp: App {
    @StateObject private var searchViewModel = SearchViewModel()
    var body: some Scene {
        WindowGroup {
            ContentView().environmentObject(searchViewModel)
        }
    }
}
