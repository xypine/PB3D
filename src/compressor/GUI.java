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

import codenameprojection.demos.Record;
import codenameprojection.demos.demoInterface;
import codenameprojection.models.ModelUtils;
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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
public class GUI extends JFrame implements demoInterface{
    
    public final String NAME = "Compressor GUI";
    public final String COMMENT = "Simple lz4 compressor GUI & demo";
    
    
    JTextArea input;
    JLabel output;
    public static void main(String[] args) {
        new GUI(true);
    }

    public GUI() {
    }
    
    
    
    static {
        try {
            Record.announce(GUI.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ModelUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public GUI(boolean doStuff) throws HeadlessException {
        setTitle("Compressor GUI");
        setSize(600, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        System.out.println(JFrame.DISPOSE_ON_CLOSE);
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
        JButton compress_assets = new JButton("Compress the assets folder");
        compress_assets.addActionListener(new ButtonAct(5, this));
        JButton uncompress_assets = new JButton("Uncompress the assets folder");
        uncompress_assets.addActionListener(new ButtonAct(6, this));
        input.setMinimumSize(new Dimension(200, 200));
        JPanel center = new JPanel(new GridLayout(4, 1));
        center.add(button_c_to_file);
        //center.add(button_compress);
        center.add(uncompress_from_file);
        center.add(compress_assets);
        center.add(uncompress_assets);
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
                //inStream.read(restored);
                int read = inStream.read();
                LinkedList<Byte> red = new LinkedList();
                while(read != -1){
                    red.add((byte)read);
                    read = inStream.read();
                }
                inStream.close();
                Byte[] uncompressed = red.toArray(new Byte[red.size()]);
                byte[] clean = new byte[uncompressed.length];
                int ind = 0;
                for(Byte i : red){
                    clean[ind] = i;
                    ind++;
                }
                parent.input.setText(new String(clean));
                String out = "Loaded file succesfully! (" + (System.currentTimeMillis()-a) + "ms)";
                parent.output.setText(out);
                System.out.println(out);
                restored = null;
            } catch (IOException iOException) {
                JFUtils.quickTools.alert(iOException + "");
            }
            data = null;
        }
        if(type == 5){
            File dir = new File("assets_compressed");
            File dir2 = new File("assets");
            if(dir.exists()){
                try {
                    Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }
                        
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException ex) {
                    JFUtils.quickTools.alert(ex + "");
                    Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            dir.mkdirs();
            long size = compress_assets(dir2, "assets_compressed/", parent);
            /*
            int ind = 1;
            for(File i : dir2.listFiles()){
                System.out.println(i.getName());
                try {
                    String readFile = readFile(i.getPath(), Charset.defaultCharset());
                    LZ4FrameOutputStream outStream = new LZ4FrameOutputStream(new FileOutputStream(new File("assets_compressed/models/" + i.getName())));
                    outStream.write(readFile.getBytes("UTF-8"));
                    outStream.close();
                    parent.output.setText(ind + "/" + dir2.listFiles().length);
                } catch (IOException ex) {
                    //Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
                ind = ind + 1;
            }*/
            String out = "Assets folder compressed (" + getSize(dir2) + " --> " + size + ", " + (getSize(dir2)/size) + "x)";
            System.out.println(out);
            parent.output.setText(out);
        }
        if(type == 6){
            File dir = new File("assets_uncompressed");
            File dir2 = new File("assets_compressed");
            if(dir.exists()){
                try {
                    Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }
                        
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException ex) {
                    JFUtils.quickTools.alert(ex + "");
                    Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            dir.mkdirs();
            String out = "Uncompress Failed.";
            try {
                //uncompress_assets(dir2, "assets_uncompressed/", parent);
                long size = uncompress_assets(dir2, "assets_uncompressed/", parent);
                out = "Assets folder compressed (" + size + " --> " + getSize(dir) + ", " + (getSize(dir)/size) + "x)";
                
            } catch (Exception e3) {
                JFUtils.quickTools.alert(e3 + "");
            }
            /*
            int ind = 1;
            for(File i : dir2.listFiles()){
                System.out.println(i.getName());
                try {
                    String readFile = readFile(i.getPath(), Charset.defaultCharset());
                    LZ4FrameOutputStream outStream = new LZ4FrameOutputStream(new FileOutputStream(new File("assets_compressed/models/" + i.getName())));
                    outStream.write(readFile.getBytes("UTF-8"));
                    outStream.close();
                    parent.output.setText(ind + "/" + dir2.listFiles().length);
                } catch (IOException ex) {
                    //Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
                ind = ind + 1;
            }*/
            System.out.println(out);
            parent.output.setText(out);
        }
    }
    static long getSize(File dir){
        long size = 0;
        if(dir.isDirectory()){
            for(File i : dir.listFiles()){
                size = size + getSize(i);
            }
        }
        else{
            size = size + dir.length();
        }
        return size;
    }
    static boolean fileSupported(File f){
        String n = f.getName().toLowerCase();
        if(n.endsWith(".pb3d") || n.endsWith(".txt")){
            return true;
        }
        return false;
    }
    static long compress_assets(File dir, String prefix, GUI parent){
        int errors = 0;
        long size = 0;
        for(File i : dir.listFiles()){
            //System.out.println(prefix + i.getName());
            //System.out.println(i.getName());
            if (i.isFile() && fileSupported(i)) {
                try {
                    String readFile = readFile(i.getPath(), Charset.defaultCharset());
                    File file = new File(prefix + i.getName());
                    LZ4FrameOutputStream outStream = new LZ4FrameOutputStream(new FileOutputStream(file));
                    outStream.write(readFile.getBytes("UTF-8"));
                    outStream.close();
                    size = size + file.length();
                    //parent.output.setText(dir.listFiles().length);
                } catch (IOException ex) {
                    errors++;
                    JFUtils.quickTools.alert(ex + "");
                    Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            else if(i.isFile()){
                File file = new File(prefix + i.getName());
                System.out.println("Found an unsupported file: " + i.getName() + ", copying it instead");
                try {
                    Files.copy(i.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.println("Failed to copy " + i.getName());
                    errors++;
                }
            }
            else if(i.isDirectory()){
                System.out.println("Found folder: " + i.getName());
                File lol = new File(prefix + i.getName());
                lol.mkdir();
                size = size + compress_assets(i, prefix + i.getName() + "/", parent);
            }
        }
        System.out.println("Folder " + dir.getName() + " compressed with " + errors + " errors");
        return size;
    }
    static long uncompress_assets(File dir, String prefix, GUI parent){
        int errors = 0;
        long size = 0;
        for(File i : dir.listFiles()){
            //System.out.println(prefix + i.getName());
            //System.out.println(i.getName());
            if (i.isFile() && fileSupported(i)) {
                try {
                    LZ4FrameInputStream inStream = new LZ4FrameInputStream(new FileInputStream(i));
                    //inStream.read(restored);
                    int read = inStream.read();
                    LinkedList<Byte> red = new LinkedList();
                    while(read != -1){
                        red.add((byte)read);
                        read = inStream.read();
                    }
                    inStream.close();
                    Byte[] uncompressed = red.toArray(new Byte[red.size()]);
                    byte[] clean = new byte[uncompressed.length];
                    int ind = 0;
                    for(Byte i2 : red){
                        clean[ind] = i2;
                        ind++;
                    }
                    PrintWriter out = new PrintWriter(prefix + i.getName());
                    out.println(new String(clean));
                    out.close();
                    //parent.input.setText(new String(clean));
                    //parent.output.setText(dir.listFiles().length);
                    size = size + i.length();
                } catch (IOException ex) {
                    errors++;
                    JFUtils.quickTools.alert(ex + "");
                    Logger.getLogger(ButtonAct.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            else if(i.isFile()){
                File file = new File(prefix + i.getName());
                System.out.println("Found an unsupported file: " + i.getName() + ", copying it instead");
                try {
                    Files.copy(i.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.println("Failed to copy " + i.getName());
                    errors++;
                }
            }
            else if(i.isDirectory()){
                System.out.println("Found folder: " + i.getName());
                File lol = new File(prefix + i.getName());
                lol.mkdir();
                size = size + uncompress_assets(i, prefix + i.getName() + "/", parent);
            }
        }
        System.out.println("Folder " + dir.getName() + " compressed with " + errors + " errors");
        return size;
    }
    static String readFile(String path, Charset encoding) 
    throws IOException 
    {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    }
    
}