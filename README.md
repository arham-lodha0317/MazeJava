# MazeJava
Maze solving algorithm, that takes in a black and white photo and outputs a solved maze. 

Maze class takes in a maze with 2 criteria:
1. The Enterance and exit are on the first and last rows of the maze respectively
2. The Maze is surrounded by a wall

During runtime the Maze class does:
1. Creates a simplified graph datastructure, with extraneous nodes eradicated like nodes that have only 2 neighbors and some common dead ends
2. Does search algorithm
3. Returns given search algorithm's picture.

The pictures were gotten from Computerphiles mazes. 
