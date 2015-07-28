package lisgking.flash.events;

public class Event implements Cloneable{
	public static int CAPTURING_PHASE=1;
	public static int AT_TARGET=2;
	public static int BUBBLING_PHASE=3;
	public static String SEPARATOR=".";
	
	public String type;
	public boolean bubbles;
	public int eventPhase;
	public Object target;
	public long timeStamp;
	//-----------------------------------------------------------
	// constructors
	//-----------------------------------------------------------
	private Event(){
		this.eventPhase=Event.AT_TARGET;
		this.timeStamp=System.currentTimeMillis();
	}
	public Event(String type){
		this();
		if(type==null){
			throw new NullPointerException("Event type must be specified.");
		}
		this.type=type;
	}
	public Event(String type, boolean bubbles){
		this(type);
		this.bubbles=bubbles;
	}
	public Event(String type, boolean bubbles, Object target){
		this(type);
		this.bubbles=bubbles;
		this.target=target;
	}
	//-----------------------------------------------------------
	// overwrites
	//-----------------------------------------------------------
	@Override
	public String toString(){
		char colon=':';
		String sep=", ";
		StringBuffer sbuf=new StringBuffer();
		sbuf.append('{')
			.append("type").append(colon).append(type).append(sep)
			.append("bubbles").append(colon).append(bubbles).append(sep)
			.append("eventPhase").append(colon).append(eventPhase).append(sep)
			.append("target").append(colon).append(target).append(sep)
			.append("timeStamp").append(colon).append(timeStamp)
		.append('}');
		return sbuf.toString();
	}
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Event){
			Event that=(Event)obj;
			if(this==that||
				this.bubbles==that.bubbles&&
				this.eventPhase==that.eventPhase&&
				(this.target==that.target||this.target!=null&&this.target.equals(that.target))&&
				this.timeStamp==that.timeStamp&&
				(this.type==that.type||this.type!=null&&this.type.equals(that.type))){
				return true;
			}
		}
		return false;
	}
	public Event clone(){
		Event copy=new Event(this.type,this.bubbles,this.target);
		copy.timeStamp=this.timeStamp;
		copy.eventPhase=this.eventPhase;
		return copy;
	}
}
