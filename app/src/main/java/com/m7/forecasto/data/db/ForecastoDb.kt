package com.m7.forecasto.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.m7.forecasto.data.model.Forecast

@Database(entities = [Forecast::class], version = 1)
abstract class ForecastoDb : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ForecastoDb? = null

        fun getInstance(context: Context): ForecastoDb = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): ForecastoDb =
            Room.databaseBuilder(context, ForecastoDb::class.java, "ForecastoDb")
                .fallbackToDestructiveMigration()
                .build()
    }
}