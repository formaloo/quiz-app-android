package co.idearun.learningapp.feature

import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import co.idearun.learningapp.worker.SubmitWorker

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        callWorker()

    }

    fun callWorker() {
        val constraint: Constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val submitWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<SubmitWorker>()
            .setConstraints(constraint).build()

        val manager = WorkManager.getInstance(this)
        manager.enqueueUniqueWork("Submit", ExistingWorkPolicy.KEEP, submitWorkRequest);

    }
}