package com.wpf.mediaplayer.tool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Author: feipeng.wang
 * Time:   2023/2/16
 * Description : This is description.
 */
public class DecodeActivity extends Activity implements SurfaceHolder.Callback {
    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() + "/test/xx.avi";
    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() + "/test/test.wmv";
    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() +
    // "/netease/cloudmusic/Music/Alborada Del Inka - Vientos suaves.mp3";

    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() +
    // "/DCIM/Camera/VID_20161117_175920.mp4";
    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() + "/test/H264_1080.mp4";
    // private static final String strVideo =
    // Environment.getExternalStorageDirectory() + "/test/H265_1080.mp4";
    private final String strVideo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ffmpeg/play/child.mp4";

    private String TAG = "DecodeActivity";

    private PlayerThread mPlayerThread = null;
    private VideoDecoder videoDecoder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView sv = new SurfaceView(this);
        sv.getHolder().addCallback(this);
        setContentView(sv);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
/*        if (mPlayerThread == null) {
            mPlayerThread = new PlayerThread(holder.getSurface());
            mPlayerThread.setDataSourcePath(strVideo);
            mPlayerThread.start();
        }*/
        if (videoDecoder == null){
            videoDecoder = new VideoDecoder("rtmp://10.1.55.170:1935/live/test",holder.getSurface());
            videoDecoder.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayerThread != null) {
            mPlayerThread.interrupt();
        }
    }

}
