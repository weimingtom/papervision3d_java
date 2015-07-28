package lisgking.flash.events;

import java.util.Date;

public class EventTest{
	public static void main(String[] args){
		EventDispatcher vmEvents=new EventDispatcher();
		vmEvents.addEventListener(VMEvents.OPERATION, null);
		vmEvents.addEventListener(VMEvents.STATUS_CHANGE, null);
		vmEvents.addEventListener(VMEvents.STARTING, null);
		vmEvents.addEventListener(VMEvents.STARTING, null);
		vmEvents.addEventListener(VMEvents.STARTING, null);
		vmEvents.addEventListener(VMEvents.STARTED, null);
		vmEvents.addEventListener(VMEvents.STOPPED, null);
		vmEvents.addEventListener(VMEvents.MODIFIED, null);
		vmEvents.addEventListener(VMEvents.IP_MODIFIED, null);
		vmEvents.addEventListener(VMEvents.STORAGE_MODIFIED, null);
		
		Event startingEvent=new Event(VMEvents.STARTING,true,new Date());
		vmEvents.dispatchEvent(startingEvent);
	}
}
