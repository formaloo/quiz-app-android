package co.idearun.learningapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.idearun.learningapp.data.repository.FormzRepo
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import timber.log.Timber

class SubmitWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams), KoinComponent {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            Timber.e("doWork")
            val repo: FormzRepo = get(named("FormzRepo"))
            repo.sendSavedSubmitToServer()

            Result.success()

        } catch (ex: Exception) {
            Timber.e(ex, "Error ")
            Result.failure()
        }
    }


    companion object {
        private const val TAG = "SubmitWorker"
    }
}
