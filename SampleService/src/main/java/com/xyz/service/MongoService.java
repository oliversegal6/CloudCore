package com.xyz.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.xyz.dao.MongoDAO;

@Component
public class MongoService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
	 
	 public void save(JSONObject o, String collectionName) {
		 mongoDAO.save(o, collectionName);
	 }
	 
	 public List<JSONObject> findAll(String collectionName, Integer pageIndex, Integer pageSize) {
		 logger.info("Query params: " + collectionName);
		 List<JSONObject> res =   mongoDAO.findAll(collectionName, pageIndex, pageSize);
		 logger.info("Mongo findAll finished. Size: " + res.size());
		 return res;
	 }
	 
	 public List<JSONObject> find(Map<String, String> params, String collectionName) {
		 logger.info("Query params: " + params);
		 List<JSONObject> res =  mongoDAO.find(params, collectionName);
		 logger.info("Mongo Query finished. Size: " + res.size());
		 return res;
	 }

	 public  void dropCollection(String cName){
	 	mongoDAO.dropCollection(cName);
	 }
}
