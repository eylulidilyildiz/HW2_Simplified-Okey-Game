import java.util.ArrayList;

public class Player {
    private String playerName;
    private Tile[] playerTiles;
    private int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        for (int i = 0; i < playerTiles.length; i++)
        {
            Tile t = new Tile(0);
            playerTiles[i] = t;
        }
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: checks this player's hand to determine if this player is winning
     * the player with a complete chain of 14 consecutive numbers wins the game
     * note that the player whose turn is now draws one extra tile to have 15 tiles in hand,
     * and the extra tile does not disturb the longest chain and therefore the winning condition
     * check the assigment text for more details on winning condition
     */
    public boolean checkWinning() {
        int playersLongestChain = this.findLengthOfLongestChain();

        return playersLongestChain >= 14;
    }

    /*
     * Used for finding the length of the longest chain in this player hand
     */
    public int findLengthOfLongestChain() {
        Tile[] longestChain = this.findLongestChain();
        return longestChain.length;
    }


        /*
     * TODO: used for finding the longest chain in this player hand
     * this method should iterate over playerTiles to find the longest chain
     * of consecutive numbers, used for checking the winning condition
     * and also for determining the winner if tile stack has no tiles
     */
    public Tile[] findLongestChain() {
        int lengthOfLongestChain = 0;
        ArrayList<Tile> longestChain = new ArrayList<Tile>();
        ArrayList<Tile> currentChain = new ArrayList<Tile>();

        Tile previousTile = this.playerTiles[0];
        longestChain.add(this.playerTiles[0]);
        currentChain.add(this.playerTiles[0]);

        int lengthOfCurrentChain = 1;

        for (int i = 1; i < this.numberOfTiles; i++) //Can be checked since the tiles are kept sorted in ascending order
        {
            Tile currentTile = this.playerTiles[i];

            if(currentTile.canFormChainWith(previousTile)) 
            {
                currentChain.add(currentTile);
                lengthOfCurrentChain ++;
            }
            else
            {
                if(lengthOfCurrentChain > lengthOfLongestChain)
                {
                    lengthOfLongestChain = lengthOfCurrentChain;
                    longestChain = new ArrayList<Tile>(currentChain);
                }
                lengthOfCurrentChain = 1;
                currentChain.clear();
                currentChain.add(currentTile);
            }

            previousTile = currentTile;
        }

        Tile[] longestChainArray = new Tile[lengthOfLongestChain];
        for (int i = 0; i < lengthOfLongestChain; i++)
        {
            longestChainArray[i] = longestChain.get(i);
        }
        return longestChainArray;
    }



    /*
     * TODO: removes and returns the tile in given index position
     */
    public Tile getAndRemoveTile(int index) {
        Tile tileBeingRemoved = this.playerTiles[index];

        for (int i = index; i < this.playerTiles.length - 1 ; i ++)
        {
            this.playerTiles[i] = this.playerTiles[i + 1];
        } 
        this.playerTiles[this.playerTiles.length - 1] = null;
        this.numberOfTiles --;

        return tileBeingRemoved;
    }

    /*
     * TODO: adds the given tile to this player's hand keeping the ascending order
     * this requires you to loop over the existing tiles to find the correct position,
     * then shift the remaining tiles to the right by one
     */
    public void addTile(Tile t) {

        //finding t's position by looping over the existing tiles
        int indexOfTileBeingChecked = 0;
        boolean tilePositionIsFound = false;
        while(!tilePositionIsFound && indexOfTileBeingChecked < 15)
        {
            Tile tileBeingChecked = this.playerTiles[indexOfTileBeingChecked];
            int resultOfComparison = t.compareTo(tileBeingChecked);

            if(resultOfComparison == 1)
            {
                indexOfTileBeingChecked ++;
            }
            else{ // (resultOfComparison == 0) || (resultOfCompaison == -1)
                tilePositionIsFound = true;
            }
        }
        

        //shifting the remaining tiles to the right and adding the Tile t to its position
        for (int i = this.playerTiles.length - 1; i > indexOfTileBeingChecked; i--)
        {
            this.playerTiles[i] = this.playerTiles[i - 1];
        }
        this.playerTiles[indexOfTileBeingChecked] = t;
        this.numberOfTiles ++;
    }

    /**
     * This method helps to add the initial tiles while they are
     * first being distributed to the players.
     * @param t
     */
    public void setTile (Tile t)
    {
        int indexOfTile = 0;
        boolean isTileAdded = false;

        while (!isTileAdded) 
        {
            if (this.playerTiles [indexOfTile].getValue() == 0)
            {
                this.playerTiles [indexOfTile] = t;
                isTileAdded = true;
            }
            else
            {
                indexOfTile++;

            }
        }

        this.numberOfTiles++;
    }


    public void sortInitialTiles ()
    {
        //Putting tiles in ascending order first
        for (int i = 0; i < this.numberOfTiles - 1; i++)
        {
            for (int j = i + 1; j < this.numberOfTiles; j++)
            {
                if (this.playerTiles[i].getValue() > this.playerTiles[j].getValue() && this.playerTiles [j].getValue() != 0 && this.playerTiles [i].getValue() != 0)
                {
                    Tile temp = this.playerTiles[i];
                    this.playerTiles[i] = this.playerTiles[j];
                    this.playerTiles [j] = temp;
                }
            } 
        } 

        

    }

    /*
     * finds the index for a given tile in this player's hand
     */
    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /*
     * displays the tiles of this player
     */
    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
