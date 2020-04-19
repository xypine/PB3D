/*
 * The MIT License
 *
 * Copyright 2020 Elias Eskelinen.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package compressor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameInputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.jpountz.lz4.LZ4SafeDecompressor;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;

/**
 *
 * @author Jonnelafin
 */
public class GUI extends JFrame{
        JTextArea input;
        JLabel output;
    public static void main(String[] args) {
        new GUI();
    }
    public GUI() throws HeadlessException {
        setTitle("Compressor GUI");
        setSize(600, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(JFrame.EXIT_ON_CLOSE);
        JLabel head;
        head = new JLabel("Compressor test");
        input = new JTextArea("Type the stuff to compress here");
        //scroll.add(input);
        JScrollPane scroll = new JScrollPane(input);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel from = new JPanel();
        //from.add(input);
        output = new JLabel("...");
        JPanel to = new JPanel();
        to.add(output);
        JButton button_compress = new JButton("Compress!");
        button_compress.addActionListener(new ButtonAct(1, this));
        JButton button_c_to_file = new JButton("Compress To File");
        button_c_to_file.addActionListener(new ButtonAct(2, this));
        JButton uncompress = new JButton("Uncompress!");
        uncompress.addActionListener(new ButtonAct(3, this));
        JButton uncompress_from_file = new JButton("Uncompress From File");
        uncompress_from_file.addActionListener(new ButtonAct(4, this));
        input.setMinimumSize(new Dimension(200, 200));
        JPanel center = new JPanel(new GridLayout(2, 2));
        center.add(button_c_to_file);
        //center.add(button_compress);
        center.add(uncompress_from_file);
        //center.add(uncompress);
        
        add(head, BorderLayout.PAGE_START);
        add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.LINE_END);
        add(output, BorderLayout.PAGE_END);
        setVisible(true);
    }

    
    
}
class ButtonAct implements ActionListener{
    int type;
    GUI parent;
    public ButtonAct(int type, GUI parent) {
        this.type = type;
        this.parent = parent;
    }
    int seed = 0x9747b28c;
    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(type == 1){
            LZ4Factory factory = LZ4Factory.fastestInstance();
            String input = parent.input.getText();
            byte[] data = {};
            try {
                data = input.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                
            }
            final int decompressedLength = data.length;
            // compress data
            LZ4Compressor compressor = factory.fastCompressor();
            int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
            byte[] compressed = new byte[maxCompressedLength];
            int compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);
            parent.input.setText(new String(compressed));
            System.out.println("---------");
            System.out.println(decompressedLength);
            //System.out.println(new String(compressed));
            System.out.println(compressedLength);
        }
        if(type == 2){
            LZ4Factory factory = LZ4Factory.fastestInstance();
            String input = parent.input.getText();
            byte[] data = {};
            try {
                data = input.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                
            }
            final int decompressedLength = data.length;
            // compress data
            try {
                long a = System.currentTimeMillis();
                LZ4FrameOutputStream outStream = new LZ4FrameOutputStream(new FileOutputStream(new File("test.lz4")));
                outStream.write(data);
                outStream.close();
                long newSize = new File("test.lz4").length();
                String out = decompressedLength + " --> " + newSize + " (" + (System.currentTimeMillis()-a) + "ms)";
                System.out.println(out);
                parent.output.setText(out);
                System.out.println("Saved file succesfully!");
            } catch (IOException iOException) {
                JFUtils.quickTools.alert(iOException + "");
            }
        }
        if(type == 3){
            LZ4Factory factory = LZ4Factory.fastestInstance();
            String input = parent.input.getText();
            byte[] data = {};
            try {
                data = input.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                
            }
            final int compressedLength = data.length;
            byte[] restored = new byte[compressedLength*20];
            // decompress data
            LZ4SafeDecompressor decompressor2 = factory.safeDecompressor();
            int decompressedLength2 = decompressor2.decompress(data, 0, compressedLength, restored, 0);
            parent.input.setText(new String(restored));
            System.out.println("---------");
            System.out.println(compressedLength);
            System.out.println(new String(restored));
            System.out.println(decompressedLength2);
        }
        if(type == 4){
            LZ4Factory factory = LZ4Factory.fastestInstance();
            String input = parent.input.getText();
            byte[] data = {};
            try {
                data = input.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                
            }
            data = new byte[Integer.MAX_VALUE/1000];
            final int decompressedLength = data.length;
            // compress data
            try {
                long a = System.currentTimeMillis();
                byte[] restored = new byte[decompressedLength];
                LZ4FrameInputStream inStream = new LZ4FrameInputStream(new FileInputStream(new File("test.lz4")));
                inStream.read(restored);
                inStream.close();
                parent.input.setText(new String(restored));
                String out = "Loaded file succesfully! (" + (System.currentTimeMillis()-a) + "ms)";
                parent.output.setText(out);
                System.out.println(out);
                restored = null;
            } catch (IOException iOException) {
                JFUtils.quickTools.alert(iOException + "");
            }
            data = null;
        }
    }
    
}