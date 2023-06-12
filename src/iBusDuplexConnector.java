import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;


public interface iBusDuplexConnector<T>  {
	
	
	public void postData(T data, int senderAdress );
	
	public void receiveData(T data, int senderAdress);
	
	public void registerDevice(iBusDevice device);
	
	public void registerWire( DuplexWire wire );
}