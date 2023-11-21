package com.example.notasapp

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
// Para la base de datos de firebase
class NoteDao {
    private val db = FirebaseFirestore.getInstance()
    val noteCollection = db.collection("Notes")
    // Para la authenticacion
    private val auth = Firebase.auth

    // Funcion para añadir nota como string
    fun addNote(text: String) {
        val currentUserId = auth.currentUser!!.uid
        val note = Note(text, currentUserId)
        noteCollection.document().set(note)
    }

}