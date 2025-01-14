package essayer;


import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;

/*import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;*/

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    public static void main(String[] args) {
        // MongoDB 服务器主机地址和端口号
    	// int port = 27017; par default
        //ConnectionString connectionString =  new ConnectionString(null);
        
        String connectionString = "mongodb+srv://dysm1869:PYeYBFWVX677vQgM@cluster0.thzsihz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        
     // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("BDD");
                //database.runCommand(new Document("ping", 1));
                // 获取集合
                MongoCollection<Document> collection = database.getCollection("users");

                // 查询集合中的文档
                FindIterable<Document> documents = collection.find();
                
                //System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                
             // 遍历查询结果
                for (Document document : documents) {
                    System.out.println(document.toJson());
                }
                
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

}

