/*
 *  This class implements the delivery plan window
 *
 *
 *
 */


import java.awt.*;
import java.awt.event.*;
import javax.java.swing.*;

//
// main implementation
//
public class Delivery
{
  public static void main (String args [])
  {
    System.out.println ("Starting Delivery plan.....");
    javax.java.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(
               new javax.java.swing.plaf.metal.DefaultMetalTheme ());
    JFrame frame = new DeliveryFrame ();
    frame.setVisible (true);
  }
}
