import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.util.List;
//import java.util.regex.Pattern;
//import java.io.FileNotFoundException;

public class Dnp3MessageViewer extends JFrame {

    private JFrame frame;
    private JLabel statusbar;
    private JScrollPane scrollPane;
    private JTextWrapPane displayTextPane;
    boolean highlightFromMaster=true;
    boolean highlightPrimaryMsg=true;
    private List<Message> msgs;
    private int itemsRead=0;
    //private JTextPane textArea1; // displays demo string

    public Dnp3MessageViewer() {

        initUI();
    }

 private void appendToPane(JTextPane textPane, String msg)
    {
	Color fg, bg;
	fg = isMsgFromMaster(msg)==true ? Color.RED : Color.BLUE;
	if(highlightFromMaster == false) fg=Color.BLACK;
	bg = isMsgInitiating(msg)==true ? Color.LIGHT_GRAY : Color.PINK;
	if(highlightPrimaryMsg == false) bg=Color.WHITE;

	appendToPane(textPane, msg, fg, bg);
    }

 private void appendToPane(JTextPane textPane, String msg, Color fg, Color bg)
    {
        StyledDocument doc = textPane.getStyledDocument();

        Style style = textPane.addStyle("MyStyle", null);

        StyleConstants.setForeground(style, fg);
        StyleConstants.setBackground(style, bg);

	Font font = new Font("Courier", Font.PLAIN, 16);
        textPane.setFont(font);

        try { doc.insertString(doc.getLength(), msg ,style); }
        catch (BadLocationException e){}

        //this.getContentPane().add(textPane);
	//itemsRead++;
    }

    private void populatePane() {
	displayTextPane.setText("");
	itemsRead=0;
	if (msgs != null) {
		for(Message m : msgs) {
      			appendToPane(displayTextPane, m.getWholeMessage());
			itemsRead++;
			//System.out.println(m.getWholeMessage());
		}
	}
	else {
		String s="Operating Instructions:\n\n";
      		s+="Select File->Open to read in messages from a valid DNP3 file. The file \"sol.dnp\" is the file I use for testing. \n\n";
      		s+="Select View for message filtering.  By default, MASTER messages are in red text and SLAVE in blue.\n";
      		s+="INITIATING (sometimes called PRIMARY) messages have a gray background.\n";
      		s+="View->Highlight allows you to eliminate colour, if you find it distracting.\n\n";
      		s+="Double-click any one of the messages shown to see its detail in a \"Detail Panel\"\n";
      		s+="The detail panel also highlights the componets of the message - Yellow for header, Blue for body and Red for\nCRC's.";
		s+="\n\n";
		s+="To Do:\n\n";
		s+="Filter out SYNC messages via View->Highlight.  How do we identify a SYN message?\n";
		s+="Would it be useful to have a panel in View to show all addresses?  If so, should they be source/dest, or both?\n";
		s+="Does the detail panel show addresses properly (the documentation is not clear on the interpretation of the\n2 bytes)?\n";
		s+="Would it be useful to filter messages from specific addresses?\n";
      		displayTextPane.setText(s);
	}
        statusbar.setText(itemsRead+" Messages read");
	scrollPane.setViewportView(displayTextPane);

    }

