package ZapposChallenge;

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Search {
	private static ArrayList<ZapposItems> allitems = new ArrayList<ZapposItems>();
	private static ArrayList<ZapposItems> finalitems = new ArrayList<ZapposItems>(); 
	private static ArrayList<ZapposItems> possibleitems = new ArrayList<ZapposItems>();
	
	
	private int numitems;
	private double totalprice;
	private int page;
	private JSONArray avaliableproducts;
	private double max;
	private final double delta = Math.pow(10, -8); 
	//inspired from http://www.ccs.neu.edu/home/lieber/courses/csg270/sp07/submissions/vlad/Polynomial.java
	

	public Search(int num, double total){
		numitems = num;
		totalprice = total;
		avaliableproducts = new JSONArray();
		page = 1; //set it to 1. so it always goes through atleast one page.
		//products = new JSONArray();
		finalitems = new ArrayList<ZapposItems>(); //all possible items that can be a part of a combination
		possibleitems = new ArrayList<ZapposItems>(); //possible combinations
 	}
	
	public Double pricecheck(Object product){
		Double toreturn = Double.parseDouble(((String)((JSONObject)product).get("price")).substring(1));
		return toreturn;
	}

	@SuppressWarnings("unchecked")
	private void possibleProducts() throws IOException, ParseException{
		String pagination = Iterate.httpGet(Iterate.searchurl + "&term=&limit=100&sort={\"price\":\"asc\"}");
		
		JSONObject foundobject = Iterate.parseReply(pagination);
		JSONArray resultarray = Iterate.resultarray(foundobject);
		
		
		double first = pricecheck(resultarray.get(0)); //get the price of the first product on page
		
		if((first*numitems)>totalprice){
			avaliableproducts = null;
		}else {
		
		 max = totalprice - (numitems-1)*first;
		
		page++;
			
		Double last = pricecheck(resultarray.size()-1);
		
		while(last<max){
			String next = Iterate.httpGet(Iterate.searchurl +"&term=&limit=100&sort={\"price\":\"asc\"}&page=" + page);
			JSONObject nextobj = Iterate.parseReply(next);
			JSONArray newarray = Iterate.resultarray(nextobj);
			
			resultarray.addAll(newarray);
			
			//reset the last items price each time. because it differs for each page.
			last = pricecheck(newarray.get(newarray.size()-1));
			
			//keep incrementing the page until we reach the max price
			page++;
		}
		}
		
		avaliableproducts = resultarray;
	}
	
	@SuppressWarnings("unchecked")
	private void convertToObj(){
		finalitems.add(new ZapposItems(((JSONObject)avaliableproducts.get(0))));
		
		int count = 1; //starts at 1 because we already added one product to the list
		int prices = 1; //we already have 1 price that we need to incorporate
		
		for(int i=1; i<avaliableproducts.size() && pricecheck(avaliableproducts.get(i))<max; i++){
			double current = pricecheck(avaliableproducts.get(i));
			if(current > finalitems.get(prices-1).pricecheck()){
			avaliableproducts.add(new ZapposItems(((JSONObject)avaliableproducts.get(i))));
			prices++;
			count =1;
		}
		else if(Math.abs(current - pricecheck(avaliableproducts.get(numitems-1)))<delta&&count<numitems){
			//inspired code from http://www.ccs.neu.edu/home/lieber/courses/csg270/sp07/submissions/vlad/Polynomial.java
			//it "Determines if the two values are equal to 0 within the tolerance of 10^-8.
			//if they are equal then we add them to final items array which is all the possible items that can be chosen
			finalitems.add(new ZapposItems(((JSONObject)avaliableproducts.get(i))));
			prices++;
			count++; 
		}else {
			//if the current price is less than the one before then we move onto the next iteration. and
			//set count to zero because we dont have any products in avaliableproducts array. 
			i++;
			count = 0; 
			
		}
	
	}

	
}
	private void recursivecombo(ArrayList<ZapposItems> products, double end, ArrayList<ZapposItems> comboprices){
		double total = 0;
		
		if(comboprices.size()>numitems){ //you exceed the numitems the customer wants
			return;
		}
		for(ZapposItems x : comboprices){
			total += x.getPrice();
			if(Math.abs(total-end)<1 && comboprices.size()==numitems){ 
				if(possibleitems.size()==0){
					//possibleitems.add(x);
					possibleitems.add(x);
				}
				else if(possibleitems.size()<numitems){
					//continue;
					//continue because possible combinations havent been reached yet
			possibleitems.add(x);
				} else {
					return;
				}
			}
			
			if(total >= end + 1){
				return;
			}
			
		}
		for(int i =0; i<products.size() && !(comboprices.size()==numitems && total < end); i++){
			ArrayList<ZapposItems> rest = new ArrayList<ZapposItems>();
			ZapposItems y = products.get(i);
			//JSONObject y = (JSONObject)avaliableproducts.get(i);
			for(int j=i+1; j<products.size(); j++){
				rest.add((ZapposItems) products.get(j));
			}
			ArrayList<ZapposItems> partial = new ArrayList<ZapposItems>(rest);
			//JSONObject partialobj = (JSONObject)partial.addAll(y);
			//ArrayList<ZapposItems> partial = new ArrayList<ZapposItems>();
			partial.add(y);
			recursivecombo(rest, end, partial);
		}
	}
	
	private void allCombinations(){
		Collections.shuffle(possibleitems);
	}
	
	private void setCombinations(){
		recursivecombo(finalitems, totalprice, new ArrayList<ZapposItems>());
	}
	
	public String getCombinations() throws IOException, ParseException{
		String concatenate = "";
		this.possibleProducts();
		
		System.out.println("Finding combinations...");
		this.convertToObj();
		this.setCombinations();
		this.allCombinations();
		
		if(possibleitems.size()!=0){
			for(ZapposItems x: possibleitems){
				concatenate+=x.toString();
			}
			return concatenate;
		} else {
			String error = "Could not find any combinations. Please change your input.";
			return error;
		}
	}
	
}
