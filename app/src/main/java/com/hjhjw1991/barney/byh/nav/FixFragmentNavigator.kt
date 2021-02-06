package com.hjhjw1991.barney.byh.nav

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.hjhjw1991.barney.byh.util.Logger

/**
 * 实现Fragment复用
 * @author huangjun.barney
 * @since 2021/2/6
 */

@Navigator.Name("fix_fragment")
class FixFragmentNavigator(
    val ctx: Context,
    val manager: FragmentManager,
    val containerId: Int
): FragmentNavigator(ctx, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Logger.log("Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = ctx.packageName + className
        }
        // we reuse fragment
        val frag = manager.findFragmentByTag(className)?: instantiateFragment(
            ctx, manager,
            className, args
        )
        frag.arguments = args
        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        // change ft.replace to ft.hide/show
        val fragments = manager.fragments
        fragments.forEach {
            ft.hide(it)
        }
        if (!frag.isAdded) {
            ft.add(containerId, frag, className)
        }
        ft.show(frag)
        ft.setPrimaryNavigationFragment(frag)

        val mBackStack = acquireParentBackStack()
        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.last() == destId)

        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.last()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    // copy from parent
    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

    private fun acquireParentBackStack(): ArrayDeque<Int> {
        return try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            field.get(this) as ArrayDeque<Int>
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayDeque()
        }
    }
}