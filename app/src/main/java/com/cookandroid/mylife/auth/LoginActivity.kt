package com.cookandroid.mylife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.cookandroid.mylife.MainActivity
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth


        binding.backBtn.setOnClickListener {
            var intent = Intent(this,IntroActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                      var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                       Toast.makeText(this,"이메일과 패스워드가 잘못되었습니다.",Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}

