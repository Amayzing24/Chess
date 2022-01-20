=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: 64585800
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - 2D arrays were used to represent the 8x8 chess board. Piece
  objects were stored in tiles that had a Piece on them and null objects
  were stored in empty tiles. Iteration on the 2D arrays with out of bounds
  checking was done in the various Piece methods. For example, all diagonals
  from a given point in the 2D array were explored and handled for the Bishop
  class, and loops were broken out of as soon as a Piece or boundary was hit.

  2. File I/O - File I/O is used to store the current game state. When the user
  exits the application and reopens it, the file is read and the old game state
  is created accordingly. In the event that the file being read in is not found
  or is formatted incorrectly, the game is restored to the starting state. In
  the event that there is some error while writing the state of the game, the
  game runs fine, but does not save upon exit.

  3. Inheritance and Subtyping - This concept was used with the Piece type and
  its various subclasses (Pawn, Rook, Bishop, Knight, Queen, King). The Piece
  type was made abstract as it had some defined methods like getSide and
  move, but also several undefined methods. These undefined methods, called
  getPossibleMoves, getSquaresThreatened, and checkStatus, were the methods
  subtypes had distinct implementations for. For example, a knight's
  movement capabilities are far different than that of a rooks, and thus
  their getPossibleMoves implementations are distinct.

  4. Complex Game Logic - The chess game implements all game features,
  including valid movement, castling, en passant, checking, and checkmating.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

The Piece class is an abstract class that is extended by Pawn, Bishop, Rook,
Knight, Queen, and King. This class represented a chess piece and was able
to calculate valid moves given a position.
The ChessBoard class was the model of the game, storing the current state.
It made use of a 2D array to store all the Pieces on the board, and had
a method makeMove that updated the board with a move.
The Position class represented the row and column location on a board. Piece
objects had a Position field.
The Move class represented a move, containing the Piece being moved, its
new position, the Piece eliminated by a move if any, etc.
The GameWindow class was the GUI class. It stored a ChessBoard object and
was constantly repainting it as the game updated. It was also where the
File I/O was handled. It also updated the status label of the game.
The ChessRun class implemented Runnable. It created a JFrame with a
GameWindow object and had a status label as well.
The ChessPrint class was a testing class that ran the game on the terminal.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

Getting check, blocking, and checkmate functionality was difficult. There were
often very subtle bugs in the logic of Piece movement implementation that were
tough to solve. There were also issues handling synching up updates to the game
amongst pieces and the board.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think I was able to separate the model of the game well. The ChessBoard class
is made to be distinct from the GUI, and this is seen in how I was able to test
much of the game via playing on the terminal.
The design of the game gets a bit convoluted with the GUI and file I/O handling.
A lot of different things are going on in a few methods in the GameWindow class,
and this would certainly be something I would clean up if given the chance.
In terms of the logic, I think my implementation probably has too many moving parts.
I store Positions on the 2D array itself, but also within each Piece object, which
can cause some issues. I also have several complex Collections being used to check
possible moves of pieces, what squares are threatened by pieces, and if pieces are
checking the king or are blocked by just one piece. This design works, but it is
not easy to debug and understand. If I were to do this project again, I would
look into just simulating moves and checking if they were valid that way, which
would remove a lot of the convoluted methods in my class.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
Chess piece images were off Wikipedia:
https://commons.wikimedia.org/wiki/Category:SVG_chess_pieces