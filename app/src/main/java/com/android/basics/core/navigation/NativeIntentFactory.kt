package com.android.basics.core.navigation

import android.content.Context
import android.content.Intent


class NativeIntentFactory : IntentFactory {
    override fun create(context: Context?, clazz: Class<out Context?>?): Intent {
        return Intent(context, clazz)
    }

    override fun create(action: String?): Intent? {
        return Intent(action)
    }

    override fun create(): Intent? {
        return Intent()
    }
}