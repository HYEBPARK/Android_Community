package com.cookandroid.mylife.comment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.cookandroid.mylife.R
import com.cookandroid.mylife.utils.FBAuth

class CommentLVAdapter(val commentList : MutableList<CommentModel>) :BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(p0: Int): Any {
        return commentList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1

        view = LayoutInflater.from(p2?.context).inflate(R.layout.board_list_item, p2, false)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemLinearView)

        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)


        title!!.text = commentList[p0].commentTitle
        time!!.text = commentList[p0].commentCreateTime

        return view!!
    }
}