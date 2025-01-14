package TmdbtoMongodb;

import org.json.JSONObject;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TMDBtoMongodb {
	private final String apiKey;

	public TMDBtoMongodb(String apiKey) {this.apiKey = apiKey;}
	
	public String getApiKey() {
		return this.apiKey;
	}
	
	public int getTotalMovieCount() {
        try {
            HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/discover/movie?")
                    .queryString("api_key", this.apiKey)
                    .queryString("page", 1) // We only need the first page to get the total count
                    .asString();

            if (response.getStatus() == 200) {
            	String jsonResponse = response.getBody();
                JSONObject movieJson = new JSONObject(jsonResponse);
                return movieJson.getInt("total_results"); // Extract the total number of results
            } else {
                System.out.println("Error: " + response.getStatusText());
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
	
    public int getTotalPersonCount() {
        try {
            HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/person/popular?")
                    .queryString("api_key", apiKey)
                    .queryString("page", 1) // We only need the first page to get the total count
                    .asString();

            if (response.getStatus() == 200) {
            	String jsonResponse = response.getBody();
                JSONObject personJson = new JSONObject(jsonResponse);
                return personJson.getInt("total_results"); // Extract the total number of results
            } else {
                System.out.println("Error: " + response.getStatusText());
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
	public static void main(String[] args) {
		String apiKey = "a5362b38ec62c678acd4737ef5ad21dc"; // Replace with your actual TMDB API key
		TMDBtoMongodb tmdbMovieCount = new TMDBtoMongodb(apiKey);
        int totalMovies = tmdbMovieCount.getTotalMovieCount();
        int totalPersons = tmdbMovieCount.getTotalPersonCount();
        System.out.println("Total number of movies in TMDB: " + totalMovies);
        System.out.println("Total number of persons in TMDB: " + totalPersons);
        
        for (int i=0; i<totalMovies; i++) {
        	TMBDMovies tmdbMovie = new TMBDMovies(i);
        	tmdbMovie.tmdb_movies_to_mongodb(apiKey);
        }
        
        for (int i=0; i<totalPersons; i++) {
        	TMDBPersonnes tmdbPerson = new TMDBPersonnes(i);
        	tmdbPerson.tmdb_personnes_to_mongodb(apiKey);
        }

	}

	
}
