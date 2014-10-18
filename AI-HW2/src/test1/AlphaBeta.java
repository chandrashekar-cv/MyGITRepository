package test1;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AlphaBeta {

String log = "Node,Depth,Value,Alpha,Beta";
	
	void findNextAlphaBetaMove(Agent myAgent)
	{

		int depth=0;
		
		Node startMode = new Node();
		startMode.mode = myAgent.mode;
		startMode.gameMode = myAgent.mode;
		startMode.Alpha = -99999;
		startMode.Beta = 99999;
		startMode.copyArray(myAgent);
		startMode.score=-99999;
		
		for(int i=0;i<myAgent.currentPositions.size();i++)
			startMode.currentPositions.add(myAgent.currentPositions.get(i).clone());
		
		
		Node bestMove = MAX(startMode, depth, myAgent, myAgent.myPlayer, myAgent.opponent, -99999,99999);
		
		displayMove(bestMove,log);
	}
	
	
	
	Node MAX(Node current, int depth, Agent myAgent,String player, String opponent, int Alpha, int Beta)
	{
		
		
		PriorityQueue<Node> movesAvail = new PriorityQueue<Node>();
		
		List<Node> result = possibleMoves(current,depth+1,myAgent,player,opponent);
		if(result!=null)
			movesAvail.addAll(result);
		
		
		Node myMove = null;
		if(!movesAvail.isEmpty())
		{
			myMove=movesAvail.peek();
			while(!movesAvail.isEmpty())
			{
					AddLog(current, Alpha, Beta, depth);
					Node next = movesAvail.poll();
		
					//Call MIN
				Node temp = MIN(next, depth+1, myAgent,opponent,player,Alpha, Beta);
				
				if(Alpha < temp.Beta)
				{
					Alpha = temp.Beta;
					
					myMove = next;
					myMove.Alpha = Alpha;
					myMove.score= Alpha;
					
					current.score = Alpha;
					current.Alpha = Alpha;
				}
				
				if(Alpha >= Beta)
				{
					myMove.Alpha = Alpha;
					current.Alpha = Alpha;
					current.score = current.Alpha;
					AddLog(current, Alpha, Beta, depth);
				
					return myMove;
				}
					
			}
			
		}
		else  //No move found for the current player
		{
			int currentScore = current.score;
			current.Alpha = Alpha;
			current.Beta = Beta;
			myMove = current;
			
			if(depth<myAgent.searchCutOffDepth)
			{
				if(current.pass)
				{
					return myMove;
				}
				else
				{
					AddLog(current, Alpha, Beta, depth);
					current.pass = true;
					
					Node temp = MIN(current, depth+1, myAgent, opponent, player, Alpha, Beta);
					
					myMove.score = currentScore;
					myMove.Beta = Beta;
					myMove.Alpha = temp.Alpha;
					
				}
			
			}
			
		}
		
		
		AddLog(current, Alpha, Beta, depth);
		return myMove;
		
	}
	
	Node MIN(Node current, int depth, Agent myAgent, String player, String opponent, int Alpha, int Beta)
	{
		PriorityQueue<Node> movesAvail = new PriorityQueue<Node>();
		
		List<Node> result = possibleMoves(current,depth+1,myAgent,player, opponent);
		if(result!=null)
			movesAvail.addAll(result);
		
		Node myMove =null;
		if(!movesAvail.isEmpty())
		{
			myMove = movesAvail.peek();
			while(!movesAvail.isEmpty())
			{
				AddLog(current, Alpha, Beta, depth);
				
				Node next = movesAvail.poll();
				
				//Call MAX
				Node temp = MAX(next, depth+1, myAgent, opponent, player, Alpha, Beta);
				
				if(Beta > temp.score)
				{
					Beta = temp.score;
					myMove = next;
					myMove.score = Beta;
					myMove.Beta = Beta;
					current.score = Beta;
					current.Beta = Beta;
					//update current.score
				}
				
				if(Alpha >= Beta)
				{
					myMove.Beta = Beta; 
					current.Beta = Beta;
					current.score = current.Beta;
		
					AddLog(current, Alpha, Beta, depth);
					return myMove;
				}
				
				
			}
		}
		else
		{
			
			int currentScore = current.score;
			current.Alpha = currentScore;
			current.Beta = Beta;
			myMove = current;
			
			if(depth<myAgent.searchCutOffDepth)
			{
				if(current.pass)
				{
					return myMove;
				}
				else
				{
					current.pass = true;
					AddLog(current, Alpha, Beta, depth);
					
					Node temp  = MAX(current, depth+1, myAgent, opponent, player, Alpha, Beta);
					
					myMove.Alpha = Alpha;
					myMove.Beta = temp.Beta;
					myMove.score = temp.Beta;
					
				}
			}
			
		}
		
		AddLog(current, Alpha, Beta, depth);
		return myMove;
	
	}
	
	
	
	
	/*
	 * Identify the possible moves in the current board configuration.
	 * For every player coin on board, identify the possible moves in all directions.
	 */
	List<Node> possibleMoves(Node node,int depth,Agent myAgent, String player, String opponent)
	{
		
		List<Node> positions = new ArrayList<Node>();
	
		if(depth<=myAgent.searchCutOffDepth)
		{
			for(int p=0;p<node.currentPositions.size();p++)
			{
				int[]myMove = node.currentPositions.get(p);
				int i=myMove[0];
				int j=myMove[1];
				
				for(int k=0;k<8;k++)
				{
					int[] newPosition = null;
					switch(k)
					{
						
						case 0: if(i>1 && j>1 && myAgent.dataSet[i-1][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j-1,k,player);
								}
								break;
						
						case 1: if(i>1 && myAgent.dataSet[i-1][j]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j,k,player);
								}
								break;
						
						case 2:	if(i>1 && j<6 && myAgent.dataSet[i-1][j+1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j+1,k,player);
								}
								break;
						
						case 3:	if(j>1 && myAgent.dataSet[i][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i,j-1,k,player);
								}
								break;
				
						case 4:	if(j <6 && myAgent.dataSet[i][j+1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i,j+1,k,player);
									
								}
								break;
								
						case 5: if(i<6 && j>1 && myAgent.dataSet[i+1][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i+1,j-1,k,player);
								}
								break;
				
						case 6: if(i<6 && myAgent.dataSet[i+1][j]>=0)
								{
									newPosition = searchPosition(node, myAgent,i+1,j,k,player);
								}
								break;
					
						case 7: if(i<6 && j<6 && myAgent.dataSet[i+1][j+1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i+1,j+1,k,player);
								}
								break;
					}
					
					if(newPosition!=null)
					{
						Node temp = new Node();
						temp .startNode = myMove.clone();
						temp.endNode = newPosition.clone();
						temp.mode = k;
						temp.gameMode = myAgent.mode;
						temp.Alpha = node.Alpha;
						temp.Beta = node.Beta;
						temp.calculateScore(node, myAgent,player,opponent,depth);
						
						positions.add(temp);
					}
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
	
	int[] searchPosition(Node node, Agent myAgent, int i, int j, int mode, String player)
	{
		int m=i;
		int n=j;
		
		boolean oppCoins=false;
		
		
		while(m>=0 && m<=7 && n>=0 && n<=7)
		{
			if(node.newConfig[m][n]>=0)
			{
				if((player.equalsIgnoreCase("X") && node.newConfig[m][n]==0)  ||
						(player.equalsIgnoreCase("O") && node.newConfig[m][n]==1))
				{
					oppCoins=true;
				}
				if(oppCoins && (player.equalsIgnoreCase("O") && node.newConfig[m][n]==0)  ||
						(player.equalsIgnoreCase("X") && node.newConfig[m][n]==1))
				{
					//No move as player coin appears after opponent coins before an empty location
					break;
				}
			}
			else if(node.newConfig[m][n]<0 && oppCoins)
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

	void displayMove(Node node, String log)
	{
		if(node!=null)
		{
			for(int i=0;i<8;i++)
			{
			String temp ="";
			for(int j=0;j<8;j++)
				temp+=node.newStringConfig[i][j]+"	";
			
			System.out.println(temp);
			}
		}
		System.out.println(log);
	}
	
	void AddLog(Node current, int Alpha, int Beta, int depth)
	{
		this.log = (current.endNode==null || current.pass)? 
				this.log+"\n"+((current.pass)?"pass":"root")+","+depth+","+
				((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score)) +","+
				((Alpha==99999)? "Infinity" : ((Alpha==-99999)?"-Infinity":Alpha))+","+
				((Beta==99999)? "Infinity" : ((Beta==-99999)?"-Infinity":Beta)):
				this.log+"\n"+((char)(current.endNode[1]+97))+(current.endNode[0]+1)+","+depth+","+
				((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score))+","+
				((Alpha==99999)? "Infinity" : ((Alpha==-99999)?"-Infinity":Alpha))+","+
				((Beta==99999)? "Infinity" : ((Beta==-99999)?"-Infinity":Beta));
	}
	
	
}
