import Shared
import SwiftUI

struct ContentView: View {

    var body: some View {
        SearchScreen()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        let viewModel = SearchViewModel()
        ContentView().environmentObject(viewModel)
    }
}
