package com.example.monstarmusicnew.view.fragment

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.adapter.MusicAdapter
import com.example.monstarmusicnew.customInterface.IClickItemInList
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.view.activity.HomeActivity
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.fragment_offline.view.*

class OfflineFragment : Fragment(),IClickItemInList,View.OnClickListener {

    private var mMusicViewModel:MusicViewModel?=null
    private lateinit var mAdapter:MusicAdapter
   // private var mMusicService: MusicService? = null
    private lateinit var mConnection: ServiceConnection
    private var mListPlay = mutableListOf<SongOffline>()
    private var isCheckBoundService: Boolean = false
    private var isCheckMusicRunning: Boolean = false
    private var mSongOffline: SongOffline? = null
    private var mPosition: Int = 0
    private var mTimeMusicIsRunning = 0
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var intentFil: IntentFilter
    var startstop = Intent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_offline, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rcy_listOffline.layoutManager=LinearLayoutManager(activity)
        mAdapter= MusicAdapter(this)
        view.rcy_listOffline.adapter=mAdapter
        mMusicViewModel= MusicViewModel()
        //createConnection()
        //startService(startstop)
        requestReadListMusicOffline()
    }
    private fun requestReadListMusicOffline() = if (ContextCompat.checkSelfPermission(
            context as Activity,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    } else {
        getListOff()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getListOff()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun getListOff(){
        mMusicViewModel?.getListMusicOffLine( (context as Activity).contentResolver)
        mMusicViewModel?.listMusicOffline?.observe(this, Observer {
            (view?.rcy_listOffline?.adapter as MusicAdapter).setListMusic(it)
        })
    }
    override fun clickOnItem(songOffline: SongOffline, position: Int) {
        Log.d("o","${songOffline}")
        Log.d("ok","${HomeActivity().mMusicService?.playMusic(songOffline)}")
        (activity as HomeActivity).mMusicService?.playMusic(songOffline)
    }

    override fun onClick(v: View?) {

    }
}