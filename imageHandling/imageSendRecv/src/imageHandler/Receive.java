// Receive.java
//
// For receiving image files, one at a time.  
// rev: 171031A
//

package imageHandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.Spring;

public class Receive {

    public static void main(String[] args) throws Exception {
    	
    	Boolean run = true;
    	
    	System.out.println("--Starting image receive ...");
    	//while (run)
    	{
	        ServerSocket serverSocket = new ServerSocket(13085);
	        
	        System.out.println("--Waiting for client ...");
	        Socket socket = serverSocket.accept();
	        socket.setSoTimeout(8000);
	        InputStream inputStream = socket.getInputStream();
	
	        System.out.println("Reading time: " + System.currentTimeMillis());
	
	        String fileName = "newImage.jpg";      // New output file
	        byte[] sizeFileName = new byte[4];        // Size of filename;

	        inputStream.read(sizeFileName);
	        int sizeFile = ByteBuffer.wrap(sizeFileName).asIntBuffer().get();
	        System.out.println("Received FileName Size in bytes: " + sizeFile);
	        byte[] fileNameArray = new byte[sizeFile];
	       
	        inputStream.read(fileNameArray);
	        //char fileNameFromClient = ByteBuffer.wrap(fileNameArray).asCharBuffer().get();
	         
	        String fileNameFromClient =  new String(fileNameArray);
	        System.out.println("Filename: " + fileNameFromClient);
	        
	        byte[] sizeAr = new byte[4];        // Size of image in a byte array

	        inputStream.read(sizeAr);
	        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
	        System.out.println("Received Image Size in bytes: " + size);
	        byte[] imageAr = new byte[size];
	       
	        inputStream.read(imageAr);
		        
	        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
	        System.out.println("Received image dim: " + image.getHeight() + "x" + image.getWidth() + " time: " + System.currentTimeMillis());
	        
	        File fileOut = new File("C://data//images//" + fileNameFromClient);
	        		
	        if (fileOut.exists()) {
	        	fileOut.delete(); 
	        }
	      
	        ImageIO.write(image, "jpg", fileOut);
	        
	        serverSocket.close();
    	}
    	System.out.println("--End of image receive.");
    }
}