package test1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AlphaBeta {

String log = "Node,Depth,Value,Alpha,Beta";
	
	void findNextAlphaBetaMove(Agent myAgent) throws IOException
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
		@SuppressWarnings("unused")
		Node myMove = null;
		if(!movesAvail.isEmpty())
		{
			int tempAlpha = -99999;
			
			while(!movesAvail.isEmpty())
			{
				AddLog(current, Alpha, Beta, depth);
				Node next = movesAvail.poll();
		
					//Call MIN
				Node temp = MIN(next, depth+1, myAgent,opponent,player,Alpha, Beta);
				
				if(tempAlpha < temp.score)
					tempAlpha = temp.score;
					
				if(current.score < temp.score)
				{
					current.myMove = next;
					current.score = temp.score;
				}
				
				if(tempAlpha >= Beta)
				{
					AddLog(current, Alpha, Beta, depth);
					return current;
				}
				
				if(Alpha < tempAlpha)
					Alpha = tempAlpha;
				
				current.Alpha = Alpha;
					
			}
			
		}
		else  //No move found for the current player
		{
			if(depth<myAgent.searchCutOffDepth )
			{
				if(IsEndGame(current.newStringConfig,player,opponent))
				{
					current.score = current.utilityFunction(player, opponent, myAgent);
					AddLog(current, Alpha, Beta, depth);
					return current;
				}
				else if(current.pass)
				{
					AddLog(current, Alpha, Beta, depth);
					current.score = current.utilityFunction(player, opponent, myAgent);
					AddLog(current, Alpha, Beta, depth+1);
					
					if(Alpha < current.score)
						Alpha = current.score;
					
					AddLog(current, Alpha, Beta, depth);
					
					return current;
				}
				else
				{
					AddLog(current, Alpha, Beta, depth);
					
					Node temp = new Node();
					temp .startNode = current.startNode;
					temp.endNode = current.endNode;
					temp.gameMode = myAgent.mode;
					temp.Alpha = current.Alpha;
					temp.Beta = current.Beta;
					temp.pass = true;
					temp.score = current.Beta;
					temp.copyArray(current,opponent);
					
					Node uselessMove = MIN(temp, depth+1, myAgent, opponent, player, Alpha, Beta);
					int tempAlpha = -99999;
					
					if(tempAlpha < uselessMove.score)
						tempAlpha = uselessMove.score;
					
					if(current.score < uselessMove.score)
						current.score = uselessMove.score;
					
					if(tempAlpha >= Beta)
					{
						AddLog(current, Alpha, Beta, depth);
						return current;
					}
					
					if(tempAlpha > Alpha)
						Alpha = tempAlpha;
					
					current.Alpha = Alpha;
					
				}
			
			}
			
		}
		
		
		AddLog(current, Alpha, Beta, depth);
		return current;
		
	}
	
	Node MIN(Node current, int depth, Agent myAgent, String player, String opponent, int Alpha, int Beta)
	{
		PriorityQueue<Node> movesAvail = new PriorityQueue<Node>();
		
		List<Node> result = possibleMoves(current,depth+1,myAgent,player, opponent);
		if(result!=null)
			movesAvail.addAll(result);
		
		
		if(!movesAvail.isEmpty())
		{
			int tempBeta = 99999;
			
			while(!movesAvail.isEmpty())
			{
				AddLog(current, Alpha, Beta, depth);
				
				Node next = movesAvail.poll();
				
				//Call MAX
				Node temp = MAX(next, depth+1, myAgent, opponent, player, Alpha, Beta);
				
				if(tempBeta > temp.score)
				{
					tempBeta = temp.score;
				}
				
				if(current.score > temp.score)
					current.score = temp.score;
				
				if(Alpha >= Beta)
				{
					AddLog(current, Alpha, Beta, depth);
					return current;
				}
				
				if(Beta > tempBeta)
				{
					Beta = tempBeta;
				}
				
				current.Beta = Beta;
				
			}
		}
		else
		{
			if(depth<myAgent.searchCutOffDepth)
			{
				if(IsEndGame(current.newStringConfig,player,opponent))
				{
					current.score = current.utilityFunction(opponent, player, myAgent);
					AddLog(current, Alpha, Beta, depth);
					return current;
				}
				else if(current.pass)
				{
					AddLog(current, Alpha, Beta, depth);
					current.score = current.utilityFunction(opponent, player, myAgent);
					AddLog(current, Alpha, Beta, depth+1);
					
					if(Beta > current.score)
						Beta = current.score;
					
					AddLog(current, Alpha, Beta, depth);
					return current;
				}
				else
				{

					AddLog(current, Alpha, Beta, depth);
					
					Node temp = new Node();
					temp .startNode = current.startNode;
					temp.endNode = current.endNode;
					temp.gameMode = myAgent.mode;
					temp.Alpha = current.Alpha;
					temp.Beta = current.Beta;
					temp.pass = true;
					temp.score = current.Alpha;
					temp.copyArray(current,opponent);
					
					Node uselessMove  = MAX(temp, depth+1, myAgent, opponent, player, Alpha, Beta);
					int tempBeta = 99999;
					
					if(tempBeta > uselessMove.score)
					{
						tempBeta = uselessMove.score;
					}
					
					if(current.score > uselessMove.score)
						current.score = uselessMove.score;
					
					if(Alpha >= tempBeta)
					{
						AddLog(current, Alpha, Beta, depth);
						return current;
					}
					
					if(Beta > tempBeta)
						Beta = tempBeta;
					
					current.Beta = Beta;
					
				}
			}
			
		}
		
		AddLog(current, Alpha, Beta, depth);
		return current;
	
	}
	
	
	static boolean IsEndGame(String[][] state,String player, String opponent)
	{
		boolean pl=false;
		boolean op = false;
		boolean fullBoard = true;
		
		for(int i=0;i<state.length;i++)
			for(int j=0;j<state.length;j++)
			{
				if(!pl)
					{pl = (!pl && state[i][j].equalsIgnoreCase(player))? true: false;}
				if(!op)
					{op = (!op && state[i][j].equalsIgnoreCase(opponent))? true: false;}
				
				if(state[i][j].equals("*"))
					fullBoard = false;
			}
				
		return (fullBoard || !( op && pl));
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
						
						case 0: if(i>1 && j>1 && node.newConfig[i-1][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j-1,k,player);
								}
								break;
						
						case 1: if(i>1 && node.newConfig[i-1][j]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j,k,player);
								}
								break;
						
						case 2:	if(i>1 && j<6 && node.newConfig[i-1][j+1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i-1,j+1,k,player);
								}
								break;
						
						case 3:	if(j>1 && node.newConfig[i][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i,j-1,k,player);
								}
								break;
				
						case 4:	if(j <6 && node.newConfig[i][j+1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i,j+1,k,player);
									
								}
								break;
								
						case 5: if(i<6 && j>1 && node.newConfig[i+1][j-1]>=0)
								{
									newPosition = searchPosition(node, myAgent,i+1,j-1,k,player);
								}
								break;
				
						case 6: if(i<6 && node.newConfig[i+1][j]>=0)
								{
									newPosition = searchPosition(node, myAgent,i+1,j,k,player);
								}
								break;
					
						case 7: if(i<6 && j<6 && node.newConfig[i+1][j+1]>=0)
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
				else if((player.equalsIgnoreCase("O") && node.newConfig[m][n]==0)  ||
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

	void displayMove(Node node, String log) throws IOException
	{
		String [][] state = null;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));
		
		if(node.myMove==null)
			state = node.newStringConfig;
		else
			state = node.myMove.newStringConfig;
		
		for(int i=0;i<8;i++)
		{
			String temp ="";
			for(int j=0;j<8;j++)
				temp+=state[i][j];
			
			writer.append(temp+"\n");
		}
		
		writer.append(log);
		writer.close();
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
