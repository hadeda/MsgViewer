package za.co.digix;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;


public class Message
{
	List<Byte> wholeMessageWithCrcs = new ArrayList<> ();

	Message(List<Byte> arg) {
		wholeMessageWithCrcs = arg;
/*
		for(Byte b : wholeMessageWithCrcs)
			System.out.printf("%02X ", b);
		System.out.println();
*/
	}

	public String getWholeMessage() {
		String ret="";
		String tmp="";
		for(Byte b : wholeMessageWithCrcs) {
			tmp=String.format("%02X ", b);
			ret += tmp;
		}
		ret+='\n';
		return ret;
	}

	public String getWholeMessageWithoutCrcs() {
		List<Byte> tlist = new ArrayList<Byte> (wholeMessageWithCrcs);
		int count=0;
		String ret="";
		String tmp="";

		/* copy across header, less crc's */
		for(int i=0; i<8; i++) {
			tmp=String.format("%02X ", tlist.get(0));
			ret += tmp;
			tlist.remove(0);
		}
		tlist.remove(0);
		tlist.remove(0);

		/* Remove last 2 bytes of message, which must be a crc */
		if(tlist.size()>0) tlist.remove(tlist.size()-1);
		if(tlist.size()>0) tlist.remove(tlist.size()-1);

		while (tlist.size()>0) {
			for(int i=0; i<16; i++) {
				tmp=String.format("%02X ", tlist.get(0));
				ret += tmp;
				tlist.remove(0);
				if(tlist.isEmpty())
					break;
			}
			if(tlist.size()>0) tlist.remove(tlist.size()-1);
			if(tlist.size()>0) tlist.remove(tlist.size()-1);
		}

		ret+='\n';
		return ret;
	}

}

