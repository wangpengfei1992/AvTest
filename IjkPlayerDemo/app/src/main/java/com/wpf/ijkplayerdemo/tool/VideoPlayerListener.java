package com.wpf.ijkplayerdemo.tool;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Author: feipeng.wang
 * Time:   2023/2/16
 * Description : This is description.
 */
public abstract class VideoPlayerListener implements IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}
