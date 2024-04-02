import java.util.ArrayList;

public class ChecklistM
{
	private int ID;
	private ArrayList<ActionM> actions;
	private int TaskID;

	ChecklistM() {}

	ChecklistM(ArrayList<ActionM> actions)
	{
		this.actions = actions;
	}

	public float getProgress()
	{
		float completed = 0;

		for (ActionM action : actions)
		{
			if (action.isCompleted())
				completed++;
		}

		return completed / actions.size();
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public ArrayList<ActionM> getActions()
	{
		return actions;
	}

	public void setActions(ArrayList<ActionM> actions)
	{
		this.actions = actions;
	}

	public int getTaskID()
	{
		return TaskID;
	}

	public void setTaskID(int taskID)
	{
		TaskID = taskID;
	}
}