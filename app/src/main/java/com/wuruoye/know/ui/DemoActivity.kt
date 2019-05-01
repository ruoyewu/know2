package com.wuruoye.know.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.wuruoye.know.util.log

/**
 * Created at 2019-04-30 13:47 by wuruoye
 * Description:
 */
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val observer = Observer()
        observer.javaClass.canonicalName
        lifecycle.addObserver(observer)
    }

    class Observer : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onCreate(owner: LifecycleOwner, event: Lifecycle.Event) {
            log(event)
        }
    }
}