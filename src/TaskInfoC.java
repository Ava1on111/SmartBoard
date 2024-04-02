import com.alibaba.fastjson.JSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class TaskInfoC extends AnchorPane implements Initializable, IChecklist
{
	public AnchorPane anchorPane;
	public TextField nameField;
	public Hyperlink dueLink;
	public ToolBar dueContainer;
	public DatePicker dueDatepicker;
	public CheckBox completionCheckbox;
	public TextArea descriptionArea;
	public Hyperlink checklistLink;
	public VBox checklistContainer;
	public Label progressLabel;
	public ProgressBar progressBar;
	public ListView<ActionM> actionsLV;
	public Hyperlink actionLink;

	private ObservableList<ActionM> observableList;

	private boolean bDuePresent;
	private boolean bChecklistPresent;

	private TaskM taskM;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		observableList = FXCollections.observableArrayList();

		actionsLV.setItems(observableList);
		actionsLV.setCellFactory(actionsLV -> new ActionC(this));

		dueLink.setOnAction(e-> onDueLink());
		checklistLink.setOnAction(e-> onChecklistLink());
		actionLink.setOnAction(e-> onActionLink());
	}

	private void setProgress()
	{
		if (taskM.getChecklist() == null)
		{
			progressLabel.setText("N/A");
			progressBar.setProgress(0);
		}
		else
		{
			progressLabel.setText(String.format("%.2f%s", taskM.getChecklist().getProgress() * 100, "%"));
			progressBar.setProgress(taskM.getChecklist().getProgress());
		}
	}

	public void setTaskM(TaskM taskM)
	{
		this.taskM = taskM;

		nameField.setText(taskM.getName());
		if (taskM.getDue() != null)
		{
			onDueLink();
			dueDatepicker.setValue(taskM.getDue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		completionCheckbox.setSelected(taskM.isCompleted());
		descriptionArea.setText(taskM.getDescription());
		if (taskM.getChecklist() != null)
		{
			onChecklistLink();
			observableList.clear();
			observableList.addAll(taskM.getChecklist().getActions());
		}
		setProgress();
	}

	public TaskM getTaskM()
	{
		return taskM;
	}

	public void onDueLink()
	{
		bDuePresent = !bDuePresent;

		dueLink.setText(bDuePresent ? "Delete" : "Add due date");
		dueContainer.setVisible(bDuePresent);
	}

	public void onChecklistLink()
	{
		bChecklistPresent = !bChecklistPresent;

		checklistLink.setText(bChecklistPresent ? "Delete" : "Add checklist");
		checklistContainer.setVisible(bChecklistPresent);
	}

	public void onActionLink()
	{
		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Add action to checklist");
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			observableList.add(new ActionM(result.get()));

			ChecklistM checklistM = new ChecklistM(new ArrayList<>(observableList));
			taskM.setChecklist(checklistM);

			setProgress();
		}
	}

	public void onOK()
	{
		if (taskM == null)
			taskM = new TaskM();

		if (nameField.getText().isEmpty() || descriptionArea.getText().isEmpty())
		{
			new Alert(Alert.AlertType.ERROR, "Task name and description must not be left blank.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
			return;
		}

		taskM.setName(nameField.getText());
		taskM.setDue(null);
		taskM.setCompleted(false);

		if (bDuePresent && dueDatepicker.getValue() != null)
		{
			ZoneId zone = ZoneId.systemDefault();
			Instant instant = dueDatepicker.getValue().atStartOfDay().atZone(zone).toInstant();
			taskM.setDue(Date.from(instant));
			taskM.setCompleted(completionCheckbox.isSelected());
		}
		else
		{
			taskM.setDue(null);
		}

		taskM.setDescription(descriptionArea.getText());

		if (bChecklistPresent)
		{
			ChecklistM checklistM = new ChecklistM(new ArrayList<>(observableList));
			taskM.setChecklist(checklistM);
		}
		else
		{
			taskM.setChecklist(null);
		}

		Stage stage = (Stage) anchorPane.getScene().getWindow();
		stage.close();
	}

	public void onCancel()
	{
		taskM = null;

		Stage stage = (Stage) anchorPane.getScene().getWindow();
		stage.close();
	}

	@Override
	public void deleteAction(ActionM actionM)
	{
		observableList.remove(actionM);
	}

	@Override
	public void editAction(ActionM actionM)
	{
		int idx = observableList.indexOf(actionM);

		if (idx != -1)
		{
			observableList.remove(idx);
			observableList.add(idx, actionM);
		}
	}
}