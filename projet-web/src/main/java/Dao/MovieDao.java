package Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection; 	
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import TmdbtoMongodb.TMBDMovies;
import model.Movie;

public class MovieDao {
	
	private final MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    
    String uri = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    
    public MovieDao() {
        // Connect to MongoDB
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("BDD");
        this.collection = database.getCollection("movies"); 
    } 
    
      
    public void insertMovie(Document movie) {
        collection.insertOne(movie);        
    }
    
    public void insertMovietmdb(int id_tmbd) {
    	// 获取集合
    	TMBDMovies movie = new TMBDMovies(id_tmbd);
    	movie.tmdb_movies_to_mongodb("a5362b38ec62c678acd4737ef5ad21dc");
    }
    
    public String findIdbyId_Tmdb(int id_tmbd) {
    	Bson filter = Filters.eq("id_tmdb", id_tmbd); 	
        Document doc = collection.find(filter).first();
        ObjectId objectId = doc.getObjectId("_id");       
        String id = objectId.toString();        
        return id;
    }
    
    // 辅助方法：将 MongoDB 文档转换为 Movie 对象
    private Movie documentToMovie(Document document) {
    	Movie movie = new Movie();
        movie.setTitle(document.getString("title"));
        movie.setTime(document.getInteger("time"));
        movie.setReleaseDate(document.getDate("releaseDate"));
        movie.setImage(document.getString("image"));
        movie.setActors(document.getList("actors", String.class));
        movie.setCrew(document.getList("crew", String.class));
        movie.setGenres(document.getList("genres", String.class));
        movie.setVote(document.getDouble("vote"));
        movie.setVote(document.getInteger("vote_count"));
        movie.setOverview(document.getString("overview"));
        movie.setCountries(document.getList("countries",String.class));
        movie.setLanguage(document.getString("language"));
        movie.setCritiques(document.getList("critiques", String.class));
        return movie;        
    }
     
    public List<Document> findAllMovies(){
    	List<Document> movies = new ArrayList<>();
        for (Document doc : collection.find()) {
        	movies.add(doc);
        }
        return movies;
    }
    
