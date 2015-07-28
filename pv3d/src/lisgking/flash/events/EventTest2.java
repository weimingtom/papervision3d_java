package lisgking.flash.events;

public class EventTest2{
	public static void main(String[] args){
		EventTarget vmEvents=new EventTarget();
		vmEvents.addEventListener(VMEvents.OPERATION, Listener.getListener(VMListener.class,"onVmOperation"));
		vmEvents.addEventListener(VMEvents.STATUS_CHANGE, Listener.getListener(VMListener.class,"onVmStatusChanged"));
		vmEvents.addEventListener(VMEvents.STARTED, Listener.getListener(VMListener.class,"onVmStarted"));
		
		Event startedEvent=new Event(VMEvents.STARTED,true);
		
		vmEvents.dispatchEvent(startedEvent);
	}
}
