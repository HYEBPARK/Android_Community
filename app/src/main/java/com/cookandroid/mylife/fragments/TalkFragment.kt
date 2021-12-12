package com.cookandroid.mylife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.navigation.findNavController
import com.cookandroid.mylife.R
import com.cookandroid.mylife.board.BoardInsideActivity
import com.cookandroid.mylife.board.BoardListLVAdapter
import com.cookandroid.mylife.board.BoardModel
import com.cookandroid.mylife.board.BoardWriteActivity
import com.cookandroid.mylife.contentsList.ContentModel
import com.cookandroid.mylife.databinding.FragmentTalkBinding
import com.cookandroid.mylife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    private val boardDataList = mutableListOf<BoardModel>() //board 게시글을 read하기 위해 받는 list
    private val boardKeyList = mutableListOf<String>() // fb database의 게시글 키값을 저장
    private val TAG = TalkFragment::class.java.simpleName
    private lateinit var boardRVAdapter : BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        boardRVAdapter = BoardListLVAdapter(boardDataList) // 게시글을 넣어준다.
        binding.boardListView.adapter = boardRVAdapter


        // 게시글 클릭시 세부내용 read
        binding.boardListView.setOnItemClickListener{  parent, view, position, id ->
            val intent = Intent(context , BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position]) // key 값을 intent 해줌
            startActivity(intent)
        }


        binding.writeBtn.setOnClickListener{
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }

        getFBBoardDate()

        return binding.root
    }

    //firebase에서 데이터 read
    private fun getFBBoardDate(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in snapshot.children){
                    Log.d(TAG, dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!) // 게시글 add
                    boardKeyList.add(dataModel.key.toString()) // key 값 add
                }
                boardKeyList.reverse()
                boardDataList.reverse() // 최신순으로 정렬
                boardRVAdapter.notifyDataSetChanged() // 데이터 동기화
                Log.d(TAG, boardDataList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}