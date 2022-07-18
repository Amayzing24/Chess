# Chess Board Game

This chess game was initially created for a project in CIS 120 and then improved. It includes the following features:
- Fully functional chess including checkmates, en passants, castling, and promotions
- The ability to exit the game and resume where you left off
- Undoing moves
- A button to have the AI make a move (more about the AI below)

## How To Run

Simply run the Game.java file (found in src/main/java/org/cis120/Game.java)

## Chess AI

The game utilizes a minimax AI with alpha-beta pruning to reduce computation time. The evaluation function is from https://www.chessprogramming.org/Simplified_Evaluation_Function - it takes into account the material value of a piece and its position on the board.
