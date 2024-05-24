/*
 * Copyright 2023-2024 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.rpc.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.rpc.sample.data.createRpcClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.rpc.RPCClient
import kotlinx.rpc.internal.streamScoped
import kotlinx.rpc.client.withService
import kotlinx.rpc.sample.CounterApi

val Loading: Nothing? = null

class CounterViewModel : ViewModel() {
  private var rpcClient: RPCClient? = null
  private var counterApi: CounterApi? = null

  private val eventsFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 5)

  private val _uiState: MutableStateFlow<Int?> = MutableStateFlow(Loading)
  val uiState: StateFlow<Int?> = _uiState

  init {
    viewModelScope.launch(Dispatchers.IO) {
      rpcClient = createRpcClient()
      rpcClient?.let {
        counterApi = it.withService()
        fetchData()
      }
    }
  }

  private fun fetchData() {
    viewModelScope.launch(Dispatchers.IO) {
      streamScoped {
        val stateDeferred = async { counterApi?.state(eventsFlow) }
        val state = stateDeferred.await()
        state?.collect { _uiState.value = it }
      }
    }
  }

  fun onIncrement() {
    viewModelScope.launch { eventsFlow.emit(Unit) }
  }
}
