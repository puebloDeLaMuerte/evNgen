public class SimplexWire<T> extends Wire{
	
	private BusSimplexInputConnector inputCon;
	
	public void ConnectInput( BusSimplexInputConnector inputConnector ) {
		inputCon = inputConnector;
	}
	
	public void transmitData(T data, int senderAdress, int receiverAdress) {
		inputCon.receiveData(data, senderAdress, receiverAdress);
	}
}
