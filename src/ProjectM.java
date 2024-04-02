import java.util.ArrayList;
import java.util.Date;

public class ProjectM
{
	private int ID;
	private String name;
	private boolean isDefault;
	private Date timestamp;
	private ArrayList<ColumnM> columns;
	private String Username;

	ProjectM() {}

	ProjectM(String name, String username)
	{
		this.name = name;
		isDefault = false;
		timestamp = new Date();
		columns = new ArrayList<>();
		Username = username;
	}

	ProjectM(int ID, String name, boolean isDefault, Date timestamp, ArrayList<ColumnM> columns)
	{
		this.ID = ID;
		this.name = name;
		this.isDefault = isDefault;
		this.timestamp = timestamp;
		this.columns = columns;
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

	public boolean isDefault()
	{
		return isDefault;
	}

	public void setDefault(boolean aDefault)
	{
		isDefault = aDefault;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	public ArrayList<ColumnM> getColumns()
	{
		return columns;
	}

	public void setColumns(ArrayList<ColumnM> columns)
	{
		this.columns = columns;
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