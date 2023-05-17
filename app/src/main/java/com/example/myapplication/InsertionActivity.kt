package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var ctFecha: EditText
    private lateinit var ctNombrePaciente: EditText
    private lateinit var ctEdadPaciente: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        ctFecha = findViewById(R.id.ctFecha)
        ctNombrePaciente = findViewById(R.id.ctNombrePaciente)
        ctEdadPaciente = findViewById(R.id.ctEdadPaciente)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Cita")

        btnSaveData.setOnClickListener {
            saveCitaData()
        }
    }

    private fun saveCitaData() {

        //getting values
        val ctFecha = ctFecha.text.toString()
        val ctNombrePaciente = ctNombrePaciente.text.toString()
        val ctEdadPaciente = ctEdadPaciente.text.toString()

        if (ctFecha.isEmpty()) {
            ctFecha.error = "Por favor ingresa la Fecha"
        }
        if (ctNombrePaciente.isEmpty()) {
            ctNombrePaciente.error = "Por favor ingresa el Nombre del paciente"
        }
        if (ctEdadPaciente.isEmpty()) {
            ctEdadPaciente.error = "Por favor ingresa la Edad del paciente"
        }

        val ctId = dbRef.push().key!!

        val cita = CitaModel(ctId, ctFecha, ctNombrePaciente, ctEdadPaciente)

        dbRef.child(ctId).setValue(cita)
            .addOnCompleteListener {
                Toast.makeText(this, "Data insertada exitosa", Toast.LENGTH_LONG).show()

                ctFecha.text.clerar()
                ctNombrePaciente.text.clear()
                ctEdadPaciente.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}