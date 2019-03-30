package com.github.tylerhaigh.tourofheros.mongo;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDbClient {
    private String username;
    private String password;
    private String host;
    private String port;
    private String authDb;
    private String database;
    private String collection;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public String getHost() { return this.host; }
    public void setHost(String host) { this.host = host; }

    public String getPort() { return this.port; }
    public void setPort(String port) { this.port = port; }

    public String getAuthDb() { return this.authDb; }
    public void setAuthDb(String authDb) { this.authDb = authDb; }

    public String getDatabase() { return this.database; }
    public void setDatabase(String database) { this.database = database; }

    public String getCollection() { return this.collection; }
    public void setCollection(String collection) { this.collection = collection; }

    private String buildConnectionString() {
        return String.format("mongodb://%s:%s@%s:%s/%s", username, password, host, port, authDb);
    }

    private MongoClient getClient() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(buildConnectionString()));
        return mongoClient;
    }

    private MongoCollection<Document> getCollection(MongoClient client, String collectioName) {
        MongoDatabase db = client.getDatabase(this.database);
        MongoCollection<Document> coll = db.getCollection(collectioName);
        return coll;
    }

    private Object theLock = new Object();

    public String insertDocument(String json) {
        MongoClient client = getClient();
        MongoCollection<Document> coll = getCollection(client, this.collection);

        Document document = Document.parse(json);

        synchronized(theLock) {
            int id =  (int)(coll.countDocuments()+1);
            document.append("id", id);
            coll.insertOne(document);
        }

        client.close();
        document.remove("_id");
        return document.toJson();
    }

    public List<Document> getAllDocuments() {
        MongoClient client = getClient();
        MongoCollection<Document> coll = getCollection(client, this.collection);

        List<Document> docs = new ArrayList<Document>();
        synchronized(theLock) {
            FindIterable<Document> mongoDocs = coll.find();
            for (Document d : mongoDocs) {
                d.remove("_id");
                docs.add(d);
            }
        }

        client.close();
        return docs;
    }

    public String getDocumentById(int id) {
        MongoClient client = getClient();
        MongoCollection<Document> coll = getCollection(client, this.collection);

        Document doc = null;
        synchronized(theLock) {
            Document filter = new Document("id", id);
            doc = coll.find(filter).first();
        }

        client.close();

        if(doc != null) {
            doc.remove("_id");
            return doc.toJson();
        }
        return null;
    }

    public String updateDocument(int id, String json) {
        MongoClient client = getClient();
        MongoCollection<Document> coll = getCollection(client, this.collection);

        Document newDoc = Document.parse(json);
        newDoc.remove("id");
        newDoc = newDoc.append("id", id);

        Document filter = new Document("id", id);
        Document replacedDoc = null;

        synchronized(theLock) {
            replacedDoc = coll.findOneAndReplace(filter, newDoc);
        }

        client.close();

        if(replacedDoc != null) {
            newDoc.remove("_id");
            return newDoc.toJson();
        }

        return null;
    }


    public String deleteDocument(int id) {
        MongoClient client = getClient();
        MongoCollection<Document> coll = getCollection(client, this.collection);

        Document filter = new Document("id", id);
        Document doc = null;
        synchronized(theLock) {
            doc = coll.findOneAndDelete(filter);
        }

        client.close();
        if(doc != null) {
            doc.remove("_id");
            return doc.toJson();
        }
        return null;
    }
}
