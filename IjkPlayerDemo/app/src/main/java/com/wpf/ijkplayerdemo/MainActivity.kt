package com.wpf.ijkplayerdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
  /*
    fun setIjkPlayer(){
        // 如果是rtsp协议，可以优先用tcp(默认是用udp)
        // 如果是rtsp协议，可以优先用tcp(默认是用udp)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp")
// 设置播放前的探测时间 1,达到首屏秒开效果
// 设置播放前的探测时间 1,达到首屏秒开效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1)
// 设置播放前的最大探测时间 （100未测试是否是最佳值）
// 设置播放前的最大探测时间 （100未测试是否是最佳值）
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L)
// 每处理一个packet之后刷新io上下文
// 每处理一个packet之后刷新io上下文
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L)
// 需要准备好后自动播放
// 需要准备好后自动播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
// 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
// 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0)

        val url: String = mUri.toString()
        if (!TextUtils.isEmpty(url) && url.contains("rtsp://")) {  // 如果 rtmp 的协议， 修改 size 后会没有声音
// 播放前的探测Size，默认是1M, 改小一点会出画面更快
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024) //1024L)
        }
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
// 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
// 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L)
// 跳过帧 ？？
// 跳过帧 ？？
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", 8)
// 视频帧处理不过来的时候丢弃一些帧达到同步的效果
// 视频帧处理不过来的时候丢弃一些帧达到同步的效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)

    }*/
}
