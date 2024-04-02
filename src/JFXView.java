import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class JFXView extends Listener implements Initializable
{
	public AnchorPane anchorPane;
	protected Client client;
	protected UserM user;

	protected void switchTo(String file)
	{
		switchTo(file, null);
	}

	protected void switchTo(String file, Object param)
	{
		try
		{
			String path = file + "V.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Scene scene = new Scene(loader.load());

			JFXView jfxView = loader.getController();
			jfxView.passParam(param);

			Stage stage = (Stage)anchorPane.getScene().getWindow();
			stage.hide();
			stage.setScene(scene);

			stage.setOnShowing(e-> jfxView.onShowing());
			stage.setOnHiding(e-> jfxView.onHiding());

			stage.setTitle(file);
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void passParam(Object object)
	{

	}

	public void onShowing()
	{
		try
		{
			client = new Client(1024 * 1024 * 100, 1024 * 1024 * 100);

			Kryo kryo = client.getKryo();
			kryo.register(Instruction.class);
			client.start();

			client.addListener(this);
			client.connect(1000, "127.0.0.1", 9000);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void received(Connection connection, Object object)
	{
	}

	public void onHiding()
	{
		client.close();
	}
}