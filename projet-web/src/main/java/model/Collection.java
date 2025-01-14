package model;

import java.util.Date;

public class Collection {
    private String movieId; // 电影ID
    private String userId; // 用户ID
    private Date collectionDate; // 收藏日期

    // 构造函数
    public Collection() {}
    
    public Collection(String movieId, String userId, Date collectionDate) {
        this.movieId = movieId;
        this.userId = userId;
        this.collectionDate = collectionDate;
    }


    // Getter 和 Setter 方法
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
}

