//// Send.java      Class Send
//
// Sends an image to the receiver.
// Taken from:
//     https://stackoverflow.com/questions/25086868/how-to-send-images-through-sockets-in-java
//
// 171101, 171103, 171129, 171201, 171230, 180104, 
// 180301
// 180308 Added /dev/ttyUSB1 as alternate port.
// 180826 Work after dev7 attacked.  Minor mods.
// 180915 Working here now.  
// 181128A Push to remote.
// 181205 Working here. 
// 181227 Adding command line arg for server's IP address.
// 190715B Testing. Cleanup.
// 190717 Added SMS receiver, for future commands. 
// 190996 Adding feature to execute commands via SMS.  

package imageHandler;

//import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;


import java.net.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.digi.xbee.api.CellularDevice;
import com.digi.xbee.api.connection.serial.SerialPortRxTx;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.IPMessage;
import com.digi.xbee.api.models.IPProtocol;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.CopyOption.*;
import java.nio.file.StandardCopyOption.*;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;

// Thread to receive instructions via SMS text messages.  Not currently used. 
//
class RcvGrabberMsgThread extends Thread {      
	
	public boolean run = true;           // To stop thread gracefully.
	public InputStream inputStream;
	public DataInputStream dataInputStream;
	public Socket socket;
	public boolean connected = true;
			
