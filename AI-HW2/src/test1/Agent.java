package test1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Agent {
	
	int mode = -1;
	String myPlayer=null;
	String opponent = null;
	int searchCutOffDepth = -1;
	String [][]inputStringState=null;
	int [][]dataSet=null;
	
	List<int[]> currentPositions= new ArrayList<int[]>(); 
	
	int positionalWeights[][]=new int[][]{{99,-8,8,6,6,8,-8,99},
										{-8,-24,-4,-3,-3,-4,-24,-8},
										{8,-4,7,4,4,7,-4,8},
										{6,-3,4,0,0,4,-3,6},
										{6,-3,4,0,0,4,-3,6},
										{8,-4,7,4,4,7,-4,8},
										{-8,-24,-4,-3,-3,-4,-24,-8},
										{99,-8,8,6,6,8,-8,99}};

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Agent myAgent = new Agent();
		myAgent.readFile();
		//myAgent.displayInput();
		
		
		switch(myAgent.mode)
		{
			case 1: Greedy greedy = new Greedy();		
					greedy.findNextGreedyMove(myAgent);
					break;
			case 2: MinMax minmax = new MinMax();
					minmax.findNextMinMaxMove(myAgent);
					break;
			case 3: AlphaBeta alphaBeta = new AlphaBeta();
					alphaBeta.findNextAlphaBetaMove(myAgent);
		}
		

	}
	
	void readFile() throws IOException
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File("input.txt")));
		
		int counter = 0;
		
		String line=null;
		while((line=reader.readLine())!=null)
		{
			
			switch(counter)
			{
				case 0: mode = Integer.parseInt(line);
						break;
				
				case 1: myPlayer = line;
						opponent = (myPlayer.equalsIgnoreCase("x")) ? "O" : "X";
						break;
				case 2: searchCutOffDepth = Integer.parseInt(line);
						break;
				case 3: 
						inputStringState = new String[8][8];
						dataSet = new int[8][8]; 
						for(int i=0;i<8;i++)
						{
							String split[]=line.split("");
							for(int j=0;j<8;j++)
							{
								inputStringState[i][j]=split[j];
								if(split[j].equalsIgnoreCase("*"))
								{
									dataSet[i][j]=-9;
								}
								else if(split[j].equalsIgnoreCase("x"))
								{
									dataSet[i][j]=1;
									if(myPlayer.equalsIgnoreCase("x"))
										currentPositions.add(new int[]{i,j});
								}
								else if(split[j].equalsIgnoreCase("o"))
								{
									dataSet[i][j] = 0;
									if(myPlayer.equalsIgnoreCase("o"))
										currentPositions.add(new int[]{i,j});
								}
								else
									throw new IOException("Invalid input provided in the current state matrix from input.txt");
									
							}
							line=reader.readLine();
						}
						break;
			}
			
			counter++;
		}
	}
	
	void displayInput()
	{
		System.out.println("mode " + mode);
		System.out.println("myPlayer " + myPlayer);
		System.out.println("cut off deapth " +searchCutOffDepth );
		
		for(int i=0;i<8;i++)
		{
			String temp ="";
			for(int j=0;j<8;j++)
			{
				temp += inputStringState[i][j]+"|"+dataSet[i][j] +"|"+positionalWeights[i][j]+ "	";					
			}
			System.out.println(temp);
		}
		
		for(int i=0;i<currentPositions.size();i++)
		{
			int []temp = currentPositions.get(i);
			System.out.println((char)(temp[1]+97) + ""+(currentPositions.get(i)[0]+1));
		}
			
	}

}
