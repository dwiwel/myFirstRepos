package com.digi.xbee.example;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.digi.xbee.api.CellularDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IIPDataReceiveListener;
import com.digi.xbee.api.models.IPMessage;
import com.digi.xbee.api.models.IPProtocol;


public class MainApp2 {

    /* Constants */
    // TODO Replace with the serial port where your sender module is connected to.
    private static final String PORT = "COM8";
    // TODO Replace with the baud rate of your sender module.  
    private static final int BAUD_RATE = 9600;
    // TODO Optionally, replace with the text you want to send to the server.
    private static final String TEXT = "Hello XBee World!";

    private static final String ECHO_SERVER_IP = "73.40.197.83";    // Router's Internet side address.
    private static final int ECHO_SERVER_PORT = 11001;

    private static final IPProtocol PROTOCOL_TCP = IPProtocol.TCP;
    

    
    public static void main(String[] args) {
    	CellularDevice myDevice = null;
    	
        try {
        	myDevice = new CellularDevice(PORT, BAUD_RATE);
            myDevice.open();
            myDevice.setReceiveTimeout(6000);       // seconds.
            myDevice.addIPDataListener( new MyTCPReceiveListener() );

            System.out.format("MainApp2: Sending text to echo server: '%s'", TEXT);
            myDevice.sendIPData((Inet4Address) Inet4Address.getByName(ECHO_SERVER_IP),
                    ECHO_SERVER_PORT, PROTOCOL_TCP, TEXT.getBytes());

            System.out.println(" >> Success");

            // Read the echoed data.
            IPMessage response = myDevice.readIPData();
            if (response == null) {
                System.out.format("Echo response was not received from the server.");
                System.exit(1);
            }
            System.out.format("Echo response received: '%s' \n", response.getDataString());
        } catch (XBeeException | UnknownHostException e) {
            System.out.println(" >> Error");
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (myDevice != null) myDevice.close();
        }
        System.out.println("End of program. ");
     }
}