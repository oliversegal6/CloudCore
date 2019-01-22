package com.xyz.allbetter.model;

public class DailyDiscipline extends BaseModel{

	String id;
	String date;
	String disciplineId;
	String desc;
	String targetUser;
	
	public String getDisciplineId() {
		return disciplineId;
	}
	public void setDisciplineId(String disciplineId) {
		this.disciplineId = disciplineId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
