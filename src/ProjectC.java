import com.esotericsoftware.kryonet.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ProjectC extends Tab implements IProject
{
	private final UserM user;
	private final ProjectM projectM;
	private IClient iClient;

	@FXML
	public HBox hBox;

	ProjectC(ProjectM projectM, UserM user, IClient iClient)
	{
		super(projectM.getName());

		this.projectM = projectM;
		this.user = user;
		this.iClient = iClient;

		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProjectV.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();

			for (ColumnM columnM : projectM.getColumns())
			{
				hBox.getChildren().add(new ColumnC(columnM, user, iClient, this));
			}
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	int getProjectID()
	{
		return projectM.getID();
	}

	public ProjectM getProjectM()
	{
		return projectM;
	}

	@Override
	public int getColumnID(String name)
	{
		for (ColumnM columnM : projectM.getColumns())
		{
			if (columnM.getName().equals(name))
				return columnM.getID();
		}

		return -1;
	}
}
