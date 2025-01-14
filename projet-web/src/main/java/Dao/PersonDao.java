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

import TmdbtoMongodb.TMDBPersonnes;
import model.Person;


public class PersonDao {
	
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public PersonDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("personnes"); 
    } 
    
    // 方法：将Document对象中的ObjectId转换为字符串
    private Document convertObjectIdToString(Document doc) {
        if (doc != null && doc.containsKey("_id")) {
            ObjectId objectId = doc.getObjectId("_id");
            doc.put("_id", objectId.toHexString());
        }
        return doc;
    }
    
    // Convert person object to Document
    public Document PersonToDocument(Person person) {
        return new Document("id_tmdb", person.getId_tmdb())
                .append("actorName", person.getActorName())
                .append("actorPhoto", person.getActorPhoto())
                .append("type", person.getType())
                .append("cast_movies", person.getCast_movies())
                .append("crew_movies", person.getCrew_movies())
                ;
    }
    
    // Convert Document to person object
    public Person documentToPerson(Document document) {
    	Person person = new Person();
    	person.setId_tmdb(document.getInteger("id_tmdb"));
    	person.setActorName(document.getString("actorName"));
    	person.setActorPhoto(document.getString("actorPhoto"));
    	person.setType(document.getString("type"));
    	person.setCast_movies(document.getList("cast_movies", Document.class));
    	person.setCrew_movies(document.getList("crew_movies", Document.class));
        return person;
    }
    
    public void insertPersontmdb(int id_tmbd) {
    	// 获取集合
    	TMDBPersonnes person = new TMDBPersonnes(id_tmbd);
        person.tmdb_personnes_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
    }
    
    // Read: Find a person by ID 
    public Document findPerson (ObjectId id) {
    	Bson filter = Filters.eq("_id", id); 	
        Document doc = collection.find(filter).first();
        return convertObjectIdToString(doc);
    }
    
    public List<String> findRolebyPersonandMv (ObjectId id, String movieID) {
    	List<String> roles = new ArrayList<>();
    	Bson filter = Filters.eq("_id", id);     
    	Document doc = collection.find(filter).first();
    	if (doc != null && doc.containsKey("cast_movies")) {
            // 获取 cast_movies 数组

            List<Document> castMovies = doc.getList("cast_movies", Document.class);
        	//System.out.println("cast"+ castMovies);

            // 遍历 cast_movies 数组
            for (Document castMovie : castMovies) {
                // 检查是否为目标电影
            	//System.out.println("a cast"+ castMovie);
            	int id_v=castMovie.getInteger("id_tmdb");
            	//System.out.println(id_v);

                if (id_v == Integer.parseInt(movieID)) {
                    // 获取角色并添加到 roles 列表中
                    roles.add(castMovie.getString("character"));
                }
            }
         }
        return roles;
    }
    
    
    
    public void updatePerson(int id_tmbd) {
    	// 获取集合
    	TMDBPersonnes person = new TMDBPersonnes(id_tmbd);
        person.tmdb_personnes_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
    }
    
    
    
    
    // Delete: Delete a person document by ID
    public DeleteResult deletePerson(ObjectId id) {
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
