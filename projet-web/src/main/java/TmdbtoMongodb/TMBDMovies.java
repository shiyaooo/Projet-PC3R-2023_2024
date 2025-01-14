package TmdbtoMongodb;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class TMBDMovies {
	private int id;
	public TMBDMovies(int id) {
		this.id = id;
	}
	
	public void tmdb_movies_to_mongodb(String apiKey) {
	        //String apiKey = "a5362b38ec62c678acd4737ef5ad21dc";
	        String movieId = String.valueOf(this.id);  // 例如，电影"Fight Club"的ID

	        try {
	            HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/" + movieId)
	                    .queryString("api_key", apiKey)
	                    .asString();

	            if (response.getStatus() == 200) {
	                String jsonResponse = response.getBody();
	                JSONObject movieJson = new JSONObject(jsonResponse);
	                System.out.println("title: " + movieJson.getString("title"));
	                System.out.println("overview: " + movieJson.getString("overview"));
	                // Step 4: Connect to MongoDB
	                String connectionString = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
	                MongoClient mongoClient = MongoClients.create(connectionString);
	                MongoDatabase database = mongoClient.getDatabase("BDD");
	                MongoCollection<Document> moviesCollection = database.getCollection("movies");
	                
	                
	                // Step 6: Check if the movie already exists in MongoDB
	                Bson filter = Filters.eq("id_tmdb", movieJson.getInt("id"));
	                long count = moviesCollection.countDocuments(filter);
	                System.out.println("count: " + count);
	                                
	                if (count == 0) {
	                // Step 3: Extract specific fields
	                Document doc = new Document()
	                    .append("id_tmdb", movieJson.getInt("id"))
	                    .append("title", movieJson.getString("title"))
	                    .append("time", movieJson.getInt("runtime"))
	                    .append("releaseDate", convertToDate(movieJson.getString("release_date")))
	                    .append("image", movieJson.optString("poster_path",null))
	                    .append("actors", new ArrayList<String>()) 
	                    .append("crew", new ArrayList<String>()) 
	                    .append("genres", extractList(movieJson.getJSONArray("genres")))
	                    .append("vote", movieJson.getDouble("vote_average"))
	                    .append("vote_count", movieJson.getInt("vote_count"))
	                    .append("overview", movieJson.getString("overview"))
	                    .append("countries", extractList(movieJson.getJSONArray("production_countries")))
	                    .append("language", extractList(movieJson.getJSONArray("spoken_languages")).get(0))
	                    .append("critiques", new ArrayList<String>()); // You can add logic to fetch and include critiques if needed
	                       	                
	                    // Movie does not exist, insert the document
	                	update_person(apiKey,movieId);
	                	moviesCollection.insertOne(doc);
	        	    	// Step 4: update cast et crew
	                	updateCastandCrew(moviesCollection,database,this.id);

	                	
	                    System.out.println("Movie Selected data inserted successfully!");
	                } else {
	                    // Movie already exists
	                	//updateCastandCrew(moviesCollection,database,this.id);
	                    System.out.println("Movie already exists in the database.");
	                }

	                // Close MongoDB connection
	                mongoClient.close();

	            } else {
	                System.out.println("Error: " + response.getStatusText());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    // Helper method to convert date string to Date object
	    private static Date convertToDate(String dateString) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            return sdf.parse(dateString);
	        } catch (ParseException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Helper method to extract list of names from JSON array
	    private static List<String> extractList(JSONArray jsonArray) {
	        List<String> list = new ArrayList<>();
	        for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject obj = jsonArray.getJSONObject(i);
	            list.add(obj.getString("name"));
	        }
	        return list;
	    }
	    
	    public void update_person(String apiKey, String movieId){
	    	List<Document> casts = new ArrayList<>();
	        try {
	            HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/" + movieId + "/credits?")	   
	            		.queryString("api_key", apiKey)
	                    .asString();

	            if (response.getStatus() == 200) {
	                String jsonResponse = response.getBody();
	                JSONObject castCreditsJson = new JSONObject(jsonResponse);
	                JSONArray castArray = castCreditsJson.getJSONArray("cast");
	                JSONArray crewArray = castCreditsJson.getJSONArray("crew");

	                for (int i = 0; i < castArray.length(); i++) {
	                    JSONObject castJson = castArray.getJSONObject(i);
	                    int cast_id_tmdb = castJson.getInt("id");
	                    TMDBPersonnes person = new TMDBPersonnes(cast_id_tmdb);
	                    person.tmdb_personnes_to_mongodb(apiKey);
	                            
	                }
	                
	                for (int i = 0; i < crewArray.length(); i++) {
	                    JSONObject crewJson = crewArray.getJSONObject(i);
	                    int crew_id_tmdb = crewJson.getInt("id");
	                    TMDBPersonnes person = new TMDBPersonnes(crew_id_tmdb);
	                    person.tmdb_personnes_to_mongodb(apiKey);
	                            
	                }
	                
	            } else {
	                System.out.println("Error fetching movies: " + response.getStatusText());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void updateCastandCrew(MongoCollection<Document> moviesCollection,MongoDatabase database,int movieId) {
	    	/*String connectionString = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("BDD");
            MongoCollection<Document> moviesCollection = database.getCollection("movies");*/
            MongoCollection<Document> personsCollection = database.getCollection("personnes");

            List<Document> persons = personsCollection.find().into(new ArrayList<>());

            for (Document person : persons) {
                ObjectId personId = person.getObjectId("_id");  // MongoDB ObjectId
                //System.out.println(personId);
                @SuppressWarnings("unchecked")
				List<Document> castMovies = (List<Document>) person.get("cast_movies");
                //System.out.println(castMovies.get(0));
                @SuppressWarnings("unchecked")
				List<Document> crewMovies = (List<Document>) person.get("crew_movies");

                if (castMovies != null) {
                    for (Document castMovie : castMovies) {
                        Bson filter = Filters.eq("id_tmdb", movieId);
                        //System.out.println(filter);
                        Document foundMovie = moviesCollection.find(filter).first();
                        //System.out.println(foundMovie);
                        ObjectId mongoDBId = foundMovie.getObjectId("_id");
                  
                        Bson update = Updates.addToSet("actors", personId.toString());  // Only add person _id
                        //System.out.println(update);
                        moviesCollection.updateOne(Filters.eq("_id", mongoDBId), update);	
                        //System.out.println(moviesCollection.getIndexes());

                    }
                } 

                if (crewMovies != null) {
                    for (Document crewMovie : crewMovies) { 
                        Bson filter = Filters.eq("id_tmdb", movieId);
                        Bson update = Updates.addToSet("crew", personId.toString());  // Only add person _id
                        moviesCollection.updateOne(filter, update);
                    }
                }                  
            }
            //mongoClient.close();
	    }
	    
	   /* public static void main(String[] args) {
	        // Example usage
	    	TMBDMovies movie = new TMBDMovies(90); // Brad Pitt's TMDB person ID
	        movie.tmdb_movies_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
	        //movie.updateCastandCrew(90);
	    }*/
	

}
