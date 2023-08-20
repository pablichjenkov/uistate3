package com.pablichj.templato.component.demo.viewmodel

import com.pablichj.templato.component.core.ComponentLifecycle
import com.pablichj.templato.component.core.ComponentLifecycleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class ViewModel : ComponentLifecycle() {
    protected var lifecycleState: ComponentLifecycleState = ComponentLifecycleState.Created

    private val _lifecycleStateFlow = MutableStateFlow(lifecycleState)
    val lifecycleStateFlow: StateFlow<ComponentLifecycleState>
        get() = _lifecycleStateFlow.asStateFlow()

    open fun dispatchStart() {
        lifecycleState = ComponentLifecycleState.Started // It has to be the first line of this block
        onStart()
        _lifecycleStateFlow.value = ComponentLifecycleState.Started
    }

    open fun dispatchStop() {
        lifecycleState = ComponentLifecycleState.Stopped
        onStop()
        _lifecycleStateFlow.value = ComponentLifecycleState.Stopped
    }

    open fun dispatchDestroy() {
        lifecycleState = ComponentLifecycleState.Destroyed
        onDestroy()
        _lifecycleStateFlow.value = ComponentLifecycleState.Destroyed
    }

}