import Shared
import SwiftUI

struct ContentView: View {

    var body: some View {
        SearchScreen()
    }
}

#Preview {
    let viewModel = SearchViewModel()
    ContentView().environmentObject(viewModel)
}
