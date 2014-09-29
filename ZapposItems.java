package ZapposChallenge;

import org.json.simple.*;
//import org.json.simple.JSONObject;

public class ZapposItems {

	
	public static final int next = 0;
	private String productId, brandName, productName, defaultProductUrl, defaultImageUrl; //price;
	private double price;
	//public int next;
	public ZapposItems(JSONObject obj){
		price = Double.parseDouble(((String) obj.get("price")).substring(1));
		productId = "productid";
		productName = "productname";
		brandName = "brandname";
		price = 0;
		defaultProductUrl = "";
	}
	
	/*public ZapposItems(){
		productId = "productid";
		productName = "productname";
		brandName = "brandname";
		price = 0;
		defaultProductUrl = "";
		
	}*/
	
	//getters and setters for each property of product. 
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public String getProductName(){
		//this.productName = productName;
		return productName;
	}
	public void setBrandName(String brandName){
		this.brandName = brandName;
	}
	public String getBrandName(){
		//this.brandName = brandName;
		return brandName;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public double getPrice(){
		//this.price = price;
		return price;
	}
	public void setProductId(String productId){
		this.productId = productId;
	}
	public String getproductId(){
		//this.productId = productId;
		return productId;
	}
	public String toString(){
		return productName + brandName + price;
	}

	public double pricecheck() {
		// TODO Auto-generated method stub
		return price;
	}
	
}
