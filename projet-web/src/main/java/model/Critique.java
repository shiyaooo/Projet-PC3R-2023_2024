package model;

import java.util.Date;

public class Critique {
    //private String id; // 评论ID
    private String receverId; // 电影ID
    private String authorId; // 评论作者ID
    private String text; // 评论文本内容
    private Date commentDate; // 评论日期

    // 构造函数
    public Critique() {
    } 
    
    public Critique(String receverId, String authorId, String text, Date commentDate) {
        this.receverId = receverId;
        this.authorId = authorId;
        this.text = text;
        this.commentDate = commentDate;
    }

    // Getter 和 Setter 方法
    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getReceverId() {
        return receverId;
    }

    public void setReceverId(String receverId) {
        this.receverId = receverId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId; 
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}
