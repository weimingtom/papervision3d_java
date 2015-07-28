package lisgking.flash.events;

import java.lang.reflect.Method;

public class Listener{
	public static Method getListener(Class type, String name){
		try{
			return type.getDeclaredMethod(name,Event.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
