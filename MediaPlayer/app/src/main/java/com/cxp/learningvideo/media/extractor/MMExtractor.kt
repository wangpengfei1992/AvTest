package com.chenlittleping.videoeditor.decoder

import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import com.wpf.mediaplayer.MyApplication
import java.nio.ByteBuffer


/**
 * 音视频分离器
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @since VideoEditor
 * @version VideoEditor
 * @Datetime 2019-09-03
 *
 */

class MMExtractor(path: String?) {

    /**音视频分离器*/
    private var mExtractor: MediaExtractor? = null

    /**音频通道索引*/
    private var mAudioTrack = -1

    /**视频通道索引*/
    private var mVideoTrack = -1

    /**当前帧时间戳*/
    private var mCurSampleTime: Long = 0

    /**当前帧标志*/
    private var mCurSampleFlag: Int = 0

    /**开始解码时间点*/
    private var mStartPos: Long = 0

    init {
        mExtractor = MediaExtractor()
        //        path?.let { mExtractor?.setDataSource(it) }


        var videoPathUri:Uri = Uri.parse(path)
        mExtractor?.setDataSource(MyApplication.application.applicationContext,videoPathUri,null)


        //方式1
/*        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileDescriptor fd = fis.getFD();
            extractor.setDataSource(fd);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //方式2
/*        MediaExtractor extractor = new MediaExtractor();
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.test_audio);
        try {
            extractor.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 获取视频格式参数
     */
    fun getVideoFormat(): MediaFormat? {
        for (i in 0 until mExtractor!!.trackCount) {
            val mediaFormat = mExtractor!!.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime != null) {
                if (mime.startsWith("video/")) {
                    mVideoTrack = i
                    break
                }
            }
        }
        return if (mVideoTrack >= 0)
            mExtractor!!.getTrackFormat(mVideoTrack)
        else null
    }

    /**
     * 获取音频格式参数
     */
    fun getAudioFormat(): MediaFormat? {
        for (i in 0 until mExtractor!!.trackCount) {
            val mediaFormat = mExtractor!!.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime != null) {
                if (mime.startsWith("audio/")) {
                    mAudioTrack = i
                    break
                }
            }
        }
        return if (mAudioTrack >= 0) {
            mExtractor!!.getTrackFormat(mAudioTrack)
        } else null
    }

    /**
     * 读取视频数据
     */
    fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectSourceTrack()
        var readSampleCount = mExtractor!!.readSampleData(byteBuffer, 0)
        if (readSampleCount < 0) {
            return -1
        }
        //记录当前帧的时间戳
        mCurSampleTime = mExtractor!!.sampleTime
        mCurSampleFlag = mExtractor!!.sampleFlags
        //进入下一帧
        mExtractor!!.advance()
        return readSampleCount
    }

    /**
     * 选择通道
     */
    private fun selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor!!.selectTrack(mVideoTrack)
        } else if (mAudioTrack >= 0) {
            mExtractor!!.selectTrack(mAudioTrack)
        }
    }

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     */
    fun seek(pos: Long): Long {
        mExtractor!!.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        return mExtractor!!.sampleTime
    }

    /**
     * 停止读取数据
     */
    fun stop() {
        mExtractor?.release()
        mExtractor = null
    }

    fun getVideoTrack(): Int {
        return mVideoTrack
    }

    fun getAudioTrack(): Int {
        return mAudioTrack
    }

    fun setStartPos(pos: Long) {
        mStartPos = pos
    }

    /**
     * 获取当前帧时间
     */
    fun getCurrentTimestamp(): Long {
        return mCurSampleTime
    }

    fun getSampleFlag(): Int {
        return mCurSampleFlag
    }
}