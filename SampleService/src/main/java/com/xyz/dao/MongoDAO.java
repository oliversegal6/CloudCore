package com.xyz.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class MongoDAO {
	
	    @Autowired
	    private MongoTemplate mongoTemplate;

	    public void save(JsonObject o, String collectionName) {
	    	Gson gson = new Gson();
	        mongoTemplate.save((DBObject) JSON.parse(gson.toJson(o)), collectionName);
	    }

	    public Object findOneById(String id, String collectionName) {
	        Query query=new Query(Criteria.where("id").is(id));
	        DBObject user =  mongoTemplate.findOne(query, DBObject.class, collectionName);
	        return user;
	    }
	    
	    public List<JsonObject> findAll(String collectionName) {
	        Query query=new Query();
	        List<DBObject> results =  mongoTemplate.find(query, DBObject.class, collectionName);
	        
	        List<JsonObject> jsonResults = new ArrayList<JsonObject>();
	        for(DBObject result : results) {
	        	jsonResults.add(new Gson().fromJson(JSON.serialize(result), JsonObject.class));
	        }
	        return jsonResults;
	    }

	    public void updateUser(Object user, String collectionName) {
	        Query query=new Query(Criteria.where("id").is(""));
	        Update update= new Update().set("userName", "");
	        //更新查询返回结果集的第一条
	        mongoTemplate.updateFirst(query,update,collectionName);
	        //更新查询返回结果集的所有
	        // mongoTemplate.updateMulti(query,update,UserEntity.class);
	    }

	    public void deleteById(Long id) {
	        Query query=new Query(Criteria.where("id").is(id));
	        mongoTemplate.remove(query);
	    }
	}

