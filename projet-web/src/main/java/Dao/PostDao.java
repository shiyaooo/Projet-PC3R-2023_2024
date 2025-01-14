package Dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Collection;
import model.Movie;
import model.Post;

public class PostDao {
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public PostDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("posts"); 
    } 
    
    // Convert post object to Document
    public Document PostToDocument(Post post) {
        return new Document("authorId", post.getAuthorId())
                .append("text", post.getText())
                .append("image", post.getImage())
                .append("date", post.getDate())
                .append("likes", post.getLikes())
                .append("comments", post.getComments())
                ;
    }
    
    // Convert Document to post object
    public Post documentToPost(Document document) {
    	Post post = new Post();
    	post.setAuthorId(document.getString("authorId"));
    	post.setText(document.getString("text"));
    	post.setImage(document.getString("image"));
    	post.setDate(document.getDate("date"));
    	post.setLikes(document.getList("likes", String.class));
    	post.setComments(document.getList("comments", String.class));
        return post;
    }
    
    // Create: Insert a new post document
    public void insertPost(Document coll) {
        //Document critiqueDoc = critiqueToDocument(critique);
        collection.insertOne(coll);
        //System.out.println("Critique inserted successfully!");
    }
    
    // Read: Find post by recever ID
    public List<Document> findPostsByAuthor(String authorId) {
        Bson filter = Filters.eq("authorId", authorId);
        List<Document> posts = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
        	posts.add(doc);
        }
        return posts;
    }
    
    // Read: Find a post by ID 
    public Document findPost (ObjectId id) {
    	Bson filter = Filters.eq("_id", id); 	
        Document doc = collection.find(filter).first();
        return doc;
    }
    
    // Lire tous les posts
    public List<Document> findAllPosts() {
    	/*
        List<Document> posts = new ArrayList<>();
        for (Document doc : collection.find()) {
            posts.add(doc);
        }
        */
    	// Créer un objet de tri pour récupérer les documents les plus récents
        Bson sort = Sorts.descending("date");
    	
    	// Récupérer les posts les plus récents
        List<Document> posts = collection.find().sort(sort).into(new ArrayList<>());
        return posts;
    }
    
    // Update: Update a post document by ID 
    public void updateText(ObjectId postId, String updatedText) {
    	Bson filter = Filters.eq("_id", postId);
        Bson updateOperation = Updates.combine(
                Updates.set("text",updatedText),
                Updates.currentDate("date") // Sets the commentDate to the current date  
                );
        collection.updateOne(filter, updateOperation);
    }
    
    public void updateImage(ObjectId postId, String path) {
    	Bson filter = Filters.eq("_id", postId);
        Bson updateOperation = Updates.combine(
                Updates.set("image",path),
                Updates.currentDate("date") // Sets the commentDate to the current date  
                );
        collection.updateOne(filter, updateOperation);
    }
    
    public void updatecomments(ObjectId postId) {
    	try {
            // 创建更新条件，匹配电影的 ObjectId
            Bson filter = Filters.eq("_id", postId);
            
            MongoCollection<Document> critiquesCollection = database.getCollection("critiques");
            // 创建更新操作，将要更新的字段设置为新的值
            Bson filter_critque = Filters.eq("receverId", postId.toString());
            Document found = critiquesCollection.find(filter_critque).first();
            /*ObjectId critique_Id = found.getObjectId("_id");
            Bson update = Updates.addToSet("critiques", critique_Id.toString()); */
            List<String> critiqueIds = new ArrayList<>();
            for (Document critique : critiquesCollection.find(filter_critque)) {
                critiqueIds.add(critique.getObjectId("_id").toString());
            }
            Bson update = Updates.addEachToSet("comments", critiqueIds);
            // 执行更新操作
            
            UpdateResult updateResult = collection.updateOne(filter, update);
            // 检查更新结果
            if (updateResult.getModifiedCount() == 0) {
                System.out.println("未找到要更新的。");
            } else {
                System.out.println("信息已成功更新。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void likePost(ObjectId postId, String userId) {
        try {
            Bson filter = Filters.eq("_id", postId);
            Bson updateOperation = Updates.addToSet("likes", userId);
            collection.updateOne(filter, updateOperation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void dislikePost(ObjectId postId, String userId) {
        try {
            Bson filter = Filters.eq("_id", postId);
            Bson updateOperation = Updates.pull("likes", userId);
            collection.updateOne(filter, updateOperation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Delete: Delete a post document by ID
    public DeleteResult deletePost(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
        DeleteResult res =collection.deleteOne(filter);
        //System.out.println("Critique deleted successfully!");
        return res;
    }
    
    // Close the MongoDB connection
    public void close() {
        mongoClient.close();
    }

}
