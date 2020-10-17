package com.example.monstarmusicnew.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.monstarmusicnew.model.Music

class MusicViewModel : ViewModel() {
    var listMusic = MutableLiveData<MutableList<Music>>()
    private var mListMusicInViewModel = mutableListOf<Music>()
    fun getListMusicOffLine(contentResolver: ContentResolver) {

        var uriri : Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var cursor=contentResolver.query(uriri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            var urii=cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            var id = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            var title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            var songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            var duration=cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            do {
                val idd = cursor.getString(id)
                val currentTT = cursor.getString(title)
                val currentArtist = cursor.getString(songArtist)
                val uri=cursor.getString(urii)
                val durationMusic=cursor.getString(duration)
                mListMusicInViewModel.add(
                    Music(
                        idd,
                        1,
                        currentTT,
                        currentArtist,
                        uri,
                        durationMusic
                    )
                )
            } while (cursor.moveToNext())
            listMusic.value = mListMusicInViewModel
        }
    }
}