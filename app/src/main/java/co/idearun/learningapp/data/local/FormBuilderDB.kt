package co.idearun.learningapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.idearun.learningapp.data.local.FormsKeys
import co.idearun.learningapp.data.local.convertor.FormConverters
import co.idearun.learningapp.data.local.dao.FormDao
import co.idearun.learningapp.data.local.dao.FormKeysDao
import co.idearun.learningapp.data.model.form.Form


@Database(
    entities = [Form::class, FormsKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(FormConverters::class)
abstract class FormBuilderDB : RoomDatabase() {
    // DAO
    abstract fun formDao(): FormDao
    abstract fun formKeysDao(): FormKeysDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FormBuilderDB::class.java,
                "FormBuilder.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}