import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryonet.Client;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ColumnC extends AnchorPane implements Initializable
{
	public Label nameLabel;
	@FXML
	private ComboBox<String> columnChoice;
	@FXML
	private ListView<TaskM> tasksLV;
	private ObservableList<TaskM> observableList;

	private final ColumnM columnM;
	private UserM userM;
	private final IClient iClient;
	private final IProject iProject;

	ColumnC(ColumnM columnM, UserM userM, IClient iClient, IProject iProject)
	{
		super();

		try
		{
			this.columnM = columnM;
			this.columnM.setUsername(userM.getUsername());
			this.userM = userM;
			this.iClient = iClient;
			this.iProject = iProject;

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ColumnV.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();

			observableList.addAll(columnM.getTasks());

			nameLabel.setText(columnM.getName());
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
	{
		switch (t1.intValue())
		{
			case 0 ->
			{
				try
				{
					FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskInfoV.fxml"));
					Stage stage = new Stage();
					stage.setScene(new Scene(loader.load()));
					stage.initModality(Modality.APPLICATION_MODAL);
					TaskInfoC taskInfoC = loader.getController();
					stage.setTitle("Task Info");
					stage.showAndWait();

					TaskM taskM = taskInfoC.getTaskM();

					if (taskM != null)
					{
						Instruction instruction = new Instruction();
						instruction.setCode(Const.NEW_TASK);

						taskM.setColumnID(columnM.getID());
						taskM.setUsername(userM.getUsername());

						instruction.setBody(JSON.toJSONString(taskM));
						iClient.getClient().sendTCP(instruction);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			case 1 ->
			{
				TextInputDialog textInputDialog = new TextInputDialog();
				textInputDialog.setTitle("Smart Board");
				textInputDialog.setHeaderText("Rename column " + columnM.getName());
				Optional<String> result = textInputDialog.showAndWait();

				if (result.isPresent() && !result.get().isEmpty())
				{
					columnM.setName(result.get());

					Instruction instruction = new Instruction();
					instruction.setCode(Const.UPDATE_COLUMN);
					instruction.setBody(JSON.toJSONString(columnM));
					iClient.getClient().sendTCP(instruction);
				}
			}

			case 2 ->
			{
				Instruction instruction = new Instruction();
				instruction.setCode(Const.DELETE_COLUMN);
				instruction.setBody(JSON.toJSONString(columnM));
				iClient.getClient().sendTCP(instruction);
			}

			case 3 ->
			{
				ArrayList<TaskM> alTasks = new ArrayList<>(observableList);
				alTasks.sort((task0, task1) ->(int)(task0.getDue().getTime() - task1.getDue().getTime()));

				observableList.clear();
				observableList.addAll(alTasks);
			}

			case 4 ->
			{
				ArrayList<TaskM> alTasks = new ArrayList<>(observableList);
				alTasks.sort((task0, task1) ->(int)(task0.getDue().getTime() - task1.getDue().getTime()) * -1);

				observableList.clear();
				observableList.addAll(alTasks);
			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		columnChoice.getItems().addAll("Add Task", "Rename Column", "Delete Column", "Ascending", "Descending");
		columnChoice.getSelectionModel().selectedIndexProperty().addListener(this::changed);

		observableList = FXCollections.observableArrayList();
		tasksLV.setItems(observableList);
		tasksLV.setCellFactory(tasksLV -> new TaskC(columnM, iClient, userM.getUsername(), iProject));
	}
}