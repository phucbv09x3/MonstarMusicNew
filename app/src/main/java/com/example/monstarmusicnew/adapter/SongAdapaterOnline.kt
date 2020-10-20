package com.example.monstarmusicnew.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.common.Util
import com.example.monstarmusicnew.customInterface.IClickItemInList
import com.example.monstarmusicnew.customInterface.ISongClickOnline
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.model.SongSearchOnline
import com.squareup.picasso.Picasso

class SongAdapaterOnline (  var mList: MutableList<SongSearchOnline>,val onClick: ISongClickOnline)
: RecyclerView.Adapter<SongAdapaterOnline.MusicOnlineHolder>() {
    var positionM= MutableLiveData<Int>()


    class MusicOnlineHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameMusic=itemView.findViewById<TextView>(R.id.tv_nameMusic)
        val nameSinger=itemView.findViewById<TextView>(R.id.tv_nameSinger)
        val img=itemView.findViewById<ImageView>(R.id.image_of_music)
    }
    fun setListMusic( mutableList: MutableList<SongSearchOnline>){
        this.mList=mutableList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapaterOnline.MusicOnlineHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicOnlineHolder(v)
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    override fun onBindViewHolder(holder: MusicOnlineHolder, position: Int) {
        val music=mList[position]
        holder.nameMusic.text=music.songName
        holder.nameSinger.text=music.artistName

        positionM.value=position
        holder.itemView.setOnClickListener {
            onClick?.clickItemOnline(mList[position],holder.adapterPosition)
        }
        Picasso.get().load(mList[position].linkImage).into(holder.img)
    }

}