	public void run()	
	{
		System.out.format("\n---- Starting RecGrabberMsgThread to read msgs from grabber ...\n");		

		while (run)
		{
			System.out.println("----Checking for incoming msg from grabber, if connected.");
			if ( true )
			{
				try 
				{
					if (socket != null)
					{
				        inputStream = socket.getInputStream();
				        dataInputStream = new DataInputStream(inputStream);
				        
						if (dataInputStream.available() > 0 )
						{				  
							String incomingMsg;
							incomingMsg = dataInputStream.readLine();
							System.out.println("------- Incoming msg receive: " + incomingMsg );
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// End connected while
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// End while run.
		
		System.out.format("\n**--  RcvGrabberMsgThread done. \n");
	}
		
}
// end class RecvTxtMsgThread.



// Thread to connect to grabber app, init connection. (for sending commands to take image)
//
class GrabberControlThread extends Thread
{
	
    public String serverName = "localhost";
    public int port = 8210;                     // Server listen port, on grabber
    public boolean connected = false;
    public boolean run = true;
    
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    
    public DataOutputStream dataOutputStream = null;  
    public DataInputStream dataInputStream = null;
    public Socket socket = null;
    public String cmdStr = "NULL";
    
	//@SuppressWarnings("deprecation")
	public void run()	
	{		
		System.out.println("----in GrabberControlThread::run.");
		
		// Keep trying to connect to image grabber app (server side).
		//
		try
		{		
	    	while (run)
	    	{	    		
	    		if (!connected)
	    		{
	    			try
	    			{
				        socket = new Socket(serverName, port);		      
				        
				        outputStream = socket.getOutputStream();					      
				        dataOutputStream = new DataOutputStream(outputStream);
				        
				        inputStream = socket.getInputStream();
				        dataInputStream = new DataInputStream(inputStream);
				        connected = true;
	    			}
	    			catch (Exception ex)
	    			{
	    				connected = false;
	    				System.out.println("!GrabberControlTread, trouble with starting listen connection, ex:" + ex );    				 
	    				// ex.printStackTrace();
	    			}
	    		}	       
	    			    		
	    		Thread.sleep(3000);
	    	} 
	    	// End run while    	   	
	    	
           System.out.println("---GrabberControlThread: Closing socket and terminating.");
           if (connected) dataOutputStream.close(); // close the output stream when we're done.
           if (connected) dataInputStream.close(); // close the output stream when we're done.
           socket.close();           
           if (connected) connected = false;
           run = false;
           
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connected = false;           // cause a reconnect.
			System.out.println("!!GrabberControlThread, ex: " + e);
		}  catch (Exception e)
		{
			System.out.println("!!GrabberControlThread, unexpected ex: " + e);		
		}
                  
		System.out.println("---- End of run thread.");
		System.out.println("---- End of run thread.");
	} // end of run.
	
	
	public void sendGrabberCommand (String cmdStr) 
	{		
		//String cmdStrVal = cmdStr;
		//byte bytesVal[] = cmdStrVal.getBytes(cmdStrVal);
		
		if (connected)
		{	        
	        try 
	        {
	        	System.out.println("\nSending command to grabber: " + cmdStr);
	        		        	
	        	byte buffer[] = cmdStr.getBytes();   // This works !!!
	        	dataOutputStream.write(buffer);	         
			}
	        catch (IOException e) 
			{
				// TODO Auto-generated catch block
				System.out.println("!!!!Some trouble in sendGrabberCommand to grabber: " + e);
				e.printStackTrace();
				connected = false;           // should cause a reconnect attempt.
			} 	        
		}
		else 
		{
			System.out.println("!GrabberControlThread:sendGrabberCommand, not yet connected to grabber."); 	
			connected = false;
		}
	}
    	
}    
// end class GrabberControlThread.     
        




// Main class to send image/data files to server side.
//
class Send {
  
	static String serverName = "73.40.197.83";  // Default BlueJay Server IP; NY Cir router. Port fowared to Dev10.
	//static String serverName = "10.0.0.39";  // Dev7.
	                                   
    /* Constants */
    // TODO Replace with the serial port where your sender module is connected to.
    private static String TTY_PORT_0 = "/dev/ttyUSB0";             // USB port to XBee Cellular kit.
    private static String TTY_PORT_1 = "/dev/ttyUSB1";   
    // TODO Replace with the baud rate of your sender module.  
    private static final int BAUD_RATE = 921600;	// 115200  230400  921600 (need to set XBee Cellular using XCTU)
    
    private static int SERVER_PORT = 8207;                 // Server's receiver app listen port. (on DEV7 web server)

    private static final IPProtocol PROTOCOL_TCP = IPProtocol.TCP;
    
    public static CellularDevice myDevice = null;  
    
    
    
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

    	System.out.format("\n-- Starting BlueJay imageSend app rev 1909nn...\n");
    	
        String inputImagePath = "";
        String backupImagePath = "";
          
        String fileName = "";   // Filename sent with image.
        byte[] fileNameInBytes = new byte[64];  
        
        //fileNameInBytes = fileName.getBytes();
        int fileNameLen = 0;     
        
        //System.load("/usr/lib/jni/librxtxSerial.so"); 
        
    	//System.out.println("--Attempt connection to server side ...");
    	
        //Socket socket = new Socket(serverName, 8207);
        //socket.setSoTimeout(800);
    	//OutputStream outputStream = socket.getOutputStream();    
        
        if (args.length == 1)
        {
        	serverName = args[0];        	
        }
        else
        {
        	System.out.format("No command line args given.\n");        
        }
        
        System.out.format("-- BlueJay Server: %s \n", serverName );
    	System.out.format("-- tty Port to be used for ZigBee Cellular: '%s'\n", TTY_PORT_0);        
 
    	// Get the list of files in directory
    	
    	
    	// Put XBee device in bypass mode.  (no affect)
    	//
//    	FileWriter serPort = new FileWriter(TTY_PORT_0, true);CV_8UC1
//    	BufferedWriter out = new BufferedWriter(serPort);
//    	out.write("\n\n");   // Wake up boot menu.
//    	Thread.sleep(100);
//    	out.write("B\n");
//    	Thread.sleep(100);
//    	out.close();
//    	serPort.close();
    	
//    	serPort = new FileWriter(TTY_PORT_1, true);
//    	out = new BufferedWriter(serPort);
//    	out.write("\n\n");   // Wake up boot menu.
//    	Thread.sleep(500);
//    	out.write("B");
//    	Thread.sleep(500);
//    	serPort.close();    	
    	
       	//List<String> results = new ArrayList<String>();
    	
    	boolean run = true;
    	boolean connected = false;   // Flag to indicate the XBee connection status; timeouted, no ack msg, etc.
    	                             // false will cause a reconnection attempt.
     	
    	GrabberControlThread grabberControlThread = new GrabberControlThread();  // Thread to receive SMS txt messages.
    	grabberControlThread.run = run;
    	grabberControlThread.start();
    	
    	RcvGrabberMsgThread recvThread = new RcvGrabberMsgThread();         // Thread to receive TCP msgs from grabber.
    	recvThread.run = run;    	
    	recvThread.start();
    	recvThread.connected = grabberControlThread.connected;    	
    	recvThread.socket = grabberControlThread.socket;
    	
    	// loop continuously, checking for (new) files in the /data/images dir and sending those images to server side.
    	//
    	while (run)
    	{	    	    		
    		recvThread.connected = grabberControlThread.connected;
    		recvThread.socket = grabberControlThread.socket;
    		
			File devFile0 = new File(TTY_PORT_0);
			File devFile1 = new File(TTY_PORT_1);	    			
		    	   		
    		if (!connected)    // connection requested.
	   		{
    		   	try	    		   	
    	    	{			    	
    	    	    if (devFile0.exists())    // TTY Port 0  may be on port 0 or 1
    	    	    {
	    		   		System.out.println(">> Attempting new connection to XBee Cellular device via TTY_PORT_0 ... ");
    		   	    	
	       		   	    if (myDevice != null)            
	    		   	    {
	    		   	    	if (myDevice.isOpen())
    		   	    		{			    
    		   	    			myDevice.stopListening();
    		   	    			myDevice.close();			    		   	    			
    		   	    		}				    		   	    	
	    		   	    }
	       		   	    
	       		   	    myDevice = null;
	    	        	myDevice = new CellularDevice (TTY_PORT_0, BAUD_RATE);
	    	        	
	    	            myDevice.open();
	   	    			myDevice.setReceiveTimeout(6000);       // was 12 seconds.
	   	    
	   	    		   	connected = true;	  // Connection to XBee cellular is ready.
	   	 	    	
	   	    		   	MySMSReceiveListener listener =  new MySMSReceiveListener();
	   	    		   	listener.myDevice = myDevice;	   	    		   	
	   	    			myDevice.addSMSListener(listener);     // cb for incoming data msgs.
	   	    			listener.grabberThread = grabberControlThread;
	   				
	   	    			System.out.println("\n>> Waiting for SMS...");
	   	    			
						//myDevice.reset();
	    	            // myDevice.setSendTimeout(1000);
	    	           // myDevice.setParameter(parameter, parameterValue)
	    	            //String parameter = null;
	    	            //myDevice.getParameter(parameter);
    	    	    }
    	    	    else if (devFile1.exists())
    	    	    {
	    		   		System.out.println(" Attempting new connecting to XBee Cellular device via TTY_PORT_1 ... ");
	    		   	    if (myDevice != null)
	    		   	    {
	    		   	    	if (myDevice.isOpen())
	    		   	    		{			    
	    		   	    			myDevice.stopListening();
	    		   	    			myDevice.close();			    		   	    			
	    		   	    		}
	    		   	    }
	    		   	    
	    		   	    myDevice = null;
	    	        	myDevice = new CellularDevice (TTY_PORT_1, BAUD_RATE);
	    	        	
	    	            myDevice.open();
	   	    			myDevice.setReceiveTimeout(6000);       // was 12 seconds.
	    	          			    
	   	    		   	connected = true;	  // Connection to XBee cellular is ready.
		   	 	    	
	   	    		   	MySMSReceiveListener listener =  new MySMSReceiveListener();
	   	    		   	listener.myDevice = myDevice;	   	    		   	
	   	    			myDevice.addSMSListener(listener);     // cb for incoming msgs.
	   				
	   	    			System.out.println("\n>> Waiting for SMS...");
	    	            //myDevice.reset();
	    	            //myDevice.setParameter(parameter, parameterValue)
    	    	    }
    	    	    else
    	    	    {
    	    	    	connected = false;     // not connected yet, so try to connected again.
    	    	    	
    	    	    	System.out.println("!! TTY_USB Device file does not exist. Is device connected? ");
    	    	    	Thread.sleep(1000);    	    	  
    	    	    }
		   		}
    	    	catch (XBeeException e) 
    			{
    	    		connected = false;
    			    System.out.println(" !! Error opening ZigBee cellular myDevice: exc: " + e.getMessage() + "\n");
    			    System.out.println("\n     StackTrace: ");
    			    e.printStackTrace();
    			  
		   		}
	    	} // End if !connected.
    		
    		    		
    		grabberControlThread.sendGrabberCommand( "ping" );   //####    	
    		
		    System.out.println("\n>> Checking for existance of new image files, make connection to server ... ");
    		File[] files = new File("/data/images").listFiles();
    		
    		//ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));    		    	
    		//fileList.sort(c);
    		
    		System.out.println ("  Number of files now present: " + files.length );
//    		if (files.length == 0)
//    		{
//    			byte[] value = {1};    			     // Plane mode on; power off.
//    			if (myDevice != null)
//    			{
//    				myDevice.setParameter("AM", value );   	
//    				System.out.println ("  Powering Xbee RF OFF.");
//    			}
//    		}
//    		else
//    		{    	
//    			if (myDevice != null)
//    			{
//        			byte[] value = {0};    			     // Plane mode off; power on. 
//        			myDevice.setParameter("AM", value );
//        			System.out.println ("  Powering Xbee RF ON.");
//    			}
//    		}
    				
    		int fileCnt = 0;
    		for (File file : files)    // For each file in the /data/image dir.
	    	{
    			
	    		if (file.isFile())
	    		{	    
	    			fileCnt++;
	    	   		
	    	   		// Only proceed if device is open and connected to Internet;
	    	   		//
	    	   		if (myDevice == null )
	    	   		{
	    	   			connected = false;
	    	   			Thread.sleep(1000);
	    	   			break;	    	   		
	    	   		}
	    		   		    	   			    	
	    		   	if (!myDevice.isOpen() || !myDevice.isConnected() )
    		   		{
	    		   		System.out.println("-- WARNING: Device is not open and/or not connected to Internet.  ");
    		   			connected = false;             
    		   			break;
    		   		}	
	    		   			    			
	    		   	
	    			fileName = file.getName();
	    			System.out.format(">> Next Image file to be processed: '%s'\n", fileName) ;
	    			inputImagePath = "/data/images/" + fileName;
	    			backupImagePath = "/data_backup/images/" + fileName;
	    			//outputImagePathThumb = "/data/images/thumb_" + fileName;
	    			fileNameInBytes = fileName.getBytes();
	    		    fileNameLen = fileNameInBytes.length;
	    		    			    	
			        // Send size of filename string and then send image file to receiver side
			        //
			        byte[] sizeOfFilename = ByteBuffer.allocate(4).putInt(fileNameLen).array();
			       // outputStream.write(sizeOfName);
			       // outputStream.write(fileNameInBytes);
			    
			        // Sending first part of message.
			        try
			        {
//			        	myDevice.readDeviceInfo();			        	
//			        	Thread.sleep(100);
			        	
			        	//myDevice.setDestinationIPAddress((Inet4Address) Inet4Address.getByName(serverName));
			        	
//			        	System.out.format("Send null item. \n");
//				        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
//				                SERVER_PORT, PROTOCOL_TCP, null);				       
			        				        	
			        	System.out.format("---- Attempting new connection to server, send msg header/metadata . \n");			        	
			       
			        	if (!myDevice.isConnected()) System.out.println(" WARNING: Device not connected to Internet.");
			        
			        	
			        	myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),  // This will open socket connection to server, and			        	
				                SERVER_PORT, PROTOCOL_TCP, "X".getBytes());                     // send beginning of msg indicator 'X'.		        	
			        	
				        System.out.format("Send first data item to server: sizeOfFilename ... \n");	
				        		    		   	
//		        		if (connected)
//		        		{
//		        			Inet4Address myAddr = myDevice.getIPAddress();
//		        		    Inet4Address destAddr =	myDevice.getDestinationIPAddress();
//		        		    System.out.println("Server at: " + myAddr.toString() + "  Client at: " + destAddr.toString() );    		    
//		        		}
		        		
				        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				                SERVER_PORT, PROTOCOL_TCP, sizeOfFilename);
				        
				        System.out.format("Send filename to server:  ... \n");
				        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				                SERVER_PORT, PROTOCOL_TCP, fileNameInBytes);
				        connected = true;
			        }
			        catch (Exception e)
			        {
			        	   
			        	 System.out.println("!! Can't connect to server or Trouble sending msg header " +
			        	 		"\n!! and/or image metadata. exc: " + e.getMessage() + "\n");			       
			        	 e.printStackTrace();
			        	 connected = false;    // will try to reconnected.
			        	 break;
			        }
			        // resize to a fixed width (not proportional)       // now done by grabber 
			        //int scaledWidth = 640;
			        //int scaledHeight = 480;
			        //ImageResizer.resize(inputImagePath, outputImagePathThumb, scaledWidth, scaledHeight);
			        //ImageResizer.resize(inputImagePath, outputImagePathThumb, .5);
			        	
			        int sizeByte;
			        ByteArrayOutputStream byteArrayOutputStream = null;
			        try
			        {
				        // Read (resized) image file.
				        BufferedImage image = ImageIO.read(new File(inputImagePath));   // ### Exc here.
				        
				        byteArrayOutputStream = new ByteArrayOutputStream();
				        ImageIO.write(image, "jpg", byteArrayOutputStream);
				
				        sizeByte = byteArrayOutputStream.size();   // Size of image, in bytes    
			        }
			        catch (Exception ex)
			        {
			        	 System.out.println(" !! Trouble reading image from disk: exc: " + ex);
			        	 break;			        
			        }
			        
			        
			        System.out.println("--Sending image to server side ...");
			        // Send size of image to send
			        //
			        byte[] size = ByteBuffer.allocate(4).putInt(sizeByte).array();
			        System.out.println("Sending image byte count: " + byteArrayOutputStream.size());
			        
			        //outputStream.write(size);
			        try
			        {
			        	myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
			                SERVER_PORT, PROTOCOL_TCP, size);
			        	connected = true;  
			        }
			        catch (Exception ex)
			        {
			        	connected = false;  
			        	 System.out.println(" !! Trouble sending image byte count: " + byteArrayOutputStream.size());
			        	 System.out.println(" !! Exc: " + ex );
			        	 System.out.println(" !! stack: " + ex.getStackTrace() );
			        	 break;
			        }
			        // Send the image bytes.
			        //
			        int PACKET_SIZE = 1300;     // Max packet size should be 1500 bytes, but seems to more like 1300.
			        byte[] bytesSend = byteArrayOutputStream.toByteArray();    // image as a byte array, for sending.
			        //byte[] bytesSend = new byte[PACKET_SIZE];
			        
			        int numBytesRemaining = byteArrayOutputStream.size();
			        int numBytesSent = 0;
			        int startIndex = 0;
			        int endIndex = 0;
			        
			        System.out.println("--Now attempt to send image data ...");        
			        while (numBytesRemaining > 0)
			        {
			    	
			        	if (numBytesRemaining >= PACKET_SIZE)      // Send full packet.
			        	{
			        		int numBytesToSend = PACKET_SIZE;
			        		
			        		endIndex = startIndex + numBytesToSend;
			        		
			        		byte[] byteSendPartial = Arrays.copyOfRange( bytesSend, startIndex, endIndex ); 
			        		
			        		try
			        		{
			        			if (!myDevice.isConnected()) System.out.println(" WARNING: Device not connected to Internet.");
			        			
				        		myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				        								SERVER_PORT, PROTOCOL_TCP, byteSendPartial);
				        		
// Partial packet handshake. (180911)				        		
//				        	    System.out.println(" Sending partial end of packet marker 'Y'.");
//					        	myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
//						                SERVER_PORT, PROTOCOL_TCP, "Y".getBytes());    
//					        	
//					        	// Should get ack back form server; a 'Z' ;
//					        	//
//					        	IPMessage msg = myDevice.readIPData(1500);
//					        	if (msg != null)
//					        	{
//					        		String msgString = msg.getDataString();     // Should get a 'Z' ack.
//					        		System.out.println("Partial image packet sent: msgString response from server: " + msgString );
//					        	}
					            
				        		numBytesSent = numBytesToSend;
				
				        		numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
				        		connected = true;  
			        		}
			        		catch (Exception ex)
			        		{
			        			numBytesSent = 0;
			        			numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        			System.out.println(" !! Trouble sending full packet, ex: " + ex );
			        			System.out.println("\n     StackTrace: ");
					        	ex.printStackTrace();
			        			connected = false;  
			        		    break;               // Restart connection
			        		}
			        	  
			        		
			        	}
			        	else       // send remaining partial packet, which is less than Packet Size, and end of message marker.
			        	{
			        		int numBytesToSend = numBytesRemaining;
			        		
			        		endIndex = startIndex + numBytesToSend;
			        		
			        		byte[] byteSendPartial = Arrays.copyOfRange( bytesSend, startIndex, endIndex ); 
			        		     
			        		try
			        		{
				        		myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),    // Send final partial packet.
				        								SERVER_PORT, PROTOCOL_TCP, byteSendPartial);
				        							        						           
				        		numBytesSent = numBytesToSend;
				        		numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
				        		connected = true;  
				        		
//	Partial packet			        		myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),    // Send end of partial packet marker.
//						                SERVER_PORT, PROTOCOL_TCP, "Y".getBytes());    
//					        	
//					        	// Should get ack back form server 
//					        	//
//					        	IPMessage msg = myDevice.readIPData(2000);             // wait 2 seconds for ack.
//					        	if (msg != null)
//					        	{
//					        		String msgString = msg.getDataString();     // Should get a 'U' ack.
//					        		System.out.println(" Ack from Server " + msgString );
//					        	}
			        		}
			        		
			           		catch (Exception ex)
			        		{
			           			connected = false;  
			        			numBytesSent = 0;
			        			numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        			System.out.println(" !! Trouble sending partial packet, ex: " + ex );
			        			System.out.println("\n     StackTrace: \n");
					        	ex.printStackTrace();
			        		    break;
			        		}
			        	}		        
			        } // end while (numBytesRemaining)
				    
