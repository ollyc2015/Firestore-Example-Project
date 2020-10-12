package com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx

import com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Default implementations with real schedulers to be used in the production builds.
 */
class DefaultSchedulerProvider : SchedulerProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}