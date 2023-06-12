public class BusSimplexOutputConnector<T> {
	
	SimplexWire wire;
	iBusDevice device;
	
	public BusSimplexOutputConnector(iBusDevice device) {
		this.device = device;
	}
	
	public void connectWire(SimplexWire w) {
		wire = w;
	}
	
	public void postData(T data, int receiverAdress) {
		wire.transmitData(data, device.getIBusAdress(), receiverAdress);
	}
}
