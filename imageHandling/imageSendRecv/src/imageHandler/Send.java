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
        Socket socket = new Socket("localhost", 13085);
        OutputStream outputStream = socket.getOutputStream();

        BufferedImage image = ImageIO.read(new File("C:\\data\\images\\test.jpg"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        byte[] size = ByteBuffer.allocate(16).putInt(byteArrayOutputStream.size()).array();
        System.out.println("Send Image size: " + size);
        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        System.out.println("Flushed: " + System.currentTimeMillis());

        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());
        socket.close();
    	System.out.println("--Don sending image");

    }
}