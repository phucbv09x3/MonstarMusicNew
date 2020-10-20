package com.example.monstarmusicnew.customInterface

import com.example.monstarmusicnew.model.SongOffline

interface IClickItemInList {
    fun clickOnItem(songOffline: SongOffline, position:Int)
}