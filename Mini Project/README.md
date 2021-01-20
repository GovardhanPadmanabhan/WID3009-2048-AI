# 2048 AI
This project is the submission for the mini-project for WID3009: AI Game Programming  

This project plays the game 2048 using an AI algorithm (Min-Max algorithm with a-b pruning)  


# 2048
A single-player sliding tile puzzle game designed by Gabriele Cirulli.

It's a 4x4 grid with 2-power valued tiles populating it. The player needs to swipe the tiles in a possible direction, and try to combine tiles to get 2048 or greater.  
(think of it as candy crush for math fanatics)

It's available on various platforms. You can play the original browser version [here](https://play2048.co/).  


# Requirements
  
*   Python Notebook
*   Selenium Library
*   Chrome Browser
*   Chrome Web Driver
*   Internet Connection  

For Selenium to run the browser, the Chrome web driver is needed, which can be downloaded from [here](https://chromedriver.chromium.org/downloads).

***Note!!***    Here, it is coded such that the downloaded `.exe` file (for windows) is in the same directory as that of the notebooks. If otherwise, need to change the respective paths in the notebook.  


# Execution

***Important!!*** Before running the notebooks, make sure the path to the web driver is correct.

To run the program, just have to run the entire notebooks. The game will show up in a new chrome window pop-up, and will automatically start playing the game.  
  
`2048 AI (with a-b pruning).ipynb` notebook implements the minimax algorithm for 2048 with a-b pruning  
`2048 AI (without a-b pruning).ipynb` notebook implements the minimax algorithm for 2048 without a-b pruning