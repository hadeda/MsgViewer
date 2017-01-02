import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;


public class Dnp
{
	//static List<Question> qPool; // = new ArrayList<>();
	static List<Message> msgs;

	public static void main(String args[])  throws FileNotFoundException {
		if(args.length != 1) {
			System.out.println("Usage: prog filename");
			return;
		}

		DataReader rd = new DataReader(args[0]);
		msgs = rd.getMessages();
		System.out.println("queue size="+msgs.size());
		for(Message m : msgs) {
			System.out.println(m.getWholeMessage());
		}
/*
		boolean res;
		while ((res = rd.isMessageAvailable()) == true)
			System.out.println("Message Available:"+res);
*/
	}
}

