package net.glowstone.io.nosql;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.io.json.JsonPlayerStatisticIoService;
import net.glowstone.util.StatisticMap;

import org.bson.Document;
import org.json.simple.JSONObject;

public class MongoDbPlayerStatisticIoService extends JsonPlayerStatisticIoService {

    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private MongoDatabase database = mongoClient.getDatabase("soen345");
    private GlowServer server;
    private HashMap<String, Document> hashDocuments = new HashMap<String, Document>();
    // private File statsDir;
    
    /**
     * Constructor.
     */
    public MongoDbPlayerStatisticIoService(GlowServer server, File statsDir) {
        super(server, statsDir);
        this.server = server;
        // this.statsDir = statsDir;
    }
    
    /**
     * Write json object mongo database.
     * 
     * @param player Minecraft player
     */
    public void forklift(GlowPlayer player) {
        MongoCollection<Document> collection = database.getCollection("statistic");
        // read from server memory alternative we read straight from json file
        StatisticMap map = player.getStatisticMap();
        JSONObject json = new JSONObject(map.getValues());

        // lookup up user
        if (playerExist(collection, player.getName())) {
            // data already exists
            return;
        }
        
        Document document = new Document("name", player.getName());
        
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
        
        hashDocuments.put(player.getName(), document);
    }
    
    /**
     * Query mongo.
     */
    private boolean playerExist(MongoCollection<Document> collection, String name) {
        
        long result = collection.count(Filters.eq("name", name));
        if (result > 0) {
            // player exists
            return true;
        }
        return false;
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
    
    /**
     * Writes the statistics of the player into its statistics file.
     *
     * @param player the player to write the statistics file from
     */
    @Override
    public void writeStatistics(GlowPlayer player) {

        MongoCollection<Document> collection = database.getCollection("statistic");
        Document document = hashDocuments.get(player.getName());
        collection.insertOne(document);
        
        readStat(player.getName());
        
    }
}
