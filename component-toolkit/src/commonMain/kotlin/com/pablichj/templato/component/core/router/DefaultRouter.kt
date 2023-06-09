package com.pablichj.templato.component.core.router

import com.pablichj.templato.component.core.Component

class DefaultRouter : Router {

    private val destinationRegistry = mutableListOf<DeepLinkDestination>()

    override fun registerRoute(destination: DeepLinkDestination) {
        destinationRegistry.add(destination)
    }

    override fun unregisterRoute(destination: DeepLinkDestination) {
        destinationRegistry.remove(destination)
    }

    override fun handleDeepLink(destination: String): DeepLinkResult {
        destinationRegistry.forEach {
            println("${it.component.clazz}")
        }
        val componentDestination = destinationRegistry.firstOrNull {
            it.deepLinkMatcher(destination)
        } ?: return DeepLinkResult.Error(
            """
                Destination registry has no Component with destination = $destination
            """
        )

        val path = buildComponentPathFromRoot(componentDestination.component)

        val rootComponent = path.removeFirstOrNull() ?: return DeepLinkResult.Error(
            """
                Component with destination = $destination, is not attached to any parent Component
            """
        )

        println("path.size = ${path.size}")
        path.forEach { println("Component -> ${it.clazz}") }

        // The component with parentComponent == null is the root component
        return rootComponent.navigateToDeepLink(path)
    }

    private fun buildComponentPathFromRoot(component: Component): ArrayDeque<Component> {
        val path = ArrayDeque<Component>()
        var componentIterator: Component? = component
        while (componentIterator != null) {
            /*if (parentIterator is IRootComponent) {
                return path
            }*/
            path.add(0, componentIterator)
            componentIterator = componentIterator.parentComponent
        }
        return path
    }

}