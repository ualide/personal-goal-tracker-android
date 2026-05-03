package com.example.app_authetification

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.view.Gravity
import android.widget.*
import com.google.firebase.firestore.CollectionReference

class compte : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    var maVariable: Int = 0
    var OUSSA:Int=12
    var IdPer:Int=2;
    var themeIdGen:Int=0;
    private var db = FirebaseFirestore.getInstance()
    private var nextThemId: Int = 1
    private lateinit var firebaseAuth: FirebaseAuth
    private var themCollectionRef: CollectionReference = db.collection("Themes")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compte)
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setTitle("Thèmes")
        toggle.syncState()
        if (savedInstanceState == null) {
            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
            linearLayout.requestFocus()
            navigationView.setCheckedItem(R.id.nav_home)
        }
        firestoreDB = FirebaseFirestore.getInstance()

        // Récupérer la référence à l'utilisateur actuellement connecté
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Récupérer la référence à la collection "users"
        val usersCollectionRef = firestoreDB.collection("user")
        var statUser: String? = null
        // Récupérer le document correspondant à l'utilisateur actuel
        val currentUserDocRef = usersCollectionRef.document(currentUser?.uid ?: "")
        currentUserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val userName = documentSnapshot.getString("Nom")
                userName?.let {
                    // Afficher le nom d'utilisateur dans le TextView "textView9"
                    //findViewById<TextView>(R.id.noom).text = it
                    val navigationView = findViewById<NavigationView>(R.id.nav_view)
                    val navHeader = navigationView.getHeaderView(0)
                    navHeader.findViewById<TextView>(R.id.noom).text = it
                }
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs ici
            }

        currentUserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val userName = documentSnapshot.getString("Email")
                userName?.let {
                    // Afficher le nom d'utilisateur dans le TextView "textView9"
                    //findViewById<TextView>(R.id.noom).text = it
                    val navigationView = findViewById<NavigationView>(R.id.nav_view)
                    val navHeader = navigationView.getHeaderView(0)
                    navHeader.findViewById<TextView>(R.id.email2).text = it
                }
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs ici
            }
        currentUserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                IdPer  = documentSnapshot.getLong("id")?.toInt() ?: 0
                IdPer?.let {
                    println("Id user bien recu")
                }
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs ici
            }

        val myButton = findViewById<Button>(R.id.circular_button)
        currentUserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                statUser = documentSnapshot.getString("Statu")
                statUser?.let {
                    // Afficher le nom d'utilisateur dans le TextView "textView9"
                    //findViewById<TextView>(R.id.noom).text = it
                    //println(statUser)


                    if (statUser == "Administrateur") {
                        myButton.visibility = View.VISIBLE
                        maVariable=1

                    } else {
                        myButton.visibility = View.GONE
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs ici
            }


        myButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.ajoutertheme, null)
            val Titre_theme_ajout = view.findViewById<EditText>(R.id.editTitreTheme)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnEnvoyer4)?.setOnClickListener {
                dialog.dismiss()
                val themesCollectionRef = firestoreDB.collection("Themes")
                themesCollectionRef
                    .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        var nextThemId = 1
                        if (!querySnapshot.isEmpty) {
                            nextThemId = (querySnapshot.documents[0].get("id") as Long).toInt() + 1
                        }
                        val newTheme = Theme(Titre_theme_ajout.text.toString())
                        newTheme.setId(nextThemId)
                        themesCollectionRef.add(newTheme)
                            .addOnSuccessListener { documentReference ->
                                afficher()
                                Toast.makeText(this, "Le thème a été ajouté", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erreur : thème non ajouté", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
            }
            view.findViewById<Button>(R.id.btnannuler4)?.setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }



        /*val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        val themesCollection = FirebaseFirestore.getInstance().collection("Themes")

// Utiliser la méthode .get() pour récupérer tous les documents de la collection "themes"
        themesCollection.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Compter le nombre de documents dans la collection
                    val numberOfThemes = task.result?.size()
                    println("Il y a $numberOfThemes documents dans la collection \"themes\".")

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
                        val themeData = doc.data?.toMutableMap()
                        val themeButton = Button(this)
                        themeButton.text = themeData?.get("titre")?.toString() ?: ""
                        val context = this
                        val displayMetrics = context.resources.displayMetrics
                        val density = displayMetrics.density
                        val params = LinearLayout.LayoutParams(
                            (density * 300).toInt(),
                            (density * 50).toInt()
                        )
                        params.topMargin = (density * 10).toInt()
                        params.bottomMargin = (density * 8).toInt()
                        themeButton.layoutParams = params
                        themeButton.gravity = Gravity.CENTER
                        container.addView(themeButton)
                        index++
                    }
                    if (fragmentContainer != null) {
                        fragmentContainer.addView(container)
                    }

                } else {
                    val exception = task.exception
                    Toast.makeText(this, "Erreur de récupération du thème", Toast.LENGTH_SHORT)
                        .show()
                }
            }*/

        afficher()

    }
    fun afficher() {
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        val themesCollection = FirebaseFirestore.getInstance().collection("Themes")
        themesCollection.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Compter le nombre de documents dans la collection
                    val numberOfThemes = task.result?.size()
                    println("Il y a $numberOfThemes documents dans la collection \"themes\".")

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
                        val themeData = doc.data?.toMutableMap()
                        val themeButton = Button(this)
                        themeButton.text = themeData?.get("titre")?.toString() ?: ""
                        val NomThee = themeData?.get("titre")?.toString() ?: ""
                        val context = this
                        val displayMetrics = context.resources.displayMetrics
                        val density = displayMetrics.density
                        val params = LinearLayout.LayoutParams(
                            (density * 300).toInt(),
                            (density * 50).toInt()
                        )
                        params.topMargin = (density * 10).toInt()
                        params.bottomMargin = (density * 8).toInt()
                        themeButton.layoutParams = params
                        themeButton.gravity = Gravity.CENTER
                        container.addView(themeButton)
                        themeButton.setOnClickListener {
                            if (maVariable == 0) {
                                val intent = Intent(this, themeActivity2::class.java)
                                val themeId1 = doc.id
                                //var themeIdGen:Int=0
                                val userRef = FirebaseFirestore.getInstance().collection("Themes").document(themeId1)
                                userRef.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                         themeIdGen = documentSnapshot.getLong("id")?.toInt() ?: 0
                                        Toast.makeText(this, "id :$themeIdGen", Toast.LENGTH_SHORT).show()
                                        intent.putExtra("NomThee",NomThee)
                                        intent.putExtra("themeIdGen",themeIdGen)
                                        intent.putExtra("IdPer",IdPer)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { exception ->
                                        // Gérer les erreurs ici
                                        Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
                                    }

                            } else {

                                 val themeId = doc.id
// Affiche un menu contextuel pour les options "Modifier" et "Supprimer"
                                val menu = PopupMenu(this, themeButton)
                                menu.menuInflater.inflate(R.menu.mon_menu, menu.menu)

                                menu.setOnMenuItemClickListener { menuItem ->
                                    when (menuItem.itemId) {
                                        R.id.action_modifier -> {
                                            // Affiche une boîte de dialogue pour modifier le titre du thème
                                            val builder = AlertDialog.Builder(this)
                                            val view = layoutInflater.inflate(R.layout.modifier_theme, null)
                                            val Title_theme_mod = view.findViewById<EditText>(R.id.editTitre2Theme)
                                            builder.setView(view)
                                            val dialog = builder.create()

                                            view.findViewById<Button>(R.id.btnEnvoyer5).setOnClickListener {
                                                // Met à jour le champ "titre" du thème dans Firestore
                                                val userRef = FirebaseFirestore.getInstance().collection("Themes").document(themeId)
                                                userRef.update("titre", Title_theme_mod.text.toString())
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this, "Le champ 'Titre' a été mis à jour avec succès", Toast.LENGTH_SHORT).show()
                                                        afficher()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
                                                    }
                                                dialog.dismiss()
                                            }

                                            view.findViewById<Button>(R.id.btnannuler5).setOnClickListener {
                                                dialog.dismiss()
                                            }

                                            if (dialog.window != null) {
                                                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                                            }
                                            dialog.show()
                                            true // Indique que l'événement a été géré
                                        }

                                            R.id.action_supprimer -> {
                                            // Code à exécuter lorsque l'option Supprimer est sélectionnée
                                                val builder = AlertDialog.Builder(this)
                                                builder.setTitle("Supprimer")
                                                builder.setMessage("Voulez-vous supprimer le Thème ?")


                                                builder.setPositiveButton("Oui") { dialog, which ->
                                                    // Code à exécuter lorsque l'utilisateur clique sur le bouton "OK"

                                                    val firestoreDB = FirebaseFirestore.getInstance()
                                                    val themeRef = firestoreDB.collection("Themes").document(themeId)

// Supprimer le document
                                                    themeRef.delete()
                                                        .addOnSuccessListener {
                                                            // Code à exécuter si la suppression réussit
                                                            Toast.makeText(this, "Le thème a été supprimé avec succès.", Toast.LENGTH_SHORT).show()
                                                            //afficher()
                                                            //actualiserApresDelai()
                                                            val intent = Intent(this, compte::class.java)
                                                            startActivity(intent)
                                                        }
                                                        .addOnFailureListener {
                                                            // Code à exécuter si la suppression échoue
                                                            Toast.makeText(this, "Erreur lors de la suppression du thème.", Toast.LENGTH_SHORT).show()
                                                        }
                                                }

                                                builder.setNegativeButton("Non") { dialog, which ->
                                                    // Code à exécuter lorsque l'utilisateur clique sur le bouton "Annuler"
                                                }

                                                val dialog = builder.create()
                                                dialog.show()
                                                afficher()
                                            true // Indique que l'événement a été géré
                                        }
                                        else -> false
                                    }
                                }

                                menu.show()



                            }
                        }
                        index++
                    }
                    if (fragmentContainer != null) {
                        fragmentContainer.addView(container)
                    }

                } else {
                    val exception = task.exception
                    Toast.makeText(this, "Erreur de récupération du thème", Toast.LENGTH_SHORT)
                        .show()
                }
            }


    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                /*val intent = Intent(this, ::class.java)
                startActivity(intent)*/
                val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
                linearLayout.requestFocus()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, acountActivity::class.java)
                startActivity(intent)
            }
            /*R.id.nav_share -> {
                val intent = Intent(this, modifierActivity::class.java)
                startActivity(intent)
            }*/
            R.id.nav_about ->{
                val user = FirebaseAuth.getInstance().currentUser
                val db = FirebaseFirestore.getInstance()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Supprimer")
                builder.setMessage("Voulez-vous supprimer le compte ?")
                builder.setPositiveButton("Oui") { dialog, which ->
                    db.collection("user").document(user!!.uid)
                        .delete()
                        .addOnSuccessListener {
                            // Supprimer le compte de l'utilisateur dans Firebase Authentication
                            user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Code à exécuter lorsque la suppression du compte est réussie
                                        Toast.makeText(this, "Votre compte a été supprimé", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, connect::class.java)
                                        startActivity(intent)
                                    } else {
                                        // Code à exécuter lorsque la suppression du compte échoue
                                        Toast.makeText(this, "La suppression de votre compte a échoué", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                        .addOnFailureListener {
                            // Code à exécuter lorsque la suppression du document échoue
                            Toast.makeText(this, "La suppression de votre compte a échoué", Toast.LENGTH_SHORT).show()
                        }
                }
                builder.setNegativeButton("Non", null)
                val dialog = builder.create()
                dialog.show()
            }
            R.id.nav_logout -> {
                // Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Déconnexion")
                builder.setMessage("Voulez-vous déconnecter ?")
                builder.setPositiveButton("Oui") { dialog, which ->
                    // Code à exécuter lorsque l'utilisateur clique sur le bouton "Oui"
                    // Mettez ici le code pour déconnecter l'utilisateur
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, connect::class.java)
                    startActivity(intent)
                }
                builder.setNegativeButton("Non", null)
                val dialog = builder.create()
                dialog.show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
