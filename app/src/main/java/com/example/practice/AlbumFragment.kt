package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.practice.data.entities.Album
import com.example.practice.data.entities.Like
import com.example.practice.databinding.FragmentAlbumBinding
import com.example.practice.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment: Fragment() {
    lateinit var binding:FragmentAlbumBinding

    private val information= arrayListOf("수록곡", "상세정보", "영상")
    private val gson: Gson = Gson()

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAlbumBinding.inflate(inflater,container,false)
        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumJson = arguments?.getString("album") // arguments에서 데이터를 꺼냄
        val album=gson.fromJson(albumJson, Album::class.java) // Gson을 사용해서 Json을 Album객체로 바꿔줌
        isLiked=isLikedAlbum(album.id)
        setInit(album) // 받아온 데이터로 위젯 변경하는 함수
        setOnClickListeners(album)

//        binding.songLalacLayout.setOnClickListener{
//            Toast.makeText(activity,"Lilac", Toast.LENGTH_SHORT).show()
//        }
        val albumAdapter = AlbumVPAdapter(this) // 초기화
        binding.albumContentVp.adapter=albumAdapter
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
                tab, position ->
            tab.text =information[position]
        }.attach()
        return binding.root
    }

    private fun setInit(album: Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text=album.title.toString()
        binding.albumSingerNameTv.text=album.singer.toString()
        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt(): Int{
        val spf=activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }
    private fun isLikedAlbum(albumId:Int): Boolean {
        val songDB=SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId,albumId)

        return likeId != null // 유저가 인자로 받은 albumId를 가진 앨범을 좋아요 눌렀으면 null이 아닌 값을 리턴하므로 true를 리턴하게 됨
    }
    private fun likeAlbum(userId:Int, albumId:Int) {
        // 앨범을 좋아요 눌렀을 때 앨범에 대한 정보를 DB에 추가해주기 위함
        val songDB=SongDatabase.getInstance(requireContext())!!
        val like = Like(userId,albumId)

        songDB.albumDao().likeAlbum(like)
    }
    private fun disLikedAlbum(albumId:Int) {
        val songDB=SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId,albumId) // 유저가 좋아요를 해제하면 DB에서 데이터 삭제
    }
    private fun setOnClickListeners(album: Album){
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked){
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            } else{
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }
        }
    }
}