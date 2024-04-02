import com.alibaba.fastjson.JSON;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class TaskC extends ListCell<TaskM>
{
	private FXMLLoader loader;
	@FXML
	public AnchorPane anchorPane;
	@FXML
	public Label nameLabel;
	@FXML
	public Label dueLabel;
	@FXML
	public ImageView progressIV;
	@FXML
	public Label progressLabel;

	private ColumnM columnM;
	private IClient iClient;
	private TaskM taskM;
	private String username;
	private IProject iProject;

	public TaskC(ColumnM columnM, IClient iClient, String username, IProject iProject)
	{
		this.columnM = columnM;
		this.iClient = iClient;
		this.username = username;
		this.iProject = iProject;
	}

	@Override
	protected void updateItem(TaskM taskM, boolean empty)
	{
		super.updateItem(taskM, empty);

		this.taskM = taskM;

		if (empty || taskM == null)
		{
			setText(null);
			setGraphic(null);
			return;
		}

//		if (loader == null)
		{
			try
			{
				loader = new FXMLLoader(getClass().getResource("TaskV.fxml"));
				loader.setController(this);
				loader.load();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		nameLabel.setText(String.valueOf(taskM.getName()));
		if (taskM.getDue() != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dueLabel.setText(sdf.format(taskM.getDue()));
		}

		progressLabel.setText(taskM.getProgress());

		if (taskM.isCompleted())
		{
			dueLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#1BBF75"), new CornerRadii(0), Insets.EMPTY)));
		}
		else
		{
			Date now = new Date();

			if (taskM.getDue() != null)
			{
				long diff = taskM.getDue().getTime() - now.getTime();

				if (diff < 0)
				{
					dueLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F44D00"), new CornerRadii(0), Insets.EMPTY)));
				}
				else if (diff / 1000 / 3600 / 24 < 2)
				{
					dueLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F9AD08"), new CornerRadii(0), Insets.EMPTY)));
				}
			}
		}

		if (taskM.checklistCompleted())
		{
			progressLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#1BBF75"), new CornerRadii(0), Insets.EMPTY)));
		}

		setText(null);
		setGraphic(anchorPane);
	}

	public void onEdit(ActionEvent actionEvent)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskInfoV.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(loader.load()));
			stage.initModality(Modality.APPLICATION_MODAL);
			TaskInfoC taskInfoC = loader.getController();
			taskInfoC.setTaskM(taskM);
			stage.setTitle("Task Info");
			stage.showAndWait();

			TaskM taskM = taskInfoC.getTaskM();

			if (taskM != null)
			{
				Instruction instruction = new Instruction();
				instruction.setCode(Const.UPDATE_TASK);
				taskM.setColumnID(columnM.getID());
				taskM.setUsername(columnM.getUsername());
				instruction.setBody(JSON.toJSONString(taskM));
				iClient.getClient().sendTCP(instruction);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onMove()
	{
		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Move task " + taskM.getName() + " to");
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			int col = iProject.getColumnID(result.get());
			if (col != -1)
			{
				Instruction instruction = new Instruction();
				instruction.setCode(Const.UPDATE_TASK);
				taskM.setColumnID(col);
				taskM.setUsername(columnM.getUsername());
				instruction.setBody(JSON.toJSONString(taskM));
				iClient.getClient().sendTCP(instruction);
			}
		}
	}

	public void onDelete(ActionEvent actionEvent)
	{
		taskM.setUsername(username);
		Instruction instruction = new Instruction();
		instruction.setCode(Const.DELETE_TASK);
		instruction.setBody(JSON.toJSONString(taskM));
		iClient.getClient().sendTCP(instruction);
	}
}