    public List<Document> findMoviesByGenre(String genre) {
        List<Document> movies = new ArrayList<>();
        try {
            // 构建查询条件
            Bson filter = Filters.in("genres", genre);
            // 查询符合条件的电影
            FindIterable<Document> documents = collection.find(filter);

            // 遍历查询结果，并将每个文档添加到列表中
            for (Document document : documents) {
                movies.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    public List<Document> findMoviesByPartialName(String partialName) {
        List<Document> movies = new ArrayList<>();
        try {
            // 创建正则表达式，用于模糊匹配电影名称
            Pattern pattern = Pattern.compile(".*" + partialName + ".*", Pattern.CASE_INSENSITIVE);
            
            // 创建过滤器以匹配电影名称
            Document filter = new Document("title", pattern);
            
            // 执行查询操作
            FindIterable<Document> documents = collection.find(filter);
                
            // 遍历查询结果，并将每个文档转换为 Movie 对象
            for (Document document : documents) {
                movies.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
    
    public List<Document> findMoviesByTitle(String title) {
        List<Document> movies = new ArrayList<>();
        try {
            // 构建查询条件
            Document query = new Document("title", title);
            // 执行查询
            FindIterable<Document> cursor = collection.find(query);
            // 遍历查询结果并转换为 Movie 对象
            for (Document document : cursor) {
                movies.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
     
    // Read: Find a movie by ID 
    public Document findMovie (ObjectId id) {
    	Bson filter = Filters.eq("_id", id); 	
        Document doc = collection.find(filter).first();
        return doc;
    }
    
    
    
    /*public void updateMovie(ObjectId movieId, Movie updatedMovie) {
        try {
            // 创建更新条件，匹配电影的 ObjectId
            Bson filter = Filters.eq("_id", movieId);

            // 创建更新操作，将要更新的字段设置为新的值
            Bson update = Updates.combine(
	    		Updates.set("title", updatedMovie.getTitle()),
			    Updates.set("time", updatedMovie.getTime()),
			    Updates.set("releaseDate", updatedMovie.getReleaseDate()),
			    Updates.set("image", updatedMovie.getImage()),
			    Updates.set("actors", updatedMovie.getActors()),
			    Updates.set("crew", updatedMovie.getCrew()),
			    Updates.set("genres", updatedMovie.getGenres()),
			    Updates.set("vote", updatedMovie.getVote()),
			    Updates.set("vote_count", updatedMovie.getVote_count()),
			    Updates.set("overview", updatedMovie.getOverview()),
			    Updates.set("countries", updatedMovie.getCountries()),
			    Updates.set("language", updatedMovie.getLanguage()),
			    Updates.set("critiques", updatedMovie.getCritiques())
	    );

            // 执行更新操作
            UpdateResult updateResult = collection.updateOne(filter, update);

            // 检查更新结果
            if (updateResult.getModifiedCount() == 0) {
                System.out.println("未找到要更新的电影。");
            } else {
                System.out.println("电影信息已成功更新。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    public void updateMovie_critique(ObjectId movieId) {
        try {
            // 创建更新条件，匹配电影的 ObjectId
            Bson filter = Filters.eq("_id", movieId);
            
            MongoCollection<Document> critiquesCollection = database.getCollection("critiques");
            // 创建更新操作，将要更新的字段设置为新的值
            Bson filter_critque = Filters.eq("receverId", movieId.toString());
            //Document found = critiquesCollection.find(filter_critque).first();
            /*ObjectId critique_Id = found.getObjectId("_id");
            Bson update = Updates.addToSet("critiques", critique_Id.toString()); */
            List<ObjectId> critiqueIds = new ArrayList<>();
            for (Document critique : critiquesCollection.find(filter_critque)) {
                critiqueIds.add(critique.getObjectId("_id"));
            }
            Bson update = Updates.addEachToSet("critiques", critiqueIds);
            // 执行更新操作
            
            UpdateResult updateResult = collection.updateOne(filter, update);
            // 检查更新结果
            if (updateResult.getModifiedCount() == 0) {
                System.out.println("未找到要更新的电影。");
            } else {
                System.out.println("电影信息已成功更新。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void updateVote(ObjectId movieId, String userId) {
        try {
        	// 获取电影文档
            Document movieDocument = findMovie(movieId);
            System.out.println("user:"+userId);


            // 检查电影是否存在
            if (movieDocument == null) {
                System.out.println("Movie n'existe pas.");
                return;
            }             
           MongoCollection<Document> votesCollection = database.getCollection("votes");
           Bson filter = Filters.and(
                   Filters.eq("movieId", movieId.toString()),
                   Filters.eq("userId", userId)
               );
           Document userVote = votesCollection.find(filter).first();
           int userScore = 0;
           if (userVote != null) {
               userScore = userVote.getInteger("score");
           }
           
           // 获取当前电影的评分和总评分人数
           double currentVote = movieDocument.getDouble("vote");
           int currentVoters = movieDocument.getInteger("vote_count");
           
           // 计算新的总评分和总评分人数
           double newTotalScore = currentVote*currentVoters  + userScore;
           int newVoters = currentVoters + 1;
           double newVote = newTotalScore / newVoters;
                    
           // 更新电影文档中的评分和评分人数字段
           Bson update = Updates.combine(
               Updates.set("vote", newVote),
               Updates.set("vote_count", newVoters)
           );
           collection.updateOne(Filters.eq("_id", movieId), update);
           System.out.println("vote:"+newVote);
           System.out.println("voters:"+newVoters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /*public DeleteResult deleteMovieByName(String movieName) {
        try {
            // 创建过滤器以匹配电影名称
            Bson filter = Filters.eq("title", movieName);           
            // 删除匹配的电影
            DeleteResult res=collection.deleteOne(filter);
            System.out.println("Successfully deleted movie with name: " + movieName);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
    
    public DeleteResult deleteMovieById(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
       	DeleteResult res =collection.deleteOne(filter);           
        return res;
        
    }
    
        
     
    // Close the MongoDB connection
    public void close() {
        mongoClient.close();
    }

}
