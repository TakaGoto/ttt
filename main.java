/*Takayuki Goto
*tictactoe with unbeatable AI
*
*The AI will have three types of moves
*Initial, Defensive and Offensive.
*
*	1. Initial is the first three turns of the game
*	
*	2a.(POSSIBLE)4th turn phase.  Important turn, have to watch out for
*  	   possible set up.
*
*	2b. Defensive is done when the player is about to win with one
*      more move left.
*
*	3. Offensive is done when there are no threats by the player
*
*Under a while loop, the game will continue as long as it hasn't
*reached 9 total turns or there is a clear winner. 
*
*For the time being, player will always play first.  
*
*/




public class main{


	public static void main(String[] args){

	tictactoe game = new tictactoe();
	
	System.out.println("Time to play Tic Tac Toe!");
	System.out.println("You will play the first move, so you are 'X'");

		while(game.keepPlaying()){
			game.printBoard();
			game.makeMove();
			if(game.count != game.TURNS)
				game.AiMove();
		}

  }	

}

