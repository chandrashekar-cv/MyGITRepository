package test1;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MinMax {
	
	String log = "Node,Depth,Value";
	
	void findNextMinMaxMove(Agent myAgent)
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
		
		
		Node bestMove = MAX(startMode, depth, myAgent, myAgent.myPlayer, myAgent.opponent);
		
		displayMove(bestMove,log);
	}
	
	
	
	Node MAX(Node current, int depth, Agent myAgent,String player, String opponent)
	{
		
		
		PriorityQueue<Node> movesAvail = new PriorityQueue<Node>();
		
		List<Node> result = possibleMoves(current,depth+1,myAgent,player,opponent);
		if(result!=null)
			movesAvail.addAll(result);
		
		Node max = null;
		Node myMove = null;
		while(!movesAvail.isEmpty())
		{
			this.log = (current.endNode==null)? this.log+"\n"+"root,"+depth+","+
					((current.score==99999)? "Infinity" :((current.score==-99999)?"-Infinity":current.score)) :
												this.log+"\n"+((char)(current.endNode[1]+97))+(current.endNode[0]+1)+","+depth+","+
													((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score));
			
			
					Node next = movesAvail.poll();
			//Call MIN
			Node temp = MIN(next, depth+1, myAgent,opponent,player);
			
			if(max == null || temp.score > max.score)
			{
				max = temp;
				myMove=next;
				myMove.Alpha = max.Beta;
				myMove.score = max.score;
				current.score = myMove.score;
			}
			else if(temp.score == max.score)
			{
				if(temp.endNode[0] < max.endNode[0])
					{
						max = temp;
						myMove=next;
						myMove.Alpha = max.Beta;
						myMove.score = max.score;
						current.score = myMove.score;
						
					}
				else if(temp.endNode[0] == max.endNode[0])
				{
					if(temp.endNode[1] < max.endNode[1])
						{
							max = temp;
							myMove=next;
							myMove.Alpha = max.Beta;
							myMove.score = max.score;
							current.score = myMove.score;
						}
				}
			}
		//	Alpha = max.Alpha;
			
		}
		
		
		this.log = (current.endNode==null)? 
				this.log+"\n"+"root,"+depth+","+((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score)) :
				this.log+"\n"+((char)(current.endNode[1]+97))+(current.endNode[0]+1)+","+depth+","+
				((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score));
		
		if(myMove!=null)
			return myMove;
		else 
		{
			current.Alpha = current.score;
			current.Beta = current.score;
			return current;
		}
	}
	
	Node MIN(Node current, int depth, Agent myAgent, String player, String opponent)
	{
		PriorityQueue<Node> movesAvail = new PriorityQueue<Node>();
		
		List<Node> result = possibleMoves(current,depth+1,myAgent,player, opponent);
		if(result!=null)
			movesAvail.addAll(result);
		
		
		Node min=null;
		Node myMove =null;
		
		while(!movesAvail.isEmpty())
		{
			this.log = (current.endNode==null)? 
					this.log+"\n"+"root,"+depth+","+((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score)) :
					this.log+"\n"+((char)(current.endNode[1]+97))+(current.endNode[0]+1)+","+depth+","+
					((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score));
			
			Node next = movesAvail.poll();
			
			//Call MAX
			Node temp = MAX(next, depth+1, myAgent, opponent, player);
			
			if(min==null || temp.score < min.score)
			{
				min = temp;
				myMove = next;
				myMove.Beta= min.Alpha;
				myMove.score = min.score;
				current.score = myMove.score;
			}
			else if(temp.score == min.score)
			{
				if(temp.endNode[0] < min.endNode[0])
				{
					min = temp;
					myMove=next;
					myMove.Beta= min.Alpha;
					myMove.score = min.score;
					current.score = myMove.score;
				}
				else if(temp.endNode[0] == min.endNode[0])
				{
					if(temp.endNode[1] < min.endNode[1])
						{
							min = temp;
							myMove=next;
							myMove.Beta = min.Alpha;
							myMove.score = min.score;
							current.score = myMove.score;
						}
				}
			}
			
		//	Beta = min.Beta;
		}
		
		this.log = (current.endNode==null)? 
				this.log+"\n"+"root,"+depth+","+((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score)) :
				this.log+"\n"+((char)(current.endNode[1]+97))+(current.endNode[0]+1)+","+depth+","+
				((current.score==99999)? "Infinity" : ((current.score==-99999)?"-Infinity":current.score));
				
		if(myMove!=null)
			return myMove;
		else 
		{
			current.Alpha = current.score;
			current.Beta = current.score;
			return current;
		}
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
				if((player.equalsIgnoreCase("x") && node.newConfig[m][n]==0)  ||
						(player.equalsIgnoreCase("o") && node.newConfig[m][n]==1))
				{
					oppCoins=true;
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
		for(int i=0;i<8;i++)
		{
			String temp ="";
			for(int j=0;j<8;j++)
				temp+=node.newStringConfig[i][j]+"	";
			
			System.out.println(temp);
		}
		System.out.println(log);
	}
	
}
