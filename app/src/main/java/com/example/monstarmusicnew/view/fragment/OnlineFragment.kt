package com.example.monstarmusicnew.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.adapter.SongAdapterOnline
import com.example.monstarmusicnew.customInterface.ISongClick
import com.example.monstarmusicnew.model.SongM
import com.example.monstarmusicnew.view.activity.HomeActivity
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_activity.*
import kotlinx.android.synthetic.main.fragment_online.view.*


class OnlineFragment : Fragment(), ISongClick {
    private var mMusicViewModel: MusicViewModel? = null
    private var mAdapter: SongAdapterOnline? = null
    var pr: ProgressDialog? = null
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
        mAdapter = SongAdapterOnline(
            (if ((activity as HomeActivity).mMusicService == null ||
                (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value == null
            )
                mutableListOf<SongM>() else
                (activity as HomeActivity).mMusicService?.getModel()?.listMusicOnline?.value!!),
            this
        )

        view.rcy_listOnline.adapter = mAdapter
        mMusicViewModel = MusicViewModel()
        register()
        (activity as HomeActivity).btn_search.setOnClickListener {

            mMusicViewModel?.searchSong((activity as HomeActivity).edt_text.text.toString())
            mMusicViewModel?.listMusicOnline?.observe(this, Observer {
                (view?.rcy_listOnline?.adapter as SongAdapterOnline).setListMusic(it)
            })
        }


    }

    fun register() {

//        (activity as HomeActivity).mMusicService?.getModel()?.linkGetOnline?.observe(this, Observer {
//            (activity as HomeActivity).mMusicService?.play(it)
//
//        })
        mMusicViewModel?.searchSong("")
        mMusicViewModel?.listMusicOnline?.observe(this, Observer {
            (view?.rcy_listOnline?.adapter as SongAdapterOnline).setListMusic(it)
        })
    }

    override fun clickItemOnline(songM: SongM, position: Int) {
        val m = (activity as HomeActivity)
        mMusicViewModel?.getFullLinkOnline(songM.linkSong.toString())
        mMusicViewModel?.linkGetOnline?.observe(this, androidx.lifecycle.Observer {
            (activity as HomeActivity).tv_nameSingerShow?.text = it.link.toString()
            songM.linkMusic = it.link.toString()
            (activity as HomeActivity)?.mMusicService?.playMusic(songM)
        })

        mMusicViewModel?.getLyricOfMusic(songM.linkSong!!)
        mMusicViewModel?.lyricMusic?.observe(this, androidx.lifecycle.Observer {

        })
    }

}
