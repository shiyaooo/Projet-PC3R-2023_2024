package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id; // 事件ID
    private String movieId; // 电影ID
    private String description; // 描述
    private String authorId; // 作者ID
    private List<String> participants; // 参与者列表
    private Date date; // 日期
    private int maxPlace; // 最大参与人数
    private String address; // 地址

    // 构造函数
    /*public Events() {
    }*/

    // Getter 和 Setter 方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public List<String> getParticipants() {
    	return new ArrayList<String>(this.participants); 
    }

    public void setParticipants(List<String> participants) {
    	this.participants = new ArrayList<String>(participants);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMaxPlace() {
        return maxPlace;
    }

    public void setMaxPlace(int maxPlace) {
        this.maxPlace = maxPlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
