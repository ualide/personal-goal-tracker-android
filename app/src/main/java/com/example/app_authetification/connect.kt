package com.example.app_authetification

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.app_authetification.databinding.ActivityConnectBinding
import com.google.firebase.auth.FirebaseAuth

class connect : AppCompatActivity() {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        /*val textView = findViewById<TextView>(R.id.cree_compte)
        textView.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }*/
        binding.creeCompte.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.seconnecter.setOnClickListener {
            val email = binding.Email.text.toString()
            val password = binding.code1.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val verification = firebaseAuth.currentUser?.isEmailVerified
                        if(verification==true){
                          val user=firebaseAuth.currentUser
                          val intent = Intent(this, compte::class.java)
                          startActivity(intent)
                        }else
                            Toast.makeText(this,"Verify your Email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.motPasse.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.motpasseoblie, null)
            val userEmail = view.findViewById<EditText>(R.id.editNom)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnEnvoyer).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnannuler).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
    }
    //Outside onCreate

    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
