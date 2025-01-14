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
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

import model.Critique;

public class CritiqueDao {
	
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public CritiqueDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("critiques"); 
    } 
    
 // 方法：将Document对象中的ObjectId转换为字符串
    private Document convertObjectIdToString(Document doc) {
        if (doc != null && doc.containsKey("_id")) {
            ObjectId objectId = doc.getObjectId("_id");
            doc.put("_id", objectId.toHexString());
        }
        return doc;
    }
    
    
    // Convert Critique object to Document
    public Document critiqueToDocument(Critique critique) {
        return new Document("receverId", critique.getReceverId())
                .append("authorId", critique.getAuthorId())
                .append("text", critique.getText())
                .append("commentDate", critique.getCommentDate());
    }
    
    // Convert Document to Critique object
    public Critique documentToCritique(Document document) {
        return new Critique(
                document.getString("receverId"),
                document.getString("authorId"),
                document.getString("text"),
                document.getDate("commentDate")
        );
    }
    
    // Create: Insert a new critique document
    public void insertCritique(Document critique) {
        //Document critiqueDoc = critiqueToDocument(critique);
        collection.insertOne(critique);
        //System.out.println("Critique inserted successfully!");
    }
    
    // Read: Find all critiques by recever ID
    public List<Document> findCritiquesByRecever(String receverId) {
        Bson filter = Filters.eq("receverId", receverId);
        List<Document> critiques = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
            critiques.add(convertObjectIdToString(doc));
        }
        return critiques;
    }

    // Update: Update a critique document by ID 
    public void updateCritique(ObjectId critiqueId, String updatedCritique) {
    	Bson filter = Filters.eq("_id", critiqueId);
        Bson updateOperation = Updates.combine(
                Updates.set("text",updatedCritique),
                Updates.currentDate("commentDate") // Sets the commentDate to the current date  
                );
        collection.updateOne(filter, updateOperation);
    }
    
    // Delete: Delete a critique document by ID
    public DeleteResult deleteCritique(ObjectId id) {
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
