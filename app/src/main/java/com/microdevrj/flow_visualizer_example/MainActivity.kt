package com.microdevrj.flow_visualizer_example

import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import com.microdevrj.waves_visualizer.Wave
import com.microdevrj.waves_visualizer.factory.BarCustomize
import com.microdevrj.waves_visualizer.factory.BarRenderer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException

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

        customizeWaveViewOne()

        initCustomizableWaveView()

        wave.add(waveView)

        wave.add(waveView1)
    }

    private fun initCustomizableWaveView() {
        val renderer = waveView1.renderer as BarRenderer
        val customize = waveView1.renderer.customize as BarCustomize
        var property = R.id.r1
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            property = checkedId
            seekBar.progress = when (property) {
                R.id.r1 -> customize.width.toInt()
                R.id.r2 -> customize.height.toInt()
                R.id.r3 -> customize.spacing.toInt()
                else -> throw IllegalArgumentException("wtf")
            }
        }
        seekBar.max = 400
        seekBar.progress = customize.width.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (property) {
                    R.id.r1 -> customize.width = progress.toFloat()
                    R.id.r2 -> customize.height = progress.toFloat()
                    R.id.r3 -> customize.spacing = progress.toFloat()
                }
                renderer.updateCustomize()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun customizeWaveViewOne() {
        (waveView.renderer.customize as BarCustomize).apply {
            this.style = Paint.Style.STROKE
            this.color = Color.BLACK
            this.align = BarCustomize.Align.CENTER
            this.spacing = 10f
            this.width = 15f
            this.height = 60f
        }
        waveView.renderer.updateCustomize()
    }

    override fun onPermissionsGranted() {
        initPlayer()
    }

    private fun initPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_song_3)
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
