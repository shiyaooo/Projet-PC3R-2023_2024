package TmdbtoMongodb;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.bson.Document;
import org.bson.conversions.Bson;
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

public class TMDBExample {
    public static void main(String[] args) {
    	// Example usage
    	TMBDMovies movie = new TMBDMovies(90); // Brad Pitt's TMDB person ID
        movie.tmdb_movies_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
        //movie.updateCastandCrew(90);
        TMDBPersonnes person = new TMDBPersonnes(90); // Brad Pitt's TMDB person ID
        person.tmdb_personnes_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
    }

    
}
