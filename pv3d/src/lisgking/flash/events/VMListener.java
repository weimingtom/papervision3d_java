package lisgking.flash.events;

public class VMListener extends Listener{
	public synchronized static void onVmOperation(Event event){
		System.out.println("vm operation");
	}
	public synchronized static void onVmStatusChanged(Event event){
		System.out.println("vm status changed");
		throw new RuntimeException("trying to break the listener chain.");
	}
	public synchronized static void onVmStarted(Event event){
		System.out.println("vm started");
	}
	public synchronized static void onVmStopped(Event event){
		System.out.println("vm stopped");
	}
}
