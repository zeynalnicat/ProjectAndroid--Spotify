package com.example.spotifyclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.spotifyclone.databinding.ArtistsListviewBinding
import com.example.spotifyclone.databinding.RecentTracksListviewBinding
import com.example.spotifyclone.databinding.TrackListviewBinding
import com.example.spotifyclone.model.PlayedTracks
import com.example.spotifyclone.model.album.newrelease.Item
import com.example.spotifyclone.model.album.trysomething.Album

class AlbumAdapter(
    private val nav: () -> Unit
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    private val diffUtil = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TrackListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(diffUtil.currentList[position])
    }

    inner class ViewHolder(private val binding: TrackListviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(current: Album) {
            Glide.with(binding.root)
                .load(current.images[0].url)
                .into(binding.imgArtist)

            binding.txtArtistName.text = current.name
            val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            params.rightMargin = 30
            binding.root.layoutParams = params
            itemView.setOnClickListener {
                nav()
            }
        }
    }

    fun submitList(list: List<Album>) {
        diffUtil.submitList(list)
    }

}
