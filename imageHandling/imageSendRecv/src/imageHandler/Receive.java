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
    	while (run)
    	{
	        ServerSocket serverSocket = new ServerSocket(13085);
	        
	        System.out.println("--Waiting for client ...");
	        Socket socket = serverSocket.accept();
	        InputStream inputStream = socket.getInputStream();
	
	        System.out.println("Reading: " + System.currentTimeMillis());
	
	        String fileName = "newImage.jpg";
	        // byte[] fileName = new byte[32];
	        
	        
	        byte[] sizeAr = new byte[16];        // Size of image.
	        inputStream.read(sizeAr);
	        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
	        System.out.println("Received Image Size: " + size);
	        byte[] imageAr = new byte[size];
	        inputStream.read(imageAr);
	
	        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
	
	        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
	        ImageIO.write(image, "jpg", new File("C://data//images//" + fileName));
	
	        serverSocket.close();
    	}
    	System.out.println("--Ending image receive ...");
    }
}