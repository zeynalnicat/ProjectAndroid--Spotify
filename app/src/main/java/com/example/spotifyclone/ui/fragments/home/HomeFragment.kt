package com.example.spotifyclone.ui.fragments.home

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spotifyclone.R
import com.example.spotifyclone.adapters.ArtistAdapter
import com.example.spotifyclone.adapters.AlbumAdapter
import com.example.spotifyclone.databinding.FragmentHomeBinding
import com.example.spotifyclone.model.album.newrelease.Item
import com.example.spotifyclone.model.firebase.Albums
import com.example.spotifyclone.model.firebase.Tracks
import com.example.spotifyclone.model.pseudo_models.Album
import com.example.spotifyclone.resource.Resource
import com.example.spotifyclone.ui.activity.MainActivity
import com.example.spotifyclone.viewmodels.HomeViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setBottom()

        val activity = requireActivity() as MainActivity
        activity.checkVisibility()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNewRelease()
        setTrySomethingElse()
        setTextHeader()
        setPopularAlbums()

        setNavigation()

        homeViewModel.recommended.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    setRecommended(it.data)
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.message ?: "", Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {}


            }
        }

        homeViewModel.setRecommended()

    }

    private fun setBottom() {
        val activity = requireActivity() as? MainActivity
        activity?.setBottomNavigation(true)
    }

    private fun setTextHeader() {
        homeViewModel.setDateText()
        homeViewModel.date.observe(viewLifecycleOwner) {
            binding.txtGood.text = it
        }
    }

    private fun setNewRelease() {
        homeViewModel.getNewRelease()
        homeViewModel.newReleases.observe(viewLifecycleOwner) {
            val adapter =
                AlbumAdapter {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_albumViewFragment,
                        it
                    )
                }

            val albums = it.map { Album(it.images[0].url, it.id, it.name, emptyList()) }
            adapter.submitList(albums)
            binding.recyclerViewNewRelease.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewNewRelease.adapter = adapter

        }
    }

    private fun setTrySomethingElse() {
        homeViewModel.getRoomArtistAlbum(requireContext())
        homeViewModel.artists.observe(viewLifecycleOwner) {
            val adapter =
                ArtistAdapter {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_artistViewFragment,
                        it
                    )
                }
            adapter.submitList(it)
            binding.recyclerTrySomething.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerTrySomething.adapter = adapter

        }
    }

    private fun setNavigation() {
        binding.imgSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun setPopularAlbums() {
        homeViewModel.getPopularAlbums()
        homeViewModel.popularAlbums.observe(viewLifecycleOwner) {
            val album = it.map {
                Item(
                    album_type = it.album_type,
                    artists = it.artists,
                    available_markets = it.available_markets,
                    external_urls = it.external_urls,
                    href = it.href,
                    id = it.id,
                    images = it.images,
                    name = it.name,
                    release_date = it.release_date,
                    release_date_precision = it.release_date_precision,
                    total_tracks = it.total_tracks,
                    type = it.type,
                    uri = it.uri
                )
            }
            val adapter =
                AlbumAdapter {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_albumViewFragment,
                        it
                    )
                }
            val albums = it.map { Album(it.images[0].url, it.id, it.name, emptyList()) }
            adapter.submitList(albums)
            binding.recyclerViewPopular.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewPopular.adapter = adapter
        }
    }

    private fun setRecommended(list: List<Album>) {
        val adapter = AlbumAdapter {
            findNavController().navigate(
                R.id.action_homeFragment_to_albumViewFragment,
                it
            )
        }
        adapter.submitList(list)
        binding.recyclerViewRecommended.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewRecommended.adapter = adapter


    }
}
