![alt text](https://github.com/Amayzing24/Chess/blob/main/files/chess%20gif.gif "Chess Demo")

# Chess Board Game

This chess game was initially created for a project in CIS 120 and then improved. It includes the following features:
- Fully functional chess including checkmates, en passants, castling, and promotions
- Ability to exit the game and resume where you left off
- Undoing moves
- AI moves (more about AI below)

## How To Run

Either open the jar file (`out/artifacts/hw09_local_temp_jar/hw09_local_temp.jar`) or run Game.java (`src/main/java/org/cis120/Game.java`).

## Chess AI

The AI utilizes the minimax algorithm with a search depth of 4 (can be adjusted in `ChessAI.java`). Essentially, it looks ahead upto 4 moves, evaluating each board state at the end of each sequence of moves. It will then propogate certain moves up the decision tree depending on their score. For moves by the current side, it will choose the highest score, and for moves by the opposide side, it will choose the lowest score.

The evaluation function takes into account material value of pieces as well as their positions. It was taken from [Chess Programming Wiki](https://www.chessprogramming.org/Simplified_Evaluation_Function).
