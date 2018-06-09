import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* ShoeSize - Eric McCreath 2015 - GPL 
 * This class stores a persons shoe size.
 */

public class ShoeSize implements Serializable{
	private static final String SHOESIZEENAME = "SHOESIZE";
	public static final int SHOESIZEMAX = 15;
	public static final int SHOESIZEMIN = 3;
	public static int SHOESIZE;

	static final String FILENAME = "shoesize.xml";
	static String filename = "data.ser";

	private Integer shoesize;

	public ShoeSize() {
		shoesize = null;
	}

	public String show() {
		return (shoesize == null ? "" : shoesize.toString());
	}

	public boolean set(Integer v) {
		if (v == null || v >= ShoeSize.SHOESIZEMIN && v <= ShoeSize.SHOESIZEMAX) {
			shoesize = v;
			save();
			return true;
		} else {
			shoesize = null;
			return false;
		}
	}
/*Bespoke method*/
	
//	static ShoeSize load() {
//        // add code here that will load shoe size from a file called "FILENAME"
//		ShoeSize ss = new ShoeSize();
//		try {
//			BufferedReader lr = Files.newBufferedReader(
//					new File(FILENAME).toPath(), Charset.forName("US-ASCII"));
//
//			String line = lr.readLine();
//			ss.shoesize = Integer.parseInt(line);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        return ss;
//	}
//
//	void save() {
//		// add code here that will save shoe size into a file called "FILENAME"
//		try {
//			BufferedWriter lw = Files.newBufferedWriter(
//					new File(FILENAME).toPath(), Charset.forName("US-ASCII"));
//
//			lw.append(shoesize+"");
//			lw.newLine();
//			lw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


	
/*Serializable method*/
//	public static ShoeSize load() {
//		// add code here that will load shoe size from a file called "FILENAME"
//		ShoeSize ss = new ShoeSize();
//		try {
//			ObjectInputStream ois = new ObjectInputStream (new FileInputStream(FILENAME));
//			ss = (ShoeSize) ois.readObject();
//			ois.close();
//		}
//
//
//
//
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return ss;
//	}
//
//	public void save() {
//	// add code here that will save shoe size into a file called "FILENAME"
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(
//					new FileOutputStream(FILENAME));
//			oos.writeObject(this);
//			oos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
/*Preference*/
	static Preferences prefs;

	static ShoeSize load() {
		ShoeSize ss = new ShoeSize();
		prefs = Preferences.userNodeForPackage(ShoeSize.class);
		ss.shoesize = prefs.getInt("ShoeSize", 9);

		return ss;
	}
	public void save() {
		ShoeSize data = new ShoeSize();
		data.prefs.putInt("ShoeSize", this.shoesize);
	}
	
/*XML method*/
//	static ShoeSize load() {
//		File f = new File(FILENAME);
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db;
//		ShoeSize res = new ShoeSize();
//		try {
//			// load the xml tree
//			db = dbf.newDocumentBuilder();
//			Document doc = db.parse(f);
//
//			// parse the tree and obtain the person info
//			Node shoesize = doc.getFirstChild();
//			NodeList nl = shoesize.getChildNodes();
//			for (int i = 0; i<= nl.getLength();i++) {
//				Node s = nl.item(i);
//				res.shoesize = Integer.parseInt(s.getTextContent());
//			}
//		} catch (Exception e) {
//			System.err.println("Problem loading " + FILENAME);
//		}
//		return res;
//	}
//
//	void save() {
//		File f = new File(FILENAME);
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db;
//		try {
//			// make the xml tree
//			db = dbf.newDocumentBuilder();
//			Document doc = db.newDocument();
//
//			Element en = doc.createElement("ShoeSize");
//			en.appendChild(doc.createTextNode(Integer.toString(this.shoesize)));
//			doc.appendChild(en);
//
//			// save the xml file
//			TransformerFactory transformerFactory = TransformerFactory
//					.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//
//			// set xml encoding to utf-8
//			transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");
//
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(f);
//			transformer.transform(source, result);
//		} catch (Exception e) {
//			System.err.println("Problem saving " + FILENAME);
//		}
//	}
	
}
