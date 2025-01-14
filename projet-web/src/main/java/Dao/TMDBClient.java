package Dao;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class TMDBClient {
    private static final String API_KEY = "a5362b38ec62c678acd4737ef5ad21dc"; // 替换为你的 TMDB API 密钥
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public JsonNode getPopularMovies() throws Exception {
        HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/movie/popular")
                .queryString("api_key", API_KEY)
                .asJson();

        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch popular movies: " + response.getStatusText());
        }

        return response.getBody();
    }

    public JsonNode getRecentMovies() throws Exception {
        HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/movie/now_playing")
                .queryString("api_key", API_KEY)
                .asJson();

        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch recent movies: " + response.getStatusText());
        }

        return response.getBody();
    }
    
    public JsonNode getGenres() throws Exception {
        HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/genre/movie/list")
                .queryString("api_key", API_KEY)
                .asJson();

        if (response.getStatus() != 200) {
            throw new Exception("Failed to fetch recent movies: " + response.getStatusText());
        }

        return response.getBody();
    }
}
