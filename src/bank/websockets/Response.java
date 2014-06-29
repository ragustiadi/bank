package bank.websockets;

import java.io.Serializable;

public class Response implements Serializable {
	
	private Object object;
	private boolean notification;
	
	public Response(Object obj) {
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
