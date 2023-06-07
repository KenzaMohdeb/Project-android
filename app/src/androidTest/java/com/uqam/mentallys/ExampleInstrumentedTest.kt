package com.uqam.mentallys

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val isPackageProdNameCorrect = "com.uqam.mentallys" == appContext.packageName
        val isPackageDevNameCorrect = "com.uqam.mentallys.dev" == appContext.packageName
        val isPackageQANameCorrect = "com.uqam.mentallys.qa" == appContext.packageName
        assertTrue(isPackageProdNameCorrect || isPackageDevNameCorrect || isPackageQANameCorrect)
    }
}