			        if (!connected) break;
			        
			        // Send end of message marker, get ack.
			        //
			        try
			        {				       
			        	myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),  // Send end of message marker; 'UUUU'.			        	
				                SERVER_PORT, PROTOCOL_TCP, "UUUU".getBytes());
			        	IPMessage msg = myDevice.readIPData(2000);                              // Wait up to two seconds for response of 'YYYY'.
			        	
			        	if (msg != null)
			        	{
			        		String msgString = msg.getDataString();     // Should get a 'YYYY' ack.
			        		System.out.println("End of msg ack from server:" + msgString );
			        		
			        		if (!msgString.contains("YYYY"))
			        		{
			        			System.out.println("! Did not receive end of message ack from client.");
			        			connected = false;
			        			break;
			        		}			        					        				      		
			        	}		        	
			        }
			        catch (Exception ex)
			        {
			        	System.out.println("! Trouble reading receive end of msg ack from server.\n ");
			        	ex.getStackTrace();
			        	connected = false;
			        	break;
			        }			        
		        	
			        // Move image file to Backup folder for archive.
			        //
			        if(connected)
			        {
			        	file.renameTo(new File(backupImagePath));
			        	System.out.println("-- Done sending file to server.\n");
			        }
			        if (fileCnt == 1) myDevice.sendSMSAsync("7039636672", "BlueJay Detection: " + fileName);      // Send alert txt msg.	
	    		} // End if file is a file.    		
	    		
	    		Thread.sleep(10);   		
	    	} // End for (File file : files)
    	
    		System.out.println("\n--Done attempt to send any available new images in image directory.");
    		
    		Thread.sleep(2000);     // was 2000 Check for new files every two seconds, or resend what's still there.
    	} // End while run.
    	    	
    	
        //outputStream.flush();
        //System.out.println("Flushed: " + System.currentTimeMillis());

        //Thread.sleep(10);
        //System.out.println("Closing: " + System.currentTimeMillis());
        //socket.close();
    	
    	recvThread.stop();    	
    	
        System.out.println("\n---- End of BlueJay Producer Client execution.");

    }
}
