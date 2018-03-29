package net.glowstone.io.nosql;

//import com.mongodb.BasicDBObject;
import com.mongodb.DB;
// import com.mongodb.DBCollection;
// import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoDbPlayerStatisticIoService {

    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    private DB database = mongoClient.getDB("soen345");
    
    /**
     * Constructor.
     */
    public MongoDbPlayerStatisticIoService() {
        // create collection
        database.createCollection("statistic", null);
        database.getCollectionNames().forEach(System.out::println);
    }
    
}
