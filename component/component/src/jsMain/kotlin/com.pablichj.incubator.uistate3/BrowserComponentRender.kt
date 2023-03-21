package com.pablichj.incubator.uistate3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.pablichj.incubator.uistate3.node.Component
import com.pablichj.incubator.uistate3.node.TreeContext
import com.pablichj.incubator.uistate3.node.backpress.DefaultBackPressDispatcher
import com.pablichj.incubator.uistate3.node.backpress.ForwardBackPressCallback
import com.pablichj.incubator.uistate3.node.backpress.LocalBackPressedDispatcher
import com.pablichj.incubator.uistate3.node.dispatchAttachedToComponentTree

@Composable
fun BrowserComponentRender(
    rootComponent: Component,
    onBackPressEvent: () -> Unit
) {
    val webBackPressDispatcher = remember {
        DefaultBackPressDispatcher()
    }

    val treeContext = remember(rootComponent) {
        TreeContext()
    }

    CompositionLocalProvider(
        LocalBackPressedDispatcher provides webBackPressDispatcher,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            rootComponent.Content(Modifier.fillMaxSize())
            /* Should listen for keyboard back instead
            FloatingBackButton(
                modifier = Modifier.offset(y = 48.dp),
                alignment = Alignment.TopStart,
                onClick = { webBackPressDispatcher.dispatchBackPressed() }
            )*/
        }
    }

    LaunchedEffect(key1 = rootComponent, key2 = onBackPressEvent) {
        rootComponent.rootBackPressedCallbackDelegate = ForwardBackPressCallback {
            onBackPressEvent()
        }
        // Traverse the whole tree passing the TreeContext living in the root node. Useful to
        // propagate the the Navigator for example. Where each Component interested in participating
        // in deep linking will subscribe its instance an a DeepLinkMatcher lambda function.
        println("BrowserComponentRender::dispatchAttachedToComponentTree")
        rootComponent.dispatchAttachedToComponentTree(treeContext)
        rootComponent.start()
    }
}