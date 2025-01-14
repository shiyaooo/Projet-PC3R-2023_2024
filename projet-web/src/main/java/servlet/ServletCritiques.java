package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Critique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import Dao.CritiqueDao;

/**
 * Servlet implementation class ServletCritiques
 */

@WebServlet("/Critiques")
public class ServletCritiques extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CritiqueDao critiqueDao;
	Gson gson;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCritiques() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        critiqueDao = new CritiqueDao();
     // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String receverId = request.getParameter("receverId");
		if (receverId == null || receverId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing receverId parameter.");
            return;
        }
		try {
            List<Document> critiques = critiqueDao.findCritiquesByRecever(receverId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(critiques));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving critiques.");
            e.printStackTrace();
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
         	
             critiqueDao.insertCritique(doc);

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
		String critiqueIdParam = request.getParameter("_id");
        //String updatedCritique = request.getParameter("updatedCritique");
        if (critiqueIdParam == null || critiqueIdParam.isEmpty() ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing critiqueId or updatedCritique");
            return;
        }
		//System.out.println(sb);
        try {
        	// Convertit le corps de la requête en un objet JSON
        	JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        	//String _id = jsonObject.get("_id").getAsString();
        	
            ObjectId critiqueId = new ObjectId(critiqueIdParam);
            // Convertit l'objet JSON en un document BSON
            Document doc = Document.parse(gson.toJson(jsonObject));
            
            critiqueDao.updateCritique(critiqueId, doc.getString("updatedCritique"));
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(doc.toJson());   // Renvoie les informations du document en tant que réponse HTTP
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating critique");
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
			DeleteResult result = critiqueDao.deleteCritique(id);
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
			System.out.println("Pas d'critique supprimé");
		}
	}
	
	@Override
    public void destroy() {
		critiqueDao.close();
        super.destroy();
    }

}
