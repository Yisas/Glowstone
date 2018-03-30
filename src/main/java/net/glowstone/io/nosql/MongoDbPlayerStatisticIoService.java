package net.glowstone.io.nosql;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.Iterator;

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
        
        Document document = new Document("name", "chris");
        
        // iterate over json and save key value
        for (Iterator ikeys = json.keySet().iterator(); ikeys.hasNext();) {
            String key = (String) ikeys.next();
            /*
             * The json key has the format stat.<name>.
             * When storing in mongo you cannot have the dot notation in the key.
             * We are removing "stat." and just storing the real name of the key.
             */
            String newkey = key.toString().substring(5);
            document.append(newkey, json.get(key));
        }
        
        collection.insertOne(document);
        
        readStat("chris");
    }
    
    /**
     * Read from mongo.
     *
     */
    public void readStat(String name) {
        MongoCollection<Document> collection = database.getCollection("statistic");
        collection.find(Filters.eq("name", name)).forEach(printBlock);
    }
    
    /*
     * print document.
     */
    private Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
}
