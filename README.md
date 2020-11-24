# Basic Algorithms
These are homework assignments I submitted in for my Spring 2018 Basic Algorithms class taken at NYU. All programming assignments were submitted through Hackerrank.

## Problem Descriptions:
1) Given two planet names, return the list of planet names and entrance fees of all planets between the two queried names, in lexicographical order. The program utilizes TwoThree trees that were discussed in class.<br>

**Input format**:<br>
The first line is a number *n*<br>
The next *n* lines are of the form: *planetName* *entranceFeeOfPlanet*<br>
The next line is a number *m*<br>
The next *m* lines are of the form: *planetName1* *planetName2* (Note: not all of these planets have been specified in the *n* lines)<br>

**Output format**:<br>
For each of the *m* lines, the program prints the names of the planets and the entrance fees that are inclusively between *planetName1* *planetName2*.

2) Similarly to assignment 1, this program utilizes the same TwoThree trees to store planets' names, their entrance fees and the updated fees between two planets.<br>

**Input format**:<br>
The first line is a number *n*<br>
The next *n* lines are of three different forms
- 1 *planetName* *entranceFeeOfPlanet*
- 2 *planetNameOne* *planetNameTwo* *amountToIncreaseEntranceFeeForAllPlanetsBetweenBy*
- 3 *planetName*

**Output format**:<br>
For each query of type 3, the program prints the entrance fee of the planet and will print "-1" if the planet is not stored in the tree.

3) Using the BinaryHeap given to the class, this program creates a HashHeap to store and eliminate candidates that do not possess a score above the minimum requirement.<br>

**Input format**:<br>
The first line is a number *n*<br>
The next *n* lines are of the form: *candidateName* *originalEvaluation*<br>
The next line is a number *m*<br>
The next *m* lines are of two different forms:<br>
- 1 *candidateName *evaluationImprovementAmount*
- 2 *baselineEvaluation* (Note: all current candidates whose scores are less than the new baseline evaluation are eliminated)

**Output format**:<br>
The program outputs the number of qualified candidates for each *m* line of type 2.

4) Using the same BinaryHeap given in assignment #3, this program implements topological sorting.<br>

**Input format**:<br>
The first line is two numbers of the form: *n* *m*<br>
The next *m* lines are of the form: *a* *b* (Note: *a* & *b* are inclusively between 1 and *n*)<br>
*n* denotes the number of stages the recipe calls for, while each line of *a*,*b* means that stage *a* must be completed before stage *b*<br>

**Output format**:<br>
The program prints out the stages in order of completion.

5) Using the same BinaryHeap given in assignment #3, this program implements DFS to check if a graph has a cycle.<br>

**Input format**:<br>
The first line is two numbers of the form: *n* *m*<br>
The next *m* lines are of the form: *a* *b* (Note: *a* & *b* are inclusively between 1 and *n*)<br>
*n* denotes the number of rooms, *m* denotes the number of passages between rooms(edges), and each line of *a*,*b* means that room *a* points to room *b*<br>

**Output format**:<br>
If the graph doesn't have a cycle, the program prints "0". If there is a cycle, the program prints "1" on the first line, and on the second line, the program prints out, in a single line separated by a white space, all the rooms which lead to a previous room.

6) Using the Graph and Heap class discussed in class, this program implements a modified Djikstras algorithm. The goal for the program is to hit at least one reservoir and avoid as many dry roads as possible.<br>

**Input format**:<br>
The first line is two numbers of the form: *n* *m*<br>
The next *m* lines are of the form: *a* *b* *c*<br>
*n* denotes the number of crossroads, *m* denotes the number of roads connecting them, and each line of *a*,*b*,*c* means that there is a road from *a* to *b* and *c* is 1 if there is a reservoir or 2 if the road is parched.<br>
The group starts at vertex number 1 and needs to reach vertex number *n*.<br>

**Output format**:<br>
The program prints the minimum number of parched roads the group would need to hit. If there isn't a path with at least one reservoir, then the program prints "-1".