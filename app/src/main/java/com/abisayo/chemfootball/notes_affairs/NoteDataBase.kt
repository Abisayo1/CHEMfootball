package com.abisayo.chemfootball.notes_affairs


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class), version = 2, exportSchema = false)
abstract class NoteDataBase :RoomDatabase() {
    abstract fun getNotesDao(): NoteDao


    companion object{

        @Volatile
        private var INSTANCE: NoteDataBase? = null

        fun getDatabase(context: Context): NoteDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDataBase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}