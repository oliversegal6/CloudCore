package com.xyz.allbetter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xyz.allbetter.model.DailyDiscipline;
import com.xyz.allbetter.model.DailyTask;
import com.xyz.allbetter.model.Discipline;
import com.xyz.allbetter.model.Task;
import com.xyz.allbetter.model.User;
import com.xyz.allbetter.model.UserTasks;
import com.xyz.pojo.Stock;
import com.xyz.service.MongoService;
import com.xyz.util.CommonUtil;

@Component
public class AllBetterService {

	private static final String USER = "User";

	private static final String USER_TASKS = "UserTasks";

	private static final String DAILY_TASK = "DailyTask";

	private static final String DAILY_DISCIPLINE = "DailyDiscipline";

	private static final String DISCIPLINE = "Discipline";

	private static final String TASK = "Task";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MongoService mongoService;

	public void saveUser(User user) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(user), JsonObject.class);
		mongoService.save(obj, USER);
		logger.info("saveTask for " + user.getName());
	}

	public List<User> findUser(User params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), USER);
		List<User> tasks = new ArrayList<User>();
		for (JSONObject stockJson : stocksJson) {
			tasks.add(new Gson().fromJson(stockJson.toString(), User.class));
		}
		
		return tasks;
	}
	
	public void saveTask(Task task) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(task), JsonObject.class);
		mongoService.save(obj, TASK);
		logger.info("saveTask for " + task.getName());
	}

	public List<Task> findTask(Task params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), TASK);
		List<Task> tasks = new ArrayList<Task>();
		for (JSONObject stockJson : stocksJson) {
			tasks.add(new Gson().fromJson(stockJson.toString(), Task.class));
		}
		
		return tasks;
	}
	
	public void saveDiscipline(Discipline discipline) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(discipline), JsonObject.class);
		mongoService.save(obj, DISCIPLINE);
		logger.info("saveDiscipline for " + discipline.getName());
	}

	public List<Discipline> findDiscipline(Discipline params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), DISCIPLINE);
		List<Discipline> tasks = new ArrayList<Discipline>();
		for (JSONObject stockJson : stocksJson) {
			tasks.add(new Gson().fromJson(stockJson.toString(), Discipline.class));
		}
		
		return tasks;
	}
	
	public void saveDailyDiscipline(DailyDiscipline discipline) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(discipline), JsonObject.class);
		mongoService.save(obj, DAILY_DISCIPLINE);
		logger.info("saveDailyDiscipline for " + discipline.getTargetUser());
	}

	public List<DailyDiscipline> findDailyDiscipline(DailyDiscipline params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), DAILY_DISCIPLINE);
		List<DailyDiscipline> tasks = new ArrayList<DailyDiscipline>();
		for (JSONObject stockJson : stocksJson) {
			tasks.add(new Gson().fromJson(stockJson.toString(), DailyDiscipline.class));
		}
		
		return tasks;
	}
	
	public void saveDailyTask(DailyTask dailyTask) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(dailyTask), JsonObject.class);
		mongoService.save(obj, DAILY_TASK);
		logger.info("saveDailyTask for " + dailyTask.getTargetUser());
	}

	public List<DailyTask> findDailyTask(DailyTask params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), DAILY_TASK);
		List<UserTasks> userTasks = this.findUserTasks(new UserTasks());
		List<DailyTask> tasks = new ArrayList<DailyTask>();
		for(UserTasks userTask : userTasks) {
			for(String taskId : userTask.getTaskIds().split(",")) {
				DailyTask dailyTask = new DailyTask();
				dailyTask.setTaskId(taskId);
				dailyTask.setTaskName(taskId);
				dailyTask.setDate(params.getDate());
				dailyTask.setTargetUser(userTask.getTargetUser());
				for (JSONObject stockJson : stocksJson) {
					DailyTask tempDailyTask = new Gson().fromJson(stockJson.toString(), DailyTask.class);
					String dailyTaskId = tempDailyTask.getTaskId();
					if(dailyTask.getTaskId().equalsIgnoreCase(dailyTaskId) 
							&& tempDailyTask.getDate().equalsIgnoreCase(params.getDate())
							&& tempDailyTask.getTargetUser().equalsIgnoreCase(dailyTask.getTargetUser())) {
						dailyTask.setChecked(tempDailyTask.getChecked());
					}
				}
				tasks.add(dailyTask);
			}
		}
		
		return tasks;
	}
	
	public void saveUserTasks(UserTasks userTasks) {
		JsonObject obj = new Gson().fromJson(new Gson().toJson(userTasks), JsonObject.class);
		mongoService.save(obj, USER_TASKS);
		logger.info("saveUserTasks for " + userTasks.getTargetUser());
	}

	public List<UserTasks> findUserTasks(UserTasks params) {
		List<JSONObject> stocksJson = mongoService.find(CommonUtil.convert2Map(params), USER_TASKS);
		List<UserTasks> tasks = new ArrayList<UserTasks>();
		for (JSONObject stockJson : stocksJson) {
			tasks.add(new Gson().fromJson(stockJson.toString(), UserTasks.class));
		}
		
		return tasks;
	}
}
