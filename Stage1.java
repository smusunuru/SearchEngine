package ZapposChallenge;
import java.io.*;
import java.net.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.ParseException;

public class Stage1 {
	
	static int numitems = 0;
	static double total = 0;
	public static void main (String[] args){
		int error = 1;
	while(error == 1){
		String numitemswanted = "How many items are you looking to purchase?: ";
		String numItem = Stage1.pass(numitemswanted);
		String pricewanted = "How much total amount?: ";
		String totalprice = Stage1.pass(pricewanted);
		
		error = 0;
		
		try{
			numitems = Integer.parseInt(numItem);
			} catch (NumberFormatException e){
				System.err.println("Number of items cannot be 0 or less.");
				error = 1;
			}
		/*try{
			total = Double.parseDouble(totalprice);
		} catch(NumberFormatException e){
			System.err.println("Total price has to be more than $0. ");
		}*/
		
		if(numitems < 1 && error ==0){
			System.out.println("Number of items cannot be 0 or less.");
		}
		else if(total <= 0 && error == 1){
			System.out.println("Total price has to be more than $0.");
		}
		else if(error==0){
			error = 0;
		}
	}
	try {
		Search newsearch = new Search(numitems, total);
			System.out.println(newsearch.getCombinations());
		}
	catch (ParseException e){
		System.err.println("Sorry. Try again. ");
	}
	catch (IOException e){
		System.err.println("Sorry.");
		e.printStackTrace();
	}
	
	}
public static String pass (String s) {
	try{
		System.out.print(s);
		
		return (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
	}
	catch (IOException e) {
		System.err.println(e);
		return "";
	}
}
}

