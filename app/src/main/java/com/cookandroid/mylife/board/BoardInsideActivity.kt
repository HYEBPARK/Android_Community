package com.cookandroid.mylife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.cookandroid.mylife.R
import com.cookandroid.mylife.databinding.ActivityBoardInsideBinding
import com.cookandroid.mylife.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName
    private lateinit var binding : ActivityBoardInsideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        val key = intent.getStringExtra("key") // intent로 key 값을 받아옴
        getBoardData(key.toString())
        getImageData(key.toString())

    }

    // image download
    private fun getImageData(key : String){

        val storageReference = Firebase.storage.reference.child(key+".png") // 경로설정
        val imageView = binding.getImageArea

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
    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.d(TAG, snapshot.toString())
                val dataModel = snapshot.getValue(BoardModel::class.java)
                Log.d(TAG, dataModel!!.title)
                binding.titleArea.text= dataModel!!.title
                binding.textArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "load fail", error.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}