package model;

import java.util.Date;

public class Vote {

    private String movieId;
  
    private String userId;
    
    private int score;
    
    public Vote() {}
    
    public Vote(String movieId, String userId, int score) {
        this.movieId = movieId;
        this.userId = userId;
        this.score = score;
    }

    

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

 

}
