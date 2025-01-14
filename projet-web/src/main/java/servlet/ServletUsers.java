package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

/**
 * Servlet implementation class UserServelet
 */

@WebServlet("/Users")
public class ServletUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Gson gson;
	MongoClient mongoClient;
    MongoDatabase database;
	
    /** 
     * @see HttpServlet#HttpServlet()
     */
    public ServletUsers() {
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//PrintWriter pw =response.getWriter();
		//pw.println("<h1>/Users</h1>");
		String _id = request.getParameter("_id");
		
		if (_id!=null) {
			ObjectId id = new ObjectId(_id);
		
			MongoCollection<Document> collection = database.getCollection("users");
		
			Document doc = collection.find(new Document("_id", id)).first();
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(doc.toJson()); // Renvoie les informations du document en tant que réponse HTTP
		}
		else {
			String username = request.getParameter("username");
			
			if (username!=null) {
				//String name = username;
				
				MongoCollection<Document> collection = database.getCollection("users");
			
				Document doc = collection.find(new Document("username", username)).first();
				
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(doc.toJson()); // Renvoie les informations du document en tant que réponse HTTP
			}
			else {
				System.out.println("L'identifiant est null");
				/*
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("{\"error\":\"Document not found\"}");
				*/
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		// créer un utilisateur
		/*
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String mail = request.getParameter("mail");
		String birthday = request.getParameter("birthday");

		// Convertit la date de naissance en un objet Date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date birthdate = null;
		try {
            birthdate = formatter.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		/*
		if (username!=null && password!=null && mail!=null && birthday!=null) {
			Document doc = new Document("username", username)
					.append("password", password)
					.append("mail", mail)
					.append(birthday, birthdate);
			database.getCollection("users").insertOne(doc);
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(doc.toJson()); // Renvoie les informations du document en tant que réponse HTTP
			
			ObjectId id = doc.getObjectId("_id");
			System.out.println("L'_id de l'utilisateur inséré est : " + id.toHexString());
		}
		else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Les données fournies sont incomplètes.");
			System.out.println("Pas de nouveau utilisateur créé");
		}
		*/
		
		// Lit le corps de la requête 
		StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        
        try {
            String jsonString = sb.toString();
            
            JSONObject jsonObject2 = new JSONObject(jsonString);
        	String username = jsonObject2.getString("username");
        	
        	if (database.getCollection("users").find(new Document("username", username)).first()!=null) {
        		response.sendError(HttpServletResponse.SC_CONFLICT, "username déjà utilisé");
				System.out.println("username déjà utilisé");
        	}
        	else {
        		// Convertit le corps de la requête en un objet JSON
        		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        		
        		// Convertit l'objet JSON en un document BSON
        		Document doc = Document.parse(gson.toJson(jsonObject));
        		
        		// Insère le document dans la collection
        		database.getCollection("users").insertOne(doc);
        		
        		doc = database.getCollection("users").find(new Document("username", username)).first();
        		
        		response.setStatus(HttpServletResponse.SC_OK);
        		response.setContentType("application/json");
        		response.setCharacterEncoding("UTF-8");
        		response.getWriter().write(doc.toJson()); // Renvoie les informations du document en tant que réponse HTTP
        	}
        } catch (Exception e) {
            // Gère le cas où le corps de la requête n'est pas un JSON valide
        	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Les données fournies sont incomplètes.");
			System.out.println("Pas de nouveau utilisateur créé");
        }
        
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Modifie un utilisateur
		
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
			
			// Récupère l'ID du document à remplacer
	        String _id = jsonObject.get("_id").getAsString();
	        //System.out.println(_id);
	        ObjectId id = new ObjectId(_id);
	        String username = jsonObject.get("username").getAsString();
	        String oldusername = database.getCollection("users").find(new Document("_id", id)).first().getString("username");        	
	        
	        if (!username.equals(oldusername) && database.getCollection("users").find(new Document("username", username)).first()!=null) {
        		response.sendError(HttpServletResponse.SC_CONFLICT, "username déjà utilisé");
				System.out.println("username déjà utilisé");
        	}
        	else {
        		// Supprime l'ID du document JSON, car il ne doit pas être inclus dans le document de remplacement
        		jsonObject.remove("_id");
        		
        		// Convertit l'objet JSON en un document BSON
        		Document doc = Document.parse(gson.toJson(jsonObject));
        		
        		// Remplace le document dans la collection
        		database.getCollection("users").replaceOne(new Document("_id", id), doc);
        		
        		doc = database.getCollection("users").find(new Document("_id", id)).first();
        		
        		response.setStatus(HttpServletResponse.SC_OK);
        		response.setContentType("application/json");
        		response.setCharacterEncoding("UTF-8");
        		response.getWriter().write(doc.toJson()); // Renvoie les informations du document en tant que réponse HTTP
			}
		} catch (Exception e) {
			// Gère le cas où le corps de la requête n'est pas un JSON valide
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Les données fournies sont incomplètes.");
			System.out.println("Pas de nouveau utilisateur modifié");
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// permet de supprimer les informations d'un utilisateur
		// L'_id du document à supprimer.
		String _id = request.getParameter("_id");
		
		if (_id != null) { 
			ObjectId id = new ObjectId(_id);
			
			// Supprimer le document dans MongoDB.
			DeleteResult result = database.getCollection("users").deleteOne(Filters.eq("_id", id));
			
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
			System.out.println("Pas d'utilisateur supprimé");
		}
	    
	}
	
}