    private void initUI() 
    {
	// The Panel
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(550, 750));
		scrollPane.setSize(550, 750);
		scrollPane.setViewportView(displayTextPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	//frame.pack();
///*
        scrollPane.setVerticalScrollBarPolicy(
                  javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                  javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
/*
	JPanel panel = new JPanel();
	BoxLayout panelLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
	panel.setLayout(panelLayout);
	for(ExportableChartPanel chartPanel : chartPanels)  {
		panel.add(chartPanel);
		panel.add(Box.createVerticalStrut(5));
	}
*/
	
	// Scroll pane
	/* Put our wrappable pane into frame */
        displayTextPane = new JTextWrapPane();
        //displayTextPane.setContentType("text/html");
        displayTextPane.setEditable(false);
        displayTextPane.setLineWrap(false);

        //scrollPane = new JScrollPane(displayTextPane);
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(
                  javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                  javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	scrollPane.setViewportView(displayTextPane);
/*
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	scrollPane.setViewportView(panel);
*/

	// The frame
/*
	JFrame frame = new JFrame();
	frame.setTitle("Feature plot for Track scheme");
	frame.setIconImage(TRACK_SCHEME_ICON.getImage());
	frame.getContentPane().add(scrollPane);
	frame.validate();
	frame.setSize(new java.awt.Dimension(520, 320));
	frame.setVisible(true);
*/
	/* Set up frame */
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("DNP3 Message Viewer");
        frame.setSize(720, 500);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

	/* Create status bar */
        statusbar = new JLabel("Select File->Open to read in a valid DNP3 data file");
        statusbar.setBorder(BorderFactory.createEtchedBorder());
        frame.add(statusbar, BorderLayout.SOUTH);

	/* Create menu bar */
        JMenuBar menubar = createMenuBar();
        frame.add(menubar, BorderLayout.NORTH);

        //frame.add(scrollPane);
	frame.getContentPane().add(scrollPane);
	populatePane();
///////////////////////////////////////////////////////

	//frame.pack();
        //statusbar.setText(itemsRead+" Items read");

	// select given line on double-click */
      displayTextPane.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
               return;
            }
            if (e.getClickCount() != 2) {
               return;
            }

            int offset = displayTextPane.viewToModel(e.getPoint());

            try {
               int rowStart = Utilities.getRowStart(displayTextPane, offset);
               int rowEnd = Utilities.getRowEnd(displayTextPane, offset);
               String selectedLine = displayTextPane.getText().substring(rowStart, rowEnd);
        JDialog d1=new JDialog(frame,"DNP3 Message Details");
	d1.getContentPane().setBackground( Color.LIGHT_GRAY );
       
        // Set size
	d1.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
       
        // Set some layout
        //d1.setLayout(new GridBagLayout());
        //d1.setLayout(new FlowLayout());
        //d1.setLayout(null);
        d1.setLayout(new BorderLayout());
	//d1.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));
	d1.setLayout(new GridLayout(10, 1));
	d1.setPreferredSize(new Dimension(800, 300));
        d1.setSize(800,400);
       
        JButton ok = new JButton("OK");
        //d1.add(new JLabel(selectedLine));
	JLabel l1=qA("Msg:", selectedLine);
	l1.setBounds(30, 20, 800, 30); // left, top, width, height
	d1.add(l1);
	d1.add(new JLabel(colourMsg(selectedLine)));
	d1.add(qA("Length:       ", getMsgLength(selectedLine)));
	d1.add(qA("Control Byte: ", getMsgControl(selectedLine)));
	d1.add(qA("Destination:  ", getMsgShort(selectedLine, 4)));
	d1.add(qA("Source:       ", getMsgShort(selectedLine, 6)));
	String s;
	int ctrl = Integer.parseInt(selectedLine.substring(9, 11), 16);
	s= (ctrl&128)!=0 ? "(1) Master->Slave\n" : "(0) Slave->Master\n";
	d1.add(qA("Direction:    ", s));
	d1.add(qA("Origin:       ", (ctrl&64)!=0 ? "(1) Initiating Station\n" : "(0) Responding Station\n"));

        d1.add(ok);
       
	//d1.pack();
        d1.validate();
        d1.setVisible(true);
	d1.setLocationRelativeTo(null); // position centrally

      ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          d1.setVisible(false);
          d1.dispose(); // Closes the dialog
        }
      });

            } catch (BadLocationException e1) {
               e1.printStackTrace();
            }

         }
      });
    }

    private JLabel qA(String question, String answer) {
	String ret;
	ret = question + " " + answer;
	JLabel lab= new JLabel();
	Font font = new Font("Courier", Font.PLAIN, 14);
        lab.setFont(font);
	lab.setText(ret);
	return lab;
    }

    private JMenuBar createMenuBar() {

    JMenuBar menubar = new JMenuBar();

	/**** File Menu Option ****/
        JMenu fileMenu = new JMenu("File");
        fileMenu.setToolTipText("Manage Source of Data");
	/* Open Menuitem */
        JMenuItem fileOpen = new JMenuItem("Open File");
        fileOpen.setMnemonic(KeyEvent.VK_O); // set accelerator
        fileOpen.setToolTipText("Read data from file");
        fileOpen.addActionListener((ActionEvent event) -> {
		// get file name here
		//String filename = File.separator+"tmp";
		String filename=".";
		JFileChooser fc = new JFileChooser(new File(filename));
		// Show open dialog; this method does not return until the dialog is closed
		JFrame frame=new JFrame();
		fc.showOpenDialog(frame);
		File selFile = fc.getSelectedFile();
		if(selFile != null) {
			filename=fc.getSelectedFile().getAbsolutePath();
			if(filename!=null)
			try
			{
				// JOptionPane.showMessageDialog(new JFrame(), filename, filename, JOptionPane.INFORMATION_MESSAGE);
				DataReader rd = new DataReader(filename);
				msgs = rd.getMessages();
			}
			catch (FileNotFoundException e) { }
				//System.out.println("queue size="+msgs.size());
				populatePane();
        			itemsRead=msgs.size();
        			//statusbar.setText(itemsRead+" messages read");
		}
        });

        fileMenu.add(fileOpen);
        fileMenu.setMnemonic(KeyEvent.VK_F);

	/* Exit Menuitem */
 	JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        fileMenu.add(eMenuItem);
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        menubar.add(fileMenu);

	/**** View Menu Option ****/
        JMenu viewMenu = new JMenu("View");
        viewMenu.setToolTipText("Alter how messages are viewed");
        viewMenu.setMnemonic(KeyEvent.VK_V);
	/* Highlight Menuitem */
        JMenuItem viewHighlight = new JMenuItem("Highlight");
        viewHighlight.setMnemonic(KeyEvent.VK_H); // set accelerator
        viewHighlight.setToolTipText("Highlight Particular Messages");
        viewHighlight.addActionListener((ActionEvent event) -> {
// String name = JOptionPane.showInputDialog(menubar, "What is your name?", null);
MyDialog md = new MyDialog(null);
        });
        viewMenu.add(viewHighlight);

        menubar.add(viewMenu);

	/**** Help Menu Option ****/
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setToolTipText("Version Information");
        helpMenu.setMnemonic(KeyEvent.VK_H);
	/* About Menuitem */
        JMenuItem helpAbout = new JMenuItem("About");
        helpAbout.setMnemonic(KeyEvent.VK_A); // set accelerator
        helpAbout.setToolTipText("About This Software");
        helpAbout.addActionListener((ActionEvent event) -> {
//String name = JOptionPane.showInputDialog(menubar, "DNP3 Message Viwer by Digix Computers version 1.0.", null);
JOptionPane.showMessageDialog(new JFrame(), "Dnp3 Message Viewer by Digix Computers.\nVersion 1.0.", "Dialog", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(helpAbout);

        menubar.add(helpMenu);

	return menubar;
    }


class MyDialog extends JDialog {
    Container cp;
      JButton ok = new JButton("OK");
  public MyDialog(JFrame parent) {
    super(parent, "Message Highlighter", true);
    cp = getContentPane();
    cp.setLayout(new FlowLayout());
        JCheckBoxMenuItem cbFromMaster = new JCheckBoxMenuItem("Master/Slave Highlight");
        cbFromMaster.setToolTipText("Use text colour to differentiate between messages from MASTER and messages from SLAVE messages");
        cbFromMaster.setSelected(highlightFromMaster);
        cp.add(cbFromMaster);
        JCheckBoxMenuItem cbPrimaryMsg = new JCheckBoxMenuItem("Initiate/Response Highlight");
        cbPrimaryMsg.setToolTipText("Use background colour to differentiate between INITIATE and RESPONSE messages");
        cbPrimaryMsg.setSelected(highlightPrimaryMsg);
        cp.add(cbPrimaryMsg);
      cp.add(ok);
      ok.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	  highlightFromMaster=cbFromMaster.isSelected();
	  highlightPrimaryMsg=cbPrimaryMsg.isSelected();
	  populatePane();
          setVisible(false);
          dispose(); // Closes the dialog
        }
      });
    //cp.add(new JLabel("Here is my dialog"));
    setSize(250, 225);
        setLocationRelativeTo(null);
      setVisible(true);
  }
  }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Dnp3MessageViewer ex = new Dnp3MessageViewer();
            ex.setVisible(true);
        });
    }

