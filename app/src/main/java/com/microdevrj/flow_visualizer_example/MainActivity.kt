package com.microdevrj.flow_visualizer_example

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import com.microdevrj.wave_visualizer.Wave
import com.microdevrj.wave_visualizer.rendering.BarRenderer
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This class is only a showcase for the visualizer view, the visualizer view will NOT save
 * or restore visualizer state as of now, you must manually every time the app is created,
 * i.e onCreate, call with
 */
class MainActivity : PermissionsActivity(), MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer

    private var wave: Wave =
        Wave("wave_1")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waveView.renderer.setForegroundColor(Color.WHITE)
        waveView1.renderer.setForegroundColor(Color.WHITE)
        val barR = waveView.renderer as BarRenderer


        var property = R.id.r1

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            property = checkedId
            when (property) {
                R.id.r1 -> seekBar.progress = barR.barWidth.toInt()
                R.id.r2 -> seekBar.progress = barR.barHeight.toInt()
                R.id.r3 -> seekBar.progress = barR.barSpacing.toInt()
            }
        }


        seekBar.max = 1000
        seekBar.progress = barR.barWidth.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (property) {
                    R.id.r1 -> barR.barWidth = progress.toFloat()
                    R.id.r2 -> barR.barHeight = progress.toFloat()
                    R.id.r3 -> barR.barSpacing = progress.toFloat()
                }
                barR.update()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })



        wave.add(waveView)
        wave.add(waveView1)
    }

    override fun onPermissionsGranted() {
        initPlayer()
    }

    private fun initPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_song_1)
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
