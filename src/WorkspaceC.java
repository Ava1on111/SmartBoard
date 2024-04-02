import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class WorkspaceC extends JFXView implements Initializable, IClient
{
	public HBox hBox;
	public Label inspirationLabel;
	public Label nameLabel;
	public ImageView portraitIV;
	public TabPane projectsPane;

	@FXML
	public MenuItem addItem;
	public MenuItem renameItem;
	public MenuItem setDefaultItem;
	public MenuItem unsetDefaultItem;
	public RadioMenuItem deleteItem;

	private static String inspiration;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		hBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#D0D0D0"), new CornerRadii(0), Insets.EMPTY)));
		inspiration = "";
	}

	private void update(UserM user)
	{
		this.user = user;
		if (inspiration.isEmpty())
			inspiration = user.getInspiration();

		if (user.getProjects() != null)
		{
			addItem.setDisable(user.getProjects().size() == 0);
			renameItem.setDisable(user.getProjects().size() == 0);
			setDefaultItem.setDisable(user.getProjects().size() == 0);
			unsetDefaultItem.setDisable(user.getProjects().size() == 0);
			deleteItem.setDisable(user.getProjects().size() == 0);
		}

		inspirationLabel.setText(inspiration);
		portraitIV.setImage(new Image(new ByteArrayInputStream(user.getPhoto())));
		nameLabel.setText(user.getFirstname() + " " + user.getLastname());

		projectsPane.getTabs().clear();

		ArrayList<ProjectM> projectMS = new ArrayList<>();

		if (user.getProjects() != null)
			projectMS.addAll(user.getProjects());

		for (ProjectM project : projectMS)
		{
			if (project.isDefault())
			{
				project.setUsername(user.getUsername());
				projectsPane.getTabs().add(new ProjectC(project, user, this));
				projectMS.remove(project);
				break;
			}
		}

		for (ProjectM project : projectMS)
		{
			project.setUsername(user.getUsername());
			projectsPane.getTabs().add(new ProjectC(project, user, this));
		}
	}

	@Override
	public void received(Connection connection, Object object)
	{
		if (object instanceof Instruction)
		{
			Platform.runLater(() ->
			{
				Instruction instruction = (Instruction) object;

				if (instruction.getStatus())
				{
					update(JSON.parseObject(instruction.getBody(), UserM.class));
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Operation failed.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
				}
			});
		}
	}

	public void passParam(Object object)
	{
		if (object instanceof UserM)
		{
			update((UserM)object);
		}
	}

	public void onNewProject()
	{
		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Create new project");
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			ProjectM project = new ProjectM(result.get(), user.getUsername());

			Instruction instruction = new Instruction();
			instruction.setCode(Const.NEW_PROJECT);
			instruction.setBody(JSON.toJSONString(project));
			client.sendTCP(instruction);
		}
	}

	public void onAddColumn()
	{
		ProjectC project = (ProjectC) projectsPane.getSelectionModel().getSelectedItem();

		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Create new column to " + project.getText());
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			ColumnM columnM = new ColumnM(result.get(), project.getProjectID(), user.getUsername());

			Instruction instruction = new Instruction();
			instruction.setCode(Const.NEW_COLUMN);
			instruction.setBody(JSON.toJSONString(columnM));
			client.sendTCP(instruction);
		}
	}

	public void onRename()
	{
		ProjectM project = ((ProjectC) projectsPane.getSelectionModel().getSelectedItem()).getProjectM();

		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Rename Project " + project.getName());
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			project.setName(result.get());

			Instruction instruction = new Instruction();
			instruction.setCode(Const.UPDATE_PROJECT);
			instruction.setBody(JSON.toJSONString(project));
			client.sendTCP(instruction);
		}
	}

	public void onSetDefault()
	{
		ProjectM project = ((ProjectC) projectsPane.getSelectionModel().getSelectedItem()).getProjectM();
		project.setDefault(true);
		Instruction instruction = new Instruction();
		instruction.setCode(Const.UPDATE_PROJECT);
		instruction.setBody(JSON.toJSONString(project));
		client.sendTCP(instruction);
	}

	public void onUnsetDefault()
	{
		ProjectM project = ((ProjectC) projectsPane.getSelectionModel().getSelectedItem()).getProjectM();
		project.setDefault(false);
		Instruction instruction = new Instruction();
		instruction.setCode(Const.UPDATE_PROJECT);
		instruction.setBody(JSON.toJSONString(project));
		client.sendTCP(instruction);
	}

	public void onDelete()
	{
		ProjectM project = ((ProjectC) projectsPane.getSelectionModel().getSelectedItem()).getProjectM();
		Instruction instruction = new Instruction();
		instruction.setCode(Const.DELETE_PROJECT);
		instruction.setBody(JSON.toJSONString(project));
		client.sendTCP(instruction);
	}

	public void onProfile()
	{
		switchTo("Profile", user);
	}

	public void onLogout()
	{
		switchTo("SignIn");
	}

	@Override
	public Client getClient()
	{
		return client;
	}
}