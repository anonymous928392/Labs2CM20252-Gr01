package co.edu.udea.compumovil.gr01_20252_lab2.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import co.edu.udea.compumovil.gr01_20252_lab2.data.local.AppDatabase
import co.edu.udea.compumovil.gr01_20252_lab2.data.remote.RetrofitInstance
import co.edu.udea.compumovil.gr01_20252_lab2.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncArticlesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "SyncArticlesWork"
        private const val TAG = "SyncArticlesWorker"

        fun setupPeriodicSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SyncArticlesWorker>(
                repeatInterval = 15,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )

            Log.d(TAG, "Periodic sync work scheduled")
        }

        fun syncNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest = OneTimeWorkRequestBuilder<SyncArticlesWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting sync...")

            val database = AppDatabase.getDatabase(applicationContext)
            val repository = NewsRepository(
                RetrofitInstance.api,
                database.articleDao()
            )

            val result = repository.syncArticles()

            if (result.isSuccess) {
                Log.d(TAG, "Sync completed successfully")
                Result.success()
            } else {
                Log.e(TAG, "Sync failed: ${result.exceptionOrNull()?.message}")
                Result.retry()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Sync error: ${e.message}", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}