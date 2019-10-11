import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class mainClass extends Application{

	//Start Method that replaces the main
	@Override
	public void start(Stage mainStage) 
	{
		//SetUp a BorderPane to draw to
			BorderPane mainBorderPane = new BorderPane();
			mainBorderPane.setMinSize(0, 0);
			Scene mainScene = new Scene(mainBorderPane,500,500);
			mainStage.setScene(mainScene);
		//SetUp an HBox to use as the Banner
			HBox bottomBannerBox = new HBox();
			bottomBannerBox.setStyle("-fx-background-color: #4D9900;");
		//Setup an HBox to show the win text
			HBox winBox = new HBox();
			winBox.setStyle("-fx-background-color: #4D9900;");
		//CheckerGame Object
			checkerGame checkerGameObj = new checkerGame(8,mainBorderPane,bottomBannerBox,winBox);
		//Buttons on the Banner
			Button newGameUS = new Button();
			newGameUS.setText("New Game \n 2 Players");
			newGameUS.setOnMouseClicked(e ->{
				checkerGameObj.startGame();
			});
			bottomBannerBox.getChildren().add(newGameUS);
		//BackGround Rectangle
			Rectangle backRectangle = new Rectangle();
			backRectangle.heightProperty().bind(mainScene.heightProperty());
			backRectangle.widthProperty().bind(mainScene.widthProperty());
			backRectangle.setFill(Color.BROWN);
		//Add everything to the main Pane for Display
			mainBorderPane.getChildren().add(backRectangle);
			mainBorderPane.setCenter(checkerGameObj.gridPane);
			mainBorderPane.setTop(bottomBannerBox);
			mainStage.setTitle("Checkers");
			mainStage.show();
	//End of Start Method
	}
	//Main method to run from an IDE
	public static void main(String[] args) {launch();}
//End of Class
}
