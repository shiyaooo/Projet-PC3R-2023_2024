package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Person {
    private int id_tmdb;
    private String actorName;
    private String actorPhoto;
    private String type;
    private List<Document> cast_movies;
    private List<Document> crew_movies;

    // 构造函数
    public Person() {
    }

    // Getter 和 Setter 方法
    public int getId_tmdb() {
        return id_tmdb;
    }

    public void setId_tmdb(int id_tmdb) {
        this.id_tmdb = id_tmdb;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorPhoto() {
        return actorPhoto;
    }

    public void setActorPhoto(String actorPhoto) {
        this.actorPhoto = actorPhoto;
    }
    
    public String getType() {
    	return this.type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }

    public List<Document> getCast_movies() {
        return new ArrayList<Document>(this.cast_movies);
    }
    
    public void setCast_movies (List<Document> cast_movies) {
        this.cast_movies = new ArrayList<Document>(cast_movies);
    }

    public List<Document> getCrew_movies() {
        return new ArrayList<Document>(this.crew_movies);
    }
    
    public void setCrew_movies (List<Document> crew_movies) {
        this.crew_movies = new ArrayList<Document>(crew_movies);
    }


}
