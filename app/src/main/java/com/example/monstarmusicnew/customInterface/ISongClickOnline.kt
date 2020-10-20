package com.example.monstarmusicnew.customInterface

import com.example.monstarmusicnew.model.SongSearchOnline

interface ISongClickOnline {
    fun clickItemOnline(songSearchOnline: SongSearchOnline,position:Int)
}