package com.example.app_authetification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.example.app_authetification.databinding.ActivityConnectBinding
import android.util.Log
class acountActivity : AppCompatActivity() {
    private lateinit var firestoreDB: FirebaseFirestore
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_acount)
        firestoreDB = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val usersCollectionRef = firestoreDB.collection("user")
        val currentUserDocRef = usersCollectionRef.document(currentUser?.uid ?: "")
        currentUserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                    val userName = documentSnapshot.getString("Nom")
                    val userId = documentSnapshot.getLong("id")?.toString()
                    val userEmail = documentSnapshot.getString("Email")
                    val userStatu = documentSnapshot.getString("Statu")

                    // Afficher le nom d'utilisateur dans le TextView "textView9"
                    findViewById<TextView>(R.id.AfficherId).text = userId
                    findViewById<TextView>(R.id.AffcherNom).text = userName
                    findViewById<TextView>(R.id.AfficherEmail).text = userEmail
                    findViewById<TextView>(R.id.AfficherType).text = userStatu
            }
            .addOnFailureListener { exception ->
                // Gérer les erreurs ici
            }
        val CNo=findViewById<TextView>(R.id.M_NO)
        CNo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.modifiernom, null)
            val userNOOMM = view.findViewById<EditText>(R.id.editNom3)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnEnvoyer3).setOnClickListener {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                // Vérifie si un utilisateur est connecté
                if (userId != null) {

                    // Spécifie le document à mettre à jour
                    val userRef = db.collection("user").document(userId)

                    // Met à jour le champ "nom" du document
                    userRef.update("Nom",userNOOMM.text.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Le champ 'Nom' a été mis à jour avec succès", Toast.LENGTH_SHORT).show()
                            findViewById<TextView>(R.id.AffcherNom).text=userNOOMM.text.toString()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Erreur2", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()}
            view.findViewById<Button>(R.id.btnannuler3).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
        val Ch_M_P = findViewById<TextView>(R.id.M_MDP)
        Ch_M_P.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.modifierpassword, null)
            val userpasse1 = view.findViewById<EditText>(R.id.editpasse1)
            val userpasse2 = view.findViewById<EditText>(R.id.editpasse2)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnEnvoyer2).setOnClickListener {
                val text2 = userpasse1.text.toString()
                val text3 = userpasse2.text.toString()
                if (TextUtils.isEmpty(text2) || TextUtils.isEmpty(text3)) {
                    Toast.makeText(this, "Il manque un champ de mot de passe", Toast.LENGTH_SHORT).show()
                } else if (text2 != text3) {
                    Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                } else {
                    val user = auth.currentUser
                    if (user != null) {
                        user.updatePassword(text2).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userRef = db.collection("user").document(user.uid)
                                userRef.update("MotPass", text2).addOnSuccessListener {
                                    Toast.makeText(this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(this, "Erreur lors de la mise à jour du mot de passe: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Erreur lors de la mise à jour du mot de passe: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Aucun utilisateur connecté", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            view.findViewById<Button>(R.id.btnannuler2).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            dialog.show()
        }

    }
}
