package com.moustafa.mymediahub.utils

/**
 * @author moustafasamhoury
 * created on Wednesday, 9 Oct, 2019
 */


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

/**
 * @Link{https://github.com/googlecodelabs/android-testing/blob/043c38c0927c7434275aeeeafce7be1316bb73c2/app/src/sharedTest/java/com/example/android/architecture/blueprints/todoapp/MainCoroutineRule.kt}
 */
@ExperimentalCoroutinesApi
class CoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
