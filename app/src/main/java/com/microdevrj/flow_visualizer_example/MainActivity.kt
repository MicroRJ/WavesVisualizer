package com.microdevrj.flow_visualizer_example

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import com.microdevrj.wave_visualizer.engine.Wave
import com.microdevrj.wave_visualizer.source.WaveSource
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This class is only a showcase for the visualizer view, the visualizer view will NOT save
 * or restore visualizer state as of now, you must manually every time the app is created,
 * i.e onCreate, call with
 */
class MainActivity : PermissionsActivity(), MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer

    private var wave: Wave = Wave("wave_1")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waveView.renderer.setForegroundColor(resources.getColor(R.color.colorAccent))
        waveView1.renderer.setForegroundColor(resources.getColor(R.color.colorSecondary))

        wave.add(waveView)
//        wave.add(waveView1)
    }

    override fun onPermissionsGranted() {
        initPlayer()
    }

    private fun initPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_song_2)
            mediaPlayer.setOnPreparedListener(this)
        } catch (e: Exception) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPrepared(mP: MediaPlayer?) {
        wave.with(mP!!.audioSessionId)

        fab.setOnClickListener {
            mediaPlayer.toggle()
        }
    }

    private fun MediaPlayer.toggle() {
        if (this.isPlaying)
            this.pause()
        else
            this.start()

        wave.setActive(this.isPlaying)

        fab.setImageResource(if (this.isPlaying) R.drawable.ic_pause_black_24dp else R.drawable.ic_play_arrow_black_24dp)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }


}
