package com.example.app_authetification

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot

class ConsulterObjectif : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var docRef: DocumentReference
    private lateinit var toolbar1: Toolbar
    private var AC: Int=2
    var documentId=""
    var QU: Double = 0.0
    var selectedCount: Int = 0
    var nonSelectedCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulter_objectif)
        val Tit1 = intent.getStringExtra("NomOBJ")
        toolbar1 = findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar1)
        supportActionBar?.setTitle(" ")
        AC=intent.getIntExtra("AC",0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        documentId = intent.getStringExtra("documentId")!!
        val T1 = findViewById<TextView>(R.id.textView6)
        val T2 = findViewById<TextView>(R.id.textView8)
        val T3 = findViewById<TextView>(R.id.textView10)
        val T4 = findViewById<TextView>(R.id.textView12)
        val T5 = findViewById<TextView>(R.id.textView14)
        val T6 = findViewById<TextView>(R.id.textView16)
        val TQ = findViewById<TextView>(R.id.Qpr)
        //T1.text=documentId
        docRef = db.collection("objectif").document(documentId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val titre = document.getString("title")
                //val Qualf = document.getLong("Qualification")
                val Qualf: Double? = document.getDouble("Qualification")
                if (Qualf != null) {
                    val QualfPr = Qualf * 100
                    //TQ.text = QualfPr.toString()
                    val IQU: Int? = QualfPr.toInt()
                    TQ.text = "$IQU %"
                } else {
                    TQ.text = "Le champ 'Qualification' est null"
                }
                val description = document.getString("description")
                val timestamp = document.getTimestamp("dateDebut")
                supportActionBar?.setTitle(titre)
                // Extraire le jour, le mois et l'année de la date
                val date = timestamp?.toDate()
                val day = date?.date
                val month = date?.month?.plus(1) // Ajouter 1 car le mois commence à 0
                val year = date?.year?.plus(1900)
                T1.text=titre
                T2.text=description
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
                val timestamp3 = document.getTimestamp("dateFin")
                // Extraire le jour, le mois et l'année de la date
                val date2 = timestamp3?.toDate()
                val day2 = date2?.date
                val month2 = date2?.month?.plus(1) // Ajouter 1 car le mois commence à 0
                val year2 = date2?.year?.plus(1900)
                T5.setText("$day2/$month2/$year2")

                val Difficulty = document.getString("Difficulty")
                T6.text=Difficulty

            } else{
                Toast.makeText(this, "document n'existe pas", Toast.LENGTH_SHORT).show()}
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Erreur de document ", Toast.LENGTH_SHORT).show()
        }
        afficher(documentId)
        val objectifRef = db.collection("objectif").document(documentId)
        val B_A_J = findViewById<TextView>(R.id.Ajouter_Cri)

        B_A_J.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.ajouter_criteres, null)
                val Cri_titr = view.findViewById<EditText>(R.id.editTitreCritere)
                builder.setView(view)
                val dialog = builder.create()
                view.findViewById<Button>(R.id.btnEnvoyer14).setOnClickListener {
                    val newCriteres: HashMap<String, Any> = hashMapOf(
                        "titre" to Cri_titr.text.toString(),
                        "Selected" to 0
                    )
                    objectifRef.get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val objectif = documentSnapshot.data
                            if (objectif?.get("criteres") != null) {
                                // Le champ "criteres" existe déjà
                                val criteres = objectif["criteres"] as MutableList<HashMap<String, Any>>
                                criteres.add(newCriteres)
                                objectifRef.update("criteres", criteres)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "L'élément a été ajouté avec succès dans le champ 'criteres'", Toast.LENGTH_SHORT).show()
                                        selectedCount = criteres.count { it["Selected"] == 1L }
                                        nonSelectedCount = criteres.count { it["Selected"] == 0L }
                                        println("element selected $selectedCount et les elements Non Selested est $nonSelectedCount")
                                        QU=documentSnapshot.getDouble("Qualification") ?: 0.0
                                        if (QU != null) {
                                            println("La valeur de QU est : $QU")
                                        } else {
                                            println("Le champ 'Qualification' est null ou non défini")
                                        }
                                        QU = selectedCount.toDouble() / (nonSelectedCount+selectedCount+1).toDouble()
                                        objectifRef.update("Qualification", QU.toDouble())
                                        //val TQ = findViewById<TextView>(R.id.Qpr)
                                        val QualfPr = QU.toDouble() * 100
                                        //TQ.text = QualfPr.toString()
                                        val IQU: Int? = QualfPr.toInt()
                                        TQ.text = "$IQU %"
                                        afficher(documentId)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Erreur lors de l'ajout de l'élément dans le champ 'criteres'", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Le champ "criteres" n'existe pas
                                val criteres = mutableListOf<HashMap<String, Any>>(newCriteres)
                                objectifRef.update("criteres", criteres)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Le champ 'criteres' a été ajouté avec succès", Toast.LENGTH_SHORT).show()
                                        /*val QualfPr = QU.toDouble() * 100
                                        //TQ.text = QualfPr.toString()
                                        val IQU: Int? = QualfPr.toInt()*/
                                        TQ.text = "0 %"
                                        afficher(documentId)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Erreur lors de l'ajout du champ 'criteres'", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Le document n'existe pas", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                }
                view.findViewById<Button>(R.id.btnannuler14).setOnClickListener {
                    dialog.dismiss()
                }
                if (dialog.window != null){
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()

        }
    }

    fun afficher(documentId:String) {

        val documentRef = db.collection("objectif").document(documentId)
        val frameLayout = findViewById<FrameLayout>(R.id.F5)

        // Récupérer les critères depuis Firebase
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val criteres = document.get("criteres") as? List<HashMap<String, Any>>
                    QU=document.getDouble("Qualification") ?: 0.0
                    if (QU != null) {
                        println("La valeur de QU est : $QU")
                    } else {
                        println("Le champ 'Qualification' est null ou non défini")
                    }

                    if (criteres != null) {

                        for (i in criteres.indices) {
                            val critere = criteres[i]
                            val titre = critere["titre"] as String
                            val selected = critere["Selected"] as? Long

                            val checkBox = CheckBox(this)
                            checkBox.text = titre
                            checkBox.isChecked = selected?.let { it == 1L || it.toInt() == 1 } ?: false
                            val params = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.topMargin = i * resources.getDimensionPixelSize(R.dimen.checkbox_margin_top)
                            params.leftMargin = resources.getDimensionPixelSize(R.dimen.checkbox_margin_top)
                            checkBox.layoutParams = params
                            checkBox.setOnCheckedChangeListener { _, isChecked ->
                                val newValue = if (isChecked) 1L else 0L
                                criteres[i]["Selected"] = newValue
                                documentRef.update("criteres", criteres)
                                selectedCount = criteres.count { it["Selected"] == 1L }
                                nonSelectedCount = criteres.count { it["Selected"] == 0L }
                                println("element selected $selectedCount et les elements Non Selested est $nonSelectedCount")
                                /*if (isChecked) {
                                    //QU = (QU ?: 0.0) + 1.0
                                    QU = selectedCount.toDouble() / c.toDouble()
                                    documentRef.update("Qualification", QU.toDouble())
                                } else {
                                    //QU = (QU ?: 0.0) + 0.5
                                    QU Double = b.toDouble() / c.toDouble()
                                    documentRef.update("Qualification", QU.toDouble())
                                }*/
                                QU = selectedCount.toDouble() / (nonSelectedCount+selectedCount).toDouble()
                                documentRef.update("Qualification", QU.toDouble())
                                val TQ = findViewById<TextView>(R.id.Qpr)
                                val QualfPr = QU.toDouble() * 100
                                //TQ.text = QualfPr.toString()
                                val IQU: Int? = QualfPr.toInt()
                                TQ.text = "$IQU %"


                            }

                            frameLayout.addView(checkBox)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs de récupération des critères depuis Firebase
                // Log.e(TAG, "Erreur lors de la récupération des critères : $exception")
            }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                // Action à effectuer lorsque l'utilisateur sélectionne l'élément de menu HH
                val intent = Intent(this, ModifierObjectif::class.java)
                intent.putExtra("documentId",documentId)
                startActivity(intent)
                return true
            }
            R.id.action_delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Supprimer")
                builder.setMessage("Voulez-vous vraiment supprimer ?")
                builder.setPositiveButton("Oui") { _, _ ->
                    //onBackPressed()
                    //recreate()*/
                    docRef.delete()
                        .addOnSuccessListener {
                            val intent = Intent(this, compte::class.java)
                            startActivity(intent)
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
    @SuppressLint("SuspiciousIndentation")
    override fun onResume() {
        super.onResume()
        //recreate()
        //afficher2(THemeId,PersId)
        // Mettre à jour la page ici avec toutes les modifications éventuelles
        if(AC==1){
            val intent = Intent(this,ConsulterObjectif::class.java)
                intent.putExtra("documentId",documentId)
                intent.putExtra("AC",2)
            startActivity(intent)
            AC=AC+1
        }
    }
}