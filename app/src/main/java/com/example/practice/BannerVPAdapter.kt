package com.example.practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {

    // 프래그먼트 리스트 선언 및 초기화
    private val fragmentlist:ArrayList<Fragment> = ArrayList()

//    // 이 클래스에서 연결된 뷰페이저에 데이터를 전달할 때 데이터를 몇개를 전달 할 것인지 써 주는 함수
//    override fun getItemCount(): Int {
//        // 전달할 데이터는 리스트의 크기만큼이기 때문에 리스트 사이즈를 리턴
//        return fragmentlist.size
//    }

    // 아래의 코드를 풀어쓰면 위의 코드가 됨
    override fun getItemCount(): Int = fragmentlist.size

    // 0 부터 itemCount된 만큼 반환을 해줌
    // 예) getItemCount()된 값이 4라면 0~3 까지 실행
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    // homeFragment에서 framentlist에 fragment를 추가하기 위해 사용
    fun addFragment(fragment:Fragment) {
        fragmentlist.add(fragment)

        // list안에 새로운 값이 추가됐을 때 ViewPager에게 새로운 값이 추가됐음을 알려주는 역할
        // list에 새로운 값이 추가됐으니 ViewPager가 새로운 값도 반영해서 보여달라고 알려주는 코드라고 생각하기
        notifyItemInserted(fragmentlist.size-1)
    }
}