package TmdbtoMongodb;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class TMDBPersonnes {
	private int id;
	public TMDBPersonnes(int id) {
		this.id = id;
	}
	
	public void tmdb_personnes_to_mongodb(String apiKey) {
	        //String apiKey = "a5362b38ec62c678acd4737ef5ad21dc";
	        String personId = String.valueOf(this.id);  // 例如，电影"Fight Club"的ID
	        

	        try {
	            HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/person/" + personId)
	                    .queryString("api_key", apiKey)
	                    .asString();
	            
	            HttpResponse<String> response_movies_credit = Unirest.get("https://api.themoviedb.org/3/person/" + personId + "/movie_credits?")
	                    .queryString("api_key", apiKey)
	                    .asString();

	            if (response.getStatus() == 200) {
	                String jsonResponse = response.getBody();
	                JSONObject personJson  = new JSONObject(jsonResponse);
	                
	                // Step 1: Connect to MongoDB
	                String connectionString = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
	                MongoClient mongoClient = MongoClients.create(connectionString);
	                MongoDatabase database = mongoClient.getDatabase("BDD");
	                MongoCollection<Document> collection = database.getCollection("personnes");
	                
	                // Step 2: Check if the movie already exists in MongoDB
	                Bson filter = Filters.eq("id_tmdb", personJson.getInt("id"));
	                long count = collection.countDocuments(filter);
	                //System.out.println("count: " + count);
	                
	                if (count == 0) {
	                	// Step 3: Extract specific fields
	                	Document doc = new Document()
	                			.append("id_tmdb", personJson.getInt("id"))
	                			.append("actorName", personJson.getString("name"))
	                			.append("actorPhoto", personJson.optString("profile_path", null))
	                			.append("type", personJson.optString("known_for_department",null))
	                			.append("cast_movies", getCast_Movies(personId, apiKey,response_movies_credit))
	                			.append("crew_movies", getCrew_Movies(personId, apiKey,response_movies_credit)); // Fetch and include movies
	                
	                    // Movie does not exist, insert the document
	                    collection.insertOne(doc);
	                    //System.out.println("Person Selected data inserted successfully!");
	                } else {
	                    // Movie already exists
	                	if (response.getStatus() == 200) {
	                        String jsonResponse_movies = response_movies_credit.getBody();
	                        JSONObject movieCreditsJson = new JSONObject(jsonResponse_movies);
	                        JSONArray castArray = movieCreditsJson.getJSONArray("cast");
	                        JSONArray crewArray = movieCreditsJson.getJSONArray("crew");
	                        Document personDoc = collection.find(filter).first();
	                        
	                        for (int i = 0; i < castArray.length(); i++) {
	                            JSONObject movieJson = castArray.getJSONObject(i);
	                            updateCastMovies(personDoc, movieJson);       
	                        }
	                        
	                        for (int i = 0; i < crewArray.length(); i++) {
	                            JSONObject movieJson = crewArray.getJSONObject(i);
	                            updateCrewMovies(personDoc, movieJson);                            
	                        }
	                        
	                     // 更新后的文档写回到 MongoDB
	                     collection.updateOne(filter, new Document("$set", personDoc));
	                                           
	                	}
	                    System.out.println("Person already exists in the database.");
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
	
	// Helper method to fetch movies of the person
    private static List<Document> getCast_Movies(String personId, String apiKey, HttpResponse<String> response) {
    	List<Document> movies = new ArrayList<>();
        try {
            /*HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/person/" + personId + "/movie_credits?")
                    .queryString("api_key", apiKey)
                    .asString();*/

            if (response.getStatus() == 200) {
                String jsonResponse = response.getBody();
                JSONObject movieCreditsJson = new JSONObject(jsonResponse);
                JSONArray castArray = movieCreditsJson.getJSONArray("cast");

                for (int i = 0; i < castArray.length(); i++) {
                    JSONObject movieJson = castArray.getJSONObject(i);
                    Document movieDoc = new Document()
                            .append("id_tmdb", movieJson.getInt("id"))
                            .append("title", movieJson.getString("title"))
                            .append("character", movieJson.getString("character"));
                    movies.add(movieDoc);
                }
            } else {
                System.out.println("Error fetching movies: " + response.getStatusText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    private static List<Document> getCrew_Movies(String personId, String apiKey,HttpResponse<String> response) {
    	List<Document> movies = new ArrayList<>();
        try {
            /*HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/person/" + personId + "/movie_credits?")
                    .queryString("api_key", apiKey)
                    .asString();*/

            if (response.getStatus() == 200) {
                String jsonResponse = response.getBody();
                JSONObject movieCreditsJson = new JSONObject(jsonResponse);
                JSONArray crewArray = movieCreditsJson.getJSONArray("crew");

                for (int i = 0; i < crewArray.length(); i++) {
                    JSONObject movieJson = crewArray.getJSONObject(i);
                    Document movieDoc = new Document()
                            .append("id_tmdb", movieJson.getInt("id"))
                            .append("title", movieJson.getString("title"))
                            .append("job", movieJson.getString("job"));
                    movies.add(movieDoc);
                }
            } else {
                System.out.println("Error fetching movies: " + response.getStatusText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    // 更新 person 的 cast 电影列表
    public void updateCastMovies(Document personDoc, JSONObject newMovie) {
        List<Document> castMovies = (List<Document>) personDoc.get("cast_movies");
        if (castMovies == null) {
            castMovies = new ArrayList<>();
        }

        // 检查新电影是否已经在 cast 电影列表中
        boolean isNewMovie = true;
        for (Document movieDoc : castMovies) {
            //JSONObject movieJson = (JSONObject) movieObj;
            if (movieDoc.getInteger("id_tmdb").equals(newMovie.getInt("id"))) {
                isNewMovie = false;
                break;
            }
        }

        // 如果新电影不在列表中，将其添加到列表中
        if (isNewMovie) {
        	Document movieDoc = new Document();
            movieDoc.append("id_tmdb", newMovie.getInt("id"));
            movieDoc.append("title", newMovie.getString("title"));
            movieDoc.append("character", newMovie.getString("character"));
            castMovies.add(movieDoc);
            personDoc.put("cast_movies", castMovies);
        }
    }

    // 更新 person 的 crew 电影列表
    public void updateCrewMovies(Document personDoc, JSONObject newMovie) {
        //JSONArray crewMovies = personDoc.get("crew_movies", JSONArray.class);
        List<Document> crewMovies = (List<Document>) personDoc.get("crew_movies");
        if (crewMovies == null) {
            crewMovies = new ArrayList<>();
        }

        // 检查新电影是否已经在 crew 电影列表中
        boolean isNewMovie = true;
        for (Document movieDoc : crewMovies) {
            if (movieDoc.getInteger("id_tmdb").equals(newMovie.getInt("id"))) {
                isNewMovie = false;
                break;
            }
        }

        // 如果新电影不在列表中，将其添加到列表中
        if (isNewMovie) {
        	Document movieDoc = new Document();
            movieDoc.append("id_tmdb", newMovie.getInt("id"));
            movieDoc.append("title", newMovie.getString("title"));
            movieDoc.append("job", newMovie.getString("job"));
            crewMovies.add(movieDoc);
            personDoc.put("crew_movies", crewMovies);
        }
    }
    
    public static void main(String[] args) {
        // Example usage
    	TMDBPersonnes person = new TMDBPersonnes(90); // Brad Pitt's TMDB person ID
        person.tmdb_personnes_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
    }

}
