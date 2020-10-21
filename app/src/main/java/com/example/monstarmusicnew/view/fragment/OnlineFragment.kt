package com.example.monstarmusicnew.view.fragment

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.adapter.SongAdapter
import com.example.monstarmusicnew.customInterface.ISongClick
import com.example.monstarmusicnew.model.SongM
import com.example.monstarmusicnew.view.activity.HomeActivity
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_activity.*
import kotlinx.android.synthetic.main.fragment_online.view.*


class OnlineFragment : Fragment() ,ISongClick{
    private var mMusicViewModel: MusicViewModel? = null
    private  var mAdapter: SongAdapter?=null
    var startstop = Intent()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_online, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rcy_listOnline.layoutManager = LinearLayoutManager(activity)
        mAdapter=SongAdapter(
            (if ((activity as HomeActivity).mMusicService== null ||
                (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value == null
            )
                mutableListOf<SongM>() else
                (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value!!) as MutableList<SongM>,this)
        view.rcy_listOnline.adapter = mAdapter
        mMusicViewModel = MusicViewModel()
        register()

    }

    fun register(){

//        (activity as HomeActivity).mMusicService?.getModel()?.linkGetOnline?.observe(this, Observer {
//            (activity as HomeActivity).mMusicService?.play(it)
//
//        })
        mMusicViewModel?.searchSong("")
        mMusicViewModel?.listMusicOnline?.observe(this, Observer {
            (view?.rcy_listOnline?.adapter as SongAdapter).setListMusic(it)
        })
    }

    override fun clickItemOnline(songM: SongM, position: Int) {
        (activity as HomeActivity).mMusicService?.playMusic(songM)
        (activity as HomeActivity).tv_nameSingerShow?.text=songM.artistName
        (activity as HomeActivity).tv_nameMusicShow?.text=songM.songName
    }

}
