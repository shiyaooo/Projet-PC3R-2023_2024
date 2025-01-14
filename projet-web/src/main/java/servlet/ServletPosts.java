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

import Dao.PostDao;

/**
 * Servlet implementation class ServletPost
 */

@WebServlet("/Posts")
public class ServletPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PostDao postDao;
	Gson gson;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPosts() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
        super.init();
        postDao = new PostDao();
        // Prépare un objet Gson pour convertir les chaînes en JSON et vice versa
        this.gson = new Gson();
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String authorId = request.getParameter("authorId");
        String postId = request.getParameter("_id");
        
        try {
            if (authorId != null) {
                // Find posts by author ID
                List<Document> posts = postDao.findPostsByAuthor(authorId);
                out.write(posts.toString());
            } else if (postId != null) {
                // Find post by post ID
                ObjectId id = new ObjectId(postId);
                Document post = postDao.findPost(id);
                out.write(post.toJson());
            } else if(authorId==null && postId == null ){
                // Find all posts
                List<Document> posts = postDao.findAllPosts();
                response.setStatus(HttpServletResponse.SC_OK);
    			response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
                //out.write(posts.toString());
    			response.getWriter().write(gson.toJson(posts));
            }
            else {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\":\"Don't find post\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"" + e.getMessage() + "\"}");
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
         	
             postDao.insertPost(doc);
             
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
 		
        String action = request.getParameter("action");
        String postIdParam = request.getParameter("_id");
        String userId = request.getParameter("userId");

        if (postIdParam == null || !ObjectId.isValid(postIdParam)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing postIdParam");
            return;
        }


        try {
        	JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
            ObjectId postId = new ObjectId(postIdParam);

            if ("updateText".equals(action)) {                
                Document doc = Document.parse(gson.toJson(jsonObject));
                
                postDao.updateText(postId, doc.getString("updatedText"));
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
    			response.getWriter().write(doc.toJson());   
            }else if ("updateImage".equals(action)) {
            	Document doc = Document.parse(gson.toJson(jsonObject));
                
                postDao.updateImage(postId, doc.getString("path"));;
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
    			response.getWriter().write(doc.toJson());   
            } else if ("updateComments".equals(action)) {
                postDao.updatecomments(postId);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
    			response.getWriter().write("{\"message\":\"Post comments updated successfully\"}");   
            } else if ("likePost".equals(action)) {
                if (userId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId");
                    return;
                }
                postDao.likePost(postId, userId);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
    			response.getWriter().write("{\"message\":\"Post liked successfully\"}"); 
            } else if ("dislikePost".equals(action)) {
                if (userId == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId");
                    return;
                }
                postDao.dislikePost(postId, userId);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
    			response.setCharacterEncoding("UTF-8");
    			response.getWriter().write("{\"message\":\"Post disliked successfully\"}");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
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
			DeleteResult result = postDao.deletePost(id);
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
			System.out.println("Pas d'post supprimé");
		}
	}
	
	@Override
    public void destroy() {
        postDao.close();
        super.destroy();
    }

	

}
