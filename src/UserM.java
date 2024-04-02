import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UserM
{
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private byte[] photo;
	private String inspiration;
	private ArrayList<ProjectM> projects;

	UserM(){}

	UserM(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	UserM(String username, String password, String firstname, String lastname, String path)
	{
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;

		try
		{
			FileInputStream fs = new FileInputStream(path);
			this.photo = new byte[fs.available()];

			if (fs.read(this.photo) == -1)
				this.photo = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	UserM(String username, String password, String firstname, String lastname, byte[] photo, ArrayList<ProjectM> projects)
	{
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.photo = photo;
		this.projects = projects;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public byte[] getPhoto()
	{
		return photo;
	}

	public void setPhoto(byte[] photo)
	{
		this.photo = photo;
	}

	public String getInspiration()
	{
		return inspiration;
	}

	public void setInspiration(String inspiration)
	{
		this.inspiration = inspiration;
	}

	public ArrayList<ProjectM> getProjects()
	{
		return projects;
	}

	public void setProjects(ArrayList<ProjectM> projects)
	{
		this.projects = projects;
	}

	public void setPhoto(String path)
	{
		try
		{
			FileInputStream fs = new FileInputStream(path);
			this.photo = new byte[fs.available()];

			if (fs.read(this.photo) == -1)
				this.photo = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}