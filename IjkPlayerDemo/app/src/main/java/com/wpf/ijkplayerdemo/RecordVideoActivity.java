package com.wpf.ijkplayerdemo;

import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wpf.ijkplayerdemo.tool.ReadByteIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Author: feipeng.wang
 * Time:   2023/2/17
 * Description : This is description.
 */
public class RecordVideoActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private String TAG = RecordVideoActivity.class.getSimpleName();
    private IjkMediaPlayer player;
    private Surface surface;
    private TextureView playView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        playView = findViewById(R.id.v_play);
        playView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (surface != null) {
            this.surface = new Surface(surface);
            play();  // 存在 surface 实例再做播放
        }
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {return false; }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) { }

    private void play() {
        player = new IjkMediaPlayer();
        player.reset();
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 25 * 1024);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "threads", 1);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "sync-av-start", 0);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec",1);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "protocol_whitelist", "ijkio,crypto,file,http,https,tcp,tls,udp,rtmp,rtsp"); // 属性设置支持，转入我们自定义的播放类

        player.setSurface(this.surface);
        player.setAndroidIOCallback(ReadByteIO.Companion.getInstance());

//        Uri uri = Uri.parse("ijkio:androidio:" + ReadByteIO.Companion.getURL_SUFFIX()); // 设定我们自定义的 url
        Uri uri = Uri.parse("rtmp://10.1.55.170:1935/live/" + ReadByteIO.Companion.getURL_SUFFIX()); // 设定我们自定义的 url
        try {
            player.setDataSource(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
        player.start();
        ReadByteIO.Companion.getInstance().setCallBack(new Function1<LinkedBlockingDeque<Byte>, Unit>() {
            @Override
            public Unit invoke(LinkedBlockingDeque<Byte> bytes) {
                readStringFromAssets(bytes);
                return null;
            }
        });
    }

    private void readStringFromAssets(LinkedBlockingDeque<Byte> byteDeque) {
        //通过设备管理对象 获取Asset的资源路径
        AssetManager assetManager = this.getApplicationContext().getAssets();

        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            inputStream = assetManager.open("child.h264");
            isr = new InputStreamReader(inputStream);
//            br = new BufferedReader(isr);
            byte[] bytes = new byte[1024];

            while(inputStream.read(bytes) > 0){
                for (int t = 0; t < bytes.length; t++) {
                    byteDeque.addLast(bytes[t]);
                }
//                bos.write(bytes,0,bytes.lenght);
            }

//            sb.append(br.readLine());
//            String line = null;
//            while((line = br.readLine()) != null){
//                sb.append("\n" + line);
//            }

            br.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return sb.toString();
    }

}

