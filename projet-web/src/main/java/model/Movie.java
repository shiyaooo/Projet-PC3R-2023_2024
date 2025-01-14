package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {
    private int id_tmdb; // 电影ID
    private String title; // 标题
    private int time; // 时长（分钟）
    private Date releaseDate; // 上映日期
    private String image; // 图片
    private List<String> actors; // 演员列表
    private List<String> crew; // 演员列表
    private List<String> genres; // 类型
    private double vote; // 评分
    private int vote_count;
    private String overview; // 概述
    private List<String> countries; // 国家
    private String language; // 语言
    private List<String> critiques; // 评论列表

    // 构造函数
    public Movie() {
    }

    // Getter 和 Setter 方法
    public int getId_tmdb() {
        return id_tmdb;
    }

    public void setId(int id_tmdb) {
        this.id_tmdb = id_tmdb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getActors() {
        return new ArrayList<String>(this.actors);
    }

    public void setActors(List<String> actors) {
        this.actors = new ArrayList<String>(actors);
    }
    
    public List<String> getCrew() {
        return new ArrayList<String>(this.crew);
    }

    public void setCrew(List<String> crew) {
        this.crew = new ArrayList<String>(crew);
    }

    public List<String> getGenres() {
        return new ArrayList<String>(this.genres);
    }

    public void setGenres(List<String> genres) {
        this.genres = new ArrayList<String>(genres);
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public int getVote_count() {
    	return this.vote_count;
    }
    
    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
   
    
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    } 

    public List<String> getCountries() {
        return new ArrayList<String>(this.countries);
    }

    public void setCountries(List<String> countries) {
        this.countries = new ArrayList<String>(countries);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    
    public List<String> getCritiques() {
        return new ArrayList<String>(this.critiques);
    }

    public void setCritiques(List<String> critiques) {
        this.critiques = new ArrayList<String>(critiques);
    }
}
