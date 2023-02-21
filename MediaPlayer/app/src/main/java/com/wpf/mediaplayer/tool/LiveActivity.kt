package com.wpf.mediaplayer.tool

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentFilter
import android.content.res.Configuration
import android.media.AudioFormat
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import com.frank.live.LivePusherNew
import com.frank.live.camera.Camera2Helper
import com.frank.live.camera.CameraType
import com.frank.live.listener.LiveStateChangeListener
import com.frank.live.param.AudioParam
import com.frank.live.param.VideoParam
import com.wpf.mediaplayer.R

/**
 *  Author: feipeng.wang
 *  Time:   2023/2/16
 *  Description : This is description.
 */
class LiveActivity :Activity(){
    private var liveView: View? = null
    private var livePusher: LivePusherNew? = null
    private var isPushing = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
        initView()
        initPusher()
    }

    private fun initView() {
        liveView = findViewById(R.id.surface_camera)
        findViewById<Button>(R.id.btn_swap).setOnClickListener {
            livePusher!!.switchCamera()
        }
        (findViewById<View>(R.id.btn_live) as ToggleButton).setOnCheckedChangeListener { compoundButton, b ->
            run {

                if (b) {
                    livePusher!!.startPush(LIVE_URL, object : LiveStateChangeListener{
                        override fun onError(msg: String?) {
                            Log.e(TAG, "errMsg=$msg")
                        }

                    })
                    isPushing = true
                } else {
                    livePusher!!.stopPush()
                    isPushing = false
                }

            }
        }
        (findViewById<View>(R.id.btn_mute) as ToggleButton).setOnCheckedChangeListener {
                compoundButton, isChecked ->
            run {
                Log.i(TAG, "isChecked=$isChecked")
                livePusher!!.setMute(isChecked)
            }

        }

    }

    private fun initPusher() {
        val width = 640
        val height = 480
        val videoBitRate = 800000 // kb/s
        val videoFrameRate = 10 // fps
        val videoParam = VideoParam(width, height,
            Integer.valueOf(Camera2Helper.CAMERA_ID_BACK), videoBitRate, videoFrameRate)
        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_IN_STEREO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val numChannels = 2
        val audioParam = AudioParam(sampleRate, channelConfig, audioFormat, numChannels)
        // Camera1: SurfaceView  Camera2: TextureView
        livePusher = LivePusherNew(this, videoParam, audioParam, liveView, CameraType.CAMERA2)
        if (liveView is SurfaceView) {
            val holder: SurfaceHolder = (liveView as SurfaceView).holder
            livePusher!!.setPreviewDisplay(holder)
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        if (livePusher != null) {
            if (isPushing) {
                isPushing = false
                livePusher?.stopPush()
            }
            livePusher!!.release()
        }

    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG, "onConfigurationChanged, orientation=" + newConfig.orientation)
    }

    companion object {

        private val TAG = LiveActivity::class.java.simpleName
        private const val LIVE_URL = "rtmp://10.1.55.170:1935/live/test"
        private const val MSG_ERROR = 100
    }
}