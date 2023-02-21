package com.wpf.mediaplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cxp.learningvideo.media.decoder.AudioDecoder
import com.cxp.learningvideo.media.decoder.VideoDecoder
import com.wpf.mediaplayer.databinding.FragmentFirstBinding
import java.io.File
import java.lang.String
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    /// storage/emulated/0/mvtest.mp4
    //  storage/emulated/0/ffmpeg/play/114215.mp4
//    val path = Environment.getExternalStorageDirectory().absolutePath + "/ffmpeg/play/child.mp4"
    var path = "android.resource://" + context?.getPackageName() + "/" + R.raw.child
    lateinit var videoDecoder: VideoDecoder
    lateinit var audioDecoder: AudioDecoder



    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        path = "android.resource://" + context?.getPackageName() + "/" + R.raw.child
        initStore()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonTest1.setOnClickListener {
            //解码
            initPlayer()
        }
        binding.buttonTest2.setOnClickListener {
            //编码
/*            val path1 = "111"
            val repack = MP4Repack(path1)
            repack.start()*/
        }
        Log.e("wpf","路径：$path")
/*        var file = File(path)
        if (file.exists()){
            initPlayer()
        }else{
            Log.e("wpf","文件不存在")
        }*/
        initPlayer()
    }

    private fun initStore() {
        if (!Environment.isExternalStorageManager()){
            if (SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(String.format("package:%s", context?.getPackageName()))
                    startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(intent, 2296)
                }
            } else {
                //below android 11
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        100
                    )
                }
            }
        }
    }

    private fun initPlayer() {
        val threadPool = Executors.newFixedThreadPool(10)

        videoDecoder = VideoDecoder(path, binding.sfv, null)
        threadPool.execute(videoDecoder)

        audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        videoDecoder.stop()
        audioDecoder.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    Toast.makeText(context, "打开权限成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}