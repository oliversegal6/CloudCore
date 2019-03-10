package com.xyz.allbetter.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.xyz.allbetter.model.DailyDiscipline;
import com.xyz.allbetter.model.DailyTask;
import com.xyz.allbetter.model.Discipline;
import com.xyz.allbetter.model.Task;
import com.xyz.allbetter.model.User;
import com.xyz.allbetter.model.UserTasks;
import com.xyz.allbetter.service.AllBetterService;
import com.xyz.dao.MongoDAO;
import com.xyz.pojo.Concept;
import com.xyz.pojo.DailyBasic;
import com.xyz.pojo.Stock;
import com.xyz.pojo.StockDaily;
import com.xyz.service.MongoService;
import com.xyz.service.StockMiningService;


@RestController
public class AllBetterController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private AllBetterService allBetterService;
    
    @PostMapping("/saveUser")
    public String saveUser(User User){
        logger.info("saveUser start...");
        allBetterService.saveUser(User);
        
        return "saveUser done";
    }
    
    
    @GetMapping("/findUser")
    public User[] findUser(User params) {
    	logger.info("findUser start...");
    	
    	List<User> results = allBetterService.findUser(params);
    	return results.toArray(new User[results.size()]);
    }
    
    @PostMapping("/saveTask")
    public String saveTask(Task task){
        logger.info("saveTask start...");
        allBetterService.saveTask(task);
        
        return "saveTask done";
    }
    
    
    @GetMapping("/findTask")
    public Task[] findTask(Task params) {
    	logger.info("findTask start...");
    	
    	List<Task> results = allBetterService.findTask(params);
    	return results.toArray(new Task[results.size()]);
    }
    
    @PostMapping("/saveDiscipline")
    public String saveDiscipline(Discipline task){
        logger.info("saveDiscipline start...");
        allBetterService.saveDiscipline(task);
        
        return "saveDiscipline done";
    }
    
    
    @GetMapping("/findDiscipline")
    public Discipline[] findDiscipline(@RequestParam Discipline params) {
    	logger.info("findDiscipline start...");
    	
    	List<Discipline> results = allBetterService.findDiscipline(params);
    	return results.toArray(new Discipline[results.size()]);
    }
    
    @PostMapping("/saveDailyTask")
    public String saveDailyTask(DailyTask task){
        logger.info("saveDailyTask start...");
        allBetterService.saveDailyTask(task);
        
        return "saveDailyTask done";
    }
    
    
    @GetMapping("/findDailyTask")
    public DailyTask[] findDailyTask(DailyTask params) {
    	logger.info("findDailyTask start...");
    	
    	List<DailyTask> results = allBetterService.findDailyTask(params);
    	return results.toArray(new DailyTask[results.size()]);
    }
    
    @PostMapping("/saveDailyDiscipline")
    public String saveDailyDiscipline(DailyDiscipline task){
        logger.info("saveDailyDiscipline start...");
        allBetterService.saveDailyDiscipline(task);
        
        return "saveDailyDiscipline done";
    }
    
    
    @GetMapping("/findDailyDiscipline")
    public DailyDiscipline[] findDailyDiscipline(@RequestParam DailyDiscipline params) {
    	logger.info("findDailyDiscipline start...");
    	
    	List<DailyDiscipline> results = allBetterService.findDailyDiscipline(params);
    	return results.toArray(new DailyDiscipline[results.size()]);
    }
    
    @PostMapping("/saveUserTasks")
    public String saveUserTasks(UserTasks task){
        logger.info("saveTask start...");
        allBetterService.saveUserTasks(task);
        
        return "saveUserTasks done";
    }
    
    
    @GetMapping("/findUserTasks")
    public UserTasks[] findUserTasks(@RequestParam UserTasks params) {
    	logger.info("findUserTasks start...");
    	
    	List<UserTasks> results = allBetterService.findUserTasks(params);
    	return results.toArray(new UserTasks[results.size()]);
    }
    
}
