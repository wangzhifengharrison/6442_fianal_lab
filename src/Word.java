import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.JTextField;

/*
 rPeanut - is a simple simulator of the rPeANUt computer.
 Copyright (C) 2011  Eric McCreath

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Word extends JTextField implements KeyListener {
	protected int value;
	static Font wordfont = Peanut.setUIFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
	char style = 'a';
	static String str32 = "0x00000000";
	static String str33 = "00000000000000000000000000000000";
	static String str34 = "0000";
	
	public Word(char c) {
		this.style = c;
		this.addKeyListener(this);
		value = 0;
		this.setText(toString());
		this.setFont(wordfont);
		int width = this.getFontMetrics(wordfont).stringWidth(str32) + 5;;
		if(style == 'b')
			width = this.getFontMetrics(wordfont).stringWidth(str33) + 5;
		if(style == 'c')
			width = this.getFontMetrics(wordfont).stringWidth(str34) + 5;

		int height = this.getFontMetrics(wordfont).getHeight() + 3;
	//	this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
	//	this.setMaximumSize(new Dimension(width, height));
	}

	public Word(int v1) {
		this.addKeyListener(this);
		value = v1;
		this.setText(toString());
		this.setFont(wordfont);
	}

	public Word(int v1, int v2) {
		this.addKeyListener(this);
		value = ((v1 & 0xffff) << 16) | (v2 & 0xffff);
		this.setText(toString());
		this.setFont(wordfont);
	}

	public void updateWord(char c) {
		int width = 0;
		this.style = c;
		this.setText(toString());
		if(style == 'a')
			width = this.getFontMetrics(wordfont).stringWidth(str32) + 5;;
		if(style == 'b')
			width = this.getFontMetrics(wordfont).stringWidth(str33) + 5;
		if(style == 'c')
			width = this.getFontMetrics(wordfont).stringWidth(str34) + 5;

		int height = this.getFontMetrics(wordfont).getHeight() + 3;
		this.setMinimumSize(new Dimension(width, height));
	}

	/*
	 * public Word(int code, int r1, int r2, int rd, int add) { value = (((code
	 * & 0xF) << 28) | ((r1 & 0xF) << 24) | ((r2 & 0xF) << 20) | ((rd & 0xF) <<
	 * 16) | (add & 0xFFFF)) ; this.setFont(wordfont); }
	 */
	static int build(int code, int r1, int r2, int rd, int add) {
		return (((code & 0xF) << 28) | ((r1 & 0xF) << 24) | ((r2 & 0xF) << 20)
				| ((rd & 0xF) << 16) | (add & 0xFFFF));
	}

	static int build(int v1, int v2) {
		return ((v1 & 0xffff) << 16) | (v2 & 0xffff);
	}

	
	public String showDec() {
		return String.format("%d", value);
	}
	
	public String showAscii() {
		return String.format("%c%c%c%c", viewAscii((value>>24) & 0xFF),viewAscii((value>>16) & 0xFF),viewAscii((value>>8) & 0xFF),viewAscii((value>>0) & 0xFF));
	}

	static String hexTo2(String s) {
		s = s.replaceFirst("0x", "");
		String bi = "";
		for(int i = 0; i < s.length(); i++) {
			char num = s.charAt(i);
			String fournum = "";
			switch (num) {
				case '0':
					fournum = "0000";
					break;
				case '1':
					fournum = "0001";
					break;
				case '2':
					fournum = "0010";
					break;
				case '3':
					fournum = "0011";
					break;
				case '4':
					fournum = "0100";
					break;
				case '5':
					fournum = "0101";
					break;
				case '6':
					fournum = "0110";
					break;
				case '7':
					fournum = "0111";
					break;
				case '8':
					fournum = "1000";
					break;
				case '9':
					fournum = "1001";
					break;
				case 'a':
					fournum = "1010";
					break;
				case 'b':
					fournum = "1011";
					break;
				case 'c':
					fournum = "1100";
					break;
				case 'd':
					fournum = "1101";
					break;
				case 'e':
					fournum = "1110";
					break;
				case 'f':
					fournum = "1111";
					break;
			}
			bi += fournum;
		}
		if(bi.charAt(0) == '1'){
			char[] ca = bi.toCharArray();
			for(int i = 0; i < bi.length(); i++){
				if(bi.charAt(i) == '1') {
					ca[i] = '0';
				}else
					ca[i] = '1';
			}
			int flag = 1;
			for(int i = bi.length() - 1; i >= 0; i--){
				if(ca[i] == '1' && flag == 1) {
					ca[i] = '0';
				}else if((ca[i] == '0' && flag == 1)) {
					ca[i] = '1';
					flag = 0;
				}
				if(flag == 0)
					break;
			}
			bi = String.valueOf(ca);
		}
		return bi;
	}
	
	
	private char viewAscii(int i) {
		if (i >= 32 && i <= 126) return (char) i;
		return '.';
	}


	public String toString() {
		String hex = String.format("0x%04x%04x", 0xffff & (value >> 16),
				0xffff & value);
		String dec = showDec();
		String asc = showAscii();
		System.out.println(style);
		if (style == 'a') {
			System.out.println("a");
			System.out.println(hex);
			return hex;
		}
		else if(style == 'b') {
			System.out.println("b");
			System.out.println(dec);
			return dec;
		}
		else {
			System.out.println("c");
			System.out.println(asc);
			return asc;
		}
		
	}

	void set(int v) {
		value = v;
		this.setText(toString());
	}

	public int get() {
		return value;
	}

	
	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		D.p("key released : " + this.getText());
		String text = this.getText();
		if (text.length() > 10) {
			text = text.substring(0, 10);
			this.setText(text);
		} 
		Defines defines = new Defines();
		MySimpleTokenizer tok = new MySimpleTokenizer(defines,text);
		if (tok.hasCurrent() && tok.current() instanceof Integer) {
			value = ((Integer) tok.current()).intValue();
		}
		
	/*	ArrayList<ParseError> errorlist = new ArrayList<ParseError>();
		Attribute att = Ass.parse(text, new Lineinfo(text, 1, "<stdin>"),
				errorlist);
		if (errorlist.size() == 0 && att != null && att.type == AttType.VALUE
				&& att.val != null) {
			value = att.val;
			// System.out.println("value : " + value);
		}*/

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		D.p("key typed : " + this.getText());
	}
}
