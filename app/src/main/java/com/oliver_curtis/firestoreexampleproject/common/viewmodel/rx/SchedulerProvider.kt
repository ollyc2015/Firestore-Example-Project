package com.oliver_curtis.firestoreexampleproject.common.viewmodel.rx

import io.reactivex.Scheduler

/**
 * Mock implementations of this interface can be used for unit tests.
 */
interface SchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler
}