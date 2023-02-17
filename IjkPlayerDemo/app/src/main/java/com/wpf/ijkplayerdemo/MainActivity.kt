package com.wpf.ijkplayerdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wpf.ijkplayerdemo.tool.VideoPlayerIJK
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Author: feipeng.wang
 * Time:   2023/2/17
 * Description : 学习自：https://github.com/wangpengfei1992/IjkplayerDemo
 */
class MainActivity : AppCompatActivity() {
    private var mVideoPlayerIJK: VideoPlayerIJK? = null
    private lateinit var mContext:Context
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        mVideoPlayerIJK = findViewById<View>(R.id.mVideoPlayerIJK) as VideoPlayerIJK?

        val playUrlTv:TextView = findViewById<TextView>(R.id.playRtmpUrl) as TextView
        playUrlTv.setOnClickListener {
            //val videoPath = "http://v-cdn.zjol.com.cn/280443.mp4"
            val videoPath = "rtmp://10.1.55.170:1935/live/test"
            mVideoPlayerIJK?.setVideoPath(videoPath)
            mVideoPlayerIJK?.start()

        }
        val playStreamTv:TextView = findViewById<TextView>(R.id.playStream) as TextView
        playStreamTv.setOnClickListener {
            val intent = Intent(mContext,RecordVideoActivity::class.java)
            mContext.startActivity(intent)
        }
    }
    companion object {
        // Used to load the 'native-lib' library on application startup.
/*        init {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        }*/
    }
}
