package com.xyz.allbetter.model;

public class DailyTask extends BaseModel{

	String id;
	String date;
	String taskId;
	String taskName;
	String targetUser;
	Boolean checked;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
