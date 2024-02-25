import java.util.ArrayList;
import java.util.Arrays;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        int initialTileCount = 15;
        int totalTilesDistributed = 0;
        int addTilesStartingFromIndex = 0;
        for (int i = 0; i < this.players.length; i++)
        {
            if (i != 0)
            {
                initialTileCount = 14;
            }

            for (int j = initialTileCount + addTilesStartingFromIndex - 1; j >= addTilesStartingFromIndex; j--)
            {
                this.players [i].setTile (this.tiles[j]);   
                tileCount --; 
                totalTilesDistributed ++;  
            }
            this.players [i].sortInitialTiles ();
            addTilesStartingFromIndex += initialTileCount;
        }

        Tile[] temp = new Tile[tileCount];
            for (int n = 0; n < tileCount; n++)
            {
                temp[n] = tiles[totalTilesDistributed + n];
            }
            this.tiles = new Tile[tileCount];
            this.tiles = temp; //so that the tiles given to the player is removed from the tiles remaining on the table


    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        this.players[currentPlayerIndex].addTile(lastDiscardedTile);
        
        return "" + this.lastDiscardedTile.getValue();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        if (tileCount > 0) 
        {
            Tile tileOnTop = tiles[--tileCount];
            players[currentPlayerIndex].addTile(tileOnTop);
            return tileOnTop.toString();
        } 
        else 
        {
            return null;
        }
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        for (int i = 0; i < this.tiles.length; i++)
        {
            // Forming a random indexes from 0 to 104
            int randomIndex = (int) (Math.random() * 104);

            // Swapping the i th tile with the tile on randomly chosen index
            Tile temp = new Tile (tiles [i].getValue());
            tiles [i] = tiles [randomIndex];
            tiles [randomIndex] = temp;
        }

    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */
    public boolean didGameFinish() {
        return this.players[currentPlayerIndex].checkWinning();
    }

    /* TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    public Player[] getPlayerWithHighestLongestChain() {
        int longestChain = 0;
        Player[] winners = new Player[4];
        int numWinners = 0;

        for (Player player : players) 
        {
            int chainLength = player.findLengthOfLongestChain();
            
            if (chainLength > longestChain) 
            {
                longestChain = chainLength;
                winners[0] = player;
                numWinners = 1;
            } 
            else if (chainLength == longestChain) 
            {
                winners[numWinners++] = player;
            }
        }

        if (numWinners < 4) 
        {
            winners = Arrays.copyOf(winners, numWinners);
        }

        return winners;
    }
    
    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }


    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    public void pickTileForComputer() {
        Tile discardedTile = lastDiscardedTile;
        Tile[] longestChain = this.players[currentPlayerIndex].findLongestChain();
        boolean discardTileIsUseful;
        if (discardedTile.getValue() == longestChain[0].getValue() - 1)
        {
            discardTileIsUseful = true;
        }
        else if(discardedTile.getValue() == longestChain[longestChain.length - 1].getValue() + 1)
        {
            discardTileIsUseful = true;
        }
        else{
            discardTileIsUseful = false;
        }

        if (discardTileIsUseful) 
        {
            String discardedTileValue = getLastDiscardedTile();
            System.out.println("Player picked up the discarded tile: " + discardedTileValue);
        } 
        else 
        {
            String topTileValue = getTopTile();
            System.out.println("Player picked up the top tile: " + topTileValue);
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() {
        Tile[] playerTiles = players[currentPlayerIndex].getTiles();
        Tile tileToDiscard = playerTiles[0];
        discardTile(0);
        System.out.println("Computer discarded tile: " + tileToDiscard.toString());
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        lastDiscardedTile = players[currentPlayerIndex].getAndRemoveTile(tileIndex);
        System.out.println(players[currentPlayerIndex].getName() + " discarded: " + lastDiscardedTile.getValue());
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