/* get short at given offset, return as string.  Offset is in message bytes, not string offset */
private String getMsgShort(String msg, int offset) {
	String s="";
	int ourOffset=offset*3;
	String ourMsg="";
	ourMsg+=msg.charAt(ourOffset);
	ourMsg+=msg.charAt(ourOffset+1);
	int lsb = Integer.parseInt(ourMsg, 16);
	
	ourMsg="";
	ourMsg+=msg.charAt(ourOffset+3);
	ourMsg+=msg.charAt(ourOffset+4);
	int msb = Integer.parseInt(ourMsg, 16);

	int v = (msb<<8)+lsb;
	s=String.format("Hex=%02x. Dec=%d", v, v);
	//s=ourMsg;
	return s;
}

private String getMsgLength(String msg) {
	String s="";
	int v = Integer.parseInt(msg.substring(6, 8), 16); // idx 6 inclusive, idx 8 exclusive
	//s=String.valueOf(v);
	s=String.format("(%d) Header=%d, Body=%d", v, 10, v-5);
	return s;
}

private String getMsgControl(String msg) {
	String s="";
	int v = Integer.parseInt(msg.substring(9, 11), 16); // idx 6 inclusive, idx 8 exclusive
	s=String.valueOf(v);
	s=String.format("Hex=%02x. Binary=%s", v, Integer.toBinaryString(v));
	return s;
}

