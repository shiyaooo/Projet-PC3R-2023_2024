package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class ServletEvent
 */

@WebServlet("/Events")
public class ServletEvents extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet() 
     */
    public ServletEvents() {
        super();
    }
    
    Gson gson;
	MongoClient mongoClient;
    MongoDatabase database;
    
    public void init() {
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
		PrintWriter pw =response.getWriter();
		pw.println("<h1>/Events</h1>");
		
		String identifiant = request.getParameter("id");
		if (identifiant != null) {
			// TODO : afficher les identifiant de l'event identifié par l'id
			
		}
		else {
			System.out.println("L'identifiant est null");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		// créer l'event avec le film, la description, l'auteur et une liste vide de participant
		// user à récupérer
		
		String description = request.getParameter("description");
		// author à récupérer
		
		// date ? etc
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
