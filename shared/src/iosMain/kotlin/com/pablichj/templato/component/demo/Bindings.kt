package com.pablichj.templato.component.demo

import com.pablichj.templato.component.platform.IosBridge
import com.pablichj.templato.component.core.IosComponentRender
import com.pablichj.templato.component.demo.treebuilders.AdaptableSizeTreeBuilder
import com.pablichj.templato.component.demo.treebuilders.DrawerTreeBuilder
import com.pablichj.templato.component.demo.treebuilders.FullAppWithIntroTreeBuilder
import com.pablichj.templato.component.demo.treebuilders.PagerTreeBuilder
import com.pablichj.templato.component.core.Component
import com.pablichj.templato.component.core.drawer.DrawerComponent
import com.pablichj.templato.component.core.navbar.NavBarComponent
import com.pablichj.templato.component.core.panel.PanelComponent
import com.pablichj.templato.component.platform.DiContainer
import com.pablichj.templato.component.platform.DispatchersProxy
import platform.UIKit.UIViewController

fun ComponentRenderer(
    rootComponent: Component,
    iosBridge: IosBridge
): UIViewController = IosComponentRender(rootComponent, iosBridge)

fun buildDrawerComponent(): Component {
    return DrawerTreeBuilder.build()
}

fun buildPagerComponent(): Component {
    return PagerTreeBuilder.build()
}

fun buildAdaptableSizeComponent(): Component {
    val diContainer = DiContainer(DispatchersProxy.DefaultDispatchers)
    val subtreeNavItems = AdaptableSizeTreeBuilder.getOrCreateDetachedNavItems()
    return AdaptableSizeTreeBuilder.build().also {
        it.setNavItems(subtreeNavItems, 0)
        it.setCompactContainer(
            DrawerComponent(
                navigationDrawerState = DrawerComponent.createDefaultState(),
                config = DrawerComponent.DefaultConfig,
                diContainer = diContainer,
                content = DrawerComponent.DefaultDrawerComponentView
            )
        )
        //it.setCompactContainer(PagerComponent())
        it.setMediumContainer(
            NavBarComponent(
                navBarState = NavBarComponent.createDefaultState(),
                config = NavBarComponent.DefaultConfig,
                content = NavBarComponent.DefaultNavBarComponentView
            )
        )
        it.setExpandedContainer(
            PanelComponent(
                panelState = PanelComponent.createDefaultState(),
                config = PanelComponent.DefaultConfig,
                content = PanelComponent.DefaultPanelComponentView
            )
        )
    }
}

fun buildAppWithIntroComponent(): Component {
    return FullAppWithIntroTreeBuilder.build()
}
