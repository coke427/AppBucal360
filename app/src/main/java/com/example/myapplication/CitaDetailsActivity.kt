package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class CitaDetailsActivity : AppCompatActivity() {

    private lateinit var tvCtId: TextView
    private lateinit var tvCtFecha: TextView
    private lateinit var tvCtNombrePaciente: TextView
    private lateinit var tvCtEdadPaciente: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("ctId").toString(),
                intent.getStringExtra("ctNombrePaciente").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("ctId").toString()
            )
        }

    }

    private fun initView() {
        tvCtId = findViewById(R.id.tvCtId)
        tvCtFecha = findViewById(R.id.tvCtFecha)
        tvCtNombrePaciente = findViewById(R.id.tvCtNombrePaciente)
        tvCtEdadPaciente = findViewById(R.id.tvCtEdadPaciente)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvCtId.text = intent.getStringExtra("ctId")
        tvCtFecha.text = intent.getStringExtra("ctFecha")
        tvCtNombrePaciente.text = intent.getStringExtra("ctNombrePaciente")
        tvCtEdadPaciente.text = intent.getStringExtra("ctEdadPaciente")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Citas").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Cita eliminada", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        ctId: String,
        ctNombrePaciente: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etCtFecha = mDialogView.findViewById<EditText>(R.id.etCtFecha)
        val etCtNombrePaciente = mDialogView.findViewById<EditText>(R.id.etCtNombrePaciente)
        val etCtEdadPaciente = mDialogView.findViewById<EditText>(R.id.etCtEdadPaciente)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etCtFecha.setText(intent.getStringExtra("ctFecha").toString())
        etCtNombrePaciente.setText(intent.getStringExtra("ctNombrePaciente").toString())
        etCtEdadPaciente.setText(intent.getStringExtra("ctEdadPaciente").toString())

        mDialog.setTitle("Updating $ctNombrePaciente Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                ctId,
                etCtFecha.text.toString(),
                etCtNombrePaciente.text.toString(),
                etCtEdadPaciente.text.toString()
            )

            Toast.makeText(applicationContext, "Cita Actualizada", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvCtFecha.text = etCtFecha.text.toString()
            tvCtNombrePaciente.text = etCtNombrePaciente.text.toString()
            tvCtEdadPaciente.text = etCtEdadPaciente.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        fecha: String,
        nombrePaciente: String,
        edadPaciente: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Citas").child(id)
        val empInfo = CitaModel(id, fecha, nombrePaciente, edadPaciente)
        dbRef.setValue(empInfo)
    }

}