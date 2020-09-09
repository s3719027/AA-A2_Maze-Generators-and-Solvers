# AA_Assignment2

Source code encapsulating the requirements for the completion of Algorithms and Analysis - Assignment 1 (S1 2019).

The project required me to build an application - generator more specifically, that can generate a perfect maze given size constraints.
Two maze generators (Kruskal and HuntAndKill) are written from pre-existing algorithms to generate a perfect maze

Additionally, a maze solver was written to navigate the maze and solve it.

To compile on server:
javac -cp .:mazeSolver/SampleSolver.jar *.java

To run on server:
java -cp .:mazeSolver/SampleSolver.jar MazeTester inputFilename n
