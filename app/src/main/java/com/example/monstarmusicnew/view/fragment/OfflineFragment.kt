package com.example.monstarmusicnew.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.adapter.SongAdapter
import com.example.monstarmusicnew.customInterface.ISongClick
import com.example.monstarmusicnew.model.SongM
import com.example.monstarmusicnew.view.activity.HomeActivity
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.content_activity.*
import kotlinx.android.synthetic.main.fragment_offline.view.*

class OfflineFragment : Fragment(), ISongClick {

    private var mMusicViewModel: MusicViewModel? = null
    private lateinit var mAdapter: SongAdapter
    var mPosition = 0
    var mListPlay = mutableListOf<SongM>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_offline, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rcy_listOffline.layoutManager = LinearLayoutManager(activity)
        mAdapter = SongAdapter(mutableListOf<SongM>(), this)
        view.rcy_listOffline.adapter = mAdapter
        mMusicViewModel = MusicViewModel()
    }

    override fun clickItemOnline(songM: SongM, position: Int) {
        (activity as HomeActivity).mMusicService?.playMusic(songM)
        (activity as HomeActivity).tv_nameSingerShow?.text = songM.artistName
        (activity as HomeActivity).tv_nameMusicShow?.text = songM.songName
    }


//    private fun requestReadListMusicOffline() = if (ContextCompat.checkSelfPermission(
//            context as Activity,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//        != PackageManager.PERMISSION_GRANTED
//    ) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//            1
//        )
//    } else {
//        getListOff()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getListOff()
//            } else {
//                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//            return
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

//    private fun getListOff() {
//        mMusicViewModel?.getListMusicOffLine((context as Activity).contentResolver)
//        mMusicViewModel?.listMusicOffline?.observe(this, Observer {
//            (view?.rcy_listOffline?.adapter as SongAdapater).setListMusic(it)
//            this.mListPlay = it
//        })
//    }


}