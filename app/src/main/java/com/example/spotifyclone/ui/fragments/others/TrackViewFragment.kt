package com.example.spotifyclone.ui.fragments.others

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.spotifyclone.R
import com.example.spotifyclone.databinding.FragmentTrackViewBinding
import com.example.spotifyclone.musicplayer.MusicPlayer
import com.example.spotifyclone.sp.SharedPreference
import com.example.spotifyclone.ui.activity.MainActivity
import java.util.Locale


class TrackViewFragment : Fragment() {
    private lateinit var binding: FragmentTrackViewBinding
    private var music: MediaPlayer? = null
    private var totalTIme = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackViewBinding.inflate(inflater)
        val activity = activity as MainActivity
        activity.setBottomNavigation(false)
        setNavigation()
        setLayout()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val activity = activity as MainActivity
                activity.setBottomNavigation(true)
                activity.setMusicPlayer(true)
                activity.supportFragmentManager.popBackStack()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)
        setMusic()
    }

    private fun setNavigation() {
        binding.imgShrink.setOnClickListener {
            val activity = activity as MainActivity
            activity.setMusicPlayer(true)
            activity.setBottomNavigation(true)
            activity.supportFragmentManager.popBackStack()
        }
    }

    private fun setLayout() {
        val sharedPreference = SharedPreference(requireContext())
        var isPlayed = true
        binding.imgPause.setOnClickListener {
            isPlayed = !isPlayed
            binding.imgPause.setImageResource(if (isPlayed) R.drawable.icon_music_view_pause else R.drawable.icon_music_view_resume)
        }
        val musicName = sharedPreference.getValue("PlayingMusic", "")
        val artistName = sharedPreference.getValue("PlayingMusicArtist", "")
        val musicImg = sharedPreference.getValue("PlayingMusicImg", "")

        binding.txtTrackName.text = musicName
        binding.txtTrackHeader.text = musicName
        binding.txtArtistName.text = artistName
        Glide.with(binding.root)
            .load(musicImg)
            .into(binding.imgTrack)

    }

    private fun setMusic() {
        music = MusicPlayer.getMediaPlayer()
        music?.let {
            totalTIme = it.duration
            binding.txtTimeEnd.text = formatDuration(totalTIme)
            binding.imgPause.setOnClickListener { view ->
                if (it.isPlaying) {
                    it.pause()
                    binding.imgPause.setImageResource(R.drawable.icon_music_view_resume)

                } else {
                    it.start()
                    binding.imgPause.setImageResource(R.drawable.icon_music_view_pause)
                }
            }

            binding.seekBar.postDelayed(object : Runnable {
                override fun run() {
                    binding.txtTimeStart.text = formatDuration(it.currentPosition)
                    binding.seekBar.progress =(it.currentPosition.toFloat() / totalTIme * 100).toInt()
                    binding.seekBar.postDelayed(this, 1000)

                }
            }, 0)

            binding.seekBar.setOnSeekBarChangeListener(

                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekbar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            val position = (progress.toFloat() / seekbar!!.max) * totalTIme
                            it.seekTo(position.toInt())
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                }
            )
        }


    }

    private fun formatDuration(durationInSeconds: Int): String {
        var time = durationInSeconds / 1000
        val minutes = time / 60
        val seconds = time % 60
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        return formattedTime
    }


}