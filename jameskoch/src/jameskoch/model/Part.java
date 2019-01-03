/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jameskoch.model;

/**
 *
 * @author james
 */
public abstract class Part {
//    - id : int
    int id; 
//    - name : String
    String name;
//    - price : double
    double price;
//    - stock : int
    int stock;
//    - min : int
    int min;
//    - max : int
    int max;
    new part(int idIn, String nameIn, double priceIn, int stockIn, int minIn, int maxIn){
        id = idIn;
        name = nameIn;
        price = priceIn;
        stock = stockIn;
        min = minIn;
        max = maxIn;
        
    } 
//    + Part(id : int, name : String,
//price : double, stock : int, min : int, max : int)
//+ setId(id:int):void
    int setID int idIn(){
    return -99;
}
//+ setName(name:String):void
//+ setPrice(price:double):void
//+ setStock(stock:int):void
//+ setMin(min:int):void
//+ setMax(max:int):void
//+ setPrice(max:int):void
//+ getId():int
//+ getName():String
//+ getPrice():double + getStock():int
//+ getMin():int
//+ getMax():int
    
}
