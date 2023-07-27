package com.abisayo.chemfootball.notes_affairs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Note(@ColumnInfo(name = "question")val question:String,
           @ColumnInfo(name="topic")val topic: String,
           @ColumnInfo(name = "class")val classs:String,
           @ColumnInfo(name = "optionA")val optionA:String,
           @ColumnInfo(name="optionB")val optionB: String,
           @ColumnInfo(name="optionC")val optionC: String,
           @ColumnInfo(name="optionD")val optionD: String,
           @ColumnInfo(name="answer")val answer: String,
           @ColumnInfo(name="count")val count: String,
           @ColumnInfo(name="timer")val timer: String
            ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}