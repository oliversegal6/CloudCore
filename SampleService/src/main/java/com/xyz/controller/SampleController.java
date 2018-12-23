package com.xyz.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.xyz.dao.MongoDAO;
import com.xyz.service.MongoService;


@RestController
public class SampleController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private MongoService mongoService;
    
    //@Value("${spring.profiles}")
    private String profile;

    @GetMapping("/getUserById")
    @ApiOperation(value="getUserById", notes="根据id的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public String getUserById(String id){
        logger.info("getUserById start...");
        return "service" + id + profile;
    }
    
    @PostMapping("/save")
    public void save(JsonObject o, String collectionName){
        logger.info("save start...");
        mongoService.save(o, collectionName);
    }
}
