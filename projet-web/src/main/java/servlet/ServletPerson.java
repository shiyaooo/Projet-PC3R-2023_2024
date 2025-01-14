package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.client.result.DeleteResult;

import Dao.PersonDao;


/**
 * Servlet implementation class ServletPerson
 */
@WebServlet("/Persons")
public class ServletPerson extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PersonDao personDao;
	Gson gson;
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPerson() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        personDao = new PersonDao();
     // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String idParam = request.getParameter("_id");
		String id_mv = request.getParameter("id_mv");

		if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing receverId parameter.");
            return;
        }
		try {
            ObjectId id = new ObjectId(idParam);
            if(id_mv == null ) {
	            Document person = personDao.findPerson(id);
	            	
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(gson.toJson(person));
            }else{
        		System.out.println(id_mv);

            	List<String> roles = personDao.findRolebyPersonandMv(id,id_mv);
        		//System.out.println(roles);

            	response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(roles));
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving persons.");
            e.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     String person_tmdb = request.getParameter("id_tmdb");      	     
	     try {
	    	 if(person_tmdb !=null) {
	    		 personDao.insertPersontmdb(Integer.parseInt(person_tmdb));
	    		 response.setStatus(HttpServletResponse.SC_CREATED);
	             response.setContentType("application/json");
	             response.setCharacterEncoding("UTF-8");
	             response.getWriter().write("person in tmdb transfer mon bdd");      		    		
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
		 String person_tmdb = request.getParameter("id_tmdb");      	     
	     try {
	    	 if(person_tmdb !=null) {
	    		 personDao.updatePerson(Integer.parseInt(person_tmdb));
	    		 response.setStatus(HttpServletResponse.SC_CREATED);
	             response.setContentType("application/json");
	             response.setCharacterEncoding("UTF-8");
	             response.getWriter().write("person in tmdb update mon bdd");      		    		
	    	 }
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
			DeleteResult result = personDao.deletePerson(id);
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
			System.out.println("Pas d'person supprimé");
		}
	}
	
	@Override
    public void destroy() {
		personDao.close();
        super.destroy();
    }

}
