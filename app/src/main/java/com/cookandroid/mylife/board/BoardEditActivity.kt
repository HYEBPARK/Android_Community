package com.cookandroid.mylife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.ActivityBoardEditBinding
import com.cookandroid.mylife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String
    private lateinit var binding : ActivityBoardEditBinding
    private val TAG = BoardEditActivity::class.java

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
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

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}