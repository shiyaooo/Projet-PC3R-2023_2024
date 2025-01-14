package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.result.DeleteResult;

import Dao.MovieDao;

/**
 * Servlet implementation class ServletMovies
 */

@WebServlet("/Movies")
public class ServletMovies extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private MovieDao movieDao;
	Gson gson;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMovies() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        movieDao = new MovieDao();
     // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
		String action = request.getParameter("action");

	    if (action == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
	        return;
	    }

	    switch (action) {
	        case "movies":
	            List<Document> allMovies = movieDao.findAllMovies();
	            out.write(allMovies.toString());
	            break;
	        case "moviesbyGenre":
	            String genre = request.getParameter("genre");
	            if (genre == null || genre.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Genre parameter is missing");
	                return;
	            }
	            List<Document> moviesByGenre = movieDao.findMoviesByGenre(genre);
	            out.write(moviesByGenre.toString());
	            break;
	        case "partialname":
	            String partialName = request.getParameter("title");
	            if (partialName == null || partialName.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "PartialName parameter is missing");
	                return;
	            }
	            List<Document> moviesByPartialName = movieDao.findMoviesByPartialName(partialName);
	            out.write(moviesByPartialName.toString());
	            break;
	        case "title":
	            String title = request.getParameter("title");
	            if (title == null || title.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title parameter is missing");
	                return;
	            }
	            List<Document> moviesByTitle = movieDao.findMoviesByTitle(title);
	            out.write(moviesByTitle.toString());
	            break;
	        case "movie":
	            String id = request.getParameter("_id");
	            if (id == null || id.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id parameter is missing");
	                return;
	            }
	            Document movie = movieDao.findMovie(new ObjectId(id));
	            out.write(movie.toJson());
	            break;
	        case "find_id":
	            String id_tmdb = request.getParameter("id_tmdb");
	            if (id_tmdb == null || id_tmdb.isEmpty()) {
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id parameter is missing");
	                return;
	            }
	            int idTmdb = Integer.parseInt(id_tmdb);
	            String objectId = movieDao.findIdbyId_Tmdb(idTmdb);
	            // 返回 objectId 给前端
	            response.setContentType("text/plain");
	            response.getWriter().write(objectId);
	            break;
	        default:
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
	            break;
	    }
    
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lit le corps de la requête 
		 StringBuilder sb = new StringBuilder();
	     String line;
         try (BufferedReader reader = request.getReader()) {
        	 while ((line = reader.readLine()) != null) {
        		 sb.append(line);
        	 }
         }
         
         String movie_tmdb = request.getParameter("id_tmdb");      
         
         try {
        	 if(movie_tmdb !=null) {
        		 movieDao.insertMovietmdb(Integer.parseInt(movie_tmdb));
        		 response.setStatus(HttpServletResponse.SC_CREATED);
	             response.setContentType("application/json");
	             response.setCharacterEncoding("UTF-8");
	             response.getWriter().write("movie in tmdb transfer mon bdd");      		    		
        	 }else {
        		// Convertit le corps de la requête en un objet JSON
             	 JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
             	 
             	 // Convertit l'objet JSON en un document BSON
             	 Document doc = Document.parse(gson.toJson(jsonObject));
	         	
	             movieDao.insertMovie(doc);
	
	             response.setStatus(HttpServletResponse.SC_CREATED);
	             response.setContentType("application/json");
	             response.setCharacterEncoding("UTF-8");
	             response.getWriter().write(gson.toJson(doc));       		 
        	 }
         } catch (Exception e) {
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data.");
             e.printStackTrace();
         }
     }
	
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String movieIdParam = request.getParameter("_id");
        String userId = request.getParameter("userId");
        
        try {
            ObjectId movieId = new ObjectId(movieIdParam);
            if(movieId != null && userId == null) {
	        	movieDao.updateMovie_critique(movieId);
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("Movie's critiques updated successfully"); 
            }else if(movieId != null && userId !=null) {
            	movieDao.updateVote(movieId, userId);
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("Movie's vote updated successfully"); 
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating movie");
            e.printStackTrace();
        }
	}
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String _id = request.getParameter("_id");
		if (_id != null) {
			ObjectId id = new ObjectId(_id);
			
			// Supprimer le document dans MongoDB.
			DeleteResult result = movieDao.deleteMovieById(id);
			response.getWriter().write("delete ok");
			
			if (result.getDeletedCount() == 0) {
	            // Aucun document n'a été supprimé.
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le document à supprimer n'a pas été trouvé.");
	        }
			else {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
		else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Les données fournies sont incomplètes.");
			System.out.println("Pas d'movie supprimé");
		}
	}
	
	@Override
    public void destroy() {
        movieDao.close();
        super.destroy();
    }

}
