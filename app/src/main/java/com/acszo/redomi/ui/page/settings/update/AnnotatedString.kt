package com.acszo.redomi.ui.page.settings.update

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun AnnotatedString(string: String) {
    Text(
        text = buildAnnotatedString {
            string.lineSequence().forEach { line ->
                val find = "# "
                if (line.contains(find)) {
                    val newLine = line.replace(find, "")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        )
                    ) {
                        append("$newLine\n")
                    }
                } else {
                    append("$line\n")
                }
            }
        },
        modifier = Modifier.padding(horizontal = 10.dp),
    )
}