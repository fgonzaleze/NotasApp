package com.example.notasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddNoteActivity : AppCompatActivity() {
    // Variables que necesitaremos
    private lateinit var noteEditText: EditText
    private lateinit var addNoteButton: Button
    private lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // llamamos al layout donde añadiremos una nota
        setContentView(R.layout.activity_add_note)
        // En el layout los id que hemos creado para el text y el añadir
        noteEditText = findViewById(R.id.noteEditText)
        addNoteButton = findViewById(R.id.addNoteButton)
        noteDao = NoteDao()

        // para cuando añadamos un boton
        addNoteButton.setOnClickListener {
            val note = noteEditText.text.toString()
            if(note.isNotEmpty()) {
                noteDao.addNote(note)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}