package net.glowstone.io.nosql;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.simple.JSONObject;

public class MongoDbPlayerStatisticIoService {

    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private MongoDatabase database = mongoClient.getDatabase("soen345");
    
    /**
     * Constructor.
     */
    public MongoDbPlayerStatisticIoService() {
        // create collection
        // database.createCollection("statistic", null);
    }
    
    /**
     * Write json object mongo database.
     * 
     * @param json statistics
     */
    public void forklift(JSONObject json) {
        MongoCollection<Document> collection = database.getCollection("statistic");
        String jsonstring = json.toString();
        System.out.println(jsonstring);
        Document document = new Document("chris", jsonstring);
        collection.insertOne(document);
    }
}
