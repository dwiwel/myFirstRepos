package com.digi.xbee.example;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.digi.xbee.api.CellularDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.IPMessage;
import com.digi.xbee.api.models.IPProtocol;

import java.io.Console;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainApp {

    /* Constants */
    // TODO Replace with the serial port where your sender module is connected to.
    private static String PORT = "/dev/ttyUSB0";
    // TODO Replace with the baud rate of your sender module.  
    private static final int BAUD_RATE = 9600;
    // TODO Optionally, replace with the text you want to send to the server.
    private static  String TEXT = "Hello XBee World!";

    // defaults, if not given on command line.
    //
    private static String ECHO_SERVER_IP = "73.40.197.83";    // NewYork cr. Router's Internet side address.   used 11001,2,3
   // private static String ECHO_SERVER_IP = "216.117.131.124";    // Level10 Server. use 11010. down.
    private static int ECHO_SERVER_PORT = 11010;                 // Server's listen port.

    private static final IPProtocol PROTOCOL_TCP = IPProtocol.TCP;
    
    public static CellularDevice myDevice = null;  	
    
    public static void main(String[] args) 
    {
        System.out.println("\n---- BluJay Producer Client Prototype. V0.0 ---- ");
          
        if (args.length >= 1) PORT = args[0];
        if (args.length >= 2 ) ECHO_SERVER_IP = args[1];
        if (args.length >= 3 ) ECHO_SERVER_PORT = Integer.parseInt(args[2]);
        
        System.out.println(" Serial Port: " + PORT + " ServerIP: " +  ECHO_SERVER_IP );
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                if (myDevice != null) myDevice.close();
                System.out.println("Shutdown hook ran! Exiting. ");
            }
        });

    	System.out.format("\n-- Port to be used for ZigBee Cellular: '%s'\n", PORT);
    
    	try
    	{
        	myDevice = new CellularDevice(PORT, BAUD_RATE);
            myDevice.open();
            myDevice.setReceiveTimeout(2000);       // seconds.
    	}
    	catch (XBeeException e) 
		{
		    System.out.println(" !! Error opening ZigBee cellular device: ");
		    e.printStackTrace();
		    System.exit(0);
		} 
            boolean run = true;
            
            while (run)
            {
                try 
                {               
		            System.out.println("\n>> Enter text to send to echo server. (xxx to exit): \n>>");  
		            try {
		    			TEXT = br.readLine();
		    		} catch (IOException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		            
		            System.out.println("You entered : " + TEXT + "\n");
		            
		            if (TEXT.contains("xxx"))
		            {
		            	if (myDevice != null) myDevice.close();
		            	System.out.println("\n---- End of BluJay Producer Client execution.");
		            	System.exit(0);
		            }                
		            System.out.format("Sending text to echo server: '%s' ... \n", TEXT);		            
		            myDevice.sendIPData((Inet4Address) Inet4Address.getByName(ECHO_SERVER_IP),
		                    ECHO_SERVER_PORT, PROTOCOL_TCP, TEXT.getBytes());
		
		            System.out.println(">> Success!");
		            System.out.println(" Waiting for text to echo back ...");
		            // Read the echoed data.
		            IPMessage response = myDevice.readIPData();
		            
		            if (response == null) 
		            {		            	
		                 System.out.format("!! Echo response was not received from the server.\n");
		            }
		            else
		            {        
		            	System.out.format("Echo response received: '%s'\n", response.getDataString());
		            }
                }
                catch (XBeeException | UnknownHostException e) 
				{
				    System.out.println(" !! Error sending data: ");
				    e.printStackTrace();
				} 
            } // End main while. 
            if (myDevice != null) myDevice.close();
            System.out.println("\n---- End of BluJay Producer Client execution.");
    } // End main.
            
}