package com.microdevrj.flow_visualizer_example

import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import com.microdevrj.waves_visualizer.logic.WaveEngine
import com.microdevrj.waves_visualizer.rendering.BarCustomize
import com.microdevrj.waves_visualizer.default_renderer.BarRenderer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException

/**
 * This class is only a showcase for the visualizer view, the visualizer view will NOT save
 * or restore visualizer state as of now, you must manually every time the app is created,
 * i.e onCreate, call with
 */
class MainActivity : PermissionsActivity(), MediaPlayer.OnPreparedListener {


    //provide your own media player
    private lateinit var mediaPlayer: MediaPlayer

    //handles the Visualizer API
    //handles update/rendering engine
    //handles data parsing | converting from raw data -> display data

    private var waveEngine: WaveEngine =
        WaveEngine(0, 2)

    //permissions granted
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customizeWaveViewOne()

        initDemonstrationWaveView()

        waveEngine.add(0, waveView)

        waveEngine.add(1, waveView1)

    }

    private fun initDemonstrationWaveView() {

        //get renderer from wave view
        val renderer = waveView1.renderer as BarRenderer

        //get renderer customize object
        val customize = renderer.customize

        var property = R.id.r1

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            property = checkedId
            seekBar.progress = when (property) {
                R.id.r1 -> customize.width.toInt()
                R.id.r2 -> customize.height.toInt()
                R.id.r3 -> customize.spacing.toInt()
                else -> throw IllegalArgumentException("what a terrible failure")
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


        rStroke.setOnCheckedChangeListener { _, c ->
            customize.style =
                if (customize.style == Paint.Style.FILL)
                    Paint.Style.STROKE else Paint.Style.FILL

            renderer.updateCustomize()

        }

    }

    private fun customizeWaveViewOne() {
        (waveView.renderer.customize as BarCustomize).apply {
            this.style = Paint.Style.STROKE
            this.color = Color.BLACK
            this.align = BarCustomize.Align.CENTER
            this.spacing = 10f
            this.width = 12f
            this.height = 60f
        }
        waveView.renderer.updateCustomize()
    }


    override fun onPrepared(mP: MediaPlayer?) {
        waveEngine.with(mP!!.audioSessionId)

        fab.setOnClickListener {
            mediaPlayer.toggle()
        }
    }

    private fun MediaPlayer.toggle() {
        if (this.isPlaying)
            this.pause()
        else
            this.start()

        waveEngine.active(this.isPlaying)

        fab.setImageResource(if (this.isPlaying) R.drawable.ic_pause_black_24dp else R.drawable.ic_play_arrow_black_24dp)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

}
