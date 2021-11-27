package com.formaloo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.formaloo.data.local.convertor.FormConverters
import com.formaloo.data.local.dao.FormDao
import com.formaloo.data.local.dao.FormKeysDao
import com.formaloo.data.local.dao.SubmitDao
import com.formaloo.data.model.form.Form
import com.formaloo.data.model.form.SubmitEntity


@Database(
    entities = [Form::class, FormsKeys::class,SubmitEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(FormConverters::class)
abstract class FormBuilderDB : RoomDatabase() {
    // DAO
    abstract fun formDao(): FormDao
    abstract fun formKeysDao(): FormKeysDao
    abstract fun submitDao(): SubmitDao

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