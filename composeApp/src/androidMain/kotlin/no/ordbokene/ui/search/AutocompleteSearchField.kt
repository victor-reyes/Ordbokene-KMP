package no.ordbokene.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

  var textFieldWidthPx by remember { mutableStateOf(0.dp) }
  val density = LocalDensity.current

  val modifier by remember {
    derivedStateOf { if (showSuggestions) Modifier.Companion.fillMaxWidth() else Modifier.Companion }
  }
  Column(Modifier.Companion.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
    OutlinedTextField(
      query,
      onQueryChanged,
      modifier
        .focusRequester(focusRequester)
        .onFocusChanged { showSuggestions = it.isFocused }
        .onGloballyPositioned { coordinates -> textFieldWidthPx = with(density) { coordinates.size.width.toDp() } }
        .animateContentSize(),
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
    )
    AnimatedVisibility(showSuggestions) {
      ElevatedCard(Modifier.width(textFieldWidthPx).heightIn(max = 256.dp)) {
        LazyColumn {
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
