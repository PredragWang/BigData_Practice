package gw.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Guanyu on 3/16/16.
 */
public class GWMongoDBConnector {

    private MongoClient mongoClient;
    private MongoDatabase database;

    public GWMongoDBConnector(String mongoURIString, String db_name) {
        mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        database = mongoClient.getDatabase(db_name);
    }
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }
}
