package com.example.app_authetification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.app_authetification.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private var personsCollectionRef: CollectionReference = db.collection("user")
    private var nextPersonId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        /*firebaseAuth = FirebaseAuth.getInstance()
        // Récupération du prochain id de personne à partir de Firebase Firestore
        personsCollectionRef
            .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    nextPersonId = (querySnapshot.documents[0].get("id") as Long).toInt() + 1
                }
            }*/

        binding.button2.setOnClickListener {
            val email = binding.Email.text.toString()
            val password = binding.code1.text.toString()
            val confirmPassword = binding.code2.text.toString()
            val noom = binding.UserName.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && noom.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Envoi d'un e-mail de vérification au nouvel utilisateur
                                firebaseAuth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        // Création de l'objet Person et ajout dans Firestore
                                        val person1 = Person(noom, email, password, "Participant")

                                        // Récupération du prochain id de personne à partir de Firebase Firestore
                                        personsCollectionRef
                                            .orderBy(
                                                "id",
                                                com.google.firebase.firestore.Query.Direction.DESCENDING
                                            )
                                            .limit(1)
                                            .get()
                                            .addOnSuccessListener { querySnapshot ->
                                                if (!querySnapshot.isEmpty) {
                                                    val nextPersonId =
                                                        (querySnapshot.documents[0].get("id") as Long).toInt() + 1
                                                    person1.setId(nextPersonId)
                                                    val userMap = hashMapOf(
                                                        "id" to person1.getId(),
                                                        "Nom" to person1.getName(),
                                                        "Email" to person1.getEmail(),
                                                        "MotPass" to person1.getPassword(),
                                                        "Statu" to person1.getStatus()
                                                    )
                                                    val userId =
                                                        FirebaseAuth.getInstance().currentUser!!.uid
                                                    db.collection("user").document(userId).set(userMap)
                                                    Toast.makeText(this, "Verify your Email", Toast.LENGTH_SHORT).show()
                                                }

                                            }
                                            ?.addOnFailureListener {
                                                Toast.makeText(
                                                    this,
                                                    "Email n'existe pas",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                            }
                                else{
                                    Toast.makeText(
                                        this,
                                        "${it.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    else{
                        Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please fill up the fields", Toast.LENGTH_SHORT).show()
                }
            }

            binding.haveAcount.setOnClickListener {
                val intent = Intent(this, connect::class.java)
                startActivity(intent)
            }
        }
    }
