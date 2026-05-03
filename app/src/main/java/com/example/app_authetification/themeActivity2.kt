package com.example.app_authetification
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.graphics.drawable.ColorDrawable
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.Gravity
import android.widget.LinearLayout
import android.graphics.Color
import android.util.Log
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import java.util.*


//TOOLBAR
private lateinit var mytoolbar: Toolbar


class themeActivity2 : AppCompatActivity() {
    var Cli: Int = 1
    private var firestoreDB: FirebaseFirestore = Firebase.firestore
    private var db = FirebaseFirestore.getInstance()
    private var nextSousThId: Int = 1
    private var THemeId:Int=0
    private var PersId:Int=0
    var maVariable: Int = 0
    var SousthemeIdGen:Int=0;
    private lateinit var firebaseAuth: FirebaseAuth
    private var Sous_ThCollectionRef: CollectionReference = db.collection("SousTheme")
    private var ObjCollectionRef: CollectionReference = db.collection("objectif")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme2)

        val intent = intent
        THemeId = intent.getIntExtra("themeIdGen", 0)
        PersId = intent.getIntExtra("IdPer", 0)
        val NomThee2 = intent.getStringExtra("NomThee")
        val toolbar: Toolbar = findViewById(R.id.toolbar1)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(NomThee2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        println("Theme Id0: $THemeId ,Id Pers0: $PersId")
        //wById<BuToast.makeText(this, "id $THemeId idp  $PersId", Toast.LENGTH_SHORT).show()
        val myButtonPrin = findViewById<Button>(R.id.button12)
        val myButtonSec1 = findViewById<Button>(R.id.button13)
        val myButtonSec2 = findViewById<Button>(R.id.button14)
        val T1 = findViewById<TextView>(R.id.textViewSec)
        val T2 = findViewById<TextView>(R.id.textViewSec2)
        myButtonPrin.setOnClickListener {
            if (Cli % 2 == 1) {
                myButtonSec1.visibility = View.VISIBLE
                myButtonSec2.visibility = View.VISIBLE
                T1.visibility = View.VISIBLE
                T2.visibility = View.VISIBLE
                myButtonPrin.text = "x"
                myButtonPrin.textSize = 14F
                Cli++
            } else {
                myButtonSec1.visibility = View.INVISIBLE
                myButtonSec2.visibility = View.INVISIBLE
                T1.visibility = View.INVISIBLE
                T2.visibility = View.INVISIBLE
                myButtonPrin.text = "+"
                myButtonPrin.textSize = 19f
                Cli++
            }
        }
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnClickListener {
            myButtonSec1.visibility = View.INVISIBLE
            myButtonSec2.visibility = View.INVISIBLE
            T1.visibility = View.INVISIBLE
            T2.visibility = View.INVISIBLE
            myButtonPrin.text = "+"
            myButtonPrin.textSize = 19F
            if (Cli % 2 != 1) {
                Cli++
            }
        }

        //SecondActivity: ajouter un objectif dans un thème
        val myButton = findViewById<Button>(R.id.button13)
        myButton.setOnClickListener {
            val intent = Intent(this, Objectif::class.java)
            intent.putExtra("themeIdGen",THemeId)
            intent.putExtra("IdPer",PersId)
            startActivity(intent)

        }

        afficher2(THemeId, PersId)
    }

    fun afficher2(THemeId: Int, PersId: Int) {
        val myButtonSec2 = findViewById<Button>(R.id.button14)
        val layout = findViewById<LinearLayout>(R.id.ouss)

        /*  val fragmentF1 = findViewById<FrameLayout>(R.id.F1)
          val Sous_themesCollectionRef = firestoreDB.collection("SousTheme")
          Sous_themesCollectionRef
              .whereEqualTo("idTh", THemeId)
              .whereEqualTo("idUs", PersId)
              .get()
              .addOnSuccessListener { querySnapshot ->
                  // Créer un conteneur pour les boutons
                  val container = LinearLayout(this)
                  val containerParams = RelativeLayout.LayoutParams(
                      RelativeLayout.LayoutParams.MATCH_PARENT,
                      RelativeLayout.LayoutParams.WRAP_CONTENT
                  )
                  containerParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                  container.layoutParams = containerParams
                  container.orientation = LinearLayout.VERTICAL

                  // Ajouter les boutons au conteneur
                  for (document in querySnapshot.documents) {
                      val titre = document.getString("titre")
                      var NomSsT = document.getString("titre")
                      NomSsT = NomSsT?.toUpperCase() ?: ""
                      val id = document.getLong("id")
                      val button = Button(this)
                      button.text = titre
                      button.setBackgroundColor(Color.rgb(255, 165, 0))
                      val dm = resources.displayMetrics
                      val density = dm.density
                      val params = LinearLayout.LayoutParams(
                          (density * 300).toInt(),
                          (density * 38).toInt()
                      )
                      params.gravity = Gravity.CENTER_HORIZONTAL
                      params.topMargin = (density * 10).toInt()
                      params.bottomMargin = (density * 8).toInt()
                      //button.gravity = Gravity.CENTER
                      button.layoutParams = params
                      container.addView(button)
                      button.setOnClickListener {
                          val intent = Intent(this, Sous_themes::class.java)
                          intent.putExtra("NomSsT",NomSsT)
                          intent.putExtra("documentId", document.id)
                          intent.putExtra("nextSousThId",nextSousThId)
                          println("Sous_Theme Id: $nextSousThId ")
                          startActivity(intent)

                      }
                      /*val intent = Intent(this, ObjectifSousTheme::class.java)
                      intent.putExtra("nextSousThId",nextSousThId)
                      println("Sous_Theme Id888: $nextSousThId ")*/
                  }
                  fragmentF1.addView(container)
              }
              .addOnFailureListener { e ->
                  Toast.makeText(
                      this,
                      "Erreur lors de la récupération des sous-thèmes : ${e.message}",
                      Toast.LENGTH_SHORT
                  ).show()
              }*/


        val fragmentF1 = findViewById<FrameLayout>(R.id.F1)
        val sousThemesCollection = FirebaseFirestore.getInstance().collection("SousTheme")
        sousThemesCollection
            .whereEqualTo("idTh", THemeId)
            .whereEqualTo("idUs", PersId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Compter le nombre de documents dans la collection
                    val numberOfSousThemes = task.result?.size()
                    println("Il y a $numberOfSousThemes documents dans la collection \"SousTheme\".")

                    // Créer un conteneur pour les boutons
                    val container = LinearLayout(this)
                    val containerParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    container.layoutParams = containerParams
                    container.orientation = LinearLayout.VERTICAL
                    container.gravity = Gravity.CENTER_HORIZONTAL

                    // Boucle sur les documents dans la collection et créer un bouton pour chaque document
                    var index = 0
                    task.result?.documents?.forEach { doc ->
                        val SousthemeData = doc.data?.toMutableMap()
                        val SousthemeButton = Button(this)
                        SousthemeButton.text = SousthemeData?.get("titre")?.toString() ?: ""
                        val NomThee = SousthemeData?.get("titre")?.toString() ?: ""
                        val context = this
                        SousthemeButton.setBackgroundColor(Color.rgb(255, 165, 0))
                        val displayMetrics = context.resources.displayMetrics
                        val density = displayMetrics.density
                        val params = LinearLayout.LayoutParams(
                            (density * 300).toInt(),
                            (density * 50).toInt()
                        )
                        params.topMargin = (density * 10).toInt()
                        params.bottomMargin = (density * 8).toInt()
                        SousthemeButton.layoutParams = params
                        SousthemeButton.gravity = Gravity.CENTER
                        container.addView(SousthemeButton)
                        SousthemeButton.setOnClickListener {
                            if (maVariable == 0) {
                                val intent = Intent(this, Sous_themes::class.java)
                                val sousthemeId = doc.id
                                //var themeIdGen:Int=0
                                val userRef = FirebaseFirestore.getInstance().collection("SousTheme")
                                    .document(sousthemeId)
                                userRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        SousthemeIdGen =
                                            documentSnapshot.getLong("id")?.toInt() ?: 0
                                        Toast.makeText(
                                            this,
                                            "id :$SousthemeIdGen",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        intent.putExtra("documentId", sousthemeId)
                                        intent.putExtra("NomThee", NomThee)
                                        intent.putExtra("SousthemeIdGen", SousthemeIdGen)
                                        println("Sous_Theme ID0000: $SousthemeIdGen")
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { exception ->
                                        // Gérer les erreurs ici
                                        Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
                                    }

                            }

                        }
                    }
                    fragmentF1.addView(container)
                }

            }


        val fragmentF2 = findViewById<FrameLayout>(R.id.F2)
        val ObjectifCollectionRef = firestoreDB.collection("objectif")
        ObjectifCollectionRef
            .whereEqualTo("idTh", THemeId)
            .whereEqualTo("idUs", PersId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Créer un conteneur pour les boutons
                val container2 = LinearLayout(this)
                val containerParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                containerParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                container2.layoutParams = containerParams
                container2.orientation = LinearLayout.VERTICAL

                // Ajouter les boutons au conteneur
                for (document in querySnapshot.documents) {
                    val titre = document.getString("title")
                    var NomOBJ = document.getString("title")
                    val id = document.getLong("id")
                    val button = Button(this)
                    button.text = titre
                    button.setBackgroundColor(Color.rgb(69,163,255))
                    val dm = resources.displayMetrics
                    val density = dm.density
                    val params = LinearLayout.LayoutParams(
                        (density * 300).toInt(),
                        (density * 38).toInt()
                    )
                    params.gravity = Gravity.CENTER_HORIZONTAL
                    params.topMargin = (density * 10).toInt()
                    params.bottomMargin = (density * 8).toInt()
                    //button.gravity = Gravity.CENTER
                    button.layoutParams = params
                    container2.addView(button)
                    button.setOnClickListener {
                        val intent = Intent(this, ConsulterObjectif::class.java)
                        intent.putExtra("NomOBJ",NomOBJ)
                        intent.putExtra("documentId", document.id)
                        intent.putExtra("AC",0)
                        startActivity(intent)
                    }
                }
                fragmentF2.addView(container2)
                println("Theme Id999: $THemeId ,Id Pers999: $PersId")

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erreur lors de la récupération des objectifs : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }



        firebaseAuth = FirebaseAuth.getInstance()
