import java.util.ArrayList;

public class ColumnM
{
	private int ID;
	private String name;
	private ArrayList<TaskM> tasks;
	private int ProjectID;
	private String Username;

	ColumnM() {}

	ColumnM(String name, int ProjectID, String Username)
	{
		this.name = name;
		this.ProjectID = ProjectID;
		this.Username = Username;
		tasks = new ArrayList<>();
	}

	ColumnM(int ID, String name, ArrayList<TaskM> tasks)
	{
		this.ID	= ID;
		this.name = name;
		this.tasks = tasks;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<TaskM> getTasks()
	{
		return tasks;
	}

	public void setTasks(ArrayList<TaskM> tasks)
	{
		this.tasks = tasks;
	}

	public int getProjectID()
	{
		return ProjectID;
	}

	public void setProjectID(int projectID)
	{
		ProjectID = projectID;
	}

	public String getUsername()
	{
		return Username;
	}

	public void setUsername(String username)
	{
		Username = username;
	}
}