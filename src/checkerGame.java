import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class checkerGame {
	
	//Keeps track of if an object is currently running a game
	static boolean isCurrentGame;
	//Keeps track of how many losses the red team has
	static int redLeft = 12;
	//Keeps track of how many losses the black team has
	static int blackLeft = 12;
	//Keeps track of who's turn it is
	boolean isBlackTurn;
	//keeps track of if any piece on the board is currently selected
	boolean isAnySelected;
	//keeps track of if the currently selected piece is ready to perform a kill jump
	boolean isPieceDelete;
	//holds the currently selected piece
	checkerPiece currentselectedpiece;
	//how big the grid should be
	int gridSize;
	//the gridpane we draw the squares and pieces too
	GridPane gridPane = new GridPane();
	//an array that stores all our checker squares
	checkerSquare[][] checkerArray;
	//a box to be displayed on the end of a game
	HBox winBox;
	//a label to be displayed when the red team wins
	Label redWins;
	//a label to be displayed when the black team wins
	Label blackWins;
	//a label that will be used to display who's turn it currently is
	Label currentTurn;
	//holds the borderPane we will draw to
	BorderPane borderPane;
	//a box that is to be the top banner
	HBox botBanner;
	
	//checkerGame constructor
	public checkerGame(int gridSize, BorderPane borderPane, HBox botBanner, HBox winBox)
	{
		//take care of our winscreen stuff
		this.winBox = winBox;
		blackWins = new Label();
		redWins = new Label();
		blackWins.setText("Black Wins");
		blackWins.setFont(Font.font(75));
		redWins.setText("Red Wins");
		redWins.setFont(Font.font(75));
		//since the object is just being constructed, a game has not been started
		isCurrentGame = false;
		//black goes first
		isBlackTurn = true;
		//create an array to the specified size
		this.gridSize = gridSize;
		checkerArray = new checkerSquare[gridSize][gridSize];
		//pass the draw groups to the objects fields
		gridPane = new GridPane();
		this.borderPane = borderPane;
		this.botBanner = botBanner;
		
		
	}
	
	//A method that starts a new game
	public void startGame() 
	{
		//make sure we clean up the last game if one was running
			if(isCurrentGame == true) {endGame();}
		//Setup the currentTurn Label
			currentTurn = new Label();
			currentTurn.setStyle("-fx-background-color: #C6E3FF;");
			currentTurn.setText("Current Turn \nBlack");
			botBanner.getChildren().add(currentTurn);
		 //fill our array with checker square objects
			for(int i =0; i<gridSize; i++) 
			{
				for(int c=0; c<gridSize; c++)
				{
					checkerArray[i][c] = new checkerSquare(i,c,gridPane,borderPane,botBanner,this);
					gridPane.add(checkerArray[i][c].drawRect,i,c);
				}
			}
		//fill the checker squares with checker pieces at the appropriate places
			for(int i =0; i<gridSize; i++)
			{
				for(int c=0; c<gridSize; c++)
				{
				if((i+c)%2==0 && (c<3||c>4))
				{
					checkerPiece piece = new checkerPiece(i,c,gridPane, this);
					if(c>4)
					{
						piece.drawCircle.setFill(Color.BLACK); piece.isBlack = true;
					}
					checkerArray[i][c].placedCheckerPiece = piece; checkerArray[i][c].hasPlacedPiece = true;
					}
				}
			}
			isCurrentGame = true;
	}
	
	//A Method that resets the proper values so a new game can be started
	public void endGame()
	{
		redLeft = 12;
		blackLeft = 12;
		borderPane.setCenter(gridPane);
		borderPane.setTop(botBanner);
		isBlackTurn = true;
		botBanner.getChildren().remove(currentTurn);
		for(int i =0; i<gridSize; i++) {for(int c=0; c<gridSize; c++){gridPane.getChildren().remove(checkerArray[i][c].drawRect);}}
		for(int i =0; i<gridSize; i++) {for(int c=0; c<gridSize; c++){if(checkerArray[i][c].hasPlacedPiece == true){gridPane.getChildren().remove(checkerArray[i][c].drawRect);}}}
	}
	
	//A Method that highlights where a piece can move to depending on it's position and the pieces around it
	public void highlightPositions(int x, int y)
	{
		//Keeps track of if there is a killjump to make so we can deny the player the option to take the non-kill jump
		boolean forceJump = false;
		//Square 5 -Square 1 KillJump
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != gridSize - 1 && y != gridSize -1) 
			{
				if(checkerArray[x+1][y+1].hasPlacedPiece == true && checkerArray[x+1][y+1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x < gridSize -2 && y < gridSize - 2)
					{if(checkerArray[x+2][y+2].hasPlacedPiece == false){forceJump = true; isPieceDelete = true; checkerArray[x+2][y+2].drawRect.setStroke(Color.CYAN);}}
			}
		//Square 6 -Square 2 KillJump
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != 0 && y != gridSize -1) 
			{
				if(checkerArray[x-1][y+1].hasPlacedPiece == true && checkerArray[x-1][y+1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x > 1 && y < gridSize - 2)
					{if(checkerArray[x-2][y+2].hasPlacedPiece == false){forceJump = true; isPieceDelete = true; checkerArray[x-2][y+2].drawRect.setStroke(Color.CYAN);}}
			}
		//Square 7 -Square 3 KillJump
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != gridSize -1 && y != 0)  
			{
				if(checkerArray[x+1][y-1].hasPlacedPiece == true && checkerArray[x+1][y-1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x < gridSize -2 && y > 1)
					{if(checkerArray[x+2][y-2].hasPlacedPiece == false){forceJump = true; isPieceDelete = true; checkerArray[x+2][y-2].drawRect.setStroke(Color.CYAN);}}
			}
		//Square 8 -Square 4 KillJump
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != 0 && y != 0)  
			{
				if(checkerArray[x-1][y-1].hasPlacedPiece == true && checkerArray[x-1][y-1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x > 1 && y > 1)
					{if(checkerArray[x-2][y-2].hasPlacedPiece == false) {forceJump = true;  isPieceDelete = true; checkerArray[x-2][y-2].drawRect.setStroke(Color.CYAN);}}
			}
		//Square 1
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != gridSize - 1 && y != gridSize -1) 
			{
				 if(checkerArray[x+1][y+1].hasPlacedPiece == false && forceJump == false) 
					{checkerArray[x+1][y+1].drawRect.setStroke(Color.CYAN);}
			}
		//Square 2
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != 0 && y != gridSize -1) 
			{
				 if(checkerArray[x-1][y+1].hasPlacedPiece == false && forceJump == false) 
					{checkerArray[x-1][y+1].drawRect.setStroke(Color.CYAN);}
			}
		//Square 3
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != gridSize -1 && y != 0)  
			{
				if(checkerArray[x+1][y-1].hasPlacedPiece == false && forceJump == false) 
					{checkerArray[x+1][y-1].drawRect.setStroke(Color.CYAN);}
			}
		//Square 4
			if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != 0 && y != 0)  
			{
				if(checkerArray[x-1][y-1].hasPlacedPiece == false && forceJump == false) 
					{checkerArray[x-1][y-1].drawRect.setStroke(Color.CYAN);}
			}
	}
	
	//A method that checks to see if a piece has another kill jump to make and returns true if it does
	public boolean checkDouble(int x, int y) 
	{
		boolean canJump = false;
		//Square 5
				if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != gridSize - 1 && y != gridSize -1) 
				{
					if(checkerArray[x+1][y+1].hasPlacedPiece == true && checkerArray[x+1][y+1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x < gridSize -2 && y < gridSize - 2)
					{if(checkerArray[x+2][y+2].hasPlacedPiece == false){canJump = true;}}
				}
				//Square 6
				if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == false) && x != 0 && y != gridSize -1) 
				{
					
					if(checkerArray[x-1][y+1].hasPlacedPiece == true && checkerArray[x-1][y+1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x > 1 && y < gridSize - 2)
					{if(checkerArray[x-2][y+2].hasPlacedPiece == false){canJump = true;}}

				}
				//Square 7
				if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != gridSize -1 && y != 0)  
				{
					
					if(checkerArray[x+1][y-1].hasPlacedPiece == true && checkerArray[x+1][y-1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x < gridSize -2 && y > 1)
					{if(checkerArray[x+2][y-2].hasPlacedPiece == false){canJump = true;}}
				}
				//Square 8
				if((currentselectedpiece.isKing == true || currentselectedpiece.isBlack == true) && x != 0 && y != 0)  
				{
					
					if(checkerArray[x-1][y-1].hasPlacedPiece == true && checkerArray[x-1][y-1].placedCheckerPiece.isBlack != currentselectedpiece.isBlack && x > 1 && y > 1)
					{if(checkerArray[x-2][y-2].hasPlacedPiece == false) {canJump = true;}}
				}
				return canJump;
	}
	
	//A Method that undoes the highlightPositions method and resets the board's values for the next move
		public void deHighlightPositions (int x, int y) 
		{
			isPieceDelete = false;
			for(int i=0; i<gridSize; i++) 
			{
				for(int c=0; c<gridSize; c++) {isPieceDelete = false; checkerArray[i][c].drawRect.setStroke(Color.BLACK);}
			}
		}

}
