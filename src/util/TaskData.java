package util;

public class TaskData {

	private String taskName;
	private double task_Ra_deg;
	private double task_Dec_deg;
	private String  task_level;
	private String task_start_time;
	private String task_end_time;

	public TaskData(String name,double ra_deg,double dec_deg,String level) {
		this.taskName=name;
		this.task_Ra_deg=ra_deg;
		this.task_Dec_deg=dec_deg;
		this.task_level=level;
	}
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public double getTask_Ra_deg() {
		return task_Ra_deg;
	}
	public void setTask_Ra_deg(double task_Ra_deg) {
		this.task_Ra_deg = task_Ra_deg;
	}
	public double getTask_Dec_deg() {
		return task_Dec_deg;
	}
	public void setTask_Dec_deg(double task_Dec_deg) {
		this.task_Dec_deg = task_Dec_deg;
	}
	public String getTask_level() {
		return task_level;
	}
	public void setTask_level(String task_level) {
		this.task_level = task_level;
	}
	public String getTask_start_time() {
		return task_start_time;
	}
	public void setTask_start_time(String task_start_time) {
		this.task_start_time = task_start_time;
	}
	public String getTask_end_time() {
		return task_end_time;
	}
	public void setTask_end_time(String task_end_time) {
		this.task_end_time = task_end_time;
	}
}
