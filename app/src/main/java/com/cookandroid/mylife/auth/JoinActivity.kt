package com.cookandroid.mylife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.cookandroid.mylife.MainActivity
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        auth = Firebase.auth

        binding.creatAccount.setOnClickListener {

            var isGoToJoin = true

            val email = binding.emailArea.text.toString();
            val password1 = binding.passwordArea1.text.toString();
            val password2 = binding.passwordArea2.text.toString();

            //이메일 값이 비어있는지 check
            if(email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            //패스워드 값이 비어있는지 check
            if(password1.isEmpty()) {
                Toast.makeText(this,"패스워드를 입력해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            //패스워드 확인값이 비어있는지 check
            if(password2.isEmpty()) {
                Toast.makeText(this,"패스워드를 확인해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            //패스워드 형식이 바른지 check
            if(password1.length < 6) {
                Toast.makeText(this,"패스워드는 6자리 이상으로 입력해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            // 패스워드와 확인 값이 다른지 check
            if(!password1.equals(password2)) {
                Toast.makeText(this,"패스워드가 다릅니다",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            // 모든 조건을 충족하는 경우 회원 등록
            if(isGoToJoin){

                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()

                        }
                    }
            }

        }

    }

}