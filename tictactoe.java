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
		
	/*for loop to check if the 2 out of 3 winning combination is
	* filled and the last position is empty
	* first two params are compared and the third has to be empty
	*/

	public boolean checkCombinations(int one, int two, int three){
		if(board[one] == board[two] &&
	       board[one] == player &&
		   board[three] != computer)
			return true;
		else
			return false;			
	}

	/*Check if game is over.
	*if count = 9, board is filled and without a winner, its a draw.
	*if isWinner is true, there is a winner.
	*/
	public boolean keepPlaying(){
		boolean gameOver = false;
		String playAgain;
		if(count >= TURNS){
			printBoard();
			System.out.println("SORRY! It's a draw!  Play again? (y/n)");
			playAgain = s.next();
			if(playAgain.toLowerCase().equals("n"))
				gameOver = true;
			else{
				resetBoard();
				gameOver = false;
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
				gameOver = true;
			else{
				resetBoard();
				gameOver = false;
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
	* 1. first priority is center
	* 2. if player put center first
	*	a. must put corner spot
	* 3. if player doesnt put center
	*   a. AI place center, AI must be careful of 2nd move.
	*  
	*/
		
	public void initialAiMove(){
		int computerMove = 0;
		if(positionEmpty(CENTER)){
			board[CENTER] = computer;
			count++;
		}
		else{
			for(int i = 0; i <= TURNS; i += 2){
				if(i!=4)
					if(positionEmpty(i))
						computerMove = i;
			}
			board[computerMove] = computer;
			System.out.println("Make initial move on " + computerMove);
			count++;
		}
	}

	/* Offensive AI move
	* Only if player doesn't force computer to make a move
	* to prevent player from winning
	* Check if its a set up, otherwise safe to make offensive move.
	*/
	
	public int AiWin(){
		int computerMove = -1;			
		
		for(int i = 0; i < COMBINATIONS; i++){
		
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
			
			if(board[winNumOne] == board[winNumTwo] &&
			   board[winNumOne] == computer &&
			   board[winNumThree] != computer)
				computerMove = winNumThree;
			else if (board[winNumTwo] == board[winNumThree] &&
					   board[winNumTwo] == computer && 
					   board[winNumOne] != computer)	
				computerMove = winNumOne;
			else if (board[winNumOne] == board[winNumThree] &&
					   board[winNumThree] == computer && 
					   board[winNumTwo] != computer)
				computerMove = winNumTwo;
		}
		return computerMove;
	}

	public void makeOffensiveMove(){
		int computerMove = 0;
		if(AiWin() == -1){
			System.out.println("Ai win is -1");
				for(int j = 0; j < TURNS; j++){
					if(board[j] == computer){
						for(int i = 0; i < COMBINATIONS; i++){
									
							int winNumOne = win[i][0];
							int winNumTwo = win[i][1];
							int winNumThree = win[i][2];
				
							if(board[j] == board[winNumOne] &&
								   positionEmpty(winNumTwo) &&
								   positionEmpty(winNumThree)){
							   if(checkMove(winNumThree,winNumTwo))
									computerMove = winNumThree;
							   else if(checkMove(winNumTwo,winNumThree))
									computerMove = winNumTwo;
							}
							else if(board[j] == board[winNumTwo] &&
									positionEmpty(winNumOne) &&
									positionEmpty(winNumThree)){
							   if(checkMove(winNumOne,winNumThree))
									computerMove = winNumOne;
							   else if(checkMove(winNumThree,winNumOne))
									computerMove = winNumThree;	
							}
							else if(board[j] == board[winNumThree] &&
									positionEmpty(winNumTwo) &&
									positionEmpty(winNumOne)){
							   if(checkMove(winNumTwo,winNumOne))
									computerMove = winNumTwo;
							   else if(checkMove(winNumOne,winNumTwo))
									computerMove = winNumOne; 
							}
						}
					}
				}
		}
	  else if(AiWin() != -1){
			System.out.println("Aiwin is not -1");
			board[AiWin()] = computer;	
	  }
	  //If all fails, make random move for inevitable draw
	  else if(computerMove == 0){
		for(int k = 0; k < TURNS; k++){
			if(positionEmpty(k))
				computerMove = k;
		}
	  }
	  
	  System.out.println("Make Offensive Move on " + (computerMove));
	  board[computerMove] = computer;
	  count++;
	}

	/* computer can accidently set up an automatic loss, so have to 
	*  Check through this method first if the move is ok.
	*/

	public boolean checkMove(int compPos, int playerPos){
		//if count > 2, then cannot do.
		int c = 0;
		//making replica of board
		char tempBoard[] = new char[]{'1','2','3','4','5','6','7','8','9'};

		for(int j = 0; j < TURNS; j++){
			tempBoard[j] = board[j];
		}

		tempBoard[compPos] = computer;
		tempBoard[playerPos] = player;
		
		for(int i = 0; i < COMBINATIONS; i++){
						
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
		
			if(tempBoard[winNumOne] == tempBoard[winNumTwo] &&
			   tempBoard[winNumOne] == player &&
			   tempBoard[winNumThree] != computer)
							c++;
			else if (tempBoard[winNumTwo] == tempBoard[winNumThree] &&
					   tempBoard[winNumTwo] == player && 
					   tempBoard[winNumOne] != computer)	
							c++;
			else if (tempBoard[winNumOne] == tempBoard[winNumThree] &&
					   tempBoard[winNumThree] == player && 
					   tempBoard[winNumTwo] != computer)	
							c++;
		}
	//reset possible move
	if(c >= 2){
		return false;
	}
	else{
		return true;
	}
  }







	/* Defensive Move
	* 
	*/

	public boolean makeDefensiveMove(){
		boolean forced = false;
		int computerMove = 0;	
		for(int i = 0; i < COMBINATIONS; i++){
			
			int winNumOne = win[i][0];
			int winNumTwo = win[i][1];
			int winNumThree = win[i][2];
		
			if(checkCombinations(winNumOne,winNumTwo,winNumThree)){
				computerMove = winNumThree;
				forced = true;
			}
			else if (checkCombinations(winNumTwo,winNumThree,winNumOne)){	
				computerMove = winNumOne;
				forced = true;
			}
			else if (checkCombinations(winNumOne,winNumThree,winNumTwo)){	
				computerMove = winNumTwo;
				forced = true;
			}
		}
		System.out.println("Computer has made a defensive move on " + computerMove);
		board[computerMove] = computer;
		count++;
		return forced;
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


	
	/* Ai move
	*/

	public void Aimove(){
		if(count < 3)
			initialAiMove();
		else if(checkThreat())
			makeDefensiveMove();
		else
			makeOffensiveMove(); 	
	}
}
















