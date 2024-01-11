package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;
import sounds.PlaySound;
import sounds.Sound;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends Observable
{
  private Product     theProduct = null;          // Current product
  private Basket      theBasket  = null;          // Bought items

  private String      pn = "";                    // Product being processed

  private StockReader     theStock     = null;
  private OrderProcessing theOrder     = null;
  private ImageIcon       thePic       = null;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReader();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
  }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum, String amountChosen)
  {
    theBasket.clear();                          // Clear s. list
    String theAction = "";
    pn  = productNum.trim();  
    int amount;
    if (variableValidator.TestVariable.validIntForStock(amountChosen)) {
    	amount = Integer.valueOf(amountChosen);
        try
        //The code if the test works...
        {
          if ( theStock.exists( pn ) )              // Stock Exists?
          {                                         // T
            Product pr = theStock.getDetails( pn ); //  Product
            if ( pr.getQuantity() >= amount )       //  In stock?
            { 
              theAction =                           //   Display 
                String.format( "%s : %7.2f (%2d) ", //
                  pr.getDescription(),              //    description
                  pr.getPrice(),                    //    price
                  pr.getQuantity() );               //    quantity
              pr.setQuantity( amount );             //   Require 1
              theBasket.add( pr );                  //   Add to basket
              thePic = theStock.getImage( pn );     //    product
              PlaySound.playSound(Sound.Confirmation);

            } else {                                //  F
              theAction =                           //   Inform
                pr.getDescription() +               //    product not
                " only " + pr.getQuantity() + " left in stock!"; 
          		PlaySound.playSound(Sound.Click);
//        in stock
            }
          } else {                                  // F
            theAction =                             //  Inform Unknown
              "Unknown product number " + pn; 
      		  PlaySound.playSound(Sound.Click);
    //  product number
          }
        } catch( StockException e )
        {
          DEBUG.error("CustomerClient.doCheck()\n%s",
          e.getMessage() );
        }
    } else {
    	amount = 1;
    	theAction = "Not a valid quantity!";
    }
    

    setChanged(); notifyObservers(theAction);
  }

  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
	PlaySound.playSound(Sound.Click);
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public ImageIcon getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}

