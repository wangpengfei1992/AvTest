package com.wpf.ijkplayerdemo.tool

import android.util.Log
import tv.danmaku.ijk.media.player.misc.IAndroidIO
import java.util.concurrent.LinkedBlockingDeque

/**
 *  Author: feipeng.wang
 *  Time:   2023/2/17
 *  Description : 裸流数据处理
 */
class ReadByteIO private constructor(): IAndroidIO {

    companion object {
        private var instance: ReadByteIO? = null
//        var URL_SUFFIX = "recv_data_online"
        var URL_SUFFIX = "test"

        @Synchronized
        fun getInstance(): ReadByteIO { // 单例
            instance?.let {
                return it
            }
            instance = ReadByteIO()
            return instance!!
        }
    }

    private var TAG = ReadByteIO::class.java.simpleName
    private var flvData = LinkedBlockingDeque<Byte>()  // 内存队列，用于缓存获取到的流数据，要实现追帧效果，只需要根据策略丢弃本地缓存的内容即可

    private fun takeFirstWithLen(len : Int): ByteArray {  // 取 byte 数据用于界面渲染
        var byteList = ByteArray(len)
        for (i in 0 until len) {
            byteList[i] = flvData.take()
        }
        return byteList
    }

    @Synchronized
    fun addLast(bytes: ByteArray): Boolean { // 新收到的数据通过该接口，添加到缓存队列的队尾
        var tmpList:List<Byte> = bytes.toList()
        Log.e(TAG, "tmpList size " + tmpList.size)
        return flvData.addAll(tmpList)
    }

    // 如果是播放本地文件，可在此处打开文件流，后续读取文件流即可
    override fun open(url: String?): Int {
        if (url == URL_SUFFIX) {
            return 1 // 打开播放流成功
        }
        return -1 // 打开播放流失败
    }

    override fun read(buffer: ByteArray?, size: Int): Int {
        var tmpBytes = takeFirstWithLen(size) // 阻塞式读取，没有数据不渲染画面
        System.arraycopy(tmpBytes, 0, buffer, 0, size)
        Log.e(TAG, "read size: $size")
        return size
    }

    override fun seek(offset: Long, whence: Int): Long {
        return 0
    }

    override fun close(): Int {
        return 0
    }
}
