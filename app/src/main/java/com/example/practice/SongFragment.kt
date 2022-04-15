package com.example.practice

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSongBinding.inflate(inflater,container,false)
        return binding.root
    }

    // 챌린지 과제
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songMixoffTg.setOnClickListener {
            setMixStatus(true)
        }
        binding.songMixonTg.setOnClickListener {
            setMixStatus(false)
        }
        binding.songLalacLayout.setOnClickListener {
            Toast.makeText(context,"라일락 수록곡 클릭",Toast.LENGTH_SHORT).show()
        }
    }
    private fun setMixStatus(isOn:Boolean){
        if(isOn){
            binding.songMixonTg.visibility=View.VISIBLE
            binding.songMixoffTg.visibility=View.GONE
        }
        else{
            binding.songMixonTg.visibility=View.GONE
            binding.songMixoffTg.visibility=View.VISIBLE
        }
    }
    // 여기까지
}