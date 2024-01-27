package com.macaosoftware.app

import androidx.compose.runtime.mutableStateOf
import com.macaosoftware.component.core.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication

class MacaoKoinApplicationState(
    // iosBridge: IosBridge, // Might be useful later
    dispatcher: CoroutineDispatcher,
    val rootComponentKoinProvider: RootComponentKoinProvider,
    private val koinModuleInitializer: KoinModuleInitializer
) {
    private val coroutineScope = CoroutineScope(dispatcher)

    var stage = mutableStateOf<Stage>(Stage.Created)

    fun start() {
        coroutineScope.launch {

            stage.value = Stage.KoinLoading
            val appModule = koinModuleInitializer.initialize()

            val koinApplication = koinApplication {
                printLogger()
                modules(appModule)
            }

            val koinDiContainer = KoinDiContainer(koinApplication)

            val rootComponent = rootComponentKoinProvider.provideRootComponent(koinDiContainer)

            stage.value = Stage.Started(rootComponent)
        }
    }
}

sealed class Stage {
    data object Created : Stage()
    data object KoinLoading : Stage()
    class Started(val rootComponent: Component) : Stage()
}
