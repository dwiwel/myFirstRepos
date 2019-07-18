package imageHandler;

import com.digi.xbee.api.CellularDevice;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.ISMSReceiveListener;
import com.digi.xbee.api.models.SMSMessage;

public class MySMSReceiveListener implements ISMSReceiveListener {
	int itest;
	public CellularDevice myDevice; 
	String cmdStr = null;
	
	@Override
	public void smsReceived(SMSMessage smsMessage) {
		// TODO Auto-generated method stub
		String msgIn = smsMessage.getData();
		String phoneNum = smsMessage.getPhoneNumber();
		
		System.out.format(">>>Received SMS from %s >> '%s'", phoneNum , 
				msgIn);
		try {
			myDevice.sendSMSAsync(smsMessage.getPhoneNumber(), "BlueJay Ack: \n" + msgIn);
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        // ack msg
		
		msgIn = msgIn.toUpperCase(); 
		
		if (msgIn.matches("TAKE"))
		{
			try {
				myDevice.sendSMSAsync( phoneNum, "Cmd rcvd: TAKE image." );
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XBeeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cmdStr = msgIn;        // A valid command. 
		}
	}	
	
}
