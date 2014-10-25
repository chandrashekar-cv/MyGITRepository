package test1;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
	
	int Alpha;
	int Beta;
	int [] startNode=null;
	int [] endNode=null;
	int mode;
	int score=0;
	int gameMode =0;
	boolean pass = false;
	Node myMove = null;
	
	List<int[]> currentPositions= new ArrayList<int[]>();
	
	int [][]newConfig= new int[8][8];
	String[][] newStringConfig = new String[8][8];
	
	
	void calculateScore(Node parent, Agent myAgent, String player, String opponent, int depth)
	{
		copyArray(parent,null);
		
		int i=startNode[0];
		int j=startNode[1];
		
		while(i!=endNode[0] || j!=endNode[1])
		{
			switch(mode)
			{
				case 0: i=i-1;
						j=j-1;
						break;
				
				case 1: i=i-1;
						break;
						
				case 2: i=i-1;
						j=j+1;
						break;
				
				case 3: j=j-1;
						break;
						
				case 4: j=j+1;
						break;
						
				case 5: i=i+1;
						j=j-1;
						break;
					
				case 6: i=i+1;
						break;
						
				case 7: i=i+1;
						j=j+1;
			}
			
			
			newConfig[i][j]= (player.equalsIgnoreCase("x")) ? 1 : 0;
			newStringConfig[i][j] = player;
		}
		
		
			int playerScore = 0;
			int opponentScore = 0;
		
			for(int m=0;m<8;m++)
			{
				for(int n=0;n<8;n++)
				{
					if(opponent.equalsIgnoreCase(newStringConfig[m][n]))
						currentPositions.add(new int[]{m,n});
					
					if((myAgent.myPlayer.equalsIgnoreCase("x") && newConfig[m][n]==0)||(myAgent.myPlayer.equalsIgnoreCase("o") && newConfig[m][n]==1))
						opponentScore += myAgent.positionalWeights[m][n];
					else if((myAgent.myPlayer.equalsIgnoreCase("o") && newConfig[m][n]==0)||(myAgent.myPlayer.equalsIgnoreCase("x") && newConfig[m][n]==1))
						playerScore+= myAgent.positionalWeights[m][n];
				}
			}
			
			score = playerScore - opponentScore;
		
			score = (depth==myAgent.searchCutOffDepth || currentPositions.size() == 0)?score: ((player == myAgent.myPlayer)?99999:-99999);
		
	}

	void copyArray(Node parent, String player)
	{
		if(player!=null)
			currentPositions = new ArrayList<int[]>();
			
		for(int i=0;i<parent.newConfig.length;i++)
			for(int j=0;j<parent.newConfig.length;j++)
			{
				newConfig[i][j]=parent.newConfig[i][j];
				newStringConfig[i][j]=parent.newStringConfig[i][j];
				
				if(player != null && newStringConfig[i][j].equalsIgnoreCase(player))
					currentPositions.add(new int[]{i,j});
			}
	}
	
	void copyArray(Agent myAgent)
	{
		for(int i=0;i<myAgent.dataSet.length;i++)
			for(int j=0;j<myAgent.inputStringState.length;j++)
			{
				newConfig[i][j]=myAgent.dataSet[i][j];
				newStringConfig[i][j]=myAgent.inputStringState[i][j];
			}
	}

	public int compareTo(Node o2) {
		
		switch(gameMode)
		{
			case 2: return MinMaxCompareTo(o2);
			
			case 3: return AlphaBetaCompareTo(o2);
			
			default: return greedyCompareTo(o2);
		}
	}
	
	int greedyCompareTo(Node o2)
	{
		if(this.score<o2.score)
			return 1;
		else if(this.score>o2.score)
			return -1;
		else
		{
			if(this.endNode[0] > o2.endNode[0])
				return 1;
			else if(this.endNode[0] < o2.endNode[0])
				return -1;
			else
			{
				if(this.endNode[1] >= o2.endNode[1])
					return 1;
				else return -1;
			}
		}
	}

	int MinMaxCompareTo(Node o2)
	{
		if(this.endNode[0] > o2.endNode[0])
			return 1;
		else if(this.endNode[0] < o2.endNode[0])
			return -1;
		else
		{
			if(this.endNode[1] >= o2.endNode[1])
				return 1;
			else return -1;
		}
	}
	
	int AlphaBetaCompareTo(Node o2)
	{
		if(this.endNode[0] > o2.endNode[0])
			return 1;
		else if(this.endNode[0] < o2.endNode[0])
			return -1;
		else
		{
			if(this.endNode[1] >= o2.endNode[1])
				return 1;
			else return -1;
		}
	}
	
	void calculateScore(Agent myAgent, String player, String opponent)
	{
		copyArray(myAgent);
		
		int i=startNode[0];
		int j=startNode[1];
		
		while(i!=endNode[0] || j!=endNode[1])
		{
			switch(mode)
			{
				case 0: i=i-1;
						j=j-1;
						break;
				
				case 1: i=i-1;
						break;
						
				case 2: i=i-1;
						j=j+1;
						break;
				
				case 3: j=j-1;
						break;
						
				case 4: j=j+1;
						break;
						
				case 5: i=i+1;
						j=j-1;
						break;
					
				case 6: i=i+1;
						break;
						
				case 7: i=i+1;
						j=j+1;
			}
			
			
			newConfig[i][j]= (player.equalsIgnoreCase("x")) ? 1 : 0;
			newStringConfig[i][j] = player;
		}
		
		
		for(int m=0;m<8;m++)
		{
			for(int n=0;n<8;n++)
			{
				if(opponent.equalsIgnoreCase(newStringConfig[m][n]))
					currentPositions.add(new int[]{m,n});
				
				if((player.equalsIgnoreCase("x") && newConfig[m][n]==0)||(player.equalsIgnoreCase("o") && newConfig[m][n]==1))
					score = score- myAgent.positionalWeights[m][n];
				else if((player.equalsIgnoreCase("o") && newConfig[m][n]==0)||(player.equalsIgnoreCase("x") && newConfig[m][n]==1))
					score = score+ myAgent.positionalWeights[m][n];
			}
		}
		
	}
	
	
	int utilityFunction(String player, String opponent, Agent myAgent)
	{
		int score = 0;
		
		for(int m=0;m<8;m++)
		{
			for(int n=0;n<8;n++)
			{
				if((player.equalsIgnoreCase("x") && newConfig[m][n]==0)||(player.equalsIgnoreCase("o") && newConfig[m][n]==1))
					score = score- myAgent.positionalWeights[m][n];
				else if((player.equalsIgnoreCase("o") && newConfig[m][n]==0)||(player.equalsIgnoreCase("x") && newConfig[m][n]==1))
					score = score+ myAgent.positionalWeights[m][n];
			}
		}
		
		return score;
		
	}
	
}
