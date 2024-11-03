// Receive.java
//
// For receiving image files, one at a time.  
// rev: 171031A, 171103, 171129,  
//      180903, 180908 Moved to DEV10 (Ubuntu).
//      180912
//

package imageHandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
//import javax.swing.Spring;

class Receive {

	ServerSocket serverSocket = null;

	static BufferedImage image = null;
	static byte[] fileNameArray = null;
	static byte[] imageAr = null;
    static boolean connected = false;
    static InputStream inputStream = null;
    static OutputStream outputStream = null; // Data read from client.
    static Socket socket = null;
    static int temp;
    
	
	
	public static void main(String[] args) throws Exception {

		Boolean run = true;

		System.out.println("--Starting BlueJay image receive server (listen on port 8207) ...");
		ServerSocket serverSocket = new ServerSocket(8207);
		
		while (run)
		{
			if (!connected) {
				if (inputStream != null) inputStream.close();
				if (outputStream != null) outputStream.close();
				
				if (socket != null) {
					socket.close();
				}
				
				System.out.println("-- Waiting for client ...");
				socket = serverSocket.accept();		  // Blocks here until client requests connection.

				socket.setSoTimeout(8000);
				
				if (inputStream != null)					// ex.printStackTrace();
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();

				System.out.println("\n>> New client connected. Current time: " + System.currentTimeMillis());
				connected = true;
			}
			
			while (run) {          // After connection from client, run inner loop,
	
				byte[] startofMsg = new byte[1];
				byte[] sizeFileName = new byte[4]; // Size of filename;
				System.gc();
	
				int cnt = 0;
				while (startofMsg[0] != (byte) 'X') {
					try 
					{	cnt++;
						if (cnt > 4) { 
							System.out.println("!! Can'f find start of msg. Too many retries.");
							connected = false;         // Cause a reconnection.
							break;
						}		
						System.out.println("Looking for message header start char ... retry cnt: " + cnt);
						inputStream.read(startofMsg); // start of msg character.
						connected = true;
			
					} catch (Exception ex) 
					{
						System.out.println("! Trouble looking for start of msg character (timeout is okay). Exc: " + ex);
					}
				} // End while startOfMsg.
				if (connected == false) break;
				
				try 
				{
					System.out.println("Reading first metadata item; sizeFileName.");
					inputStream.read(sizeFileName); // first data item read from client
									

					connected = true;
				} catch (Exception ex) {
					System.out.println("!! Trouble reading first item in message; sizeFilename. Exc: " + ex);
					ex.printStackTrace();
					connected = false;
					break;               
				}
				if (connected == false) break;
	
				int sizeFilename = ByteBuffer.wrap(sizeFileName).asIntBuffer().get();
				System.out.println("Received FileName Size in bytes: " + sizeFilename);
				if (sizeFilename > 120){
					System.out.println("!! Size of filename is too large: " + sizeFileName);
					connected = false;
					break;
				}
				fileNameArray = null;
				fileNameArray = new byte[sizeFilename];
	
				int sizeImage = 0; // size of image byte array
				imageAr = null; // image array
				String fileNameFromClient = null;
				try {
					inputStream.read(fileNameArray);
					// char fileNameFromClient =
					// ByteBuffer.wrap(fileNameArray).asCharBuffer().get();
	
					fileNameFromClient = null;
					fileNameFromClient = new String(fileNameArray);
					System.out.println("Filename: " + fileNameFromClient);
					
					byte[] sizeAr = new byte[4]; // Size of image in a byte array (always < 9999)
	
					inputStream.read(sizeAr);
					sizeImage = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
					
					System.out.println("Received Image Size value in bytes: " + sizeImage);
					if (sizeImage > 2000000){
						System.out.println("!! Image size is too large: " + sizeImage);
						connected = false;
						break;
					}
					imageAr = null;
					imageAr = new byte[sizeImage];
					connected = true;
				} catch (Exception ex) {
					System.out.println("!! Trouble reading file attributes. Exc: " + ex);
					ex.printStackTrace();
					connected = false;
					break;
				}
	
				int numBytesRemaining = sizeImage;
				int totalNumBytesRead = 0;
				int numOfBytesRead = 0;
	
				if (imageAr != null)
					System.out.println("Length of imageAr: " + imageAr.length);
	
				// Read image array
				//
				//byte[] endOfPacketMark = new byte[1];
				try {
					System.out.println("Reading image array ...");
					while (numBytesRemaining > 0) {
						numOfBytesRead = inputStream.read(imageAr, totalNumBytesRead, numBytesRemaining);
						numBytesRemaining = numBytesRemaining - numOfBytesRead;
						totalNumBytesRead = totalNumBytesRead + numOfBytesRead;
// Partial packet handshake (180911)	
//						inputStream.read(endOfPacketMark);   //  Several packets make a complete image.
//						if (endOfPacketMark[0] == 'Y')
//							outputStream.write("Z".getBytes()); // Return end of
//																// packet mark.
						System.out.println("numOfBytesRead: " + numOfBytesRead);
					}
	
					// Thread.sleep(2);
					image = null;
					image = ImageIO.read(new ByteArrayInputStream(imageAr));
					if (image != null)
						System.out.println("Done receiving image, dim: " + image.getHeight() + "x" + image.getWidth() + " time: "
								+ System.currentTimeMillis());
									
					byte[] endOfMsg = new byte[5];
					inputStream.read(endOfMsg); // End of msg mark; should be "UUUU"
					String s = new String(endOfMsg);
					System.out.println( " End of msg marker from client: " + s  );
					
					outputStream.write("YYYY".getBytes()); // Send ack end of message marker to client				
					System.out.println("--Done receiving image msg.");
					connected = true;
				} catch (Exception ex) {
					System.out.println("!! Trouble reading the image from client. Ex: " + ex);
					ex.printStackTrace();
					connected = false;
					break;
				}
				File fileOut = new File("/data/images/" + fileNameFromClient);
	
	
				if (fileOut.exists()) {
					fileOut.delete();
				}
	
				if (image != null)
					ImageIO.write(image, "jpg", fileOut);
	
			} // end of inner run loop for running connected client.
		} // End of main run loop
		
		serverSocket.close();
		System.out.println("--End of image receive process.");
	}
}