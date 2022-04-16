package com.example.practice

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var song : Song // song 객체
    lateinit var timer : Timer // Timer 객체

    // activity가 소멸될 때 mediaPlayer를 해제시켜줘야 하기 때문에 nullable(mediaPlayer?)로 설정
    private var mediaPlayer: MediaPlayer?=null

    // gson 선언
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // 바인딩 초기화
        /* inflater는 xml에 표기된 레이아웃들을 메모리에 객체화 시키는 행동으로 이해 */
        setContentView(binding.root) // ctrl + 'root'click 하면 activity_song.xml로 이동

        initSong()
        setPlayer(song)

        if(intent.hasExtra("title")&&intent.hasExtra("singer")){ // intent에 title과 singer의 값이 있는지 확인
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.songSingerNameTv.text=intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

        // 챌린지 과제 반복 재생 이미지 변경 *******************************************
        var repeat:Boolean=false
        binding.songRepeatIv.setOnClickListener {
            if(!repeat){
                binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_random_inactive)// 이미지가 없어서 일단 이걸로
            }
            else{
                binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            }
            repeat=!repeat
        }
        var random:Boolean=false
        binding.songRandomIv.setOnClickListener {
            if(!random){
                binding.songRandomIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            }
            else{
                binding.songRandomIv.setImageResource(R.drawable.nugu_btn_random_inactive)
            }
            random=!random
        }
        var like:Boolean=false
        binding.songLikeIv.setOnClickListener {
            if(!like){
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
                like
            }
            else{
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            }
            like=!like
        }
        // 여기까지 *****************************************************************
    }
    /* onCreate는 AppCompatActivity라는 클래스 안에 있는 함수를 오버라이딩 */

    // song 객체 초기화
    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!, // not null
                intent.getStringExtra("singer")!!, // not null
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    // UI변경
    private fun setPlayer(song:Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music) // mediaPlayer에 이 음악 재생할 것임을 알려줌

        setPlayerStatus(song.isPlaying)
    }

    // 노래 재생중 여부에 따라 재생 버튼 이미지 변경
    private fun setPlayerStatus(isPlaying: Boolean) {
        // song의 isPlaying 값과 timer의 isPlaying값을 setPlayerStatus의 isPlaying값으로 변경해줌
        // 노래 재생버튼이 눌렸을때 song,timer의 isPlaying값은 true
        // 노래 퍼즈버튼이 눌렸을때 song,timer의 isPlaying값은 false
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

            // mediaPlayer가 재생중이 아닐때 pause를 하면 오류발생 위험이 있어 if문 작성
            if(mediaPlayer?.isPlaying==true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer=Timer(song.playTime,song.isPlaying) // Timer 클래스의 객체 초기화
        timer.start() // thread 시작
    }

    // 시간이 지남에 따라 seekbar와 타이머의 Text값을 바꿔줘야 하기 때문에 binding 변수를 사용해야함
    // binding 변수를 사용하기 위해 inner class로 만들어줌
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try{
                while(true){
                    if(second>=playTime){ // 곡의 재생이 끝나면 Timer 종료
                        break
                    }
                    if(isPlaying){ // 노래 재생중이면 Timer 계속 실행
                        sleep(50)
                        mills += 50

                        // 프로그래스 바 변경
                        runOnUiThread{
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt() // *100을 통해퍼센테이지로 나타냄
                        }

                        if(mills % 1000 == 0f){ // 1000ms(1s)가 지나면 노래 재생타이머의 초를 1증가시킴
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e:InterruptedException){
                Log.d("Song","스레드가 죽었습니다. ${e.message}") // 스레드 종료 log 호출
            }
//            while(true){ // 재생하지 않을때도 무한반복하는 while문은 비효율적임 -> 인터럽트 호출 -> try-catch문 작성
//                if(second>=playTime){ // 곡의 재생이 끝나면 Timer 종료
//                    break
//                }
//                if(isPlaying){ // 노래 재생중이면 Timer 계속 실행
//                    sleep(50)
//                    mills += 50
//
//                    // 프로그래스 바 변경
//                    runOnUiThread{
//                        binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
//                    }
//
//                    if(mills % 1000 == 0f){ // 1000ms(1s)가 지나면 노래 재생타이머의 초를 1증가시킴
//                        runOnUiThread {
//                            binding.songStartTimeTv.text =
//                                String.format("%02d:%02d", second / 60, second % 60)
//                        }
//                        second++
//                    }
//                }
//            }
        }
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause(){
        super.onPause()
        setPlayerStatus(false)
        // 1000으로 나눠주는 이유는 현재 계산된 값이 ms 단위인데 song 데이터 객체는 s 단위이기 때문에 나눠줌
        song.second=((binding.songProgressSb.progress*song.playTime)/100)/1000
        // 내부 저장소에 데이터를 저장할 수 있게 도와줌 -> 어플이 종료되어도 남아있으려면 어딘가에 저장해줘야 하기 때문
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor=sharedPreferences.edit() // 에디터
        // editor.putString("title",song.title) // (key,value) // 비효율적 -> Json 사용
        val songJson = gson.toJson(song) // gson이 song 객체를 json 포맷으로 바꿔줌
        editor.putString("songData",songJson)
        editor.apply() // apply까지 해줘야 실제로 저장 작업이 완료됨
    }

    override fun onDestroy() { // 액티비티 종료시
        super.onDestroy()
        // thread interrupt -> 리소스 낭비 방지
        timer.interrupt() // 인터럽트 발생시 Timer 클래스의 try-catch문에 걸려 catch문에 작성한 log가 호출됨
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

}