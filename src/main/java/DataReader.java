import java.nio.*;
import java.io.*;
import java.lang.Object;
import java.util.*;
import java.io.FileNotFoundException;


public class DataReader
{
	//
	//Queue<Byte> queue = new LinkedList<>();
	//Queue<Byte> queue = new ArrayDeque<>();
	List<Byte> queue = new ArrayList<>();
	List<Message> msgs = new ArrayList<>();
	Byte array[];
	
	int[] table = {
	0x0000, 0x365E, 0x6CBC, 0x5AE2, 0xD978, 0xEF26, 0xB5C4, 0x839A,
	0xFF89, 0xC9D7, 0x9335, 0xA56B, 0x26F1, 0x10AF, 0x4A4D, 0x7C13,
	0xB26B, 0x8435, 0xDED7, 0xE889, 0x6B13, 0x5D4D, 0x07AF, 0x31F1,
	0x4DE2, 0x7BBC, 0x215E, 0x1700, 0x949A, 0xA2C4, 0xF826, 0xCE78,
	0x29AF, 0x1FF1, 0x4513, 0x734D, 0xF0D7, 0xC689, 0x9C6B, 0xAA35,
	0xD626, 0xE078, 0xBA9A, 0x8CC4, 0x0F5E, 0x3900, 0x63E2, 0x55BC,
	0x9BC4, 0xAD9A, 0xF778, 0xC126, 0x42BC, 0x74E2, 0x2E00, 0x185E,
	0x644D, 0x5213, 0x08F1, 0x3EAF, 0xBD35, 0x8B6B, 0xD189, 0xE7D7,
	0x535E, 0x6500, 0x3FE2, 0x09BC, 0x8A26, 0xBC78, 0xE69A, 0xD0C4,
	0xACD7, 0x9A89, 0xC06B, 0xF635, 0x75AF, 0x43F1, 0x1913, 0x2F4D,
	0xE135, 0xD76B, 0x8D89, 0xBBD7, 0x384D, 0x0E13, 0x54F1, 0x62AF,
	0x1EBC, 0x28E2, 0x7200, 0x445E, 0xC7C4, 0xF19A, 0xAB78, 0x9D26,
	0x7AF1, 0x4CAF, 0x164D, 0x2013, 0xA389, 0x95D7, 0xCF35, 0xF96B,
	0x8578, 0xB326, 0xE9C4, 0xDF9A, 0x5C00, 0x6A5E, 0x30BC, 0x06E2,
	0xC89A, 0xFEC4, 0xA426, 0x9278, 0x11E2, 0x27BC, 0x7D5E, 0x4B00,
	0x3713, 0x014D, 0x5BAF, 0x6DF1, 0xEE6B, 0xD835, 0x82D7, 0xB489,
	0xA6BC, 0x90E2, 0xCA00, 0xFC5E, 0x7FC4, 0x499A, 0x1378, 0x2526,
	0x5935, 0x6F6B, 0x3589, 0x03D7, 0x804D, 0xB613, 0xECF1, 0xDAAF,
	0x14D7, 0x2289, 0x786B, 0x4E35, 0xCDAF, 0xFBF1, 0xA113, 0x974D,
	0xEB5E, 0xDD00, 0x87E2, 0xB1BC, 0x3226, 0x0478, 0x5E9A, 0x68C4,
	0x8F13, 0xB94D, 0xE3AF, 0xD5F1, 0x566B, 0x6035, 0x3AD7, 0x0C89,
	0x709A, 0x46C4, 0x1C26, 0x2A78, 0xA9E2, 0x9FBC, 0xC55E, 0xF300,
	0x3D78, 0x0B26, 0x51C4, 0x679A, 0xE400, 0xD25E, 0x88BC, 0xBEE2,
	0xC2F1, 0xF4AF, 0xAE4D, 0x9813, 0x1B89, 0x2DD7, 0x7735, 0x416B,
	0xF5E2, 0xC3BC, 0x995E, 0xAF00, 0x2C9A, 0x1AC4, 0x4026, 0x7678,
	0x0A6B, 0x3C35, 0x66D7, 0x5089, 0xD313, 0xE54D, 0xBFAF, 0x89F1,
	0x4789, 0x71D7, 0x2B35, 0x1D6B, 0x9EF1, 0xA8AF, 0xF24D, 0xC413,
	0xB800, 0x8E5E, 0xD4BC, 0xE2E2, 0x6178, 0x5726, 0x0DC4, 0x3B9A,
	0xDC4D, 0xEA13, 0xB0F1, 0x86AF, 0x0535, 0x336B, 0x6989, 0x5FD7,
	0x23C4, 0x159A, 0x4F78, 0x7926, 0xFABC, 0xCCE2, 0x9600, 0xA05E,
	0x6E26, 0x5878, 0x029A, 0x34C4, 0xB75E, 0x8100, 0xDBE2, 0xEDBC,
	0x91AF, 0xA7F1, 0xFD13, 0xCB4D, 0x48D7, 0x7E89, 0x246B, 0x1235
	};
	
	public int calcCrc(int offset, int length) {
// System.out.println("\toffset="+offset);
		int crc = 0;
		for(int i=0; i<length; ++i) {
			crc = table[(crc^array[offset+i]) & 0xFF]^(crc >> 8);			 
		}
		return (~crc) & 0xFFFF;
	}

