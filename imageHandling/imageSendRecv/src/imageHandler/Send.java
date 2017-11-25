// Send.java
// Sends an image to the receiver.
// Taken from:
//     https://stackoverflow.com/questions/25086868/how-to-send-images-through-sockets-in-java
//
// 171101, 171103

package imageHandler;

import java.net.ServerSocket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.*;

class Send {

    public static void main(String[] args) throws Exception {
    	
    	System.out.println("--Sending image ...");
        Socket socket = new Socket("localhost", 11010);
        socket.setSoTimeout(8000);
        OutputStream outputStream = socket.getOutputStream();

        String inputImagePath = "/data/images/test.jpg";
        String outputImagePathThumb = "/data/images/test_thumb.jpg";
         
        String fileName = "Cam01_171030_180045.jpg";   // Filename sent with image.
        byte[] fileNameInBytes = new byte[64];  
        
        fileNameInBytes = fileName.getBytes();
        int fileNameLen = fileNameInBytes.length;
        
        // Send size of filename and then filename to receiver
        //
        byte[] sizeOfName = ByteBuffer.allocate(4).putInt(fileNameLen).array();
        outputStream.write(sizeOfName);
        outputStream.write(fileNameInBytes);
    
        // resize to a fixed width (not proportional)
        int scaledWidth = 640;
        int scaledHeight = 480;
        //ImageResizer.resize(inputImagePath, outputImagePathThumb, scaledWidth, scaledHeight);
        ImageResizer.resize(inputImagePath, outputImagePathThumb, .2);

        // Read resized image file.
        BufferedImage image = ImageIO.read(new File(outputImagePathThumb));
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        int sizeByte = byteArrayOutputStream.size();       
        
        // Send size of image to send
        //
        byte[] size = ByteBuffer.allocate(4).putInt(sizeByte).array();
        System.out.println("Send Image size: " + byteArrayOutputStream.size());
        outputStream.write(size);
        
        // Send the image bytes.
        //
        byte[] bytesSend = byteArrayOutputStream.toByteArray();
        
        outputStream.write(bytesSend, 0, bytesSend.length);      // All the bytes are here okay. 
        Thread.sleep(1000);
        outputStream.flush();
        System.out.println("Flushed: " + System.currentTimeMillis());

        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());
        socket.close();
    	System.out.println("--Done sending image");

    }
}