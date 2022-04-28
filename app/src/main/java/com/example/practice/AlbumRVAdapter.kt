package com.example.practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemAlbumBinding

class AlbumRVAdapter(private var albumList:ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    // 외부에서 클릭 이벤트를 사용하기 위해서 외부에서 리스너 객체를 넘겨줘야함
    // 외부에서 리스너 객체를 전달받는 함수와 전달받은 리스너 객체를 어댑터에서 사용할 수 있도록 따로 저장할 변수가 필요
    interface MyItemClickListener{ // 인터페이스
        fun onItemClick(){
//            Log.d("test","인터페이스")
        }
    }
    private lateinit var mItemClickListener: MyItemClickListener // 전달받은 리스너 객체를 어댑터에서 사용할 수 있도록 따로 저장할 변수
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){ // 외부에서 리스너 객체를 전달받는 함수 -> 어댑터 외부에있는 homeFragment에서 리스너 객체를 던져줌
        mItemClickListener = itemClickListener
    }

    // 아이템 뷰 객체들에게 데이터를 바인딩 해주기 위해 만든 데이터 리스트를 매개변수로 받아옴
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        // 뷰 홀더를 생성해줘야 할 때 호출
        // 예) 처음에 화면에 보일 몇개의 아이템만 생성한 뒤에 이를 계속 재활용하기 때문에 처음에 몇번 호출되고 맒

        // 아이템 뷰 객체를 생성
        val binding:ItemAlbumBinding= ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        // 아이템 뷰 객체를 재활용하기 위해 뷰홀더에 던져줌
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        // 뷰 홀더에 데이터를 바인딩해줘야 할 때마다 호출
        // 예) 사용자가 화면을 위아래로 스크롤 할 때마다 호출
        // recyclerView 에선 index id를 position 이라고 부름
        holder.bind(albumList[position]) // ViewHolder 클래스의 bind 함수 호출 -> 함수에서 받아온 데이터를 아이템 뷰 객체에 넣어주는 동작 실행

        // 뷰 홀더의 아이템 뷰가 클릭됐을 때 클릭 리스너의 역할을 하는 인터페이스의 함수인 onItemClick 함수 호출
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick() }
    }

    // 데이터 셋 크기를 알려주는 함수 = 받아온 데이터리스트의 크기
    // recyclerView의 마지막이 언제인지 알려줌
    override fun getItemCount(): Int = albumList.size

    // viewHolder class 생성
    // 아이템 뷰 객체들을 재활용하기 위해 담아두는 그릇
    // 매개변수로 아이템 뷰 객체를 받음
    // ViewHolder 클래스 상속받음
    inner class ViewHolder(val binding:ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}