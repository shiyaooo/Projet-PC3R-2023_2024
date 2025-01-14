package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    //private String id; // 帖子ID
    private String authorId; // 作者ID
    private String text; // 文本内容
    private String image; // 图片
    private Date date; // 日期
    private List<String> likes; // 点赞列表
    private List<String> comments; // 评论列表

    // 构造函数
    public Post() {
    }

    // Getter 和 Setter 方法
    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getLikes() {
        return new ArrayList<String>(this.likes);
    }

    public void setLikes(List<String> likes) {
        this.likes = new ArrayList<String>(likes);
    }

    public List<String> getComments() {
        return new ArrayList<String>(this.comments);
    }

    public void setComments(List<String> comments) {
        this.comments = new ArrayList<String>(comments);
    }
}
