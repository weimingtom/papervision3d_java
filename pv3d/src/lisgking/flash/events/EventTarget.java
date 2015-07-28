package lisgking.flash.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTarget{
	private Map<String,List<Method>> events;
	
	public EventTarget(){
		this.events=new HashMap<String,List<Method>>();
	}
	public void addEventListener(String type, Method listener){
		if(!events.containsKey(type)){
			events.put(type,new ArrayList<Method>());
		}
		List<Method> listeners=events.get(type);
		if(listeners.indexOf(listener)==-1){
			listeners.add(listener);
		}
	}
	public void removeEventListener(String type){
		if(!events.containsKey(type)){
			return;
		}
		List<Method> listeners=events.get(type);
		listeners.clear();
	}
	public void removeEventListener(String type, Method listener){
		if(!events.containsKey(type)){
			return;
		}
		List<Method> listeners=events.get(type);
		int index=listeners.indexOf(listener);
		if(index!=-1){
			listeners.remove(index);
		}
	}
	public void dispatchEvent(Event event){
		String type=event.type;
		if(!events.containsKey(type)){
			return;
		}
		List<Method> listeners=events.get(event.type);
		int l=listeners.size(),i;
		for(i=0;i<l;i++){
			try{
				listeners.get(i).invoke(null,event);
			}catch(Exception ex){
				System.out.println("[error]"+ex.getMessage());
			}
		}
		String sep=Event.SEPARATOR;
		if(event.bubbles&&event.eventPhase==Event.AT_TARGET){
			int lastIndex;
			String superType=type;
			Event superEvent;
			while((lastIndex=superType.lastIndexOf(sep))!=-1){
				superType=superType.substring(0,lastIndex);
				superEvent=event.clone();
				superEvent.type=superType;
				superEvent.eventPhase=Event.BUBBLING_PHASE;
				this.dispatchEvent(superEvent);
			}
		}
	}
	public boolean hasEventListener(String type){
		return false;
	}
	public boolean willTrigger(String type){
		return false;
	}
}
