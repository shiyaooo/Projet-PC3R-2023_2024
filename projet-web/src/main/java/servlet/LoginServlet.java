package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.bson.Document;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class LoginServelet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Gson gson;
	MongoClient mongoClient;
    MongoDatabase database;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }
    
    public void init() {
    	//"mongodb://localhost:27017"
    	String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        
        // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    } 
	
	public void destroy() {
        mongoClient.close();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
        String password = request.getParameter("password");
		
        if (login!=null && password!=null) {
	        Document doc = database.getCollection("users").find(new Document("username", login)).first();
			
	        if (doc==null) {
	        	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le login n'existe pas.");
				System.out.println("login inexistant");
	        }
	        else {
	        	String jsonStr = doc.toJson();
	        	JSONObject json = new JSONObject(jsonStr);
	        	String mdp = json.getString("password");
	        	
	        	if (mdp.equals(password)) {
	        		response.setStatus(HttpServletResponse.SC_OK);
	        		response.setContentType("application/json");
	    			response.setCharacterEncoding("UTF-8");
	    			response.getWriter().write(doc.toJson());
	        		System.out.println("mot de passe valide");
	        	}
	        	else {
	        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Le mot de passe est incorrect.");
	        		response.setContentType("application/json");
	    			response.setCharacterEncoding("UTF-8");
	    			response.getWriter().write((new Document()).toJson());
	        		System.out.println("mot de passe invalide");
	        	}
	        }
        }
        else {
        	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "erreur");
			System.out.println("erreur");
        }
	}

}
