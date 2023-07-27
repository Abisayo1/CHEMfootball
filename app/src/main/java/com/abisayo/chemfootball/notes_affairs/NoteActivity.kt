package com.abisayo.chemfootball.notes_affairs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abisayo.chemfootball.databinding.ActivityNoteBinding



class NoteActivity : AppCompatActivity(), NoteClickDeleteInterface, NoteClickInterface {
    private lateinit var binding: ActivityNoteBinding
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notesRecycler.layoutManager = LinearLayoutManager(this)

        val noteRvAdapter = NoteRVAdapter(this, this, this)
        binding.notesRecycler.adapter = noteRvAdapter
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NoteViewModel::class.java)
        viewModel.allNote.observe(this, Observer { list ->
            list?.let {
                noteRvAdapter.updateList(it)
            }
        })

        binding.FABaddNote.setOnClickListener {
            val intent = Intent(this@NoteActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this, "${note.topic} Deleted", Toast.LENGTH_LONG)
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@NoteActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.topic)
        intent.putExtra("noteDescription", note.classs)
        intent.putExtra("noteID", note.id)
        intent.putExtra("question", note.question)
        intent.putExtra("optionA", note.optionA)
        intent.putExtra("optionB", note.optionB)
        intent.putExtra("optionC", note.optionC)
        intent.putExtra("optionD", note.optionD)
        intent.putExtra("answer", note.answer)
        intent.putExtra("count", note.count)
        intent.putExtra("timer", note.timer)
        startActivity(intent)
        this.finish()
    }
}