package com.pablichj.incubator.uistate3.node.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.pablichj.incubator.uistate3.node.*
import com.pablichj.incubator.uistate3.node.navigation.DeepLinkResult
import com.pablichj.incubator.uistate3.node.stack.BackStack
import com.pablichj.incubator.uistate3.platform.DiContainer
import com.pablichj.incubator.uistate3.platform.DispatchersProxy.Companion.DefaultDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class DrawerComponent(
    private val config: Config,
    private var diContainer: DiContainer
) : Component(), NavComponent, IDrawerNode {
    final override val backStack = BackStack<Component>()
    override var navItems: MutableList<NavItem> = mutableListOf()
    override var selectedIndex: Int = 0
    override var childComponents: MutableList<Component> = mutableListOf()
    override var activeComponent: MutableState<Component?> = mutableStateOf(null)
    private val coroutineScope = CoroutineScope(diContainer.dispatchers.main)
    private val navDrawerState = NavigationDrawerState(
        coroutineScope,
        DrawerHeaderState(
            title = "A Drawer Header Title",
            description = "Some description or leave it blank",
            imageUri = "",
            style = config.drawerHeaderStyle
        ),
        emptyList()
    )

    init {
        coroutineScope.launch {
            navDrawerState.navItemClickFlow.collect { navItemClick ->
                backStack.push(navItemClick.component)
            }
        }
        backStack.eventListener = { event ->
            val stackTransition = processBackstackEvent(event)
            processBackstackTransition(stackTransition)
        }
    }

    // region: ComponentLifecycle

    override fun start() {
        super.start()
        if (activeComponent.value == null) {
            println("$clazz::start(). Pushing selectedIndex = $selectedIndex, children.size = ${childComponents.size}")
            if (childComponents.isNotEmpty()) {
                backStack.push(childComponents[selectedIndex])
            } else {
                println("$clazz::start() with childComponents empty")
            }
        } else {
            println("$clazz::start() with activeNodeState = ${activeComponent.value?.clazz}")
            activeComponent.value?.start()
        }
    }

    override fun stop() {
        super.stop()
        println("$clazz::stop()")
        activeComponent.value?.stop()
    }

    override fun handleBackPressed() {
        println("$clazz::handleBackPressed, backStack.size = ${backStack.size()}")
        if (backStack.size() > 1) {
            backStack.pop()
        } else {
            // We delegate the back event when the stack has 1 element and not 0. The reason is, if
            // we pop all the way to zero the stack empty view will be show for a fraction of
            // milliseconds and this creates an undesirable effect.
            delegateBackPressedToParent()
        }
    }

    // endregion

    // region: IDrawerComponent

    override fun open() {
        println("$clazz::open")
        navDrawerState.setDrawerState(DrawerValue.Open)
    }

    override fun close() {
        println("$clazz::close")
        navDrawerState.setDrawerState(DrawerValue.Closed)
    }

    // endregion

    // region: NavigatorItems

    override fun getComponent(): Component {
        return this
    }

    override fun onSelectNavItem(selectedIndex: Int, navItems: MutableList<NavItem>) {
        val navItemsDeco = navItems.map { it.toNavItemDeco() }
        navDrawerState.navItemsDeco = navItemsDeco
        navDrawerState.selectNavItemDeco(navItemsDeco[selectedIndex])
        if (getComponent().lifecycleState == ComponentLifecycleState.Started) {
            backStack.push(childComponents[selectedIndex])
        }
    }

    override fun updateSelectedNavItem(newTop: Component) {
        getNavItemFromComponent(newTop).let {
            println("$clazz::updateSelectedNavItem(), selectedIndex = $it")
            navDrawerState.selectNavItemDeco(it.toNavItemDeco())
            selectedIndex = childComponents.indexOf(newTop)
        }
    }

    override fun onDestroyChildComponent(component: Component) {
        if (component.lifecycleState == ComponentLifecycleState.Started) {
            component.stop()
            component.destroy()
        } else {
            component.destroy()
        }
    }

    // endregion

    // region: DeepLink

    override fun getDeepLinkSubscribedList(): List<Component> {
        return childComponents
    }

    override fun onDeepLinkNavigation(matchingComponent: Component): DeepLinkResult {
        println("$clazz.onDeepLinkMatch() matchingNode = ${matchingComponent.clazz}")
        backStack.push(matchingComponent)
        return DeepLinkResult.Success
    }

    // endregion

    @Composable
    override fun Content(modifier: Modifier) {
        println(
            """$clazz.Composing() stack.size = ${backStack.size()}
                |lifecycleState = ${lifecycleState}
            """
        )
        NavigationDrawer(
            modifier = modifier,
            navDrawerState = navDrawerState
        ) {
            Box {
                val activeComponentCopy = activeComponent.value
                if (activeComponentCopy != null && backStack.size() > 0) {
                    activeComponentCopy.Content(Modifier)
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "$clazz Empty Stack, Please add some children",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    class Config(
        var drawerHeaderStyle: DrawerHeaderStyle
    )

    companion object {
        val DefaultConfig = Config(
            DrawerHeaderStyle()
        )
    }

}
