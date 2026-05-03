package com.example.app_authetification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import java.util.Calendar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.*
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ObjectifSousTheme: AppCompatActivity() {

    private var firestoreDB: FirebaseFirestore = Firebase.firestore
    private var db = FirebaseFirestore.getInstance()
    private var nextObjId: Int = 0

    private lateinit var firebaseAuth: FirebaseAuth
    private var ObjCollectionRef: CollectionReference = db.collection("objectifSousTheme")



    // Properties for storing selected dates
    private lateinit var dateDebut: Date
    private lateinit var dateFin: Date
    private lateinit var btn_rappel: Date
    private lateinit var Difficulty: String
    private lateinit var Difficulty1: String
    private lateinit var Difficulty2: String
    private lateinit var Difficulty3: String
    private lateinit var Difficulty4: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objectif_sous_theme)

        val toolbar: Toolbar = findViewById(R.id.mytoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Ajouter")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        val SousthemeId = intent.getIntExtra("SousthemeIdGen", 0)

        println("SousTheme Id555: $SousthemeId")



// dateDebut
        val cal1 = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH,4 )
            set(Calendar.DAY_OF_MONTH, 31)
        }

        dateDebut = cal1.time

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val myButton6 = findViewById<Button>(R.id.btn_dateDebut)
        myButton6.setOnClickListener {
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
                },
                year,
                month,
                day
            )
            datedialog.show()
        }

// dateFin
        val cal2 = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH,5 )
            set(Calendar.DAY_OF_MONTH, 7)
        }

        dateFin = cal2.time

        val myButton7 = findViewById<Button>(R.id.btn_dateFin)
        myButton7.setOnClickListener {
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
                },
                year,
                month,
                day
            )
            datedialog1.show()
        }

// Rappel
        val cal3 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
        }
        btn_rappel = cal3.time

        val myButton8 = findViewById<Button>(R.id.btn_rappel)
        myButton8.setOnClickListener {
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
                }, hour, minute, true)
            timePicker.show()
        }


        Difficulty = ""

        val button1 = findViewById<Button>(R.id.Difficulty1)
        val button2 = findViewById<Button>(R.id.Difficulty2)
        val button3 = findViewById<Button>(R.id.Difficulty3)
        val button4 = findViewById<Button>(R.id.Difficulty4)

        button1.setOnClickListener {
            // Button 1 clicked
            // Do something
            Difficulty1 = "BANAL"
            Difficulty = Difficulty1
        }

        button2.setOnClickListener {
            // Button 2 clicked
            // Do something
            Difficulty2 = "FACILE"
            Difficulty = Difficulty2
        }

        button3.setOnClickListener {
            // Button 3 clicked
            // Do something
            Difficulty3 = "MOYEN"
            Difficulty = Difficulty3
        }

        button4.setOnClickListener {
            // Button 4 clicked
            // Do something
            Difficulty4 = "DIFFICILE"
            Difficulty = Difficulty4
        }


        val btnSubmit = findViewById<Button>(R.id.btn_confirm)
        btnSubmit.setOnClickListener {

            println("SousTheme Id126: $SousthemeId")
//saveObjectifToFirestore(THemeId, PersId)

            //val intent = Intent(this, ObjectifsListe::class.java)

            intent.putExtra("SousthemeIdGen",SousthemeId)
            intent.putExtra("Difficulty",Difficulty)

            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid


// Récupérer les données du formulaire
            val title = findViewById<EditText>(R.id.titreObjectif).text.toString()
            val description = findViewById<EditText>(R.id.desc).text.toString()
            val dateDebut = dateDebut
            val dateFin = dateFin
            val rappel = btn_rappel


// Enregistrer les données dans Firebase Firestore
            firebaseAuth = FirebaseAuth.getInstance()
            // Récupération du prochain id d'objectif à partir de Firebase Firestore
            ObjCollectionRef
                .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        nextObjId = (querySnapshot.documents[0].get("id") as Long).toInt() + 1
                    }else{
                        nextObjId = 1
                    }
                    //val P = 1.0 / 2.0
                    val P=0.0
                    val objectif = hashMapOf(
                        "id" to nextObjId,
                        "title" to title,
                        "description" to description,
                        "dateDebut" to dateDebut,
                        "dateFin" to dateFin,
                        "rappel" to btn_rappel,
                        "Difficulty" to Difficulty,
                        "idSTh" to SousthemeId,
                        "Qualification" to P
                    )

                    val db = FirebaseFirestore.getInstance()
                    val ObjCollectionReference = db.collection("objectifSousTheme")
                    ObjCollectionReference.add(objectif)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Objectif ajouté avec ID: $nextObjId")
                            /*val intent = Intent(this, themeActivity2::class.java)
                            startActivity(intent)*/
                            onBackPressed()
                            println("id: $nextObjId ,title: $title,description: $description,dateDebut: $dateDebut,dateFin: $dateFin,rappel: $btn_rappel,Difficulty: $Difficulty,idSTh: $SousthemeId")

                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Erreur lors de l'ajout de l'objectif", e)

                        }

                    //startActivity(intent)
                }


                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Erreur lors de la récupération du prochain id d'objectif : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        //recreate()
        // Mettre à jour la page ici avec toutes les modifications éventuelles
    }

}