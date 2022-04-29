package com.example.practice

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment: Fragment() {

    lateinit var binding:FragmentLockerSavedsongBinding
    private var songDatas = ArrayList<Album>() // 과제용

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLockerSavedsongBinding.inflate(inflater,container,false)

        // 과제용
        songDatas.apply{
            add(Album("라일락","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Flu","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Weekend","태연 (Tae Yeon)",R.drawable.img_album_exp6))
            add(Album("Coin","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단 (BTS)",R.drawable.img_album_exp))
            add(Album("봄 안녕 봄","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Celebrity","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("돌림노래 (Feat.DEAN)","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("BBoom BBoom","모모랜드 (MOMOLAND)",R.drawable.img_album_exp5))
            add(Album("Next Level","에스파 (AESPA)",R.drawable.img_album_exp3))
            add(Album("어푸 (Ah puh)","아이유 (IU)",R.drawable.img_album_exp2))
            add(Album("Boy with Luv","방탄소년단(BTS)",R.drawable.img_album_exp4))
            add(Album("소우주","방탄소년단(BTS)",R.drawable.img_album_exp4))
            add(Album("HOME","방탄소년단(BTS)",R.drawable.img_album_exp4))
        }
        val albumlockerRVAdapter=AlbumLockerRVAdapter(songDatas)
        binding.lockerSavedSongRecyclerView.adapter=albumlockerRVAdapter
        binding.lockerSavedSongRecyclerView.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        albumlockerRVAdapter.setMyItemClickListener(object: AlbumLockerRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                Log.d("test","아이템 클릭")
                AlertDialog.Builder(context)
                    .setTitle("앨범 정보")
                    .setMessage("제목: "+album.title.toString()+"\n가수: "+album.singer.toString())
                    .create()
                    .show()
            }
            override fun onRemoveAlbum(position: Int) {
                albumlockerRVAdapter.removeItem(position)
            }
            override fun onAddAlbum(album: Album) {
                albumlockerRVAdapter.addItem(album)
            }
        })
        // 여기까지

        return binding.root
    }
}