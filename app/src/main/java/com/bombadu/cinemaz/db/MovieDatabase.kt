package com.bombadu.cinemaz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Movie::class],version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    private class MovieDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { movieDatabase->
                scope.launch {

                }
            }
        }
    }




    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MovieDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                   MovieDatabase::class.java,
                    "record_database"
                ).addCallback(MovieDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}