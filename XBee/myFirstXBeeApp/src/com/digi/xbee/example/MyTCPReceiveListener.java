<<<<<<< HEAD

package com.digi.xbee.example;

import com.digi.xbee.api.listeners.IIPDataReceiveListener;
import com.digi.xbee.api.models.IPMessage;

public class MyTCPReceiveListener implements IIPDataReceiveListener   {

	public void ipDataReceived(IPMessage ipMessage) {
		// TODO Auto-generated method stub
		System.out.format("Received TCP msg from %s >> '%s' \n", ipMessage.getIPAddress(), ipMessage.getDataString());
		
	}

}
=======


package com.digi.xbee.example;

import com.digi.xbee.api.listeners.IIPDataReceiveListener;
import com.digi.xbee.api.models.IPMessage;

public class MyTCPReceiveListener implements IIPDataReceiveListener   {

	public void ipDataReceived(IPMessage ipMessage) {
		// TODO Auto-generated method stub
		System.out.format("Received TCP msg from %s >> '%s' \n", ipMessage.getIPAddress(), ipMessage.getDataString());
		
	}

}

>>>>>>> 745d6ea4f76af992b72f44803ef0efdafb2e2ad8
