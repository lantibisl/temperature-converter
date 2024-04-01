package org.d3if0158.temperatureconverter.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.d3if0158.temperatureconverter.R
import org.d3if0158.temperatureconverter.ui.theme.TemperatureConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
        )
    }) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    val unitOptions = listOf(
        stringResource(id = R.string.celsius),
        stringResource(id = R.string.reaumur),
        stringResource(id = R.string.kelvin),
        stringResource(id = R.string.fahrenheit)
    )

    val isExpanded1 = rememberSaveable {
        mutableStateOf(false)
    }

    val isExpanded2 = rememberSaveable {
        mutableStateOf(false)
    }

    val temperatureUnit1 = rememberSaveable {
        mutableStateOf("")
    }

    val temperatureUnit2 = rememberSaveable {
        mutableStateOf("")
    }

    var initialValue by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UnitSelector(isExpanded1 = isExpanded1, temperatureUnit1 = temperatureUnit1, temperatureUnit2 = temperatureUnit2, unitOptions = unitOptions)
        UnitSelector(isExpanded1 = isExpanded2, temperatureUnit1 = temperatureUnit2, temperatureUnit2 = temperatureUnit1, unitOptions = unitOptions)
        OutlinedTextField(
            value = initialValue,
            onValueChange = { initialValue = it },
            label = { Text(text = stringResource(id = R.string.initial_value)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSelector(isExpanded1: MutableState<Boolean>, temperatureUnit1: MutableState<String>, temperatureUnit2: MutableState<String>, unitOptions: List<String>) {
    Box{
        ExposedDropdownMenuBox(expanded = isExpanded1.value, onExpandedChange = { isExpanded1.value = it }) {
            OutlinedTextField(
                value = temperatureUnit1.value,
                onValueChange = {},
                label = { Text(text = stringResource(id = R.string.initial_unit))},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded1.value)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            ExposedDropdownMenu(expanded = isExpanded1.value, onDismissRequest = { isExpanded1.value = false }) {
                val temperatureUnitIndex2 = unitOptions.indexOf(temperatureUnit2.value)
                val filteredUnitOptions2 = unitOptions.filterIndexed { index, _ -> index != temperatureUnitIndex2 }

                when (temperatureUnitIndex2) {
                    in unitOptions.indices -> {
                        filteredUnitOptions2.forEach { text ->
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    temperatureUnit1.value = text
                                    isExpanded1.value = false
                                }
                            )
                        }
                    }
                    else -> {
                        unitOptions.forEach { text ->
                            DropdownMenuItem(
                                text = { Text(text = text) },
                                onClick = {
                                    temperatureUnit1.value = text
                                    isExpanded1.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainPreview() {
    TemperatureConverterTheme {
        MainScreen()
    }
}