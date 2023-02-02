package example.nodes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pablichj.incubator.uistate3.node.*
import com.pablichj.incubator.uistate3.node.backstack.BackStack
import com.pablichj.incubator.uistate3.node.navigation.SubPath
import com.pablichj.incubator.uistate3.node.topbar.TitleSectionStateHolder
import com.pablichj.incubator.uistate3.node.topbar.TopBar
import com.pablichj.incubator.uistate3.node.topbar.TopBarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TopBarComponent(
    val screenName: String,
    val screenIcon: ImageVector? = null,
    val onMessage: (Msg) -> Unit
) : Component(), Container {
    override val backStack = BackStack<Component>()
    override var navItems: MutableList<NodeItem> = mutableListOf()
    override var selectedIndex: Int = 0
    override var childComponents: MutableList<Component> = mutableListOf()
    override var activeComponent: MutableState<Component?> = mutableStateOf(null)
    private val topBarState = TopBarState()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)// TODO: Use DispatchersBin

    val Step1 = SimpleComponent(
        "$screenName / Page 1",
        Color.Yellow
    ) { msg ->
        when (msg) {
            SimpleComponent.Msg.Next -> {
                backStack.push(Step2)
            }
        }
    }.also {
        it.attachToParent(this@TopBarComponent)
        it.subPath = SubPath("Page1")
    }

    val Step2 = SimpleComponent(
        "$screenName / Page 1 / Page 2",
        Color.Green
    ) { msg ->
        when (msg) {
            SimpleComponent.Msg.Next -> {
                backStack.push(Step3)
            }
        }
    }.also {
        it.attachToParent(this@TopBarComponent)
        it.subPath = SubPath("Page2")
    }

    val Step3 =
        SimpleComponent(
            "$screenName / Page 1 / Page 2 / Page 3",
            Color.Cyan
        ) { msg ->
            when (msg) {
                SimpleComponent.Msg.Next -> {
                    onMessage(Msg.OnboardDone)
                }
            }
        }.also {
            it.attachToParent(this@TopBarComponent)
            it.subPath = SubPath("Page3")
        }

    override fun start() {
        super.start()
        println("$clazz::start()")
        backStack.eventListener = { event ->
            processBackstackEvent(event)
        }
        if (activeComponent.value == null) {
            backStack.push(Step1)
        } else {
            activeComponent.value?.start()
        }
    }

    override fun stop() {
        println("$clazz::stop()")
        super.stop()
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

    // region: NavigatorItems

    override fun getComponent(): Component {
        return this
    }

    override fun onSelectNavItem(selectedIndex: Int, navItems: MutableList<NodeItem>) {
        //navBarState.navItems = navItems
        //navBarState.selectNavItem(navItems[selectedIndex])
        if (getComponent().lifecycleState == LifecycleState.Started) {
            backStack.push(childComponents[selectedIndex])
        }
    }

    override fun updateSelectedNavItem(newTop: Component) {

    }

    override fun onDestroyChildComponent(component: Component) {
        if (component.lifecycleState == LifecycleState.Started) {
            component.stop()
            component.destroy()
        } else {
            component.destroy()
        }
    }

    // endregion


    /*override fun onStackPush(oldTop: SimpleComponent?, newTop: SimpleComponent) {
        activeNodeState.value = newTop
        newTop.start()
        oldTop?.stop()

        if (backStack.size() > 1) {
            setTitleSectionForBackClick(newTop)
        } else {
            setTitleSectionForHomeClick(newTop)
        }
    }

    override fun onStackPop(oldTop: SimpleComponent, newTop: SimpleComponent?) {
        activeNodeState.value = newTop
        newTop?.start()
        oldTop.stop()

        if (newTop != null) {
            if (stack.size > 1) {
                setTitleSectionForBackClick(newTop)
            } else {
                setTitleSectionForHomeClick(newTop)
            }
        }

    }*/

    private fun setTitleSectionForHomeClick(node: SimpleComponent) {
        topBarState.setTitleSectionState(
            TitleSectionStateHolder(
                title = node.text,
                icon1 = resolveFirstIcon(),
                onIcon1Click = {
                    findClosestIDrawerNode()?.open()
                },
                onTitleClick = {
                    findClosestIDrawerNode()?.open()
                }
            )
        )
    }

    private fun setTitleSectionForBackClick(node: SimpleComponent) {
        topBarState.setTitleSectionState(
            TitleSectionStateHolder(
                title = node.text,
                onTitleClick = {
                    handleBackPressed()
                },
                icon1 = resolveFirstIcon(),
                onIcon1Click = {
                    findClosestIDrawerNode()?.open()
                },
                icon2 = Icons.Filled.ArrowBack,
                onIcon2Click = {
                    handleBackPressed()
                }
            )
        )
    }

    private fun resolveFirstIcon(): ImageVector? {
        val canProvideGlobalNavigation = findClosestIDrawerNode() != null
        return if (canProvideGlobalNavigation) {
            Icons.Filled.Menu
        } else {
            screenIcon
        }
    }

    // region: DeepLink

    override fun getDeepLinkNodes(): List<Component> {
        return listOf(Step1, Step2, Step3)
    }

    override fun onDeepLinkMatchingNode(matchingComponent: Component) {
        println("TopBarNode.onDeepLinkMatchingNode() matchingNode = ${matchingComponent.subPath}")
        backStack.push(matchingComponent as SimpleComponent) //todo: see how get rid of the cast
    }

    // endregion

    sealed interface Msg {
        object OnboardDone : Msg
    }

    @Composable
    override fun Content(modifier: Modifier) {
        println("TopBarNode::Composing(), stack.size = ${backStack.size()}")

        Scaffold (
            modifier = modifier,
            topBar = { TopBar(topBarState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Green)
                    .padding(paddingValues)
            ) {
                val activeNodeUpdate = activeComponent.value
                if (activeNodeUpdate != null /*&& backStack.size() > 0*/) {
                    activeNodeUpdate.Content(Modifier)
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "Empty Stack, Please add some children",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}