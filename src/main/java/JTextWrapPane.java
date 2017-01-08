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

/*
public void mouseReleased(MouseEvent e) {
    if (textArea.getSelectedText() != null) { // See if they selected something 
        String s = textArea.getSelectedText();
        // Do work with String s
    }
}
*/

class JTextWrapPane extends JTextPane {

    boolean wrapState = true;
    JTextArea j = new JTextArea();

    JTextWrapPane() {
        super();
    }

    public JTextWrapPane(StyledDocument p_oSdLog) {
        super(p_oSdLog);
    }


    public boolean getScrollableTracksViewportWidth() {
        return wrapState;
    }


    public void setLineWrap(boolean wrap) {
        wrapState = wrap;
    }


    public boolean getLineWrap(boolean wrap) {
        return wrapState;
    }
} 
