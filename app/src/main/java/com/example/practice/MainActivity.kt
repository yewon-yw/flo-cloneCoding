package com.example.practice

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.practice.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 처음 실행땐 R.style.SplashTheme가 먼저 실행되나 메인 액티비티가 onCreate될 땐 다시 메인 테마로 돌아와야 하기 때문에 작성해줌
        setTheme(R.style.Theme_FLO_CloneCoding)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreference에 저장된 값을 들고올 것이기 때문에 이 값은 이제 필요가 없음
        // val song = Song(binding.mainMiniplayerTitleTv.text.toString(),binding.mainMiniplayerSingerTv.text.toString(),0,60,false,"elec")

        // DB에 더미데이터 넣어주기
        inputDummySongs()

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            // DB 사용 전
//            val intent=Intent(this, SongActivity::class.java)
//            intent.putExtra("title",song.title)
//            intent.putExtra("singer",song.singer)
//            intent.putExtra("second",song.second)
//            intent.putExtra("playTime",song.playTime)
//            intent.putExtra("isPlaying",song.isPlaying)
//            intent.putExtra("music",song.music)
//            startActivity(intent)

            // DB 사용
            // id값만 sharedPreference 값으로 넣어주고 songActivity로 화면 전환
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

            var intent = Intent(this,SongActivity::class.java)
            startActivity(intent)

        }
        initBottomNavigation()

        Log.d("Song",song.title+song.singer)

        // 챌린지 과제
        binding.mainMiniplayerBtn.setOnClickListener {
            setMiniPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setMiniPlayerStatus(false)
        }
        // 여기까지
    }

    // 챌린지 과제
    // 재생까지는 완료
    private fun setMiniPlayerStatus(isPlaying:Boolean){ // song.isPlaying
        song.isPlaying=isPlaying
        if(isPlaying){
            binding.mainPauseBtn.visibility=View.VISIBLE
            binding.mainMiniplayerBtn.visibility=View.GONE
            mediaPlayer?.start()
        }
        else{
            binding.mainMiniplayerBtn.visibility=View.VISIBLE
            binding.mainPauseBtn.visibility=View.GONE
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
        }
    }
    // 여기까지

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

    private fun setMiniPlayer(song:Song){
        binding.mainMiniplayerTitleTv.text=song.title
        binding.mainMiniplayerSingerTv.text=song.singer
        binding.mainMiniplayerProgressSb.progress=(song.second * 100000)/song.playTime

        // 챌린지 과제
        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music) // mediaPlayer에 이 음악 재생할 것임을 알려줌
        // 여기까지
    }

    // onCreate가 아닌 onStart 함수에 작성하는 이유는 액티비티 전환이 될 때 onStart 부터 시작하기 때문
    // onStart -> 사용자에게 액티비티가 보여지기 전에 실행되는 함수
    // onResume -> 화면이 보여지고 난 후 호출되는 함수기 때문에 onStart에서 ui와 관련된 코드를 초기화하는 것이 더 안정적임
    override fun onStart(){
        super.onStart()

        // DB 사용 전
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData",null)
//
//        song = if(songJson==null){
//            // 노래 제목을 내가 elec으로 해놔서 elec으로 써주기 -> 원래는 music_lilac임
//            Song("라일락","아이유(IU)",0,60,false,"music_lilac")
//        } else{
//            gson.fromJson(songJson,Song::class.java) // songJson을 java 객체인 Song 객체로 변환해달라는 의미
//        }

        // DB 사용
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        // songId가 0이라면 sharedPreference로 받아올 값이 없다는 말이기 때문에 디폴드값으로 그냥 db의 첫번째 인덱스 데이터를 받아옴
        val songDB = SongDatabase.getInstance(this)!!
        song = if(songId == 0){
            songDB.songDao().getSong(1)
        }else {
            songDB.songDao().getSong(songId)
        }

        // Log로 받아온 데이터를 출력하고 데이터 렌더링
        Log.d("song ID",song.id.toString())
        setMiniPlayer(song)
    }

    // 챌린지 과제
    override fun onPause() {
        super.onPause()
        setMiniPlayerStatus(false) // 음악 재생 종료
        Log.d("onPause","miniPlayer 실행 도중 앱이 포커스를 잃음") // Log 출력
    }
    // 여기까지

    // 챌린지 과제
    fun playOnMiniPlayer(album:Album){
        binding.mainMiniplayerTitleTv.text=album.title.toString()
        binding.mainMiniplayerSingerTv.text=album.singer.toString()
    }
    // 여기까지

    private fun inputDummySongs(){
        // 현재 songDB에 데이터가 들어있는지 확인하기 위해선(비었는지 확인) songDB의 데이터를 전부 받아와야함
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs() // songDao의 getSongs는 songTable의 모든 데이터를 가져오는 쿼리문으로 리턴되는 값은 Song객체 리스트

        // songs가 비어있다면 더미데이터를 넣어주고, 비어있지 않다면 그대로 리턴
        if(songs.isNotEmpty()) return
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,

                false,
            )
        )
        // 더미 데이터가 잘 들어갔는지 확인하기 위해 한번 더 데이터를 받아와줌
        val _songs = songDB.songDao().getSongs()
        // log를 사용해서 들어간 데이터 확인
        Log.d("DB data",_songs.toString())
    }
}