import java.util.Date;

public class TaskM
{
	private int ID;
	private String name;
	private String description;
	private Date due;
	private boolean completed;
	private ChecklistM checklist;
	private int ColumnID;
	private String Username;

	TaskM() {}

	TaskM(int ID, String name, String description, Date due, boolean completed, ChecklistM checklist)
	{
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.due = due;
		this.completed = completed;
		this.checklist = checklist;
	}

	public boolean checklistCompleted()
	{
		if (checklist == null)
			return false;

		int nTotal = checklist.getActions().size();
		int nCompleted = 0;

		for (ActionM action : checklist.getActions())
		{
			if (action.isCompleted())
				nCompleted++;
		}

		return nTotal == nCompleted && nTotal != 0;
	}

	public String getProgress()
	{
		if (checklist == null)
			return "N/A";

		int nTotal = checklist.getActions().size();
		int nCompleted = 0;

		for (ActionM action : checklist.getActions())
		{
			if (action.isCompleted())
				nCompleted++;
		}

		return String.format("%d/%d", nCompleted, nTotal);
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getDue()
	{
		return due;
	}

	public void setDue(Date due)
	{
		this.due = due;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public ChecklistM getChecklist()
	{
		return checklist;
	}

	public void setChecklist(ChecklistM checklist)
	{
		this.checklist = checklist;
	}

	public int getColumnID()
	{
		return ColumnID;
	}

	public void setColumnID(int columnID)
	{
		ColumnID = columnID;
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