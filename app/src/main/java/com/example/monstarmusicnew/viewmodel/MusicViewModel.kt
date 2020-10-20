package com.example.monstarmusicnew.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monstarmusicnew.customInterface.SongRepository
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.model.SongSearchOnline
import com.example.monstarmusicnew.model.SongLinkOnline
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicViewModel : ViewModel() {
    var listMusicOffline = MutableLiveData<MutableList<SongOffline>>()
    private var mListMusicInViewModel = mutableListOf<SongOffline>()

    var listMusicOnline = MutableLiveData<MutableList<SongSearchOnline>>()
    var linkGetOnline=MutableLiveData<SongSearchOnline>()


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
        call?.enqueue(object : Callback<MutableList<SongSearchOnline>> {
            override fun onResponse(
                call: Call<MutableList<SongSearchOnline>>,
                response: Response<MutableList<SongSearchOnline>>
            ) {
                listMusicOnline.value = response.body()
            }

            override fun onFailure(call: Call<MutableList<SongSearchOnline>>, t: Throwable) {

            }

        })
    }

    fun getFullLinkOnline(link: String) {
        val call=songRepository?.getLinkMusic(link)
        call?.enqueue(object :Callback<SongLinkOnline>{
            override fun onResponse(
                call: Call<SongLinkOnline>,
                response: Response<SongLinkOnline>
            ) {
                for (item in listMusicOnline.value!!){
                    if (item.linkSong!!.equals(link)){
                        item.linkMusic = response?.body()?.link.toString()
                        linkGetOnline.value=item
                    }
                }
            }

            override fun onFailure(call: Call<SongLinkOnline>, t: Throwable) {

            }
        })
    }

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
                val durationMusic = cursor.getString(duration)
                mListMusicInViewModel.add(
                    SongOffline(
                        idd,
                        1,
                        currentTT,
                        currentArtist,
                        uri,
                        durationMusic
                    )
                )
            } while (cursor.moveToNext())
            listMusicOffline.value = mListMusicInViewModel
        }
    }
}