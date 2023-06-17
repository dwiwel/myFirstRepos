// Class to read SMS messages from cellular device
//
// rev: 210710  Added invalid command reply to SMS client (cell phone txt msg)
//		221207  Added REBOOT SMS command. 


//import java.io.IOException;


package imageHandler;

import com.digi.xbee.api.CellularDevice;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.ISMSReceiveListener;
import com.digi.xbee.api.models.SMSMessage;

public class MySMSReceiveListener implements ISMSReceiveListener {
	int itest;
	public CellularDevice myDevice;
	public GrabberControlThread grabberThread;            
	
	String cmdStr = null;
	
	public String phoneNum;
	private int loopCounter;
	
	@Override
	public void smsReceived(SMSMessage smsMessage) {
		// TODO Auto-generated method stub
		String msgIn = smsMessage.getData();
		phoneNum = smsMessage.getPhoneNumber();     // phone num of Cell phone sending message.
		
		System.out.format(">>Received SMS from %s >> '%s' \n", phoneNum, msgIn);
		
		try {
			if (!phoneNum.equals("7039636672"))
				myDevice.sendSMSAsync("7039636672", "BlueJay Ack Unk Caller: \n" + phoneNum 
						+ ":\n" + msgIn);
			myDevice.sendSMSAsync(phoneNum, "BlueJay Ack: \n" + msgIn);
						
		} catch (TimeoutException e) {
			System.out.println("!Trouble with sendSMSAsync #1, timeout: " + e );			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			System.out.println("!Trouble with sendSMSAsync #2, XBee: " + e );
			e.printStackTrace();
		}
		
		msgIn = msgIn.toLowerCase(); 
		
		if (msgIn.matches("take") || msgIn.matches("takeimage") || msgIn.matches("ti"))
		{
			try {
				myDevice.sendSMSAsync( phoneNum, ">> Valid Cmd rcvd: TAKEIMAGE." );
			
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #1, timeout: " + e );
				e.printStackTrace();
			} catch (XBeeException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #2, Xbee: " + e );
				e.printStackTrace();		
			}
			
			cmdStr = "takeimage";        // A valid takeimage command; send to grabber app.
			try {				
				grabberThread.sendGrabberCommand( cmdStr, loopCounter);  // !!!!!!!
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("!!Trouble with sendGrabberCommand: " + e );
				e.printStackTrace();
			}
		}
		else if (msgIn.matches("reboot") || msgIn.matches("rb"))
		{
			try {
				myDevice.sendSMSAsync( phoneNum, ">> Valid Cmd rcvd: REBOOT." );
			
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #1, timeout: " + e );
				e.printStackTrace();
			} catch (XBeeException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #2, Xbee: " + e );
				e.printStackTrace();		
			}
			
			try
			{				
				myDevice.sendSMSAsync( phoneNum, ">> Rebooting RPi ..." );
				Process p = Runtime.getRuntime().exec("reboot");
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.out.println("!!Trouble with sendGrabberCommand REBOOT: " + e );
				e.printStackTrace();
			}
		}
					
		else
		{
			try {
				myDevice.sendSMSAsync( phoneNum, "INVALID Cmd rcvd! " );
			
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #1, timeout: " + e );
				e.printStackTrace();
			} catch (XBeeException e) {
				// TODO Auto-generated catch block
				System.out.println("!Trouble with sendSMSAsync #2, Xbee: " + e );
				e.printStackTrace();		
			}
		}	
	}	
	
}
