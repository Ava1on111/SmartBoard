import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileC extends JFXView
{
	public AnchorPane anchorPane;
	public ImageView portraitIV;
	public TextField usernameField;
	public TextField firstnameField;
	public TextField lastnameField;

	private String path;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{

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
					switchTo("Workspace", user);
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Update failed.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
				}
			});
		}
	}

	public void passParam(Object object)
	{
		if (object instanceof UserM)
		{
			user = (UserM)object;
			portraitIV.setImage(new Image(new ByteArrayInputStream(user.getPhoto())));
			usernameField.setText(user.getUsername());
			firstnameField.setText(user.getFirstname());
			lastnameField.setText(user.getLastname());
		}
	}

	public void onPortrait()
	{
		try
		{
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Accept *.png *.jpg *.bmp only", "png", "jpg","bmp");
			fileChooser.setFileFilter(fileNameExtensionFilter);

			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				path = fileChooser.getSelectedFile().getPath();
				portraitIV.setImage(new Image(new FileInputStream(path)));
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void onUpdate()
	{
		String firstname = firstnameField.getText();
		String lastname = lastnameField.getText();

		if (!firstname.isEmpty() && !lastname.isEmpty())
		{
			user.setFirstname(firstnameField.getText());
			user.setLastname(lastnameField.getText());

			if (path != null)
				user.setPhoto(path);

			Instruction instruction = new Instruction(user);
			instruction.setCode(Const.UPDATE_PROFILE);
			client.sendTCP(instruction);
		}
		else
		{
			new Alert(Alert.AlertType.ERROR, "All fields are compulsory cannot be left empty.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
		}
	}

	public void onCancel()
	{
		switchTo("Workspace", user);
	}
}
