package com.example.practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemSongBinding

// 과제용
class AlbumLockerRVAdapter(private var songList:ArrayList<Album>):RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(album: Album){ }
        fun onRemoveAlbum(position: Int) { }
        fun onAddAlbum(album: Album){ }
    }
    private lateinit var mItemClickListener: AlbumLockerRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: AlbumLockerRVAdapter.MyItemClickListener){
        mItemClickListener=itemClickListener
    }

    fun addItem(album:Album){
        songList.add(Album(album.title.toString(),album.singer.toString(),album.coverImg))
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        songList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemSongBinding= ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(songList[position]) }
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveAlbum(position)
        }
        holder.binding.itemSongPlayerIv.setOnClickListener {
            mItemClickListener.onAddAlbum(songList[position])
        }
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding:ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album:Album){
            binding.itemSongImgIv.setImageResource(album.coverImg!!)
            binding.itemSongTitleTv.text=album.title.toString()
            binding.itemSongSingerTv.text=album.singer.toString()
        }
    }
}