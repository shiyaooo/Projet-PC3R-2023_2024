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

import model.Vote;

public class VoteDao {
	
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public VoteDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("votes"); 
    } 
    
    // Convert votes object to Document
    public Document VoteToDocument(Vote vote) {
        return new Document("movieId", vote.getMovieId())
                .append("userId", vote.getUserId())
                .append("score", vote.getScore());
    }
    
    // Convert Document to Vote object
    public Vote documentToVote(Document document) {
        return new Vote(
                document.getString("movieId"),
                document.getString("userId"),
                document.getInteger("score")
        );
    }
    
    // Create: Insert a new vote document
    public void insertVote(Document vote) {
        //Document critiqueDoc = critiqueToDocument(critique);
        collection.insertOne(vote);
        //System.out.println("Critique inserted successfully!");
    }
    
    // Read: Find all votes by movie ID
    public List<Document> findVotesByMovie(String movieId) {
        Bson filter = Filters.eq("movieId", movieId);
        List<Document> votes = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
        	votes.add(doc);
        }
        return votes;
    }
    
    // Read: Find all votes by movie ID
    public List<Document> findVotesByUser(String userId) {
        Bson filter = Filters.eq("userId", userId);
        List<Document> votes = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
        	votes.add(doc);
        }
        return votes;
    }
    
    // Read: Find a vote by movie ID and by user ID
    public Document findVotesByUserandMovie (String movieId, String userId) {
    	Bson filter = Filters.and(
                Filters.eq("movieId", movieId),
                Filters.eq("userId", userId)
            );  	
        Document doc = collection.find(filter).first();
        return doc;

    }
    
    // Read: Find a vote by ID 
    public Document findVotesByID (ObjectId voteId) {
    	Bson filter = Filters.eq("_id", voteId); 	
        Document doc = collection.find(filter).first();
        return doc;

    }

    // Update: Update a vote document by movie ID and author ID
    public void updateVote(ObjectId voteId,  int score) {
    	Bson filter = Filters.eq("_id", voteId);
        Bson updateOperation = Updates.set("score",score);              
        collection.updateOne(filter, updateOperation);
    }
    
    // Delete: Delete a vote document by ID
    public DeleteResult deleteVote(ObjectId id) {
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
