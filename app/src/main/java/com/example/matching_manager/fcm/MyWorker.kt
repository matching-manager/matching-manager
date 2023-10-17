package com.example.matching_manager.fcm

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

// 백그라운드에서 실행되는 긴 작업을 스케줄링하고 관리
class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "Performing long running task in scheduled job")
        // 작업 추가 예정
        return Result.success()
    }

    companion object {
        private val TAG = "MyWorker"
    }
}
