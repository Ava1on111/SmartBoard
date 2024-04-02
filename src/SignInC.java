import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInC extends JFXView
{
	public TextField usernameField;
	public TextField passwordField;

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
					switchTo("Workspace", JSON.parseObject(instruction.getBody(), UserM.class));
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Sign in failed.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
				}
			});
		}
	}

	@FXML
	void onSignIn()
	{
		String username = usernameField.getText();
		String password = passwordField.getText();

		if (!username.isEmpty() && !password.isEmpty())
		{
			client.sendTCP(new Instruction(new UserM(username, password)));
		}
		else
		{
			new Alert(Alert.AlertType.ERROR, "Username and Password cannot be left empty.", new ButtonType("OK", ButtonBar.ButtonData.YES)).show();
		}
	}

	@FXML
	void onSignUp()
	{
		switchTo("SignUp");
	}
}
