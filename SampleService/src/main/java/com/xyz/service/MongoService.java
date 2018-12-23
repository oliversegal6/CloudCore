package com.xyz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.xyz.dao.MongoDAO;

@Component
public class MongoService {

	@Autowired
	private MongoDAO mongoDAO;
	
	 public void save(JsonObject o, String collectionName) {
		 mongoDAO.save(o, collectionName);
	 }
	 
	 public void saveBatch(List<JsonObject> items, String collectionName) {
		 for(JsonObject item: items) {
			 mongoDAO.save(item, collectionName);
		 }
	 }
	 
	 public List<JsonObject> findAll(String collectionName) {
		 
		 return mongoDAO.findAll(collectionName);
	 }
}
