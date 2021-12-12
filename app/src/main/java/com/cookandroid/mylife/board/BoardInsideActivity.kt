package com.cookandroid.mylife.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.cookandroid.mylife.R
import com.cookandroid.mylife.comment.CommentLVAdapter
import com.cookandroid.mylife.comment.CommentModel
import com.cookandroid.mylife.databinding.ActivityBoardInsideBinding
import com.cookandroid.mylife.utils.FBAuth
import com.cookandroid.mylife.utils.FBRef
import com.cookandroid.mylife.utils.GetDate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)


        binding.boardsettingIcon.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString() // intent로 key 값을 받아옴
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }


        getCommentDate(key)
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter
    }

    fun getCommentDate(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in snapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                    Log.d("comment",item.toString())
                }

                    commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }



// 댓글 create
fun insertComment(key: String) {
    FBRef.commentRef // comment
        .child(key) // boardKey
        .push() // commentKey
        .setValue(
            CommentModel(
                binding.commentArea.text.toString(),
                GetDate.getTime()
            )
        ) // commentData
    Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
    binding.commentArea.setText("")

}

// 게시글 수정 삭제 dialog
private fun showDialog() {
    val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
    val mBuilder = AlertDialog.Builder(this)
        .setView(mDialogView)
        .setTitle("게시글 수정/삭제")

    val alertDialog = mBuilder.show()

    // 수정버튼
    alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
        val intent = Intent(this, BoardEditActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
    }
    // 삭제버튼
    alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
        FBRef.boardRef.child(key).removeValue()
        Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show()
    }

}

// image download
private fun getImageData(key: String) {

    val storageReference = Firebase.storage.reference.child(key + ".png") // 경로설정
    val imageView = binding.getImageArea

    storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
        if (task.isSuccessful) {
            Glide.with(this)
                .load(task.result)
                .into(imageView) // 경로의 이미지를 imageView에 넣어줌

        } else {
            binding.getImageArea.isVisible = false
        }
    })

}

// 해당하는 key의 게시글을 db에서 불러옴
private fun getBoardData(key: String) {
    val postListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            try {
                val dataModel = snapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.textArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time

                val curUid = FBAuth.getUid()
                val writerUid = dataModel.uid
                if (curUid == writerUid) {
                    binding.boardsettingIcon.isVisible = true
                }
            } catch (e: Exception) {

            }

        }

        override fun onCancelled(error: DatabaseError) {
        }
    }
    FBRef.boardRef.child(key).addValueEventListener(postListener)

}

}