private boolean isMsgFromMaster(String msg) {
	int ctrl = Integer.parseInt(msg.substring(9, 11), 16);
	if ((ctrl&128) == 0)
		return false;
	return true;
}

// return true if this is a primary message
private boolean isMsgInitiating(String msg) {
	int ctrl = Integer.parseInt(msg.substring(9, 11), 16);
	if ((ctrl&64) == 0)
		return false;
	return true;
}

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

private String colourMsg(String msgOrig) {
	String m=msgOrig.replaceAll("(\\r|\\n)", "");
	String m2=m.replaceAll(" ", "");
    	byte[] data = hexStringToByteArray(m2); // should now have original message, in byte form

	String s="<html> ";
	int start=0, end=10; // header is 10 long
	s+=colourMsgSubsData(data, start, end, "<font color='Yellow'>");

	end=data.length;
	for(start=10; start<end; start+=18) {
		s+=colourMsgSubsData(data, start, start+18, "<font color='Blue'>");
	}

	s+='\n';
	return s;
}

private String colourMsgSubsData(byte[] data, int startIdx, int endIdx, String col) {
	int remainder=data.length-startIdx;
	int requestedLength=endIdx-startIdx;
	String tmp;
	int i;

	if(requestedLength>remainder)
		requestedLength=remainder;

	String s=col;
	for(i=startIdx; i<startIdx+requestedLength-2; i++) {
		tmp=String.format("%02X ", data[i]);
		s += tmp;
	}
	s+="<font color='red'>";
	tmp=String.format("%02X ", data[i++]); s += tmp;
	tmp=String.format("%02X ", data[i++]); s += tmp;

	return s;
}

}
