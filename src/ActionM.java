public class ActionM
{
	private int ID;
	private String description;
	private boolean completed;
	private int ChecklistID;

	ActionM() {}

	ActionM(String description)
	{
		this.description = description;
		this.completed = false;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public int getChecklistID()
	{
		return ChecklistID;
	}

	public void setChecklistID(int checklistID)
	{
		ChecklistID = checklistID;
	}
}