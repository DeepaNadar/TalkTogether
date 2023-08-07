package com.example.talktogether.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.talktogether.Model.MessageModel
import com.example.talktogether.R
import com.example.talktogether.databinding.RecieverItemLayoutBinding
import com.example.talktogether.databinding.SentItemLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(var context:Context, var list: ArrayList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var ITEM_SENT=1
    var ITEM_RECEIVE=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType==ITEM_SENT){
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        } else{
            ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.reciever_item_layout,parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid==list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message=list[position]
        val timestamp=
        if(holder.itemViewType==ITEM_SENT){
            val viewHolder=holder as SentViewHolder
            viewHolder.binding.userMsg.text=message.message
            viewHolder.binding.senderDate.text = getFormattedTimestamp(message.timestamp)
        }
        else{
            val viewHolder=holder as ReceiverViewHolder
            viewHolder.binding.userMsg.text=message.message
            viewHolder.binding.receiverDate.text = getFormattedTimestamp(message.timestamp)
        }
    }

    inner class SentViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var binding=SentItemLayoutBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View) :RecyclerView.ViewHolder(view){
        var binding=RecieverItemLayoutBinding.bind(view)
    }

    private fun getFormattedTimestamp(timestamp: Long?): String {
        if (timestamp != null) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return dateFormat.format(Date(timestamp))
        }
        return ""
    }

}