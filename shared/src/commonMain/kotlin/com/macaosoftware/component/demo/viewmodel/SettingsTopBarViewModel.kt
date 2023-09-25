package com.macaosoftware.component.demo.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import com.macaosoftware.component.core.Component
import com.macaosoftware.component.demo.SimpleComponent
import com.macaosoftware.component.demo.SimpleResponseComponent
import com.macaosoftware.component.topbar.TopBarComponent
import com.macaosoftware.component.topbar.TopBarComponentViewModel
import com.macaosoftware.component.topbar.TopBarItem
import com.macaosoftware.component.topbar.TopBarStatePresenterDefault

class SettingsTopBarViewModel(
    topBarComponent: TopBarComponent<TopBarStatePresenterDefault>,
    override val topBarStatePresenter: TopBarStatePresenterDefault,
    screenName: String,
    onDone: () -> Unit
) : TopBarComponentViewModel<TopBarStatePresenterDefault>(topBarComponent) {

    private var currentComponent: Component? = null

    val Step1 = SimpleComponent(
        "$screenName/Page 1",
        Color.Yellow
    ) { msg ->
        when (msg) {
            SimpleComponent.Msg.Next -> {
                topBarComponent.backStack.push(Step2)
            }
        }
    }.also {
        it.setParent(topBarComponent)
        it.uriFragment = "Page 1"
    }

    val Step2 = SimpleComponent(
        "$screenName/Page 2",
        Color.Green
    ) { msg ->
        when (msg) {
            SimpleComponent.Msg.Next -> {
                topBarComponent.backStack.push(Step3)
            }
        }
    }.also {
        it.setParent(topBarComponent)
        it.uriFragment = "Page 2"
    }

    val Step3 =
        SimpleResponseComponent(
            "$screenName/Page 3",
            Color.Cyan
        ).also {
            it.setParent(topBarComponent)
            it.uriFragment = "Page 3"
        }

    override fun onCreate() {
    }

    override fun onStart() {
        if (currentComponent == null) {
            currentComponent = Step1
            topBarComponent.backStack.push(Step1)
        }
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }

    override fun mapComponentToStackBarItem(topComponent: Component): TopBarItem {
        return when (topComponent) {
            Step1 -> {
                TopBarItem(
                    Step1.screenName,
                    Icons.Filled.Star,
                )
            }

            Step2 -> {
                TopBarItem(
                    Step2.screenName,
                    Icons.Filled.Star,
                )
            }

            Step3 -> {
                TopBarItem(
                    Step3.screenName,
                    Icons.Filled.Star,
                )
            }

            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun onCheckChildForNextUriFragment(
        nextUriFragment: String
    ): Component? {
        println("${topBarComponent.instanceId()}::getChildForNextUriFragment = $nextUriFragment")
        return when (nextUriFragment) {
            Step1.uriFragment -> Step1
            Step2.uriFragment -> Step2
            Step3.uriFragment -> Step3
            else -> null
        }
    }

    override fun onBackstackEmpty() {
        currentComponent = null
    }

}
