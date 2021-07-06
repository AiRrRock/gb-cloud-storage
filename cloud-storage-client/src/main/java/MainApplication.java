import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.aborichev.controllers.MainController;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("./fxml/MainWindow.fxml"));;
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/CSS/BasicTheme.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/CSS/MainWindowTheme.css").toExternalForm());
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        MainController controller = loader.getController();
        primaryStage.setOnCloseRequest((event) -> controller.shutdown());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cloud storage client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
