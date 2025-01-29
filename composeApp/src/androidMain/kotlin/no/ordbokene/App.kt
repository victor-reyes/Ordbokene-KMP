package no.ordbokene

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import no.ordbokene.ui.search.SearchScreen

@Composable
fun App() {
  MaterialTheme { SearchScreen() }
}
