package com.example.monstarmusicnew.view.fragment

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.adapter.MusicAdapter
import com.example.monstarmusicnew.adapter.SongAdapaterOnline
import com.example.monstarmusicnew.customInterface.IClickItemInList
import com.example.monstarmusicnew.customInterface.ISongClickOnline
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.model.SongSearchOnline
import com.example.monstarmusicnew.service.MusicService
import com.example.monstarmusicnew.view.activity.HomeActivity
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_activity.*
import kotlinx.android.synthetic.main.fragment_online.view.*


class OnlineFragment : Fragment() ,ISongClickOnline{
    private var mMusicViewModel: MusicViewModel? = null
    private  var mAdapter: SongAdapaterOnline?=null
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
        mAdapter=SongAdapaterOnline( if ((activity as HomeActivity).mMusicService== null ||
            (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value == null
        )
            mutableListOf<SongSearchOnline>() else
            (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value!!,this)

        view.rcy_listOnline.adapter = mAdapter
        mMusicViewModel = MusicViewModel()
        register()
       // createConnection()
        //startService(startstop)
        (activity as HomeActivity).btn_search.setOnClickListener {
            //(activity as HomeActivity).mMusicService?.searchSong("cha toi")
            mMusicViewModel?.searchSong("cha toi")
            mMusicViewModel?.listMusicOnline?.observe(this, Observer {
                (view?.rcy_listOnline?.adapter as SongAdapaterOnline).setListMusic(it)
            })
        }
    }

    fun register(){

//        (activity as HomeActivity).mMusicService?.getModel()?.linkGetOnline?.observe(this, Observer {
//            (activity as HomeActivity).mMusicService?.play(it)
//
//        })
        mMusicViewModel?.searchSong("")
        mMusicViewModel?.listMusicOnline?.observe(this, Observer {
            (view?.rcy_listOnline?.adapter as SongAdapaterOnline).setListMusic(it)
        })
    }
    override fun clickItemOnline(songSearchOnline: SongSearchOnline, position: Int) {

        Log.d("mussic","${ (activity as HomeActivity).mMusicService?.play(songSearchOnline)}")

    }
}
