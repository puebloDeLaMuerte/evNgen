public class BusSimplexInputConnector<T> {
	
	public iBusReceiverDevice device;
	public T data;
	
	public void receiveData(T data, int senderAdress, int receiverAdress) {
		
		for (int a : device.getIBusIgnoreAdresses()) {
			if (a == senderAdress) {
				return;
			}
		}
		
		if (receiverAdress == 0 || receiverAdress == device.getIBusAdress()) {
			
			this.data = data;
			device.onDataReceived();
		}
	}
}
