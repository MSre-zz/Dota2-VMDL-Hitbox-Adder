package main;
//package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class VmdlReWrite {
	private File inputFile;
	private File resourceFile;
	private File tempFile;
	
	private Scanner resourceScanner;	//contains Noya's pastebinned thing. Hopefully volvo doesn't change their model encoding again.
	private Scanner inputScanner;
	private Scanner tempScanner; 	//used after tempWriter writes the tempFile so that we can re-write the  input file
	private PrintWriter tempWriter;
	private PrintWriter inputWriter; //used after temp file is constructed to write back to input file
	
	boolean usesVectorInputsFromGui = false;
	
	private int minX, minY, minZ, maxX, maxY, maxZ;
	
	public VmdlReWrite(File input) throws IOException
	{
		inputFile = input;
		resourceFile = new File("resources/vmdl_write_v01.txt");
		tempFile = new File("resources/temp.txt");
	}
	
	//prrrrrobably should have just passed these in as an array to allow for betterness,
	//and could have had it so they weren't all or nothing. 
	public VmdlReWrite(File input, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) throws IOException
	{
		inputFile = input;
		resourceFile = new File("resources/vmdl_write_v01.txt");
		tempFile = new File("resources/temp.txt");

		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		
		usesVectorInputsFromGui = true;
	}
		
	public boolean PerformReWrite()
	{
		//flow:
		// -read input file and write to temp file until we are done with the first CVModel {} block
		// -write resource file to temp file until we find the mesh block
		// -find mesh block in input file
		// -write mesh block to temp file
		// -write rest of temp file
		// -delete input file
		// -copy temp file to input path
		//below functions 
		
		//when this function returns false, the text panel will show the entry as an error.
		
		
		try {
			inputScanner = new Scanner(inputFile);
			tempWriter = new PrintWriter(tempFile);
			resourceScanner = new Scanner(resourceFile);
			//inputWriter = new PrintWriter();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!CopyFirstBlockToTempFile())
		{
			CleanUp();
			return false;
		}
		if(!CopyResourceFileToTempFile())
		{
			CleanUp();
			return false;
		}
		
		//tempfile creation complete, now we have to re-write the input file
		try {
			inputScanner.close();
			inputWriter = new PrintWriter(inputFile);
			tempWriter.close();
			tempScanner = new Scanner(tempFile);
			CopyTempToInput();
			inputWriter.close();
			tempScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CleanUp();
			return false;
		}
		
		CleanUp();
		return true;		
	}
	
	private boolean CopyFirstBlockToTempFile()
	{
		int bracketCounter = 0; //increment for {, decrement for }, when 0 after subtracting exit the loop
		boolean foundCVModel = false;
		
		while(inputScanner.hasNextLine())
		{
			String inputLine = inputScanner.nextLine();
			String[] splits = inputLine.split("\\s+");
			tempWriter.println(inputLine);
			for(String split : splits)
			{
				if(split.equalsIgnoreCase("\"CVModel\""))
				{
					foundCVModel = true;
				}
				//repeated this logic below, probably better programming practice to make a function to hold this stuff so it isn't repeated
				//could perhaps pass in a string for the thing we are looking for?
				if(foundCVModel)
				{
					if(split.contains("{"))
					{
						bracketCounter++;
					}
					if(split.contains("}"))
					{
						bracketCounter--;
						if(bracketCounter <= 0 && foundCVModel)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean CopyResourceFileToTempFile()
	{
		while(resourceScanner.hasNext())
		{
			String resourceLine = resourceScanner.nextLine();
			if(resourceLine.contains("m_pMaterialRemapList"))
			{
				if(!CopyInputMaterialTable()) return false; //something went wrong
			}
			else
			{
				tempWriter.println(resourceLine);
			}
			if(usesVectorInputsFromGui)
			{
				if(resourceLine.contains("m_vMinBounds"))
				{
					WriteBounds(minX,minY,minZ);
				}
				if(resourceLine.contains("m_vMaxBounds"))
				{
					WriteBounds(maxX,maxY,maxZ);
				}
			}
		}
		
		return true;
	}
	
	private boolean CopyInputMaterialTable()
	{
		boolean foundMaterialRemapList = false; //could have nested a loop in the if statement, but thought this would be cleaner(?)
		int bracketCounter = 0;
		while(inputScanner.hasNext())
		{
			String inputLine = inputScanner.nextLine();
			if(inputLine.contains("m_pMaterialRemapList"))
			{
				foundMaterialRemapList = true;
			}
			//some repeated logic here, should probably consider breaking it out into its own function
			if(foundMaterialRemapList)
			{
				tempWriter.println(inputLine);
				if(inputLine.contains("{"))
				{
					bracketCounter++;
				}
				if(inputLine.contains("}"))
				{
					bracketCounter--;
					if(bracketCounter <= 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//these should probably return boolean, but i'm just hard coding it
	private void WriteBounds(int x, int y, int z)
	{
		//someone is going to/should yell at me for this hard coding lol
		tempWriter.println("                                [");
		tempWriter.println("                                    " + x + ",");
		tempWriter.println("                                    " + y + ",");
		tempWriter.println("                                    " + z + ",");
		tempWriter.println("                                ]");
		
		//this probably makes good programmers cringe, but I mainly write cobol and i don't know shit about good text parsing
		resourceScanner.nextLine();
		resourceScanner.nextLine();
		resourceScanner.nextLine();
		resourceScanner.nextLine();
		resourceScanner.nextLine();
	}
	
	private void CopyTempToInput()
	{

		while(tempScanner.hasNext())
		{
			String tempLine = tempScanner.nextLine();
			inputWriter.println(tempLine);
		}
			

		
	}
	
	
	
	private void CleanUp()
	{
		//does anything bad happen if these get closed twice after only being opened once? haven't had anything bad happen yet >.>
		resourceScanner.close();
		inputScanner.close();
		tempWriter.close();
		//inputWriter.close();
	}
}
