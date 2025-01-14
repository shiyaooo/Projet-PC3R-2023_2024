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

import Dao.VoteDao;

/**
 * Servlet implementation class ServletVotes
 */

@WebServlet("/Votes")
public class ServletVotes extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private VoteDao voteDao;
	Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletVotes() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        voteDao = new VoteDao();
        // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
	    PrintWriter out = response.getWriter();
	    
	    // Récupérer les paramètres de la requête
	    String movieId = request.getParameter("movieId");
	    String userId = request.getParameter("userId");
	    String voteIdParam = request.getParameter("_id");
		
	    // Vérifier les paramètres pour déterminer quelle opération effectuer
	    if (movieId != null && userId == null) {
	        // Recherche par ID de film seulement
	        List<Document> votes = voteDao.findVotesByMovie(movieId);
	        out.println(votes); // Envoyer les votes au format JSON
	    } else if (movieId == null && userId != null) {
	        // Recherche par ID d'utilisateur seulement
	        List<Document> votes = voteDao.findVotesByUser(userId);
	        out.println(votes); // Envoyer les votes au format JSON
	    } else if (movieId != null && userId != null) {
	        // Recherche par ID de film et ID d'utilisateur
	        Document vote = voteDao.findVotesByUserandMovie(movieId, userId);
	        out.println(vote); // Envoyer le vote au format JSON
	    } else if (voteIdParam != null) {
	        // Recherche par ID 
	    	ObjectId voteId = new ObjectId(voteIdParam);
	        Document vote = voteDao.findVotesByID(voteId);
	        out.println(vote); // Envoyer le vote au format JSON
	    } else {
	        out.println("Veuillez fournir un ID de film ou un ID d'utilisateur.");
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
         try {
        	 // Convertit le corps de la requête en un objet JSON
         	 JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
         	 
         	 // Convertit l'objet JSON en un document BSON
         	 Document doc = Document.parse(gson.toJson(jsonObject));
         	
             voteDao.insertVote(doc);

             response.setStatus(HttpServletResponse.SC_CREATED);
             response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
             response.getWriter().write(gson.toJson(doc));
         } catch (Exception e) {
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data.");
             e.printStackTrace();
         }
	}
	
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Lit le corps de la requête
		StringBuilder sb = new StringBuilder();
        String line;
		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
		    }
		}
		String voteIdParam = request.getParameter("_id");
        if (voteIdParam == null || voteIdParam.isEmpty() ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing voteID ");
            return;
        }
		//System.out.println(sb);
        try {
        	// Convertit le corps de la requête en un objet JSON
        	JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        	//String _id = jsonObject.get("_id").getAsString();
        	
            ObjectId voteId = new ObjectId(voteIdParam);
            // Convertit l'objet JSON en un document BSON
            Document doc = Document.parse(gson.toJson(jsonObject));
            
            voteDao.updateVote(voteId, doc.getInteger("score"));
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(doc.toJson());   // Renvoie les informations du document en tant que réponse HTTP
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating vote");
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
			DeleteResult result = voteDao.deleteVote(id);
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
			System.out.println("Pas d'vote supprimé");
		}
	}
	
	@Override
    public void destroy() {
		voteDao.close();
        super.destroy();
    }

}
