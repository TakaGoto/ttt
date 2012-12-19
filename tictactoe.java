/*Takayuki Goto
* tictactoe with unbeatable 
*/
import java.util.Scanner;
public class tictactoe{
	
	public char player, computer;	
	public int count;
	public char gameWinner = ' ';
	
	static final int TURNS = 9; //maximum number of turns
	static final int COMBINATIONS = 8; //number of winning combinations	
	static final int CENTER = 4;
	
	public char board[] = new char[]{'1','2','3','4','5','6','7','8','9'};
	int win[][] = new int[][]{	
			{0,1,2},{3,4,5},{6,7,8},
			{0,3,6},{1,4,7},{2,5,8},
			{0,4,8},{2,4,6},
	};

	Scanner s = new Scanner(System.in);

	//Constructor

	public tictactoe(){
		player = 'X';
		computer = 'O';
		count = 0;
	}

	//check if empty
	
	public boolean positionEmpty(int pos){
		boolean isEmpty;
		
		if(board[pos] != computer && board[pos] != player)
			isEmpty = true;
		else
			isEmpty = false;
		return isEmpty;		
	}
		
	/*Check if game is over.
	*if count = 9, board is filled and without a winner, its a draw.
	*if isWinner is true, there is a winner.*/

	public boolean keepPlaying(){
		boolean gameOver = true;
		String playAgain;
		if(count == TURNS){
			printBoard();
			System.out.println("SORRY! It's a draw!  Play again? (y/n)");
			playAgain = s.next();
			if(playAgain.toLowerCase().equals("n"))
				gameOver = false;
			else{
				resetBoard();
				gameOver = true;
			} 		
		}
		else if(isWinner()){
			printBoard();
			if(gameWinner == computer)
			 System.out.println("Computer has won!  Play again? (y/n)");
			else if(gameWinner == player)
			 System.out.println("you have won! play again? (y/n)");
			playAgain = s.next();
			if(playAgain.toLowerCase().equals("n"))
				gameOver = false;
			else{
				resetBoard();
				gameOver = true;
			}
		}	
		return gameOver;
	}	

	public boolean isWinner(){
		boolean winner = false;
		for(int i = 0; i < COMBINATIONS; i++){
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
			
			if(board[winNumOne] == board[winNumTwo] &&
			   board[winNumTwo] == board[winNumThree] &&
			   board[winNumOne] == board[winNumThree]){
				if(board[winNumOne] == player){	
					winner = true;
					gameWinner = player;
				}
				else if(board[winNumOne] == computer){
					winner = true;
					gameWinner = computer;
				}
			}
		}
		return winner;
	}
	
	/* print board, reset board if they want to play again.
	*/

	public void printBoard(){
		for(int i = 0; i < TURNS; i++){
			System.out.print(" " + board[i] + " ");
			if((i+1)%3 == 0)
				System.out.println();
		}
	}

	public void resetBoard(){
	char tempBoard[] = new char[]{'1','2','3','4','5','6','7','8','9'};
		count = 0;		
		for(int i = 0; i < TURNS; i++){
			board[i] = tempBoard[i];
		}
	}
			
	/*Players move, check for validation
	* check if blank, or int and length
	*/

	public void makeMove(){
		int move;
		boolean moveOK = false;
				
		while(!moveOK){
			System.out.println("Enter the number of where you want to make the move.");
			move = s.nextInt();
				if(move < 1 || move > 9)
					System.out.println("You have entered the wrong number, please enter again. \n");
				else if(!positionEmpty(move-1))
					System.out.println("A move has already been placed there, try again.");
				else{
					System.out.println("You have made a move on " + move);
					moveOK = true;
					board[move-1] = player;
					count++;
				}
		}
	
	}

	/* AI initial moves
	* first priority is center, if the player put center first then put one of the four corners.*/
		
	public void initialAiMove(){
		int computerMove = -1;
			if(positionEmpty(CENTER))
				computerMove = CENTER;
			else{
				for(int i = 0; i <= TURNS; i += 2){
					if(i!=4)
						if(positionEmpty(i))
							computerMove = i;
				}
			}
		board[computerMove] = computer;
		count++;	
	}

	/* Offensive AI moves
	* AiWin() checks if Ai can win, first priority
	* checkMove() checks if the first parameter is viable move
	* makeOffensiveMove() the actual implementation of the move
	* checkOffensiveSpot() checks if Ai can threaten for a win
	* checkMoveAhead() the actual if statements to check steps ahead
	* canWin() the actual if statements to check for Ai wins
	*/
	
	public boolean checkOffensiveSpot(int one,int two, int three, int j){
		if(board[j] == board[one] &&
			 positionEmpty(two) && positionEmpty(three))
				return true;
		else
			return false;
	}

	public boolean checkMoveAhead(int one, int two, int three, char[] checkBoard){
		if(checkBoard[one] == checkBoard[two] &&
			 checkBoard[one] == player && checkBoard[three] != computer)
				return true;
		else
			return false;
	}

