import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpC extends JFXView implements Initializable
{
	public ImageView portraitIV;
	public TextField usernameField;
	public TextField passwordField;
	public TextField firstnameField;
	public TextField lastnameField;

	private String path;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		try
		{
			path = "D:\\portrait.png";
			portraitIV.setImage(new Image(new FileInputStream(path)));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
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
					switchTo("Workspace", JSON.parseObject(instruction.getBody(), UserM.class));
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Sign up failed.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
				}
			});
		}
	}

	public void onSignUp()
	{
		String username = usernameField.getText();
		String password = passwordField.getText();
		String firstname = firstnameField.getText();
		String lastname = lastnameField.getText();

		if (!username.isEmpty() && !password.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty())
		{
			Instruction instruction = new Instruction(new UserM(username, password, firstname, lastname, path));
			instruction.setCode(Const.SIGN_UP);
			client.sendTCP(instruction);
		}
		else
		{
			new Alert(Alert.AlertType.ERROR, "All fields are compulsory cannot be left empty.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
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
}