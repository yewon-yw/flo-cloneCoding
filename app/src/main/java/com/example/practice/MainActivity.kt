package com.example.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 처음 실행땐 R.style.SplashTheme가 먼저 실행되나 메인 액티비티가 onCreate될 땐 다시 메인 테마로 돌아와야 하기 때문에 작성해줌
        setTheme(R.style.Theme_FLO_CloneCoding)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(),binding.mainMiniplayerSingerTv.text.toString(),0,60,false,"elec")

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            startActivity(intent)
        }
        initBottomNavigation()

        Log.d("Song",song.title+song.singer)

        // 챌린지 과제
        binding.mainMiniplayerBtn.setOnClickListener {
            binding.mainPauseBtn.visibility=View.VISIBLE
            binding.mainMiniplayerBtn.visibility=View.GONE
        }
        binding.mainPauseBtn.setOnClickListener {
            binding.mainMiniplayerBtn.visibility=View.VISIBLE
            binding.mainPauseBtn.visibility=View.GONE
        }
        // 여기까지
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

}