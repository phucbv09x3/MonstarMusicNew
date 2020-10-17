package com.example.monstarmusicnew.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.`interface`.ClickItem
import com.example.monstarmusicnew.activity.HomeActivity
import com.example.monstarmusicnew.adapter.MusicAdapter
import com.example.monstarmusicnew.model.Music
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import kotlinx.android.synthetic.main.fragment_offline.view.*

class OfflineFragment : Fragment() {
    private var mMusicViewModel:MusicViewModel?=null
    private var adapter:MusicAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_offline, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rcy_listOffline.layoutManager=LinearLayoutManager(activity)
        adapter= MusicAdapter()
        mMusicViewModel= MusicViewModel()
        view.rcy_listOffline.adapter=adapter

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
                //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun getListOff(){
        mMusicViewModel?.getListMusicOffLine(contentResolver =(context as Activity).contentResolver)
        mMusicViewModel?.listMusic?.observe(this, Observer {
            (view?.rcy_listOffline?.adapter as MusicAdapter).setListMusic(it)
        })
    }


}