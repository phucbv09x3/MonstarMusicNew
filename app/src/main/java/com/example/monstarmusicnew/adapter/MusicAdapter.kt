package com.example.monstarmusicnew.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.`interface`.ClickItem
import com.example.monstarmusicnew.model.Music

class MusicAdapter
    : RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
    private var mList: MutableList<Music> = mutableListOf()
    private var onClick:ClickItem?=null
    class MusicHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val nameMusic=itemView.findViewById<TextView>(R.id.tv_nameMusic)
        val nameSinger=itemView.findViewById<TextView>(R.id.tv_nameSinger)
    }
    fun setListMusic( mutableList: MutableList<Music>){
        this.mList=mutableList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MusicHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicHolder(v)
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    override fun onBindViewHolder(holder: MusicAdapter.MusicHolder, position: Int) {
        val music=mList[position]
        holder.nameMusic.text=music.nameMusic
        holder.nameSinger.text=music.nameSinger
        holder.itemView.setOnClickListener {
            onClick?.clickOnItem(music,position)
        }
    }
}