package com.example.monstarmusicnew.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monstarmusicnew.customInterface.SongRepository
import com.example.monstarmusicnew.model.SongLinkOnline
import com.example.monstarmusicnew.model.SongM
import com.example.monstarmusicnew.model.SongSearchOnline
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicViewModel : ViewModel() {
    var listMusicOffline = MutableLiveData<MutableList<SongM>>()
    private var mListMusicInViewModel = mutableListOf<SongM>()

    var listMusicOnline = MutableLiveData<MutableList<SongM>>()
    var linkGetOnline = MutableLiveData<SongM>()


    val songRepository: SongRepository

    init {
        songRepository = Retrofit.Builder()
            .baseUrl("https://songserver.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            ).build()
            .create(SongRepository::class.java)

    }

    fun searchSong(name: String) {
        val call = songRepository?.searchSong(name)
        call?.enqueue(object : Callback<MutableList<SongM>> {
            override fun onResponse(
                call: Call<MutableList<SongM>>,
                response: Response<MutableList<SongM>>
            ) {
                listMusicOnline.value = response.body()
            }

            override fun onFailure(call: Call<MutableList<SongM>>, t: Throwable) {

            }

        })
    }

//    fun getFullLinkOnline(link: String) {
//        val call = songRepository?.getLinkMusic(link)
//        call?.enqueue(object : Callback<SongM> {
//            override fun onResponse(
//                call: Call<SongM>,
//                response: Response<SongM>
//            ) {
//                for (item in listMusicOnline.value!!) {
//                    if (item.linkSong!!.equals(link)) {
//                        item.linkMusic = response?.body()?.link.toString()
//                        linkGetOnline.value = item
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<SongM>, t: Throwable) {
//
//            }
//        })
//    }

    fun getListMusicOffLine(contentResolver: ContentResolver) {
        var uriri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var cursor = contentResolver.query(uriri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            var urii = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            var id = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            var title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            var songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            var duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            do {
                val idd = cursor.getString(id)
                val currentTT = cursor.getString(title)
                val currentArtist = cursor.getString(songArtist)
                val uri = cursor.getString(urii)

                mListMusicInViewModel.add(
                    SongM(
                        idd,
                        "",
                        currentTT,
                        currentArtist,
                        uri,
                        ""
                    )
                )
            } while (cursor.moveToNext())
            listMusicOffline.value = mListMusicInViewModel
        }
    }
}