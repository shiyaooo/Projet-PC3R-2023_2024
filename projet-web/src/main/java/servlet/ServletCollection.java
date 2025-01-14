package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.result.DeleteResult;

import Dao.CollectionDao;

/**
 * Servlet implementation class ServletCollection
 */
@WebServlet("/Collections")
public class ServletCollection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CollectionDao collectionDao;
	Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCollection() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        collectionDao = new CollectionDao();
     // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String collIdParam = request.getParameter("_id");
		String userId = request.getParameter("userId");
		
		try {
			if (collIdParam != null && userId==null) {
				ObjectId collId = new ObjectId(collIdParam);
				Document coll = collectionDao.findCollection(collId);
				
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(gson.toJson(coll));
			}else if(collIdParam == null && userId !=null) {
				List<Document> colls = collectionDao.findCollextionsByAuthor(userId);
				response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(gson.toJson(colls));			
			}else {
				response.getWriter().write("Don't find collection");
			}
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving collections.");
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
         	
         	collectionDao.insertCollection(doc);

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
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String _id = request.getParameter("_id");
		if (_id != null) {
			ObjectId id = new ObjectId(_id);
			
			// Supprimer le document dans MongoDB.
			DeleteResult result = collectionDao.deleteCollection(id);
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
			System.out.println("Pas d'collection supprimé");
		}
	}
	
	@Override
    public void destroy() {
		collectionDao.close();
        super.destroy();
    }

}
