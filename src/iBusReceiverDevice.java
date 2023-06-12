public interface iBusReceiverDevice<T> extends iBusDevice {
	
	public void onDataReceived();
	
	public int[] getIBusIgnoreAdresses();
}
