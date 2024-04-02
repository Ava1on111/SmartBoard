import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application implements EventHandler<WindowEvent>
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInV.fxml"));
			primaryStage.setScene(new Scene(loader.load()));

			SignInC signInC = loader.getController();
			primaryStage.setOnShowing(e-> signInC.onShowing());
			primaryStage.setOnHiding(e-> signInC.onHiding());

			primaryStage.setTitle("SignIn");
			primaryStage.show();

			primaryStage.setOnCloseRequest(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handle(WindowEvent windowEvent)
	{
		System.exit(0);
	}
}
