package com.joye.jiang.common.sdk

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@Keep
object FragmentUtils {

    fun add(fragmentManager: FragmentManager, contentId: Int, fragment: Fragment) {
        var transaction = fragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            transaction.add(contentId, fragment)
        }
        transaction.commitNow()
    }

    fun show(fragmentManager: FragmentManager, fragment: Fragment) {
        var transaction = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        }
        transaction.commitNow()
    }

    fun hide(fragmentManager: FragmentManager, fragment: Fragment) {
        var transaction = fragmentManager.beginTransaction()
        if (fragment.isVisible) {
            transaction.hide(fragment)
        }
        transaction.commitNow()
    }

    fun replace(fragmentManager: FragmentManager, contentId: Int, fragment: Fragment) {
        var transaction = fragmentManager.beginTransaction()
        transaction.replace(contentId, fragment)
        transaction.commitNow()
    }
}