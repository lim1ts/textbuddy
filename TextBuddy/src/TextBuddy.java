import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;


/**
 * TextBuddy for CS2103T CE1 AY2015/16 SEM2
 * Write a CLI (Command Line Interface) program called TextBuddy using Java/C++ 
 * to manipulate text in a file. 
 * 
 * Assumptions:
 * File name to be passed in to TextBuddy. If it exists, information is retrieved.
 * Else a new file with specified name will be created.
 * 
 * All changes are saved only when program exits.
 */


public class TextBuddy {
	// Constant String message outputs
	private static String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use \n";
	private static String COMMAND_PROMPT = "Command: " ;
	private static String INVALID_COMMAND_PROMPT = "Please type in a valid command\n " ;
	private static String MESSAGE_ADDED = "added to %s: \"%s\"\n" ;
	private static String MESSAGE_DISPLAY_ITEMS = "%d. %s\n";
	private static String MESSAGE_DISPLAY_EMPTY = "%s is empty\n";
	private static String MESSAGE_DELETE_TASK = "deleted from %s: \"%s\"\n";
	private static String MESSAGE_DELETE_ERROR = "no content to delete\n";
	private static String MESSAGE_CLEARED = "all content deleted from %s\n";
	private static String MESSAGE_SEARCH_RESULTS = "Your search query has returned:";
	private static String MESSAGE_SORT_RESULT = "All content now sorted lexicographically.";
	private static String MESSAGE_SEARCH_NOTHING = "No content matches your query.";
	private static String FILE_CREATE_ERROR = "Unable to create file \n";
	private static String FILE_WRITE_ERROR = "Unable to write to file \n";
	private static String FILE_LOAD_ERROR = "Unable to load data from file \n";
	
	// Commands supported
	private static String ADD_COMMAND = "add";
	private static String DELETE_COMMAND = "delete";
	private static String DISPLAY_COMMAND = "display";
	private static String CLEAR_COMMAND = "clear";
	private static String SEARCH_COMMAND = "search";
	private static String SORT_COMMAND = "sort";
	
	// Starting indexes for commands
	private static int ADD_INDEX = 4;
	private static int DELETE_INDEX = 7;
	private static int SEARCH_INDEX= 7;
	
	// Stores list of text.
	static ArrayList<String> textList = new ArrayList<String>();
	
	// Stores specified file name.
	private static String fileName;
	
	// Stores specified file as File Type
	private static File opFile;
	
	// Scanner for reading user input
	private static Scanner in = new Scanner(System.in);
	
	
	public static void main(String[] args){
		queryFileName(args[0]);
		showToUser(String.format(MESSAGE_WELCOME, fileName));
		promptUserForInput();
		writeToFile();
	}

	/**
	 * Method to process input from user
	 */
	private static void promptUserForInput(){
		do{
			showToUser(COMMAND_PROMPT);
			String input = in.nextLine();
			executeCommand(input);
		}while(in.hasNextLine());
	}

	/**
	 * Method to perform specified actions depending 
	 * on whether specified file exists.
	 * @param userInputFileName is the file name specified by user.
	 */
	private static void queryFileName(String userInputFileName){
		TextBuddy.fileName = userInputFileName;
		opFile = new File(fileName);
		if(!isExistingFile()) {
			createNewFile();
		} else {
			loadFromFile();
		}
	}
	
	/**
	 *  Method checks whether file specified under TextBuddy's file exists
	 * @return a boolean whether it exists.
	 */
	private static boolean isExistingFile(){
		return opFile.isFile();
	}
	
