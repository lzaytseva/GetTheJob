package ru.practicum.android.diploma.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        //TODO add Entity classes
    ]
)
abstract class GtjDatabase : RoomDatabase() {
}
