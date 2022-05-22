package com.example.practice

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.data.entities.Song
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    // 현재 노래는 songs[nowPos]로 접근할 것이기 때문에 song 변수는 삭제하고 모든 song은 songs[nowPos]로 변경
//    lateinit var song : Song // song 객체
    lateinit var timer : Timer // Timer 객체

    // activity가 소멸될 때 mediaPlayer를 해제시켜줘야 하기 때문에 nullable(mediaPlayer?)로 설정
    private var mediaPlayer: MediaPlayer?=null

    // gson 선언
    private var gson: Gson = Gson()

    // song 데이터를 리스트를 사용해서 관리하기 위해 리스트 선언
    val songs = arrayListOf<Song>()
    // DB도 전역변수로 선언
    lateinit var songDB: SongDatabase
    // 리스트의 인덱스로 데이터를 관리할 예정이라 인덱스용 변수 선언
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) // 바인딩 초기화
        /* inflater는 xml에 표기된 레이아웃들을 메모리에 객체화 시키는 행동으로 이해 */
        setContentView(binding.root) // ctrl + 'root'click 하면 activity_song.xml로 이동

        initPlayList()
        initSong()
        //setPlayer(song)
        initClickListener()

//        if(intent.hasExtra("title")&&intent.hasExtra("singer")){ // intent에 title과 singer의 값이 있는지 확인
//            binding.songMusicTitleTv.text=intent.getStringExtra("title")
//            binding.songSingerNameTv.text=intent.getStringExtra("singer")
//        }

    }
    /* onCreate는 AppCompatActivity라는 클래스 안에 있는 함수를 오버라이딩 */


    private fun initPlayList(){
        // songDB에 있는 데이터를 전부 songs 리스트에 넣어줌
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    // DB 사용 전
//    // song 객체 초기화
//    private fun initSong(){
//        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!, // not null
//                intent.getStringExtra("singer")!!, // not null
//                intent.getIntExtra("second",0),
//                intent.getIntExtra("playTime",0),
//                intent.getBooleanExtra("isPlaying",false),
//                intent.getStringExtra("music")!!
//            )
//        }
//        startTimer()
//    }
    // DB 사용
    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }
    // 반복문을 돌려 songs 리스트 내에 있는 데이터들 중에 매개변수로 받은 id값과 같은 id값을 가진 객체의 인덱스를 리턴
    private fun getPlayingSongPosition(songId:Int): Int {
        for(i in 0 until songs.size){
            if(songId == songs[i].id){
                return i
            }
        }
        return 0
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        // timer 스레드 멈춤
        timer.interrupt()
        // 변경된 nowPos에 대하여 스레드 재실행
        startTimer()

        // mediaPlayer도 새로운 노래를 틀어야 하기 때문에 해제시켜줌
        mediaPlayer?.release()
        mediaPlayer = null

        // mediaPlayer는 setPlayer 함수 내에서 재시작 해주기 때문에 mediaPlayer를 따로 재시작할 필요는 없음
        setPlayer(songs[nowPos])

        return
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike // DB 값을 업데이트 한 것이 아님
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        // 새롭게 렌더링(화면)
        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    // UI변경
    private fun setPlayer(song: Song) {
        // 인텐트로 화면전환만 할 뿐 데이터를 전송하지 않으니 인텐트 대신 song으로 바꿔줌
//        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songMusicTitleTv.text = song.title
//        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songSingerNameTv.text = song.singer

        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music=resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer=MediaPlayer.create(this,music) // mediaPlayer에 이 음악 재생할 것임을 알려줌

        binding.songAlbumIv.setImageResource(song.coverImg!!)

        // 좋아요 버튼
        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    // 노래 재생중 여부에 따라 재생 버튼 이미지 변경
    private fun setPlayerStatus(isPlaying: Boolean) {
        // song의 isPlaying 값과 timer의 isPlaying값을 setPlayerStatus의 isPlaying값으로 변경해줌
        // 노래 재생버튼이 눌렸을때 song,timer의 isPlaying값은 true
        // 노래 퍼즈버튼이 눌렸을때 song,timer의 isPlaying값은 false
        songs[nowPos].isPlaying=isPlaying
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
        timer=Timer(songs[nowPos].playTime,songs[nowPos].isPlaying) // Timer 클래스의 객체 초기화
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
        songs[nowPos].second=((binding.songProgressSb.progress*songs[nowPos].playTime)/100)/1000

        // 내부 저장소에 데이터를 저장할 수 있게 도와줌 -> 어플이 종료되어도 남아있으려면 어딘가에 저장해줘야 하기 때문
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor=sharedPreferences.edit() // 에디터

        // editor.putString("title",song.title) // (key,value) // 비효율적 -> Json 사용
//        val songJson = gson.toJson(songs[nowPos]) // gson이 song 객체를 json 포맷으로 바꿔줌

        // DB사용 후 song 객체 자체를 저장하는 것이 아니라 song id값을 저장해주면 됨
//        editor.putString("songData",songJson)
        editor.putInt("songId",songs[nowPos].id)

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