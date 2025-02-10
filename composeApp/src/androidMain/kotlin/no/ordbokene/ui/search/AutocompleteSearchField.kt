package no.ordbokene.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AutocompleteSearchField(
  query: String,
  suggestions: List<String>,
  interactionSource: MutableInteractionSource,
  onQueryChanged: (String) -> Unit,
  onSearch: (String) -> Unit,
) {
  val focusManager = LocalFocusManager.current
  val focusRequester = remember { FocusRequester() }
  var showSuggestions by remember { mutableStateOf(false) }
  val modifier by remember {
    derivedStateOf { if (showSuggestions) Modifier.fillMaxWidth() else Modifier.width(256.dp) }
  }
  ElevatedCard(
    modifier.background(MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (showSuggestions) 16.dp else 4.dp),
  ) {
    BasicTextField(
      query,
      onQueryChanged,
      modifier.focusRequester(focusRequester).onFocusChanged { showSuggestions = it.isFocused }.animateContentSize(),
      interactionSource = interactionSource,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Companion.Search),
      keyboardActions =
        KeyboardActions(
          onSearch = {
            focusManager.clearFocus()
            onSearch(query)
          }
        ),
      singleLine = true,
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(Modifier.width(8.dp))
          it()
          Spacer(modifier.weight(1f))
          IconButton(onClick = { focusRequester.requestFocus() }) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = "clear")
          }
        }
        AnimatedVisibility(showSuggestions) {
          LazyColumn(Modifier.heightIn(max = 256.dp)) {
            itemsIndexed(suggestions) { index, suggestion ->
              Row(
                Modifier.Companion.fillParentMaxWidth().clickable {
                  onSearch(suggestion)
                  focusManager.clearFocus()
                }
              ) {
                Text(
                  suggestion,
                  Modifier.Companion.padding(12.dp),
                  fontSize = MaterialTheme.typography.labelLarge.fontSize,
                  fontWeight = FontWeight.Companion.Bold,
                )
              }
              if (index < suggestions.lastIndex) HorizontalDivider(Modifier.Companion.width(IntrinsicSize.Max))
            }
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun AutocompleteSearchFieldPreview() {
  val suggestions = listOf("Suggestion 1", "Suggestion 2", "Suggestion 3")
  val interactionSource = remember { MutableInteractionSource() }
  var query by remember { mutableStateOf("hel") }

  AutocompleteSearchField(
    query = query,
    suggestions = suggestions,
    interactionSource = interactionSource,
    onQueryChanged = { newQuery -> query = newQuery },
    onSearch = { searchedQuery -> {} },
  )
}
