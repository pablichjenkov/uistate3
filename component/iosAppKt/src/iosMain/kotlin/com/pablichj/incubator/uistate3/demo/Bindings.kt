package com.pablichj.incubator.uistate3.demo

import com.pablichj.incubator.uistate3.IosBridge
import com.pablichj.incubator.uistate3.IosComponentRender
import com.pablichj.incubator.uistate3.demo.treebuilders.AdaptableSizeTreeBuilder
import com.pablichj.incubator.uistate3.demo.treebuilders.DrawerTreeBuilder
import com.pablichj.incubator.uistate3.demo.treebuilders.FullAppWithIntroTreeBuilder
import com.pablichj.incubator.uistate3.demo.treebuilders.PagerTreeBuilder
import com.pablichj.incubator.uistate3.node.Component
import com.pablichj.incubator.uistate3.node.drawer.DrawerComponent
import com.pablichj.incubator.uistate3.node.navbar.NavBarComponent
import com.pablichj.incubator.uistate3.node.pager.PagerComponent
import com.pablichj.incubator.uistate3.node.panel.PanelComponent
import com.pablichj.incubator.uistate3.platform.DefaultAppLifecycleDispatcher
import com.pablichj.incubator.uistate3.platform.DiContainer
import com.pablichj.incubator.uistate3.platform.DispatchersProxy
import com.pablichj.incubator.uistate3.platform.SafeAreaInsets
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
                config = DrawerComponent.DefaultConfig,
                diContainer = diContainer
            )
        )
        //it.setCompactContainer(PagerComponent())
        it.setMediumContainer(NavBarComponent())
        it.setExpandedContainer(PanelComponent())
    }
}

fun buildAppWithIntroComponent(): Component {
    return FullAppWithIntroTreeBuilder.build()
}

fun createPlatformBridge(): IosBridge {
    return IosBridge(
        appLifecycleDispatcher = DefaultAppLifecycleDispatcher(),
        safeAreaInsets = SafeAreaInsets()
    )
}