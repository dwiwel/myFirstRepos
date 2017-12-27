//// Send.java
// Sends an image to the receiver.
// Taken from:
//     https://stackoverflow.com/questions/25086868/how-to-send-images-through-sockets-in-java
//
// 171101, 171103, 171129, 171201

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
import java.net.*;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.CopyOption.*;
import java.nio.file.StandardCopyOption.*;

class Send {

	static String serverName = "73.40.197.83";  // NY Cir router.
	//static String serverName = "10.0.0.39";  // NY Cir router.
	
    /* Constants */
    // TODO Replace with the serial port where your sender module is connected to.
    private static String TTY_PORT = "/dev/ttyUSB0";             // USB port to XBee Cellular kit.
    // TODO Replace with the baud rate of your sender module.  
    private static final int BAUD_RATE = 115200;	
    
    private static int SERVER_PORT = 8207;                 // Server's listen port.

    private static final IPProtocol PROTOCOL_TCP = IPProtocol.TCP;
    
    public static CellularDevice myDevice = null;  
    
    public static void main(String[] args) throws Exception {

        String inputImagePath = "";
        String outputImagePathThumb = "";
        String backupImagePath = "";
         
        String fileName = "";   // Filename sent with image.
        byte[] fileNameInBytes = new byte[64];  
        
        //fileNameInBytes = fileName.getBytes();
        int fileNameLen = 0;
        
    	System.out.println("--Connecting to server side ...");
    	
        //Socket socket = new Socket(serverName, 8207);
        //socket.setSoTimeout(800);
    	//OutputStream outputStream = socket.getOutputStream();    
        
    	System.out.format("\n-- tty Port to be used for ZigBee Cellular: '%s'\n", TTY_PORT);
        
 
    	// Get the list of files in directory
    	
    	//List<String> results = new ArrayList<String>();
    	
    	
    	boolean run = true;
    	
    	// loop continuously, checking for (new) files.
    	while (run)
    	{
		    System.out.println(" Checking for existance of image files ... ");
    		File[] files = new File("/data/images").listFiles();
    		for (File file : files)
	    	{
	    		if (file.isFile())
	    		{
	    		   	try
	    	    	{
	    	        	myDevice = new CellularDevice (TTY_PORT, BAUD_RATE);
	    	            myDevice.open();
	    	            myDevice.setReceiveTimeout(12000);       // 12 seconds.           
	    	            //myDevice.setParameter(parameter, parameterValue)
	    	    	}
	    	    	catch (XBeeException e) 
	    			{
	    			    System.out.println(" !! Error opening ZigBee cellular device: ");
	    			    e.printStackTrace();
	    			    break;
	    			} 
	    			fileName =  file.getName();
	    			System.out.format(">>> Next Image file to be processed: '%s'\n", fileName) ;
	    			inputImagePath = "/data/images/" + fileName;
	    			backupImagePath = "/data_backup/images/" + fileName;
	    			outputImagePathThumb = "/data/images/thumb_" + fileName;
	    			fileNameInBytes = fileName.getBytes();
	    		    fileNameLen = fileNameInBytes.length;
	    		    			    	
			        // Send size of filename string and then send image file to receiver side
			        //
			        byte[] sizeOfName = ByteBuffer.allocate(4).putInt(fileNameLen).array();
			       // outputStream.write(sizeOfName);
			       // outputStream.write(fileNameInBytes);
			    
			        try
			        {
				        System.out.format("Sending data to server:  ... \n");		            
				        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				                SERVER_PORT, PROTOCOL_TCP, sizeOfName);
				        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				                SERVER_PORT, PROTOCOL_TCP, fileNameInBytes);
			        }
			        catch (Exception e)
			        {
			        	 System.out.println(" !! Trouble sending filename info. exc: " + e);
			        	 System.out.println("     StackTrace: ");
			        	 e.printStackTrace();
			        	 break;
			        }
			        // resize to a fixed width (not proportional)
			        int scaledWidth = 640;
			        int scaledHeight = 480;
			        //ImageResizer.resize(inputImagePath, outputImagePathThumb, scaledWidth, scaledHeight);
			        //ImageResizer.resize(inputImagePath, outputImagePathThumb, .5);
			        
			        
			        // Read (resized) image file.
			        BufferedImage image = ImageIO.read(new File(inputImagePath));
			        
			        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			        ImageIO.write(image, "jpg", byteArrayOutputStream);
			
			        int sizeByte = byteArrayOutputStream.size();   // Size of image, in bytes    
			        
			        System.out.println("--Sending image to server side ...");
			        // Send size of image to send
			        //
			        byte[] size = ByteBuffer.allocate(4).putInt(sizeByte).array();
			        System.out.println("Sending Image size in bytes: " + byteArrayOutputStream.size());
			        //outputStream.write(size);
			        try
			        {
			        myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
			                SERVER_PORT, PROTOCOL_TCP, size);
			        }
			        catch (Exception ex)
			        {
			        	 System.out.println(" !! Trouble sending image size: " + byteArrayOutputStream.size());
			        	 System.out.println(" !! Exc: " + ex );
			        }
			        // Send the image bytes.
			        //
			        int PACKET_SIZE = 1500;     // Max packet size is 1500 bytes.
			        byte[] bytesSend = byteArrayOutputStream.toByteArray();    // image as a byte array, for sending.
			        //byte[] bytesSend = new byte[PACKET_SIZE];
			        
			        int numBytesRemaining = byteArrayOutputStream.size();
			        int numBytesSent = 0;
			        int startIndex = 0;
			        int endIndex = 0;
			        
			        while (numBytesRemaining > 0)
			        {
			    	
			        	if (numBytesRemaining >= PACKET_SIZE)      // Send full packet.
			        	{
			        		int numBytesToSend = PACKET_SIZE;
			        		
			        		endIndex = startIndex + numBytesToSend;
			        		
			        		byte[] byteSendPartial = Arrays.copyOfRange( bytesSend, startIndex, endIndex ); 
			        		
			        		try
			        		{
				        		myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				        								SERVER_PORT, PROTOCOL_TCP, byteSendPartial);
				        		numBytesSent = numBytesToSend;
				
				        		numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        		}
			        		catch (Exception ex)
			        		{
			        			numBytesSent = 0;
			        			numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        			System.out.println(" !! Trouble sending full packet, ex: " + ex );
			        		    break;
			        		}
			        	  
			        		
			        	}
			        	else       // send remaining partial packet, which is less than Packet Size.
			        	{
			        		int numBytesToSend = numBytesRemaining;
			        		
			        		endIndex = startIndex + numBytesToSend;
			        		
			        		byte[] byteSendPartial = Arrays.copyOfRange( bytesSend, startIndex, endIndex ); 
			        		     
			        		try
			        		{
				        		myDevice.sendIPData((Inet4Address) Inet4Address.getByName(serverName),
				        								SERVER_PORT, PROTOCOL_TCP, byteSendPartial);
				        		numBytesSent = numBytesToSend;
				        		numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        		}
			        		
			           		catch (Exception ex)
			        		{
			        			numBytesSent = 0;
			        			numBytesRemaining = numBytesRemaining - numBytesSent;
				        		startIndex = startIndex + numBytesSent;
			        			System.out.println(" !! Trouble sending partial packet, ex: " + ex );
			        		    break;
			        		}
			        	}		        
			        }        
			        if (myDevice != null) myDevice.close();
			        
			        // Move image file to Backup folder.
			        //
			        file.renameTo(new File(backupImagePath));
	    		}    		
	    		   	
	    		Thread.sleep(100);
	    		
	    	}
    	
    		System.out.println("--Done sending any new images in directory.");
    		Thread.sleep(5000);     // Check for new files every five seconds, or resend what's there.
    	} // End while true.
    	
        //outputStream.flush();
        //System.out.println("Flushed: " + System.currentTimeMillis());

        //Thread.sleep(10);
        //System.out.println("Closing: " + System.currentTimeMillis());
        //socket.close();
    	
 
        System.out.println("\n---- End of BluJay Producer Client execution.");

    }
}