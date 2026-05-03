package com.example.app_authetification

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentReference
import android.text.Editable
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.*

class Sous_themes : AppCompatActivity() {

    var Cli: Int = 1
    private var sousthemeId:Int=0
    private var firestoreDB: FirebaseFirestore = Firebase.firestore
    private var db = FirebaseFirestore.getInstance()
    private lateinit var docRef: DocumentReference
    private lateinit var toolbar1: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sous_themes)

        val intent = intent
        val Tit1 = intent.getStringExtra("NomThee")
        val documentId = intent.getStringExtra("documentId")!!
        sousthemeId = intent.getIntExtra("SousthemeIdGen", 0)
        println("Sous_Theme Id105: $sousthemeId")

        //println(documentId + "HHHHHHHHHHHHHH")
        toolbar1 = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar1)
        supportActionBar?.setTitle(Tit1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        /*Sous_themesCollectionRef = FirebaseFirestore.getInstance().collection("SousTheme")
        val docRef = Sous_themesCollectionRef.document(documentId)*/
        val Sous_themesCollectionRef = db.collection("SousTheme")
        docRef = db.collection("SousTheme").document(documentId)


        //wById<BuToast.makeText(this, "id $THemeId idp  $PersId", Toast.LENGTH_SHORT).show()
        val myButtonPrin = findViewById<Button>(R.id.button12)
        val myButtonSec1 = findViewById<Button>(R.id.button13)
        val T1 = findViewById<TextView>(R.id.textViewSec2)
        myButtonPrin.setOnClickListener {
            if (Cli % 2 == 1) {
                myButtonSec1.visibility = View.VISIBLE
                T1.visibility = View.VISIBLE
                myButtonPrin.text = "x"
                myButtonPrin.textSize = 14F
                Cli++
            } else {
                myButtonSec1.visibility = View.INVISIBLE
                T1.visibility = View.INVISIBLE
                myButtonPrin.text = "+"
                myButtonPrin.textSize = 19f
                Cli++
            }
        }
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnClickListener {
            myButtonSec1.visibility = View.INVISIBLE
            T1.visibility = View.INVISIBLE
            myButtonPrin.text = "+"
            myButtonPrin.textSize = 19F
            if (Cli % 2 != 1) {
                Cli++
            }
        }

        //SecondActivity: ajouter un objectif dans un Sous_Thème
        val myButton = findViewById<Button>(R.id.button13)
        myButton.setOnClickListener {
            val intent = Intent(this, ObjectifSousTheme::class.java)
            intent.putExtra("SousthemeIdGen", sousthemeId)
            startActivity(intent)

        }
        afficher3(sousthemeId)

    }
      fun   afficher3(idSTh: Int) {
          val fragmentF2 = findViewById<FrameLayout>(R.id.F2)
          val ObjectifCollectionRef = firestoreDB.collection("objectifSousTheme")
          ObjectifCollectionRef
              .whereEqualTo("idSTh", sousthemeId)
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
                      val titre = document.getString("title")
                      val id = document.getLong("id")
                      var NomOBJ = document.getString("title")
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
                      container.addView(button)
                      button.setOnClickListener {
                          val intent = Intent(this, ConsulterObjectifSousTheme::class.java)
                          intent.putExtra("NomOBJ", NomOBJ)
                          intent.putExtra("documentId", document.id)
                          intent.putExtra("AC", 0)
                          startActivity(intent)
                      }
                  }
                  fragmentF2.addView(container)
                  println("SousTheme Id: $sousthemeId")

              }
              .addOnFailureListener { e ->
                  Toast.makeText(
                      this,
                      "Erreur lors de la récupération des objectifs : ${e.message}",
                      Toast.LENGTH_SHORT
                  ).show()
              }

      }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                //Toast.makeText(this, "Modifier cliqué", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.modifier_soustheme, null)
                val sousDesc = view.findViewById<EditText>(R.id.descri2)
                val sousTitre = view.findViewById<EditText>(R.id.editTitre_sous_Theme2)
                builder.setView(view)
                val dialog = builder.create()
                docRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        /* val titre = document.getString("titre") ?: ""
                         val description = document.getString("description") ?: "" */
                        val titre = document.getString("titre")
                        val description = document.getString("description")
                        sousTitre.text = Editable.Factory.getInstance().newEditable(titre)
                        sousDesc.text = Editable.Factory.getInstance().newEditable(description)
                    } else {
                        Toast.makeText(this, "document n'existe pas", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Erreur de document ", Toast.LENGTH_SHORT).show()
                }

                view.findViewById<Button>(R.id.btnEnvoyer9).setOnClickListener {
                    val nouveauTitre = sousTitre.text.toString()
                    val nouvelleDescription = sousDesc.text.toString()
                    val data = hashMapOf(
                        "titre" to nouveauTitre,
                        "description" to nouvelleDescription
                    )

                    docRef.update(data as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Sous-thème modifié avec succès !",
                                Toast.LENGTH_SHORT
                            ).show()
                            //refreshPage()
                            /*setSupportActionBar(toolbar1)
                            supportActionBar?.setTitle(nouveauTitre)
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
                            toolbar1 = findViewById(R.id.toolbar2)
                            setSupportActionBar(toolbar1)
                            supportActionBar?.setTitle(nouveauTitre)
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Erreur lors de la modification du sous-thème",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    dialog.dismiss()
                }
                view.findViewById<Button>(R.id.btnannuler9).setOnClickListener {
                    dialog.dismiss()
                }
                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()
                return true
            }
            R.id.action_delete -> {
                //Toast.makeText(this, "Supprimer cliqué", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Supprimer")
                builder.setMessage("Voulez-vous vraiment supprimer ?")
                builder.setPositiveButton("Oui") { _, _ ->
                docRef.delete()
                    .addOnSuccessListener {
                        val intent = Intent(this, compte::class.java)
                        startActivity(intent)
                        //onBackPressed()
                        //recreate()*/
                    }
                    .addOnFailureListener { e ->
                        // Log.w(TAG, "Erreur lors de la suppression du document", e)
                        Toast.makeText(this, "ER 401", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("Non", null)
                val dialog = builder.create()
                dialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        /*val intent = Intent(this,themeActivity2::class.java)
        startActivity(intent)*/
        return true
    }
    /*override fun onSupportNavigateUp(): Boolean {
        finish()
        super.onSupportNavigateUp()
        return true
    }*/
    /* fun refreshPage() {
         val intent = intent // récupérer l'intent actuel
         finish() // fermer l'activité actuelle
         startActivity(intent) // démarrer une nouvelle instance de l'activité

     }*/
    override fun onResume() {
        super.onResume()
        afficher3(sousthemeId)
        //recreate()
       //afficher2(THemeId,PersId)
        // Mettre à jour la page ici avec toutes les modifications éventuelles
    }
}
