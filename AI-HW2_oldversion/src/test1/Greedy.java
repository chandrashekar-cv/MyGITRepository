package test1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Greedy {
	
	void findNextGreedyMove(Agent myAgent)
	{
		PriorityQueue<Node> scores = new PriorityQueue<Node>();
		List<int []> currentPositions = myAgent.currentPositions;
		int depth=0;
		for(int i=0;i<currentPositions.size();i++)
		{
			scores.addAll(possibleMoves(myAgent, currentPositions.get(i),depth++));
		}
		
		displayMove(scores.peek());
	}
	
	
	/*
	 * Identify the possible moves in the current board configuration.
	 * For every player coin on board, identify the possible moves in all directions.
	 */
	List<Node> possibleMoves(Agent myAgent,int []myMove,int depth)
	{
		
		List<Node> positions = new ArrayList<Node>();
		int score =-999;
		int i=myMove[0];
		int j=myMove[1];
		
		
		if(depth<=myAgent.searchCutOffDepth)
		{
			
			for(int k=0;k<8;k++)
			{
				int[] newPosition = null;
				switch(k)
				{
					
					case 0: if(i>2 && j>2 && myAgent.dataSet[i-1][j-1]>=0)
							{
								newPosition = searchPosition(myAgent,i-1,j-1,k);
							}
							break;
					
					case 1: if(i>2 && myAgent.dataSet[i-1][j]>=0)
							{
								newPosition = searchPosition(myAgent,i-1,j,k);
							}
							break;
					
					case 2:	if(i>2 && j<6 && myAgent.dataSet[i-1][j+1]>=0)
							{
								newPosition = searchPosition(myAgent,i-1,j+1,k);
							}
							break;
					
					case 3:	if(j>2 && myAgent.dataSet[i][j-1]>=0)
							{
								newPosition = searchPosition(myAgent,i,j-1,k);
							}
							break;
			
					case 4:	if(j <6 && myAgent.dataSet[i][j+1]>=0)
							{
								newPosition = searchPosition(myAgent,i,j+1,k);
								
							}
							break;
							
					case 5: if(i<6 && j>2 && myAgent.dataSet[i+1][j-1]>=0)
							{
								newPosition = searchPosition(myAgent,i+1,j-1,k);
							}
							break;
			
					case 6: if(i<6 && myAgent.dataSet[i+1][j]>=0)
							{
								newPosition = searchPosition(myAgent,i+1,j,k);
							}
							break;
				
					case 7: if(i<6 && j<6 && myAgent.dataSet[i+1][j+1]>=0)
							{
								newPosition = searchPosition(myAgent,i+1,j+1,k);
							}
							break;
				}
				
				if(newPosition!=null)
				{
					Node temp = new Node();
					temp .startNode = myMove.clone();
					temp.endNode = newPosition.clone();
					temp.mode = k;
					temp.calculateScore(myAgent,myAgent.myPlayer,myAgent.opponent);
					
					positions.add(temp);
				}
			}
			
			//after exploring all the possible moves for 1 coin on the board, return something. not sure what for now.
			return positions;
			
		}
		else
			return null;
		
	}
	
	/*
	 * Identifies the next available position for a node in 1 direction
	 * returns the point that is a legal move on the board.
	 */
	
	int[] searchPosition(Agent myAgent, int i, int j, int mode)
	{
		int m=i;
		int n=j;
		
		boolean oppCoins=false;
		
		
		while(m>=0 && m<=7 && n>=0 && n<=7)
		{
			if(myAgent.dataSet[m][n]>=0)
			{
				if((myAgent.myPlayer.equalsIgnoreCase("x") && myAgent.dataSet[m][n]==0)  ||
						(myAgent.myPlayer.equalsIgnoreCase("o") && myAgent.dataSet[m][n]==1))
				{
					oppCoins=true;
				}
			}
			else if(myAgent.dataSet[m][n]<0 && oppCoins)
			{
				//available empty position to place and capture opposite coins
				return new int[]{m,n};
			}
			
			switch(mode)
			{
				case 0: m=m-1;
						n=n-1;
						break;
				
				case 1: m=m-1;
						break;
						
				case 2: m=m-1;
						n=n+1;
						break;
				
				case 3: n=n-1;
						break;
						
				case 4: n=n+1;
						break;
						
				case 5: m=m+1;
						n=n-1;
						break;
					
				case 6: m=m+1;
						break;
						
				case 7: m=m+1;
						n=n+1;
			}
			
		}
		
		return null;
		
	}

	void displayMove(Node node)
	{
		for(int i=0;i<8;i++)
		{
			String temp ="";
			for(int j=0;j<8;j++)
				temp+=node.newStringConfig[i][j]+"	";
			
			System.out.println(temp);
		}
	}
	
	
}
