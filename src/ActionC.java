import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;

public class ActionC extends ListCell<ActionM>
{
	private FXMLLoader loader;
	@FXML
	public AnchorPane anchorPane;
	@FXML
	public CheckBox descCheckbox;

	private ActionM actionM;
	private final IChecklist iChecklist;

	ActionC(IChecklist iChecklist)
	{
		this.iChecklist = iChecklist;
	}

	@Override
	protected void updateItem(ActionM actionM, boolean empty)
	{
		super.updateItem(actionM, empty);

		this.actionM = actionM;

		if (empty || actionM == null)
		{
			setText(null);
			setGraphic(null);
			return;
		}

		if (loader == null)
		{
			try
			{
				loader = new FXMLLoader(getClass().getResource("ActionV.fxml"));
				loader.setController(this);
				loader.load();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		descCheckbox.setText(actionM.getDescription());
		descCheckbox.setSelected(actionM.isCompleted());

		setText(null);
		setGraphic(anchorPane);
	}

	public void onCompletionChanged(ActionEvent actionEvent)
	{
		actionM.setCompleted(((CheckBox)actionEvent.getSource()).isSelected());
	}

	public void onDelete(ActionEvent actionEvent)
	{
		iChecklist.deleteAction(actionM);
	}

	public void onEdit(ActionEvent actionEvent)
	{
		TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle("Smart Board");
		textInputDialog.setHeaderText("Edit action");
		Optional<String> result = textInputDialog.showAndWait();

		if (result.isPresent() && !result.get().isEmpty())
		{
			actionM.setDescription(result.get());
		}

		iChecklist.editAction(actionM);
	}
}
