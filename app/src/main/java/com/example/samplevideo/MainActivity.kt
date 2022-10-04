package com.example.samplevideo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.samplevideo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos.add(
            Video(
                "Mountain",
                "https://cdn.pixabay.com/vimeo/338863706/Flowers%20-%2023872.mp4?width=2560&hash=f9d0a7916a128f35606406c51a0340c37a43efe0"
            )
        )

        videos.add(
            Video(
                "Flower",
                "https://cdn.pixabay.com/vimeo/606079270/Buttercup%20-%2088331.mp4?width=3840&hash=2026b64f49d9b9d97487fd744553c9be7bedfcd8"
            )
        )

        videos.add(
            Video(
                "Nature",
                "https://cdn.pixabay.com/vimeo/361827510/Globe%20Flower%20-%2027148.mp4?width=3840&hash=208cf27a1beff1087c9aa8dbde52ae406cf488ba"
            )
        )


        adapter = VideoAdapter(this, videos, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })

        binding.ViewPager2.adapter = adapter

        binding.ViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.ViewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.ViewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}