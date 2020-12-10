package imageHandler;

import java.io.IOException;

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
	
	@Override
	public void smsReceived(SMSMessage smsMessage) {
		// TODO Auto-generated method stub
		String msgIn = smsMessage.getData();
		String phoneNum = smsMessage.getPhoneNumber();
		
		System.out.format(">>Received SMS from %s >> '%s' \n", phoneNum, msgIn);
		
		try {
			myDevice.sendSMSAsync(smsMessage.getPhoneNumber(), "BlueJay Ack: \n" + msgIn);
			
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
			
			cmdStr = "takeimage";        // A valid takeimage command.
			try {
				grabberThread.sendGrabberCommand( cmdStr );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("!!Trouble with sendGrabberCommand: " + e );
				e.printStackTrace();
			}
		}
	}	
	
}
