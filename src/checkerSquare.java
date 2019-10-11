import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class checkerSquare {
	
	//The rectangle that is drawn to represent the square
	Rectangle drawRect;
	//X Coordinate of the square
	int xCord;
	//Y Coordinate of the square
	int yCord;
	//Holds a checker piece that might exist on the square
	public checkerPiece placedCheckerPiece;
	//Keeps track of if a checker piece is placed on this square or not
	public boolean hasPlacedPiece;
	//Holds the gridPane to draw to
	GridPane gridPane;
	//Holds the checker game object
	checkerGame checkerGameObj;
	
	//checkerSquare Constructor
	public checkerSquare(int x, int y, GridPane gridPane,BorderPane borderPane,HBox botBaner,checkerGame checkerGameObj)
	{
		this.checkerGameObj = checkerGameObj;
		this.gridPane = gridPane;
		xCord = x;
		yCord = y;
		//We create a rectangle to draw the actual square
		drawRect = new Rectangle();
		drawRect.setHeight(10);
		drawRect.setWidth(10);
		drawRect.setArcHeight(25);
		drawRect.setArcWidth(25);
		drawRect.widthProperty().bind(borderPane.widthProperty().divide(8));
		drawRect.heightProperty().bind(borderPane.heightProperty().divide(8).subtract(botBaner.heightProperty().divide(8)));
		if(((xCord+yCord) %2) == 0) {drawRect.setFill(Color.TAN);}
		else {drawRect.setFill(Color.DIMGREY);}
		drawRect.setStroke(Color.BLACK);
		//if the rectangle is one of the play area rectangles, then we add an event so it can be clicked on to allow the player to move
		if((xCord+yCord) %2==0) 
		{
			drawRect.setOnMouseClicked(e -> 
			{
				//if a piece is selected and this rectangle is highlighted, then we can call the movement method
				if(checkerGameObj.isAnySelected == true && drawRect.getStroke().equals(Color.CYAN)) 
				{
					drawRect.setStroke(Color.BLACK);
					checkerGameObj.currentselectedpiece.updatePosition(xCord, yCord); 
				}
			}); 
		}
	//End of Constructor
	}
	//A method that removes the currently placed checker piece from the game
	public void deleteHeldPiece() 
	{
		gridPane.getChildren().remove(placedCheckerPiece.drawCircle);
	}
//End of Class
}
