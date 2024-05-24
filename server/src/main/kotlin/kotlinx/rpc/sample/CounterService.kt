/*
 * Copyright 2023-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.rpc.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class CounterService(override val coroutineContext: CoroutineContext) : CounterApi {
  override suspend fun state(events: Flow<Unit>): Flow<Int> =
    moleculeFlow(RecompositionMode.Immediate) { Counter(events) }
}

@Composable
fun Counter(events: Flow<Unit>): Int {
  var count: Int by remember { mutableStateOf(0) }

  LaunchedEffect(Unit) {
    events.collect { count++ }
  }

  return count
}
