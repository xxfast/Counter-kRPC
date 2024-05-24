/*
 * Copyright 2023-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.rpc.sample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

  private val viewModel: CounterViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val count by viewModel.uiState.collectAsState()
      MaterialTheme {
        CounterView(
          count = count,
          onIncrement = viewModel::onIncrement
        )
      }
    }
  }
}

@Composable
fun CounterView(
  count: Int?,
  onIncrement: () -> Unit
) {
  Card(
    onClick = onIncrement,
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .height(100.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      if (count == Loading) CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
      ) else Row(
        modifier = Modifier.align(Alignment.Center)
      ) {
        count.toString().forEach { digit ->
          AnimatedContent(
            targetState = digit,
            label = "",
            transitionSpec = {
              slideInVertically { -it } + fadeIn() + scaleIn() togetherWith
                  slideOutVertically { it } + fadeOut() + scaleOut()
            },
          ) {
            Text(
              text = "$it",
              style = MaterialTheme.typography.headlineLarge
            )
          }
        }
      }
    }

  }
}
