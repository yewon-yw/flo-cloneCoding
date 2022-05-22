package com.example.practice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practice.data.entities.Album
import com.example.practice.data.entities.Like

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Insert
    fun likeAlbum(like: Like)

    // 사용자가 앨범을 좋아요 눌렀는지 안눌렀는지 확인해주는 함수 (albumFragment에서 사용)
    // 찾는 앨범이 좋아요가 안눌러져있다면 null 리턴해주기 때문에 null 처리 해줘야함 (safe call 사용(==?))
    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int) : Int?

    // 좋아요 취소
    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikedAlbum(userId: Int, albumId: Int)

    // 보관함에 보여주기 위해 유저를 구분해서 좋아요한 앨범을 가져오는 쿼리문
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int) : List<Album>
}