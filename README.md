# Numerate

Numerate a simple, number-driven rearrangement game written in Java with Swing and AWT, similar to Sudoku.

## The Rules

Numerate operates with a simple grid of numbers. Grids are always square, and if you have a 4x4 grid, you will have, for example, four 1's, four 2's etc. up to 4. The pieces are arranged manually at the start of a game. You may move pieces by swapping them out with other pieces, according to the following rules:

1. Even numbered pieces can only move up-down-left-right, odd numbered pieces can only move diagonally.
2. Pieces can only move against pieces that are of lesser value than it. That means that 1's can't move against anything.
3. The highest valued piece cannot move against 1.
4. After a piece cannot move against another piece, it is blocked at that point and can't go any further.

These rules will make more sense once you start playing.

You must use these rules to win at certain game modes. These are:

* Line-Up: Every row or column must consist entirely of one number, and these numbers must clearly ascend or descend.
* Alternation: Every row or column must clearly alternate between two numbers.
* Scatter: No piece should be touching the same piece in either the up, down, left or right directions.
* Summation: Every row or column must have every piece on it.

You are scored according to how long you take and how many moves you take.

## The Code

The code could not be simpler to compile - you simple run javac NumerateGame.java. It should automatically build all the source files and produce a playable NumerateGame.

The game is not 100% complete, but it is fully playable. You won't be able to save high scores or play multiple games without restarting the application, but the game modes themselves, along with the interface, are complete. The code is of generally good quality (hard not to be for a small project like this), so enjoy!
