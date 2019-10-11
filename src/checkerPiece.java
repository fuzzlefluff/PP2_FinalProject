import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class checkerPiece {
	
	//The Circle that is drawn to represent the checker piece
	Circle drawCircle;
	//keeps track of if the piece is selected
	boolean isSelected;
	//keeps track of if the player and de-select the piece
	static boolean canDeSelect;
	//keeps track of it the piece is a king
	boolean isKing;
	//keeps track of which team the piece is on
	boolean isBlack;
	//The Xcord of the checker piece
	int xCord;
	//The Ycord of the checker piece
	int yCord;
	//holds the grid pane that is drawn to the screen
	GridPane gridPane;
	//holds the checker game object
	checkerGame checkerGameObj;
	
	//Checker piece constructor
	public checkerPiece(int x, int y, GridPane gridPane, checkerGame checkerObj )
	{
		xCord = x;
		yCord = y;
		isKing = false;
		canDeSelect = true;
		checkerGameObj = checkerObj;
		this.gridPane = gridPane;
		//create a Circle to draw the piece
		drawCircle = new Circle(); 
		drawCircle.setFill(Color.RED);
		isBlack = false;
		drawCircle.setStroke(Color.BLACK);
		drawCircle.setRadius(10);
		drawCircle.radiusProperty().bind(gridPane.widthProperty().divide(20));
		drawCircle.centerXProperty().bind(gridPane.widthProperty().divide(8));
		drawCircle.centerYProperty().bind(gridPane.heightProperty().divide(8));
		//We create an event so that if the player clicks on the circle then the piece becomes selected or de-selected
		drawCircle.setOnMouseClicked(IntputEvent -> 
			{
				//Check to see if nothing else is selected and that the piece is being selected on the right turn
				if(isSelected == false && checkerGameObj.isAnySelected == false && checkerGameObj.isBlackTurn == true && drawCircle.getFill().equals(Color.BLACK)) 
				{
					drawCircle.setStroke(Color.YELLOW); 
					isSelected = true;
					checkerGameObj.isAnySelected = true;
					checkerGameObj.currentselectedpiece = this;
					checkerGameObj.highlightPositions(xCord, yCord);
				}
				//Check to see if nothing else is selected and that the piece is being selected on the right turn
				else if (isSelected == false && checkerGameObj.isAnySelected == false && checkerGameObj.isBlackTurn == false && drawCircle.getFill().equals(Color.RED)) 
				{
					drawCircle.setStroke(Color.YELLOW); 
					isSelected = true;
					checkerGameObj.currentselectedpiece = this;
					checkerGameObj.isAnySelected = true;
					checkerGameObj.highlightPositions(xCord, yCord);
				}
				//If something is selected and we are allowed to de-select, then allow the player to de-select their piece and select another
				else if(isSelected == true && canDeSelect == true) 
				{
					if(isKing == false) {drawCircle.setStroke(Color.BLACK);}
					else if (isKing == true) {drawCircle.setStroke(Color.WHITE);}
					isSelected = false;
					checkerGameObj.isAnySelected = false;
					checkerGameObj.deHighlightPositions(xCord, yCord);
				}
			});
			gridPane.add(drawCircle, x, y);	
	}
	//A Method that Moves the Piece, handles upgrading the pieces to King status and figuring out when to force doublejumps and what pieces to delete
	public void updatePosition(int x, int y)
	{
		boolean doubleJump = false;
		//variables used to keep track of the coordinates of the piece we should delete
		int oneX =0;
		int oneY =0;
		//these logic statements determine the location of the piece that was jumped over in this movement
		if(xCord > x && yCord < y ) {oneX = xCord-1; oneY = yCord+1;}
		if(xCord < x && yCord < y ) {oneX = xCord+1; oneY = yCord+1;}
		if(xCord > x && yCord > y ) {oneX = xCord-1; oneY = yCord-1;}
		if(xCord < x && yCord > y ) {oneX = xCord+1; oneY = yCord-1;}
		//Prevent us from deleting ourselves
		if(x == oneX || y == oneY) {checkerGameObj.isPieceDelete = false;}
		//If we are set to delete something, then delete it
		if(checkerGameObj.isPieceDelete == true) 
			{
				//Subtract a loss to the game's total depending on which team the piece is on
				if(checkerGameObj.currentselectedpiece.isBlack == true) {checkerGameObj.redLeft -= 1;}
				if(checkerGameObj.currentselectedpiece.isBlack == false) {checkerGameObj.blackLeft -= 1;}
				//check to see if the game is won and display the right message
				if(checkerGameObj.redLeft == 0) {checkerGameObj.endGame(); checkerGameObj.winBox.getChildren().add(checkerGameObj.blackWins); checkerGameObj.borderPane.setCenter(checkerGameObj.winBox);}
				if(checkerGameObj.blackLeft == 0) {checkerGameObj.endGame(); checkerGameObj.winBox.getChildren().add(checkerGameObj.redWins); checkerGameObj.borderPane.setCenter(checkerGameObj.winBox);}
				//remove the circle from the gridPane
				gridPane.getChildren().remove(checkerGameObj.checkerArray[oneX][oneY].placedCheckerPiece.drawCircle);
				//tell the square it doesn't have a piece anymore
				checkerGameObj.checkerArray[oneX][oneY].hasPlacedPiece = false; 
				checkerGameObj.checkerArray[oneX][oneY].placedCheckerPiece = null; 
				//tell the game object the delete was performed
				checkerGameObj.isPieceDelete = false;
				//check to see if we must force the player to jump again
				doubleJump = checkerGameObj.checkDouble(x, y);
			}
		//tell the square that we were at that is doesn't have a piece anymore
		checkerGameObj.checkerArray[xCord][yCord].hasPlacedPiece = false;
		//clean the board up for next move
		checkerGameObj.deHighlightPositions(xCord, yCord);
		//update our coordinates
		xCord = x;
		yCord = y;
		//if we must force the player to double jump, then we highlight where they can move to and prevent them from de-selecting
		if (doubleJump == true) 
		{
			checkerGameObj.highlightPositions(x, y);
			canDeSelect = false;
		}
		//if there is no other jump, we clean up and switch turns
		else if(doubleJump == false) 
		{
			canDeSelect = true;
			isSelected = false;
			if(isKing == false) {drawCircle.setStroke(Color.BLACK);}
			else if(isKing == true) {drawCircle.setStroke(Color.SNOW);}
			checkerGameObj.isAnySelected = false;
			if(checkerGameObj.isBlackTurn == false) { checkerGameObj.currentTurn.setText("Current Turn\n Black"); checkerGameObj.isBlackTurn = true;}
			else if(checkerGameObj.isBlackTurn == true) {checkerGameObj.currentTurn.setText("Current Turn\n Red"); checkerGameObj.isBlackTurn = false;}
		}
		//if the piece is meant to become a King, then King it
		if(drawCircle.getFill().equals(Color.BLACK) && y == 0) {isKing = true; drawCircle.setStroke(Color.SNOW);}
		else if (drawCircle.getFill().equals(Color.RED) && y == checkerGameObj.gridSize - 1) {isKing = true; drawCircle.setStroke(Color.WHITE);}
		//Tell the square we moved to that it now has a piece and where to find it in memory
		checkerGameObj.checkerArray[xCord][yCord].hasPlacedPiece = true;
		checkerGameObj.checkerArray[xCord][yCord].placedCheckerPiece = this;
		//Tell the gridPane to draw the piece in the new location and to stop drawing the piece in the old location
		gridPane.getChildren().remove(this.drawCircle);
		gridPane.add(this.drawCircle, x, y);
	}
//End of Class
}
