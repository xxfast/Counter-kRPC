/*
 * Copyright 2023-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.rpc.sample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.rpc.RPC

interface CounterApi : RPC {
  suspend fun state(events: Flow<Unit>): Flow<Int>
}
