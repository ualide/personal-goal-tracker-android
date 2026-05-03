package com.example.app_authetification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
class ModifierObjectifSousTheme : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var docRef: DocumentReference
    private lateinit var toolbar1: Toolbar
    private lateinit var dateDebut: Date
    private lateinit var dateFin: Date
    private lateinit var btn_rappel: Date
    private lateinit var Difficulty: String
    private var titre10:String =""
    private var documentId:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier_objectif_sous_theme)
        toolbar1 = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar1)
        supportActionBar?.setTitle("Modifier Objectif")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        documentId = intent.getStringExtra("documentId")!!
        val T1 = findViewById<EditText>(R.id.textView6)
        val T2 = findViewById<EditText>(R.id.textView8)
        val T3 = findViewById<TextView>(R.id.textView10)
        val T4 = findViewById<TextView>(R.id.textView12)
        val T5 = findViewById<TextView>(R.id.textView14)
        val T6 = findViewById<TextView>(R.id.textView16)
        //T1.text=documentId
        docRef = db.collection("objectifSousTheme").document(documentId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val titre = document.getString("title")
                titre10=document.getString("title") ?: ""
                //titre10=titre
                val description = document.getString("description")
                val timestamp = document.getTimestamp("dateDebut")
                // Extraire le jour, le mois et l'année de la date
                val date = timestamp?.toDate()
                val day = date?.date
                val month = date?.month?.plus(1) // Ajouter 1 car le mois commence à 0
                val year = date?.year?.plus(1900)
                //T1.text=titre
                T1.setText(titre)
                T2.setText(description)
                //T2.text=description
                T3.setText("$day/$month/$year")
                val timestamp2 = document.getTimestamp("rappel")
                // Extraire le jour, le mois et l'année de la date
                val date1 = timestamp2?.toDate()
                val day1 = date1?.date
                val month1 = date1?.month?.plus(1) // Ajouter 1 car le mois commence à 0
                val year1 = date1?.year?.plus(1900)
                val minute1 = date1?.minutes
                val hour1 = date1?.hours
                val formattedMinute1 = if (minute1?.compareTo(10) ?: -1 < 0) "0$minute1" else "$minute1"
                //T4.setText("$day1/$month1/$year1  $hour1:$formattedMinute1")
                T4.setText("$hour1:$formattedMinute1")
                //T4.setText("$day1/$month1/$year1")
                val timestamp3 = document.getTimestamp("dateFin")
                // Extraire le jour, le mois et l'année de la date
                val date2 = timestamp3?.toDate()
                val day2 = date2?.date
                val month2 = date2?.month?.plus(1) // Ajouter 1 car le mois commence à 0
                val year2 = date2?.year?.plus(1900)
                T5.setText("$day2/$month2/$year2")
                dateDebut = timestamp?.toDate() ?: Date()
                dateFin = timestamp3?.toDate() ?: Date()
                btn_rappel=timestamp2?.toDate() ?: Date()

                val Difficulty = document.getString("Difficulty")
                T6.text=Difficulty

            } else{
                Toast.makeText(this, "document n'existe pas", Toast.LENGTH_SHORT).show()}
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Erreur de document ", Toast.LENGTH_SHORT).show()
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        T3.setOnClickListener {
            val datedialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // Store the selected date in the class-level property
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    dateDebut = cal.time
                    T3.setText("$dayOfMonth/${month + 1}/$year")
                },
                year,
                month,
                day
            )
            datedialog.show()
        }

        T4.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePicker =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // Store the selected time in the class-level property
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }
                    btn_rappel = cal.time

                    val formattedMinute = if (minute?.compareTo(10) ?: -1 < 0) "0$minute" else "$minute"
                    //T4.setText("$day1/$month1/$year1  $hour1:$formattedMinute1")
                    T4.setText("$hourOfDay:$formattedMinute")


                }, hour, minute, true)
            timePicker.show()
        }
        T5.setOnClickListener {
            val datedialog1 = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    // Store the selected date in the class-level property
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    dateFin = cal.time


                    T5.setText("$dayOfMonth/${month + 1}/$year")
                },
                year,
                month,
                day
            )
            datedialog1.show()
        }
        val b11 = findViewById<Button>(R.id.button11)
        b11.setOnClickListener {
            val titre = T1.text.toString()
            val description = T2.text.toString()
            titre10=T1.text.toString()
            val nouvelObjet = hashMapOf<String, Any>()

            if (titre.isNotEmpty()) nouvelObjet.put("title", titre)
            if (description.isNotEmpty()) nouvelObjet.put("description", description)
            nouvelObjet.put("dateDebut", dateDebut)
            nouvelObjet.put("dateFin", dateFin)
            nouvelObjet.put("rappel", btn_rappel)

            docRef.update(nouvelObjet.toMap()).addOnSuccessListener {
                // Affichage d'un message pour confirmer la modification
                Toast.makeText(this, "Objectif modifié avec succès", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,compte::class.java)
                //intent.putExtra("AC",1)
                //intent.putExtra("documentId",documentId)
                //intent.putExtra("NomOBJ",titre10)
                startActivity(intent)
                //onBackPressed()
            }.addOnFailureListener {
                // Affichage d'un message d'erreur en cas d'échec de la mise à jour
                Toast.makeText(this, "Erreur lors de la mise à jour de l'objet", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        /*val intent = Intent(this,themeActivity2::class.java)
        startActivity(intent)*/
        return true
    }
    /*override fun onSupportNavigateUp(): Boolean {
        //onBackPressed()
        val intent = Intent(this,ConsulterObjectifSousTheme::class.java)
        intent.putExtra("AC",1)
        intent.putExtra("documentId",documentId)
        intent.putExtra("NomOBJ",titre10)
        startActivity(intent)
        return true
    }*/
}