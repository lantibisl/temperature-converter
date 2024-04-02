package org.d3if0158.temperatureconverter.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0158.temperatureconverter.R
import org.d3if0158.temperatureconverter.navigation.Screen
import org.d3if0158.temperatureconverter.ui.theme.TemperatureConverterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.app_name))
            }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.About.route)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(id = R.string.about),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    val context = LocalContext.current

    val unitOptions = listOf(
        stringResource(id = R.string.celsius),
        stringResource(id = R.string.reaumur),
        stringResource(id = R.string.kelvin),
        stringResource(id = R.string.fahrenheit)
    )

    val otherUnitOptions = listOf(
        "° C", "° R", "K", "° F"
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

    var finalValue by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    var unitError1 by rememberSaveable {
        mutableStateOf(false)
    }

    var unitError2 by rememberSaveable {
        mutableStateOf(false)
    }

    var valueError by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UnitSelector(
            isExpanded1 = isExpanded1,
            temperatureUnit1 = temperatureUnit1,
            temperatureUnit2 = temperatureUnit2,
            unitOptions = unitOptions,
            label = stringResource(id = R.string.initial_unit),
            unitError1
        )
        UnitSelector(
            isExpanded1 = isExpanded2,
            temperatureUnit1 = temperatureUnit2,
            temperatureUnit2 = temperatureUnit1,
            unitOptions = unitOptions,
            label = stringResource(id = R.string.final_unit),
            unitError2
        )
        OutlinedTextField(
            value = initialValue,
            onValueChange = { initialValue = it },
            label = { Text(text = stringResource(id = R.string.initial_value)) },
            isError = valueError,
            trailingIcon = {
                IconPicker(
                    isError = valueError,
                    when (temperatureUnit1.value) {
                        unitOptions[0] -> otherUnitOptions[0]
                        unitOptions[1] -> otherUnitOptions[1]
                        unitOptions[2] -> otherUnitOptions[2]
                        unitOptions[3] -> otherUnitOptions[3]
                        else -> ""
                    }
                )
            },
            supportingText = { ErrorHint(isError = valueError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                unitError1 = temperatureUnit1.value == ""
                unitError2 = temperatureUnit2.value == ""
                valueError = initialValue == ""
                if (unitError1 || unitError2 || valueError) return@Button
                finalValue = temperatureConverter(
                    context,
                    temperatureUnit1.value,
                    temperatureUnit2.value,
                    initialValue.toFloat()
                )
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (finalValue != 0f) {
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = stringResource(
                    id = R.string.final_value_x,
                    initialValue.toFloat(),
                    temperatureUnit1.value.lowercase(),
                    temperatureUnit2.value.lowercase(),
                    finalValue
                ),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.final_value_x,
                            initialValue.toFloat(),
                            temperatureUnit1.value.lowercase(),
                            temperatureUnit2.value.lowercase(),
                            finalValue
                        )
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.share))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSelector(
    isExpanded1: MutableState<Boolean>,
    temperatureUnit1: MutableState<String>,
    temperatureUnit2: MutableState<String>,
    unitOptions: List<String>,
    label: String,
    isError: Boolean
) {
    Box {
        ExposedDropdownMenuBox(
            expanded = isExpanded1.value,
            onExpandedChange = { isExpanded1.value = it }) {
            OutlinedTextField(
                value = temperatureUnit1.value,
                onValueChange = {},
                label = { Text(text = label) },
                isError = isError,
                supportingText = { ErrorHint(isError = isError) },
                readOnly = true,
                trailingIcon = {
                    if (isError) Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
                    else ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded1.value)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
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

            ExposedDropdownMenu(
                expanded = isExpanded1.value,
                onDismissRequest = { isExpanded1.value = false }) {
                val temperatureUnitIndex2 = unitOptions.indexOf(temperatureUnit2.value)
                val filteredUnitOptions2 =
                    unitOptions.filterIndexed { index, _ -> index != temperatureUnitIndex2 }

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

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.invalid_input))
    }
}

private fun temperatureConverter(
    context: Context,
    initialUnit: String,
    finalUnit: String,
    initialValue: Float
): Float {
    var finalValue = 0f
    val unitOptions = listOf(
        context.resources.getString(R.string.celsius),
        context.resources.getString(R.string.reaumur),
        context.resources.getString(R.string.kelvin),
        context.resources.getString(R.string.fahrenheit)
    )
    when (initialUnit) {
        unitOptions[0] -> when (finalUnit) {
            unitOptions[1] -> finalValue = initialValue / 5 * 4
            unitOptions[2] -> finalValue = initialValue + 273
            unitOptions[3] -> finalValue = initialValue / 5 * 9 + 32
        }

        unitOptions[1] -> when (finalUnit) {
            unitOptions[0] -> finalValue = initialValue / 4 * 5
            unitOptions[2] -> finalValue = initialValue / 4 * 5 + 273
            unitOptions[3] -> finalValue = initialValue / 4 * 9 + 32
        }

        unitOptions[2] -> when (finalUnit) {
            unitOptions[0] -> finalValue = initialValue - 273
            unitOptions[1] -> finalValue = (initialValue - 273) / 5 * 4
            unitOptions[3] -> finalValue = (initialValue - 273) / 5 * 9 + 32
        }

        unitOptions[3] -> when (finalUnit) {
            unitOptions[0] -> finalValue = (initialValue - 32) / 9 * 5
            unitOptions[1] -> finalValue = (initialValue - 32) / 9 * 4
            unitOptions[2] -> finalValue = (initialValue - 32) / 9 * 5 + 273
        }
    }
    return finalValue
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainPreview() {
    TemperatureConverterTheme {
        MainScreen(rememberNavController())
    }
}