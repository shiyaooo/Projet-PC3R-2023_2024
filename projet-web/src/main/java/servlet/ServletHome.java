package servlet;


import kong.unirest.JsonNode;

import java.io.IOException;

import com.google.gson.Gson;

import Dao.TMDBClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Home")
public class ServletHome extends HttpServlet {
    private TMDBClient tmdbClient;
    Gson gson;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletHome() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    @Override
    public void init() throws ServletException {
        super.init();
        tmdbClient = new TMDBClient();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonNode popularMovies = tmdbClient.getPopularMovies();
            JsonNode recentMovies = tmdbClient.getRecentMovies();

            request.setAttribute("popularMovies", popularMovies.getObject().getJSONArray("results"));
            request.setAttribute("recentMovies", recentMovies.getObject().getJSONArray("results"));

            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching movie data");
            e.printStackTrace();
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
}
