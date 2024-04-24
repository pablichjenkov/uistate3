package com.macaosoftware.component.demo

import com.macaosoftware.plugin.app.PluginManager
import com.macaosoftware.plugin.app.RootComponentProvider
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.demo.startup.StartupCoordinatorViewModel
import com.macaosoftware.component.demo.startup.StartupCoordinatorViewModelFactory
import com.macaosoftware.component.stack.StackComponent
import com.macaosoftware.component.stack.StackComponentDefaults
import platform.posix.exit

class IosRootComponentProvider : RootComponentProvider {

    override suspend fun provideRootComponent(
        pluginManager: PluginManager
    ): Component {

        return StackComponent<StartupCoordinatorViewModel>(
            viewModelFactory = StartupCoordinatorViewModelFactory(
                stackStatePresenter = StackComponentDefaults.createStackStatePresenter(),
                pluginManager = pluginManager,
                onBackPress = {
                    exit(0)
                    true
                }
            ),
            content = StackComponentDefaults.DefaultStackComponentView
        ).also {
            it.deepLinkPathSegment = "_root_navigator_stack"
        }
    }
}
