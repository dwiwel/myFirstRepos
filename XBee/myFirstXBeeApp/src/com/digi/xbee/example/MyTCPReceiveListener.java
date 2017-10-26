package com.digi.xbee.example;

import com.digi.xbee.api.listeners.IIPDataReceiveListener;
import com.digi.xbee.api.models.IPMessage;

public class MyTCPReceiveListener implements IIPDataReceiveListener   {

	public void ipDataReceived(IPMessage ipMessage) {
		// TODO Auto-generated method stub
		System.out.format("Received TCP msg from %s >> '%s' \n", ipMessage.getIPAddress(), ipMessage.getDataString());
		
	}

}
