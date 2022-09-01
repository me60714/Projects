## Gomoku  

Gomoku, also known as "Five in a raw," is a board game traditionally played with Go pieces (black and white stones) on a Go board. It uses a 15×15 board.  

Players need to place a stone of black or white on an empty intersection in turns. The winner is the one who forms an unbroken chain of five stones horizontally, vertically, or diagonally.

In this project, I practiced the concept of the character array. It seemed easy before I started implementing it, and I finally faced some problems and figured them out.

The most challenging part is how to implement the isWin function. If the player places the stone consisting of a line of five stones, then the player wins. Therefore, the isWin function must identify other stones four units away from that stone in every direction. That would be all the possibilities that if that stone may cause win.

It can be demonstrated as:

             *       *       *
               *     *     *
                 *   *   *
                   * * *
             * * * * * * * * *
                   * * *
                 *   *   *
               *     *     *
             *       *       *


I then created four character arrays to indicate four different directions. After that,  
1. I set the middle index of the arrays to the current stone.  
2. Place the surrounding stones in these arrays.  
3. I write a function to identify if there are five same continuous stones.

This is the brief instruction on this project and how I implement it.