// Get the next SousTheme ID
        Sous_ThCollectionRef
            .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    nextSousThId = (querySnapshot.documents[0].get("id") as Long).toInt() + 1
                    println("Theme Id000: $THemeId ,Id Pers000: $PersId,Dernier Sous_Theme Id000: $nextSousThId")

                }
            }





        myButtonSec2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.sous_theme, null)
            builder.setView(view)
            val dialog = builder.create()
            val T3 = view.findViewById<EditText>(R.id.editTitre_sous_Theme)
            val T4 = view.findViewById<EditText>(R.id.descri)
            view.findViewById<Button>(R.id.btnEnvoyer6).setOnClickListener {
                val Sous_themesCollectionRef = firestoreDB.collection("SousTheme")
                val soUtH1 = SousTheme(T3.text.toString(), T4.text.toString(), THemeId, PersId, nextSousThId)



                Sous_themesCollectionRef.add(soUtH1)
                    .addOnSuccessListener { documentReference ->
                        afficher2(THemeId, PersId)
                        Toast.makeText(this, "le sous_thème à été ajouté", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "$THemeId")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "le sous_thème n'a pas été ajouté",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnannuler6).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
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
        afficher2(THemeId,PersId)
        // Mettre à jour la page ici avec toutes les modifications éventuelles
    }
}