	/**
	 * Method creates a new file named accordingly to user input
	 */
	private static void createNewFile(){
		try{
			opFile.createNewFile();
		} catch (IOException e) {
			showToUser(FILE_CREATE_ERROR);
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes in a string and prints to console
	 * @param messageToShow is printed to the console.
	 */
	private static void showToUser(String messageToShow){
		System.out.println(messageToShow);
	}
	
	/**
	 * Method to perform action based on user's input.
	 * @param userInput contains the 
	 */
	private static void executeCommand(String userInput){
		String cmd = getCommand(userInput);
		if(cmd.equalsIgnoreCase(ADD_COMMAND)) {
			String addToFile = userInput.substring(ADD_INDEX);
			addTextToFile(addToFile);
		} else if (cmd.equalsIgnoreCase(DELETE_COMMAND)){
			int deleteIndex = Integer.parseInt(userInput.substring(DELETE_INDEX));
			deleteTextFromFile(deleteIndex);
		} else if(cmd.equalsIgnoreCase(SEARCH_COMMAND)){
			String query = userInput.substring(SEARCH_INDEX);
			ArrayList<String> searchResults = new ArrayList<String>();
			searchResults = searchForString(query);
			displayResults(searchResults);
		} else if (cmd.equalsIgnoreCase(DISPLAY_COMMAND)){
			displayText();
		} else if (cmd.equalsIgnoreCase(CLEAR_COMMAND)){
			clearAllText();
		} else if(cmd.equalsIgnoreCase(SORT_COMMAND)){
			sortListLexico();
		} else {
			showToUser(INVALID_COMMAND_PROMPT);
		}
	}

	/**
	 * @param input The entire string entered by user
	 * @return the first word of the string as command
	 */
	private static String getCommand(String input) {
		String[] splitString = input.split(" ");
		return splitString[0];
	}
	
	/**
	 * Adds string to ArrayList textList.
	 * @param addThisLine
	 */
	static void addTextToFile(String addThisLine){
		textList.add(addThisLine);
		showToUser(String.format(MESSAGE_ADDED, fileName, addThisLine));
	}
	
	/**
	 * Deletes line number specified by user from text file.
	 * @param deleteLine
	 */
	static void deleteTextFromFile(int deleteLine){
		if(isAbleToDelete(deleteLine -1)){
			String lineToBeDeleted = textList.get(deleteLine - 1);
			textList.remove(deleteLine - 1);
			showToUser(String.format(MESSAGE_DELETE_TASK, fileName, lineToBeDeleted));
		} else {
			showToUser(MESSAGE_DELETE_ERROR);
		}
	}
	
	private static boolean isAbleToDelete(int deleteNum){
		if (deleteNum >= 0 && deleteNum < textList.size()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Displays all text from file.
	 */
	private static void displayText(){
		String displayThis = "%";
		if(!isTextEmpty()){
			for(int i = 0; i < textList.size(); i++){
				displayThis = textList.get(i);
				showToUser(String.format(MESSAGE_DISPLAY_ITEMS,i + 1, displayThis));
			}
		} else {
			showToUser(String.format(MESSAGE_DISPLAY_EMPTY, fileName));
		}
	}
	
	private static boolean isTextEmpty(){
		return textList.isEmpty();
	}
	
	/**
	 * Removes all text added.
	 */
	static void clearAllText(){
		int numberOfTextLines = textList.size();
		for(int i = 0 ; i < numberOfTextLines ; i ++){
			textList.remove(0);
		}
		showToUser(String.format(MESSAGE_CLEARED, fileName));
	}
	
	/**
	 * Method searches through all content in textList so far, and returns
	 * an arraylist with strings containing the search term.
	 * @param searchQuery is the search term.
	 * @return an arraylist containg all the results.
	 */
	
	static ArrayList<String> searchForString(String searchQuery){
		ArrayList<String> results = new ArrayList<String>();
		for(String eachLine : textList){
			if(eachLine.contains(searchQuery)){
				results.add(eachLine);
			}
		}
		return results;
	}
	/**
	 * Method prints out the query returned by searchForString
	 * @param listOfResults contains the list of results to be printed.
	 */
	private static void displayResults(ArrayList<String> listOfResults){
		if(listOfResults.size() == 0){
			showToUser(MESSAGE_SEARCH_NOTHING);
		} else{
			showToUser(MESSAGE_SEARCH_RESULTS);
			for(String eachResult : listOfResults){
				showToUser(eachResult);
			}
		}
	}
	
	/**
	 * Sorts current content lexicographically.
	 */
	static void sortListLexico(){
		List<String> unsortedList = textList;
		Collections.sort(unsortedList);
		showToUser(MESSAGE_SORT_RESULT);
	}
	
	/**
	 * Method reads existing data from file and adds them into textList
	 */
	private static void loadFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				textList.add(line);
			}
			br.close();

		} catch (IOException e) {
			showToUser(FILE_LOAD_ERROR);
			e.printStackTrace();
		}
	}
	
	private static void writeToFile(){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(opFile));
			for(String eachLine : textList){
				writer.write(eachLine + "\n");
			}
			writer.close();
		} catch (IOException e){
			showToUser(FILE_WRITE_ERROR);
			e.printStackTrace();
		}
	}
}   
    