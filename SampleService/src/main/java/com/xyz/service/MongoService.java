package com.xyz.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
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
	 
	 public List<JSONObject> findAll(String collectionName, Integer pageIndex, Integer pageSize) {
		 
		 return mongoDAO.findAll(collectionName, pageIndex, pageSize);
	 }
	 
	 public List<JSONObject> find(Map<String, String> params, String collectionName) {
		 
		 return mongoDAO.find(params, collectionName);
	 }
}
