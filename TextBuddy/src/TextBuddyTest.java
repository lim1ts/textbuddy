import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.ArrayList;

public class TextBuddyTest{
	
	@Test 
	public void testAddTask(){
		String addText = "3 merlinis pogchamp";
		assertFalse(TextBuddy.textList.contains(addText));
		TextBuddy.addTextToFile(addText);
		assertTrue(TextBuddy.textList.contains(addText));
	}

	@Test
	public void testDeleteTask(){
		TextBuddy.addTextToFile("Don't delete me");
		TextBuddy.addTextToFile("Or me");
		TextBuddy.addTextToFile("Delete me");
		TextBuddy.deleteTextFromFile(3);
		assertFalse(TextBuddy.textList.contains("Delete me"));
	}

	@Test
	public void testClearTask(){
		TextBuddy.addTextToFile("123");
		TextBuddy.clearAllText();
		assertTrue(TextBuddy.textList.isEmpty());
	}

	@Test
	public void testSortTask(){
		ArrayList<String> compareText = new ArrayList<String>();
		compareText.add("AAAA");
		compareText.add("BBBB");
		compareText.add("CCCC");

		TextBuddy.addTextToFile("CCCC");
		TextBuddy.addTextToFile("AAAA");
		TextBuddy.addTextToFile("BBBB");
		TextBuddy.sortListLexico();
		assertEquals(compareText, TextBuddy.textList);
	}

	@Test
	public void testSearchFunc(){
		String findThisString = "Single and ready to MingLee.";
		String dontFindThisString = "Kappa pride";
		TextBuddy.addTextToFile(dontFindThisString);
		TextBuddy.addTextToFile(findThisString);
		ArrayList<String> results = TextBuddy.searchForString("MingLee");
		assertEquals(findThisString, results.get(0));
		assertEquals(1, results.size());
	}

}
