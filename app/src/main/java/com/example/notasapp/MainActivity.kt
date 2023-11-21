package com.example.notasapp

import android.app.DownloadManager.Query
import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var noteDao: NoteDao
    private lateinit var auth: FirebaseAuth
    private lateinit var adaptor: RVAdaptator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        noteDao = NoteDao() // Para manejar las notas
        auth = Firebase.auth // La autenticacion de firebase

        fab.setOnClickListener {
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }
        // Llamamos a la funcion del recycler view
        setUpRecyclerView()
    }
    // Funcion para el hacer funcionar el recyclerview
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteCollection = noteDao.noteCollection
        val currentUserid = auth.currentUser!!.uid
        // Para que solo el usuario pueda acceder a sus propias notas cuando el uid de google coincida con el guardado
        val query = noteCollection.whereEqualTo("uid", currentUserid)
            // Para ordenar las notas alfabeticamente
            .orderBy("text", com.google.firebase.firestore.Query.Direction.ASCENDING)

        // La cola para el recycler view
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note::class.java).build()

        adaptor = RVAdaptator(recyclerViewOption)
        recyclerView.adapter = adaptor

        // Para cuando toquemos la pantalla en este caso hacia la derecha
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                adaptor.deleteNote(position)
            }

        }).attachToRecyclerView(recyclerView)

    }
    // Al iniciar el adaptor empieza a esperar
    override fun onStart() {
        super.onStart()
        adaptor.startListening()
    }
    // Al acabar, que pare de esperar
    override fun onStop() {
        super.onStop()
        adaptor.stopListening()
    }

}