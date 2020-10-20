package com.example.monstarmusicnew.view.activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.model.SongOffline
import com.example.monstarmusicnew.service.MusicService
import com.example.monstarmusicnew.view.fragment.OfflineFragment
import com.example.monstarmusicnew.view.fragment.OnlineFragment
import com.example.monstarmusicnew.viewmodel.MusicViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_activity.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {
    private var mOfflineFragment: OfflineFragment? = null
    var mMusicService: MusicService? = null
    private lateinit var mConnection: ServiceConnection
    private var mListPlay = mutableListOf<SongOffline>()
    private var isCheckBoundService: Boolean = false
    private var isCheckMusicRunning: Boolean = false
    private var mSongOffline: SongOffline? = null
    private var mPosition: Int = 0
    private var mTimeMusicIsRunning = 0
    private lateinit var musicViewModel: MusicViewModel
    private lateinit var intentFil: IntentFilter
    var intentService = Intent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        musicViewModel = MusicViewModel()
        setSupportActionBar(toolbar)
        title = "Music OffLine"
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        navigationView.setNavigationItemSelectedListener(this)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_manager_fragment, OfflineFragment())
            .commit()
        startService()
        createConnection()
        clicksPlayMusic()
    }

    private fun clicksPlayMusic() {
        btn_play.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        btn_previous.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.list_music_of_nav -> {
                title = "Music OffLine"
                Log.d("ad", OfflineFragment().isHidden.toString())
                drawer_layout.closeDrawers()
                supportFragmentManager
                    .beginTransaction()
                    .show(OfflineFragment())
                    .commit()

            }
            R.id.home_music_online_of_nav -> {
                title = "Music Online"
                drawer_layout.closeDrawers()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_manager_fragment,OnlineFragment())
                    .commit()
            }
        }
        return true
    }

    private fun createConnection() {
        mConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                isCheckBoundService = false
            }

            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                mMusicService = (service as MusicService.MyBinder).getService
                isCheckBoundService = true
                //initSeekBar()
                //runSeekBar()
            }
        }
        val intent = Intent()
        intent.setClass(this, MusicService::class.java)
        this.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private fun startService() {
        intentService.setClass(this, MusicService::class.java)
        startService(intentService)
    }

    fun getIndex() {
        (applicationContext as OfflineFragment)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_play -> {
                if (mMusicService?.getMusicManager()?.mMediaPlayer == null) {
                    Toast.makeText(this, "Vui long chon bai hat !", Toast.LENGTH_LONG).show()
                } else {
                    if (!isCheckBoundService) {
                        createConnection()
                    } else {
                        mMusicService?.getMusicManager()?.mMediaPlayer?.let { it ->
                            if (it.isPlaying) {
                                btn_play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                                mMusicService?.let {
                                    mSongOffline?.let { itt ->
                                        it.pauseMusic(itt)
                                    }
                                }
                            } else {
                                btn_play.setImageResource(R.drawable.ic_baseline_pause_24)
                                mMusicService?.let {
                                    mSongOffline?.let { itt ->
                                        it.continuePlayMusic(itt)
                                        //loopMusic()
                                    }
                                }
                            }
                        }
                    }

                }
            }

            R.id.btn_next -> {
                mMusicService?.getMusicManager()?.mMediaPlayer?.let {
                    if (it.isPlaying) {
                        btn_play.setImageResource(R.drawable.ic_baseline_pause_24)
                        if (mPosition < mListPlay.size - 1) {
                            mPosition += 1

                            tv_nameMusicShow.text = mListPlay[mPosition].nameMusic
                            tv_nameSingerShow.text = mListPlay[mPosition].nameSinger
                            mMusicService?.playMusic(mListPlay[mPosition])
                            // loopMusic()
                        } else {
                            Toast.makeText(
                                this@HomeActivity,
                                "Không thể next bài",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@HomeActivity,
                            "Không thể next bài",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            R.id.btn_previous -> {
                mMusicService?.getMusicManager()?.mMediaPlayer?.let {
                    if (it.isPlaying) {
                        if (mListPlay.size > mPosition && mPosition >= 1) {
                            mPosition -= 1
                            btn_play.setImageResource(R.drawable.ic_baseline_pause_24)
                            tv_nameMusicShow.text = mListPlay[mPosition].nameMusic
                            tv_nameSingerShow.text = mListPlay[mPosition].nameSinger
                            mMusicService?.playMusic(mListPlay[mPosition])
                            //loopMusic()
                        } else {
                            Toast.makeText(this, "Không thể back bài", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Không thể back bài", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}