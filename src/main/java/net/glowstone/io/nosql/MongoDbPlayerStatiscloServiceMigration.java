package net.glowstone.io.nosql;

import java.io.File;
//import java.util.HashMap;
//
//import org.bson.Document;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoDatabase;

import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.io.json.JsonPlayerStatisticIoService;

public class MongoDbPlayerStatiscloServiceMigration  extends JsonPlayerStatisticIoService {

    //To migrate the data from the old datastore to the new one you need to read from 
    // old data store make sure data is valid with constency cheker then write

    private GlowServer server;
    private File statsDir;
    private MongoDbPlayerStatisticIoService mongoP = 
            new MongoDbPlayerStatisticIoService(server, statsDir);
    private JsonPlayerStatisticIoService jsonP = 
            new JsonPlayerStatisticIoService(server, statsDir);
    private GlowPlayer player;

    /**
     * Constructor.
     */
    public MongoDbPlayerStatiscloServiceMigration(GlowServer server, File statsDir) {
        super(server, statsDir);
        this.server = server;
        this.statsDir = statsDir;
    }

    /**
     * Migration.
     */
    public void migration() {


    }

}
