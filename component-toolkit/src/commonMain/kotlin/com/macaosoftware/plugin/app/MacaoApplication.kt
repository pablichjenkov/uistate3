package com.macaosoftware.plugin.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.macaosoftware.component.PlatformComponentRenderer

@Composable
fun MacaoApplication(
    applicationState: MacaoApplicationState
) {

    when (val stage = applicationState.stage.value) {

        Stage.Created -> {
            SideEffect {
                applicationState.start()
            }
        }

        Stage.Loading -> {
        }

        is Stage.Started -> {
            PlatformComponentRenderer(rootComponent = stage.rootComponent)
        }
    }
}