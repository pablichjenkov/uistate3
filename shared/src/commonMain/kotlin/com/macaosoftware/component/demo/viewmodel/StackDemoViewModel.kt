package com.macaosoftware.component.demo.viewmodel

import com.macaosoftware.component.adaptive.AdaptiveSizeComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponent
import com.macaosoftware.component.bottomnavigation.BottomNavigationComponentDefaults
import com.macaosoftware.component.bottomnavigation.BottomNavigationStatePresenterDefault
import com.macaosoftware.component.bottomnavigation.BottomNavigationStyle
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.demo.view.DemoType
import com.macaosoftware.component.demo.view.MainScreenView
import com.macaosoftware.component.demo.viewmodel.factory.AdaptiveSizeDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.AppViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.BottomNavigationDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.DrawerDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.MainScreenViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.PagerDemoViewModelFactory
import com.macaosoftware.component.demo.viewmodel.factory.PanelDemoViewModelFactory
import com.macaosoftware.component.drawer.DrawerComponent
import com.macaosoftware.component.drawer.DrawerComponentDefaults
import com.macaosoftware.component.drawer.DrawerHeaderDefaultState
import com.macaosoftware.component.drawer.DrawerStatePresenterDefault
import com.macaosoftware.component.drawer.DrawerStyle
import com.macaosoftware.component.pager.PagerComponent
import com.macaosoftware.component.pager.PagerComponentDefaults
import com.macaosoftware.component.panel.PanelComponent
import com.macaosoftware.component.panel.PanelComponentDefaults
import com.macaosoftware.component.panel.PanelHeaderStateDefault
import com.macaosoftware.component.panel.PanelStatePresenterDefault
import com.macaosoftware.component.panel.PanelStyle
import com.macaosoftware.component.stack.StackComponent
import com.macaosoftware.component.stack.StackComponentDefaults
import com.macaosoftware.component.stack.StackComponentViewModel
import com.macaosoftware.component.stack.StackStatePresenter
import com.macaosoftware.component.viewmodel.StateComponent
import kotlinx.coroutines.Dispatchers

class StackDemoViewModel(
    stackComponent: StackComponent<StackComponentViewModel>,
    override val stackStatePresenter: StackStatePresenter,
    onBackPress: () -> Boolean
) : StackComponentViewModel(stackComponent) {

    private val mainScreenComponent = StateComponent<MainScreenViewModel>(
        viewModelFactory = MainScreenViewModelFactory(
            onItemSelected = {
                val component = when (it) {
                    DemoType.bottomNavigation -> bottomNavigationComponent
                    DemoType.drawer -> drawerComponent
                    DemoType.pager -> pagerComponent
                    DemoType.panel -> panelComponent
                    DemoType.adaptive -> adaptiveSizeComponent
                    DemoType.coordinator -> appComponent
                }
                stackComponent.navigator.push(component)
            },
            onBackPress = onBackPress
        ),
        content = MainScreenView
    )

    val adaptiveSizeComponent =
        AdaptiveSizeComponent(
            AdaptiveSizeDemoViewModelFactory()
        ).also {
            it.deepLinkPathSegment = "_navigator_adaptive"
        }


    val bottomNavigationComponent =
        BottomNavigationComponent(
            // pushStrategy = FixSizedPushStrategy(1), // Uncomment to test other push strategies
            viewModelFactory = BottomNavigationDemoViewModelFactory(
                bottomNavigationStatePresenter = BottomNavigationStatePresenterDefault(
                    dispatcher = Dispatchers.Main,
                    bottomNavigationStyle = BottomNavigationStyle(
                        showLabel = true
                    )
                )
            ),
            content = BottomNavigationComponentDefaults.BottomNavigationComponentView
        ).also {
            it.deepLinkPathSegment = "_navigator_bottom_navigation"
        }


    val drawerComponent =
        DrawerComponent(
            viewModelFactory = DrawerDemoViewModelFactory(
                drawerStatePresenter = DrawerStatePresenterDefault(
                    dispatcher = Dispatchers.Main,
                    drawerStyle = DrawerStyle(),
                    drawerHeaderState = DrawerHeaderDefaultState(
                        title = "Component Toolkit",
                        description = "This is the default Drawer Component",
                        imageUri = "no_image",
                        style = DrawerStyle()
                    )
                )
            ),
            content = DrawerComponentDefaults.DrawerComponentView
        ).also {
            it.deepLinkPathSegment = "_navigator_drawer"
        }

    val pagerComponent = PagerComponent(
        viewModelFactory = PagerDemoViewModelFactory(),
        content = PagerComponentDefaults.PagerComponentView
    ).also {
        it.deepLinkPathSegment = "_navigator_pager"
    }

    val panelComponent =
        PanelComponent(
            viewModelFactory = PanelDemoViewModelFactory(
                panelStatePresenter = PanelStatePresenterDefault(
                    dispatcher = Dispatchers.Main,
                    panelStyle = PanelStyle(),
                    panelHeaderState = PanelHeaderStateDefault(
                        title = "Component Toolkit",
                        description = "This is the default Panel Component",
                        imageUri = "no_image",
                        style = PanelStyle()
                    )
                )
            ),
            content = PanelComponentDefaults.PanelComponentView
        ).also {
            it.deepLinkPathSegment = "_navigator_panel"
        }

    val appComponent =
        StackComponent(
            viewModelFactory = AppViewModelFactory(
                stackStatePresenter = StackComponentDefaults.createStackStatePresenter(),
            ),
            content = StackComponentDefaults.DefaultStackComponentView
        )

    override fun onCheckChildForNextUriFragment(deepLinkPathSegment: String): Component? {
        println("${stackComponent.instanceId()}::getChildForNextUriFragment = $deepLinkPathSegment")
        return when (deepLinkPathSegment) {
            "_navigator_adaptive" -> adaptiveSizeComponent
            "_navigator_bottom_navigation" -> bottomNavigationComponent
            "_navigator_drawer" -> drawerComponent
            "_navigator_pager" -> pagerComponent
            "_navigator_panel" -> panelComponent
            else -> null
        }
    }

    override fun onStackTopUpdate(topComponent: Component) {

    }

    override fun onAttach() {
        stackComponent.navigator.push(mainScreenComponent)
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onDetach() {

    }
}
