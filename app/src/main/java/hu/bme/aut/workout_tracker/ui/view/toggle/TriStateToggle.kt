package hu.bme.aut.workout_tracker.ui.view.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.workout_tracker.utils.Constants.chartsList

@Composable
fun TriStateToggle(
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(24.dp))
                .background(backgroundColor),
        ) {
            chartsList.forEach { text ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(24.dp))
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                backgroundColor
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}