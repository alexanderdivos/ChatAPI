package de.mickare.chatapi;

public class Verify {


	public static <T> T checkNotNull(T o) {
	    if (o == null) {
	      throw new NullPointerException();
	    }
	    return o;
	  }

	
}
