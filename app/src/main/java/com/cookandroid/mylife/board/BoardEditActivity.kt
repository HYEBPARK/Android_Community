package com.cookandroid.mylife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.ActivityBoardEditBinding
import com.cookandroid.mylife.utils.FBRef
import com.cookandroid.mylife.utils.GetDate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String
    private lateinit var binding : ActivityBoardEditBinding
    private lateinit var writerUID : String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        // 수정 버튼을 누를시 수정정보 update
        binding.eidtBtn.setOnClickListener {
            editBoardData(key)
        }
    }

    private fun editBoardData(key : String){
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(binding.titleArea.text.toString(),
                binding.contentArea.text.toString(),
                writerUID,
                GetDate.getTime())
            )

        Toast.makeText(this,"수정 완료",Toast.LENGTH_LONG).show()
        finish()
    }
    // image download
    private fun getImageData(key : String){

        val storageReference = Firebase.storage.reference.child(key+".png") // 경로설정
        val imageView = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView) // 경로의 이미지를 imageView에 넣어줌

            } else{

            }
        })

    }
    // 해당하는 key의 게시글을 db에서 불러옴
    private fun getBoardData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val dataModel = snapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                writerUID = dataModel!!.uid
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}