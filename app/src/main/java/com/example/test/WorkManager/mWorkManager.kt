package com.example.test.WorkManager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.test.utils.mUtils.mToast
import java.util.concurrent.TimeUnit

class mWorkManager {
    companion object {
        @JvmStatic
        fun startContactSync(context: Context, time: Long) {
            mToast(context, "Contact Sync Started")
            val constraints = Constraints.Builder()
                .setRequiresCharging(false)
                .build()
            val work = PeriodicWorkRequestBuilder<SendContactToFirebase>(time, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(work)
            workManager.enqueueUniquePeriodicWork(
                "Send Data",
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
        }
    }
}