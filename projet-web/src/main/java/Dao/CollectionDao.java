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
import com.mongodb.client.result.DeleteResult;

import model.Collection;


public class CollectionDao {
	
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public CollectionDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("collections"); 
    } 
    
    // Convert collection object to Document
    public Document CollectionToDocument(Collection coll) {
        return new Document("movieId", coll.getMovieId())
                .append("userId", coll.getUserId())
                .append("collectionDate", coll.getCollectionDate());
    }
    
    // Convert Document to collection object
    public Collection documentToCollection(Document document) {
        return new Collection(
                document.getString("movieId"),
                document.getString("userId"),
                document.getDate("collectionDate")
        );
    }
    
    // Create: Insert a new collection document
    public void insertCollection(Document coll) {
        collection.insertOne(coll);
        //System.out.println("Critique inserted successfully!");
    }
    
    // Read: Find a collection by ID 
    public Document findCollection (ObjectId id) {
    	Bson filter = Filters.eq("_id", id); 	
        Document doc = collection.find(filter).first();
        return doc;
    }
    
    // Read: Find all collection by user ID
    public List<Document> findCollextionsByAuthor(String userId) {
        Bson filter = Filters.eq("userId", userId);
        List<Document> colls = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
        	colls.add(doc);
        }
        return colls;
    }
    
    // Delete: Delete a collection document by ID
    public DeleteResult deleteCollection(ObjectId id) {
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