	public boolean canWin(int one, int two, int three){
		if(board[one] == board[two] &&
			 board[one] == computer &&
			 board[three] != player)
				return true;
		else
			return false;
	}	

	public int AiWin(){
		int computerMove = -1;			
		
		for(int i = 0; i < COMBINATIONS; i++){
		
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
			
			if(canWin(winNumOne,winNumTwo,winNumThree))
				computerMove = winNumThree;
			else if(canWin(winNumTwo,winNumThree,winNumOne))	
				computerMove = winNumOne;
			else if(canWin(winNumOne,winNumThree,winNumTwo))
				computerMove = winNumTwo;
		}
		return computerMove;
	}

	public void makeOffensiveMove(){
		int computerMove = -1;
		if(AiWin() == -1){
			for(int j = 0; j < TURNS; j++){
				if(board[j] == computer){
					for(int i = 0; i < COMBINATIONS; i++){
								
						int winNumOne = win[i][0];
						int winNumTwo = win[i][1];
						int winNumThree = win[i][2];
			
						if(checkOffensiveSpot(winNumOne,winNumTwo,winNumThree,j)){
							 if(checkMove(winNumThree,winNumTwo))
								computerMove = winNumThree;
							 else if(checkMove(winNumTwo,winNumThree))
								computerMove = winNumTwo;
						}
						else if(checkOffensiveSpot(winNumTwo,winNumOne,winNumThree,j)){
							 if(checkMove(winNumOne,winNumThree))
								computerMove = winNumOne;
							 else if(checkMove(winNumThree,winNumOne))
								computerMove = winNumThree;	
						}
						else if(checkOffensiveSpot(winNumThree,winNumTwo,winNumOne,j)){
							 if(checkMove(winNumTwo,winNumOne))
								computerMove = winNumTwo;
							 else if(checkMove(winNumOne,winNumTwo))
								computerMove = winNumOne; 
						}
					}
				}
			 }
		}
	  else if(AiWin() > -1)
			computerMove = AiWin();
	  else{
			for(int k = 0; k < TURNS; k++){
				if(positionEmpty(k))
					computerMove = k;
			}
	  } 
	  board[computerMove] = computer;
	  count++;
	}

	/* computer can accidently set up an automatic loss, so have to 
	*  Check through this method first if the move is ok.
	*/

	public boolean checkMove(int compPos, int playerPos){
		int c = 0;
		
		char tempBoard[] = new char[]{'1','2','3','4','5','6','7','8','9'};

		for(int j = 0; j < TURNS; j++)
			tempBoard[j] = board[j];
		
		tempBoard[compPos] = computer;
		tempBoard[playerPos] = player;
		
		for(int i = 0; i < COMBINATIONS; i++){
						
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
		
			if(checkMoveAhead(winNumOne,winNumTwo,winNumThree,tempBoard))
							c++;
			else if (checkMoveAhead(winNumTwo,winNumThree,winNumOne,tempBoard))	
							c++;
			else if (checkMoveAhead(winNumOne,winNumThree,winNumTwo,tempBoard))	
							c++;
		}
		if(c >= 2)
			return false;
		else
			return true;
 }

	/* Defensive Moves
	* makeDefensivemove() actual implemention of defensive move
	* checkThreat() can't do defensive move if this isn't true.  checks for any possible wins by player
	* checkCombinations() the if statement used to check the winning combination 
	*/

	public boolean checkCombinations(int one, int two, int three){
		if(board[one] == board[two] &&
	       board[one] == player &&
		   board[three] != computer)
			return true;
		else
			return false;			
	}
	
	public void makeDefensiveMove(){
		int computerMove = -1;	
		for(int i = 0; i < COMBINATIONS; i++){
			
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
		
			if(checkCombinations(winNumOne,winNumTwo,winNumThree))
				computerMove = winNumThree;
			else if (checkCombinations(winNumTwo,winNumThree,winNumOne))
				computerMove = winNumOne;
			else if (checkCombinations(winNumOne,winNumThree,winNumTwo))	
				computerMove = winNumTwo;
		}
		board[computerMove] = computer;
		count++;
	}

	public boolean checkThreat(){
	  boolean threat = false;	
		for(int i = 0; i < COMBINATIONS; i++){
			
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
		
			if(checkCombinations(winNumOne,winNumTwo,winNumThree))
				threat = true;
			
			else if (checkCombinations(winNumTwo,winNumThree,winNumOne))	
				threat = true;
			
			else if (checkCombinations(winNumOne,winNumThree,winNumTwo))	
				threat = true;	
		}
	return threat;
  }

	/* Ai move */

	public void AiMove(){
		if(count < 3)
			initialAiMove();
		else if(checkThreat()){
			if(AiWin() > -1)
				makeOffensiveMove();
			else
				makeDefensiveMove();
		}
		else
			makeOffensiveMove(); 	
	}
}
















