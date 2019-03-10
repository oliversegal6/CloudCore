package com.xyz.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
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

    public void save(JSONObject o, String collectionName) {
        mongoTemplate.save((DBObject) JSON.parse(o.toString()), collectionName);
    }

    public Object findOneById(String id, String collectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        DBObject user = mongoTemplate.findOne(query, DBObject.class, collectionName);
        return user;
    }

    public List<JSONObject> findAll(String collectionName, Integer pageIndex, Integer pageSize) {
        return findWithPagination(new HashMap<String, String>(), collectionName, pageIndex, pageSize);
    }

    public List<JSONObject> find(Map<String, String> params, String collectionName) {
        return findWithPagination(params, collectionName, 0, 0);
    }


    public List<JSONObject> findWithPagination(Map<String, String> params, String collectionName, Integer pageIndex, Integer pageSize) {
        Query query = new Query();
        for (String key : params.keySet()) {
            query.addCriteria(Criteria.where(key).is(params.get(key)));
        }
        if (pageIndex != 0) query.skip((pageIndex - 1) * pageSize);
        if (pageSize != 0) query.limit(pageSize);
        List<DBObject> results = mongoTemplate.find(query, DBObject.class, collectionName);

        List<JSONObject> jsonResults = new ArrayList<JSONObject>();
        for (DBObject result : results) {
            //jsonResults.add(new Gson().fromJson(JSON.serialize(result), JSONObject.class));
            jsonResults.add(new JSONObject(JSON.serialize(result)));
        }

        return jsonResults;
    }

    public void updateUser(Object user, String collectionName) {
        Query query = new Query(Criteria.where("id").is(""));
        Update update = new Update().set("userName", "");
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, collectionName);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,UserEntity.class);
    }

    public void deleteById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query);
    }

    public void dropCollection(String cName) {
        mongoTemplate.dropCollection(cName);
    }
}

