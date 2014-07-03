package bank.websockets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class CommandDecoder implements Decoder.Binary<Object> {
	
	@Override
	public void destroy() { }

	@Override
	public void init(EndpointConfig config) { }

	@Override
	public Object decode(ByteBuffer bytes) throws DecodeException {
		Object dec = null;
		try {
			dec = new ObjectInputStream(new ByteArrayInputStream(bytes.array())).readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new DecodeException(bytes, e.getMessage(), e);
		}
		return dec;
	}

	@Override
	public boolean willDecode(ByteBuffer bytes) {
		return true;
	}

}
