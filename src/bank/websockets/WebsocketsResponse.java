package bank.websockets;

import java.io.Serializable;

/**
 * This class wraps an object sent from the server without distinguishing its kind.
 * @author rt
 *
 */
public class WebsocketsResponse implements Serializable {
	
	private Object object;
	private boolean notification;
	
	public WebsocketsResponse(Object obj) {
		object = obj;
	}
	
	public Object getObject() {
		return object;
	}
	
	public void setNotification(boolean n) {
		notification = n;
	}
	
	public boolean isNotification() {
		return notification;
	}
}