	public DataReader(String fileName) throws FileNotFoundException {
//System.out.println("Filename passed to DataReader is: "+fileName);
		try  ( FileInputStream reader = new FileInputStream(fileName)) {
			int i;

			while((i=reader.read()) != -1) {
				queue.add((byte)i);
			}
			reader.close();

		}
		catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		makeMessages();
	}

	public int size() {
		return queue.size();
	}

	public boolean headerValid(int offset) {
		if(array.length-offset < 10)
			return false; // not enough space for header
		if(array[offset]!=0x05)
			return false; // not at start
		if(array[offset+1]!=0x64) 
			return false; // second start byte wrong
		if(array[offset+2] < 5)
				return false; // invalid length

		if((short)calcCrc(offset, 8) != get16(offset+8)) 
			return false;
		/* At this point, header is checked and seems fine */
		return true;
	}

	public boolean makeMessage() {
		for (int offset=0; offset<array.length; offset++) {
			if(headerValid(offset)==true) {
				int bodyLength=array[2+offset]-5; // now set to length of body, less crc's
				int bodyLengthCrc;
				bodyLengthCrc = 18*(bodyLength/16);  // add crc every 16 bytes
				if(bodyLength%16 != 0)
					bodyLengthCrc += 2 + bodyLength%16;
				List<Byte> wholeMessage = new ArrayList<> (queue.subList(offset, offset+bodyLengthCrc+10));
				queue.subList(0, offset+bodyLengthCrc+10).clear();// clear processed message from list
				Message msg = new Message(wholeMessage);
				msgs.add(msg);
				return true;
			}
		}
		return false;
	}

	public void makeMessages() {
		do {
			array = new Byte[queue.size()];
			array = queue.toArray(array);
		} while (makeMessage()!=false);
	}

	/* return 16-bit value from array starting at given position */
	/* NB: DNP# message representation for 16 bit quantities is LSB then MSB */
	public short get16(int offset) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(array[offset]);
		bb.put(array[offset+1]);
		return bb.getShort(0);
	}

	public boolean isMessageAvailable() {
		array = new Byte[queue.size()];
		array = queue.toArray(array);
		int bodyOffset=0; // for CRC calc of body
		int msgLength=0;  // length = 3rd byte of message
		int numBytes=0;	  // actual number of bytes of message, including CRC
System.out.println("in isMessageAvailable *********************");

		for (int offset=0; offset<array.length; offset++) {
			if(array.length-offset < 10)
				return false; // not enough space for header
			if(array[offset]==0x05 && array[offset+1]==0x64) {
				if(array[offset+2] < 5)
					return false; // invalid length
			//short givenCrc=get16(offset+8);
			//System.out.printf("Stored crc: %02X, Calculated crc: %02X\n",givenCrc, calcCrc(offset,8));
			if((short)calcCrc(offset, 8) != get16(offset+8)) 
				return false;

// System.out.printf("\tHeader is found. offset=%d\n", offset);
			/* At this point, header is checked and fine */
			msgLength=array[offset+2]-5; // 5 bytes from header
			numBytes=10+18*(msgLength/16)+(msgLength%16)+2;
			while (msgLength>0) {
				int thisLength=msgLength>16?16:msgLength; // length of chunk to crc
				int offs= offset+bodyOffset+10+(msgLength%16)-thisLength;
System.out.println("\t\tmsgLength="+msgLength+", msg(%16)="+msgLength%16+", thisLength="+thisLength);
System.out.println("\t\there="+(offset));
System.out.println("\t\there="+(offset+bodyOffset));
System.out.println("\t\there="+(offset+bodyOffset+10));
System.out.println("\t\there="+(offset+bodyOffset+10+(msgLength%16)));
System.out.println("\t\there="+(offset+bodyOffset+10+(msgLength%16)-thisLength));
				if((short)calcCrc(offset+bodyOffset+10-thisLength+(msgLength%16), thisLength) != get16(offset+bodyOffset+10+msgLength%16))  {
			//System.out.printf("bodyOffset=%d, Stored crc: %02X, Calculated crc: %02X\n",bodyOffset, get16(offset+bodyOffset+10+msgLength%16), calcCrc(offset+bodyOffset-thisLength+10+msgLength%16, thisLength)); 
			//System.out.printf("Length byte: %d, totalNumBytes: %d offset=%d\n", array[offset+2], numBytes, offset);
					return false;
				}
				msgLength-=16;
				bodyOffset+=18;
			}

//System.out.println("\tBody is found");
			//System.out.printf("Length byte: %d, totalMsgLen: %d offset=%d\n", array[offset+2], numBytes, offset);
			queue.subList(0, offset+numBytes).clear();// clear message from list
			return true;
			}
		}

		return false;
	}

	public List<Message> getMessages()
	{
		return msgs;
	}


}
//                              | body starts here
// 05 64 0A 44 08 28 98 2D 00 D9 FE C7 81 00 00 64 13 (17)
//          --------------       ---------------
// 05 64 11 C4 96 2D 08 28 16 D7 C0 CE 01 3C 02 06 3C 03 06 3C 04 06 CA F3 (24)
//          --------------       ------------------------------------
