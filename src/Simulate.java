import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.ButtonGroup;

/*
 rPeanut - is a simple simulator of the rPeANUt computer.
 Copyright (C) 2011-2013 Eric McCreath
 Copyright (C) 2012  Tim Sergeant
 Copyright (C) 2012  Joshua Worth

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

/*
 * With thanks to Jonathan Mettes who came up with idea of having a larger view of the screen.
 *  - Eric 2/5/2013 
 */

public class Simulate extends JPanel implements ActionListener, KeyListener,
		FocusListener, LayoutManager, MouseListener {

	static final Dimension scrollsize = new Dimension(350, 300);

	static final int numReg = 8;
	static final int MEMFAULTINT = 0;
	static final int IOINT = 1;
	static final int TRAPINT = 2;
	static final int TIMERINT = 3;

	static final int OFBIT = 0;
	static final int IMBIT = 1;
	static final int TIBIT = 2;

	static final int TIMERCOUNT = 1000;

	static final int SCREENHEIGHT = 230;
	static final int SCREENWIDTH = 192;
	static final int SCREENSTART = 0x7C40;

	static final int TERMINALLINES = 8;
	static final int TERMINALWIDTH = 40;
	static Font terminalfont = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	Register r[];
	Register SP, IR, SR, PC;

	private JTextArea terminal;
	StringBuffer terminalChar;
	boolean terminalCharInterrupt;

	Screen screen;
	SimWorker simworker;

	Memory memory;
	JTable memtable;
	JScrollPane memscroll;

	Cache cache;

	HardDisk harddisk;

	JButton step, run, fast, slow, stop;

	private ButtonGroup group;

	enum RunSpeed {
		FAST, NORMAL, SLOW
	}

	boolean stoprun;
	RunSpeed runspeed;

	boolean term;

	boolean halt;

	int count, delay;
	JLabel countLabel;
	JRadioButton chooseStyle, chooseStyle1, chooseStyle2;

	int fastcount;
	boolean timeroff;
	private boolean echoInput = false;
	private boolean profile = false;
	private TableColumn profileColumn;
	private int style;

	public Simulate(boolean term, boolean echo, boolean profile) {
		this.term = term;
		this.echoInput = echo;
		this.profile = profile;
		halt = false;
		count = 0;
		style = 0;
		fastcount = 0;
		timeroff = false;
		group = new ButtonGroup();
		r = new Register[numReg];

		for (int i = 0; i < numReg; i++) {
			r[i] = (Register) RegisterUI.createReg(term, "R" + i, style);
		}

		this.setLayout(this);

		SP = RegisterUI.createReg(term, "SP", style);

		IR = RegisterUI.createReg(term, "IR", style);

		SR = RegisterUI.createReg(term, "SR", style);
		PC = RegisterUI.createReg(term, "PC", style);

		memory = createMemory(term, this);
		if (!term) {
			memtable = new JTable((MemoryUI) memory);
			memtable.setRowHeight(Peanut.getUIFontHeight()); 
			// memtable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 32));
			memtable.getColumnModel().getColumn(0).setHeaderValue("");
			memtable.getColumnModel().getColumn(0).setMaxWidth(5);
			memtable.getColumnModel().getColumn(0).setPreferredWidth(5);
			memtable.getColumnModel().getColumn(0)
					.setCellEditor(new TableCellEditor() {
						@Override
						public Component getTableCellEditorComponent(
								JTable arg0, Object arg1, boolean arg2,
								int arg3, int arg4) {
							((MemoryUI) memory).toggleDB(arg3);
							return new DebugButton((MemoryUI)memory, arg3);
						}

						@Override
						public void addCellEditorListener(
								CellEditorListener arg0) {
						}

						@Override
						public void cancelCellEditing() {
						}

						@Override
						public Object getCellEditorValue() {
							return null;
						}

						@Override
						public boolean isCellEditable(EventObject arg0) {
							return true;
						}

						@Override
						public void removeCellEditorListener(
								CellEditorListener arg0) {
						}

						@Override
						public boolean shouldSelectCell(EventObject arg0) {
							return false;
						}

						@Override
						public boolean stopCellEditing() {
							return true;
						}
					});

			memtable.getColumnModel().getColumn(0)
					.setCellRenderer(new TableCellRenderer() {

						public Component getTableCellRendererComponent(
								JTable table, Object value, boolean isSelected,
								boolean isFocused, int row, int col) {
							boolean marked = (Boolean) value;
							if (marked) {
								return new Component() {

									public void paint(java.awt.Graphics g) {
										g.setColor(Color.blue);
										g.fillRect(2, 2, 6, 6);
									};

								};
							} else {
								Component comp = new Component() {

									public void paint(java.awt.Graphics g) {
										g.setColor(Color.white);
										g.fillRect(0, 0, 10, 10);
									};

								};

								return comp;
							}

						}
					});
			memtable.getColumnModel().getColumn(1).setHeaderValue("profile");
			memtable.getColumnModel().getColumn(1).setPreferredWidth(10);

			memtable.getColumnModel().getColumn(2).setHeaderValue("label");
			memtable.getColumnModel().getColumn(2).setPreferredWidth(30);
			memtable.getColumnModel().getColumn(3).setHeaderValue("address");
			memtable.getColumnModel().getColumn(3).setPreferredWidth(26);
			memtable.getColumnModel().getColumn(4).setHeaderValue("data");
			memtable.getColumnModel().getColumn(4).setPreferredWidth(55);
			memtable.getColumnModel().getColumn(4)
					.setCellRenderer(new CenterRender());
			memtable.getColumnModel().getColumn(5).setHeaderValue("");
			memtable.getColumnModel().getColumn(5).setPreferredWidth(30);
			memtable.setFont(Peanut.setUIFont(new Font(Font.MONOSPACED,
					Font.PLAIN, 14)));

			profileColumn = memtable.getColumnModel().getColumn(1);
			if (!profile) {
				memtable.removeColumn(profileColumn);
			}

			memscroll = new JScrollPane(memtable);
			memscroll.setPreferredSize(scrollsize);

		}

		cache = createCache(term, memory);

		harddisk = new HardDisk();

		terminal = new JTextArea(10, 10);
		terminal.setFont(Peanut.setUIFont(terminalfont));

		terminal.addKeyListener(this);
		terminal.addFocusListener(this);
		terminal.addMouseListener(this);

		terminal.setEditable(false);
		terminal.setPreferredSize(new Dimension(scrollsize.width,
				scrollsize.height / 2));

		terminalChar = new StringBuffer();
		terminalCharInterrupt = false;

		screen = new Screen(memory);

		countLabel = new JLabel("Count: " + count);
		chooseStyle = new JRadioButton("Hex", true);
		chooseStyle1 = new JRadioButton("Dec", false);
		chooseStyle2 = new JRadioButton("Ascii", false);
		group.add(chooseStyle);
		group.add(chooseStyle1);
		group.add(chooseStyle2);

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eActionCommand = e.getActionCommand();
				System.out.printf("e.getActionCommand() is %s\n",
						eActionCommand);
				if(eActionCommand.equals("Dec"))
					style = 1;
				else if(eActionCommand.equals("Ascii"))
					style = 2;
				else
					style = 0;
				for (int i = 0; i < numReg; i++) {
					r[i].updateWord1(style);
				}

				SP.updateWord1(style);

				IR.updateWord1(style);

				SR.updateWord1(style);
				PC.updateWord1(style);
				update();
			}
		};
		chooseStyle.addActionListener(actionListener);
		chooseStyle1.addActionListener(actionListener);
		chooseStyle2.addActionListener(actionListener);

		reset();

		if (!term) {
			for (int i = 0; i < 8; i++) {
				add((RegisterUI) r[i]);
			}

			add((RegisterUI) SP);
			add((RegisterUI) IR);
			add((RegisterUI) SR);
			add((RegisterUI) PC);
			step = makeButton("Step");
			run = makeButton("Run");
			fast = makeButton("Fast");
			slow = makeButton("Slow");
			stop = makeButton("Stop");
	

			add(countLabel);
			add(chooseStyle);
			add(chooseStyle1);
			add(chooseStyle2);

			this.add(screen);
			this.add(terminal);
			this.add(memscroll);
		}
	}

	private Memory createMemory(boolean term, Simulate simulate) {
		if (term) {
			return new MemoryTerm(simulate);
		} else {
			return new MemoryUI(simulate);
		}
	}

	private static Cache createCache(boolean term, Memory mem) {
		if (term) {
			return new CacheTerm(mem);
		} else {
			return new CacheUI((MemoryUI) mem);
		}
	}

	private JButton makeButton(String title) {

		JButton b = new JButton(title);
		b.setFont(Peanut.setUIFont(b.getFont()));
		b.setActionCommand(title);
		b.addActionListener(this);
		add(b);
		return b;
	}

	public void reset() {
		stoprun = true;
		// slowrun = false;
		runspeed = RunSpeed.NORMAL;
		halt = false;
		timeroff = false;
		count = 0;
		memory.reset();
		cache.reset();
		for (int i = 0; i < numReg; i++) {
			r[i].reset();
		}
		PC.set(0x0100);
		IR.reset();
		SP.set(0x7000);
		SR.reset();
		terminalChar = new StringBuffer();
		terminalCharInterrupt = false;
		if (!term) {
			((MemoryUI) memory).fireTableDataChanged();
			terminal.setText("");
			screen.refreshAll();
		}

	}

	public void update() {
		((MemoryUI) memory).fireTableDataChanged();
		((MemoryUI) memory).setHighlight();
		countLabel.setText("Count: " + count);
//		for (int i = 0; i < numReg; i++) {
//			r[i] = (Register) RegisterUI.createReg(term, "R" + i, style);
//		}
//
//		this.setLayout(this);
//
//		SP = RegisterUI.createReg(term, "SP", style);
//
//		IR = RegisterUI.createReg(term, "IR", style);
//
//		SR = RegisterUI.createReg(term, "SR", style);
//		PC = RegisterUI.createReg(term, "PC", style);
		screen.refreshAll();
		// this.validateTree(); had some issues in java 7
		// this.validate();

		centerMemoryScroll();

		this.repaint();
	}

	public void fastupdate() {
		fastcount++;
		if (fastcount >= 1000) {
			update();

			fastcount = 0;
		}
	}

	public void normalupdate() {
		((MemoryUI) memory).setHighlight();
		screen.refreshAll();
		fastcount++;
		if (fastcount >= 200) {
			update();
			fastcount = 0;
		}
	}

	public boolean step() { // this is our CPU
		try {
			count++;
			// load instruction
			IR.set(cache.get(PC.get()));
			PC.set((PC.get() + 1));
			int instruction = IR.get();
			if (instruction == 0x00000000) {
				halt = true;
				return false;
			}

			int msn = (instruction >> 28) & 0xf;

			if (msn == 0x1) { // add
				setreg3(instruction, getreg1(instruction)
						+ getreg2(instruction));
			} else if (msn == 0x2) { // sub
				setreg3(instruction, getreg1(instruction)
						- getreg2(instruction));
			} else if (msn == 0x3) { // mult
				setreg3(instruction, getreg1(instruction)
						* getreg2(instruction));
			} else if (msn == 0x4) { // div
				int mv = getreg2(instruction);
				if (mv != 0) {
					setreg3(instruction, getreg1(instruction) / mv);
				}
			} else if (msn == 0x5) { // mod
				int mv = getreg2(instruction);
				if (mv != 0) {
					setreg3(instruction, getreg1(instruction) % mv);
				}
			} else if (msn == 0x6) { // and
				setreg3(instruction, getreg1(instruction)
						& getreg2(instruction));
			} else if (msn == 0x7) { // or
				setreg3(instruction, getreg1(instruction)
						| getreg2(instruction));
			} else if (msn == 0x8) { // xor
				setreg3(instruction, getreg1(instruction)
						^ getreg2(instruction));
			} else if ((instruction & 0xff000000) == 0xA0000000) { // neg
				setreg3(instruction, -getreg2(instruction));
			} else if ((instruction & 0xff000000) == 0xA1000000) { // not
				setreg3(instruction, ~getreg2(instruction));
			} else if ((instruction & 0xff000000) == 0xA2000000) { // move
				setreg3(instruction, getreg2(instruction));
			} else if ((instruction & 0xffff0000) == 0xA3000000) { // call
				SP.set(SP.get() + 1);
				cache.set(SP.get(), PC.get());
				PC.set(instruction & 0x0000ffff);
			} else if ((instruction & 0xffff0000) == 0xA3010000) { // return
				PC.set(0xffff & cache.get(SP.get()));
				SP.set(SP.get() - 1);
			} else if ((instruction & 0xffff0000) == 0xA3020000) { // trap
				SP.set(SP.get() + 1);
				cache.set(SP.get(), PC.get());
				SR.set(SR.get() | (1 << IMBIT));
				PC.set(TRAPINT);
			} else if ((instruction & 0xffff0000) == 0xA4000000) { // jump
				PC.set(instruction & 0x0000ffff);
			} else if ((instruction & 0xfff00000) == 0xA4100000) { // jumpz
				if (getreg3(instruction) == 0) {

					PC.set(instruction & 0x0000ffff);
				}
			} else if ((instruction & 0xfff00000) == 0xA4200000) { // jumpn
				if ((getreg3(instruction) & 0x80000000) == 0x80000000) {
					PC.set(instruction & 0x0000ffff);
				}
			} else if ((instruction & 0xfff00000) == 0xA4300000) { // jumpnz
				if (getreg3(instruction) != 0) {
					PC.set(instruction & 0x0000ffff);
				}

			} else if ((instruction & 0xfff00000) == 0xA5000000) { // reset
				int inlsn = (instruction >> 16) & 0x000f;
				SR.set(SR.get() & (~(1 << inlsn)));

			} else if ((instruction & 0xfff00000) == 0xA5100000) { // set
				int inlsn = (instruction >> 16) & 0x000f;
				SR.set(SR.get() | (1 << inlsn));

			} else if ((instruction & 0xfff00000) == 0xA6000000) { // push
				SP.set(SP.get() + 1);
				cache.set(SP.get(), getreg3(instruction));
			} else if ((instruction & 0xfff00000) == 0xA6100000) { // pop
				setreg3(instruction, cache.get(SP.get()));
				SP.set(SP.get() - 1);
			} else if ((instruction & 0xff000000) == 0xB0000000) { // rotate
				int rot = (instruction) & 0x1f;
				int r2v = getreg2(instruction);
				setreg3(instruction, ((r2v << rot) | (r2v >>> 32 - rot)));
			} else if ((instruction & 0xf0000000) == 0xe0000000) { // rotate
				int rot = getreg1(instruction) & 0x1f;
				int r2v = getreg2(instruction);
				setreg3(instruction, ((r2v << rot) | (r2v >>> 32 - rot)));
			} else if ((instruction & 0xfff00000) == 0xC0000000) { // load
				// immediate
				int val = extend(instruction & 0xffff);
				setreg3(instruction, val);
			} else if ((instruction & 0xfff00000) == 0xC1000000) { // load
				// absolute

				setreg3(instruction, cache.get(instruction & 0xffff));
			} else if ((instruction & 0xff000000) == 0xC2000000) { // load
				// indirect
				setreg3(instruction,
						cache.get(0x0000ffff & getreg2(instruction)));
			} else if ((instruction & 0xff000000) == 0xC3000000) { // load base
				// +
				// displacement
				int val = extend(instruction & 0xffff);
				setreg3(instruction,
						cache.get(0x0000ffff & (val + getreg2(instruction))));
			} else if ((instruction & 0xff0f0000) == 0xD1000000) { // store
				// absolute
				int val = instruction & 0xffff;
				cache.set(val, getreg2(instruction));
			} else if ((instruction & 0xff000000) == 0xD2000000) { // store
				// indirect
				cache.set(0xffff & getreg3(instruction), getreg2(instruction));
			} else if ((instruction & 0xff000000) == 0xD3000000) { // store base
				// +
				// displacement
				int val = extend(instruction & 0xffff);

				cache.set((val + getreg3(instruction)), getreg2(instruction));
			}
		} catch (MemFaultException mfe) {
			SP.set(SP.get() + 1);
			try {
				cache.set(SP.get(), PC.get());
			} catch (MemFaultException e) { // note if we run out of stack
				// we have major problems
			}
			PC.set(MEMFAULTINT);
			SR.set(SR.get() | (1 << IMBIT));
		}

		// Traps and the like:
		try {
			D.p("Simulate interrupt: " + term + " " + terminalChar.length()
					+ " " + terminalChar);
			if ((count % TIMERCOUNT == 0 || timeroff)
					&& (SR.get() >> TIBIT & 1) == 1) {
				if ((SR.get() >> IMBIT & 1) != 1) {
					SP.set(SP.get() + 1);
					try {
						cache.set(SP.get(), PC.get());
					} catch (MemFaultException e) { // note if we run out of
													// stack
						// we have major problems
					}

					PC.set(TIMERINT);
					SR.set(SR.get() | (1 << IMBIT));
					timeroff = false;
				} else {
					timeroff = true;
				}
			} else if (((!term && terminalChar.length() > 0) || (term && System.in
					.available() > 0))
					&& terminalCharInterrupt
					&& (SR.get() >> IMBIT & 1) != 1) {
				SP.set(SP.get() + 1);
				try {
					cache.set(SP.get(), PC.get());
				} catch (MemFaultException e) { // note if we run out of stack
					// we have major problems
				}

				PC.set(IOINT);
				SR.set(SR.get() | (1 << IMBIT));
			}
		} catch (IOException e) {
		}

		// screen.refreshAll(); // need to move this

		if (!term) {
			if (((MemoryUI) memory).isDBmem(PC.get())) {

				stoprun = true;
			}
		}
		return true;
	}

	private int extend(int i) {
		if ((0x8000 & i) == 0x8000) {
			return i | 0xffff0000;
		} else {
			return i;
		}
	}

	private int getreg1(int instruction) {
		return getreg((instruction >> 24) & 0xf, instruction);
	}

	private int getreg2(int instruction) {
		return getreg((instruction >> 20) & 0xf, instruction);
	}

	private int getreg3(int instruction) {
		return getreg((instruction >> 16) & 0xf, instruction);
	}

	private void setreg3(int instruction, int value) {
		setreg((instruction >> 16) & 0xf, value);
	}

	private int getreg(int regindex, int instruction) {
		if (regindex >= 0 && regindex < 8) {
			return r[regindex].get();
		} else if (regindex == 8) {
			return SP.get();
		} else if (regindex == 9) {
			return SR.get();
		} else if (regindex == 10) {
			return PC.get();
		} else if (regindex == 11) {
			return 1;
		} else if (regindex == 12) {
			return 0;
		} else if (regindex == 13) {
			return -1;
		} else if (regindex == 14) {
			return extend(instruction & 0xffff);
		} else {
			return 0; // we should really cause a trap!
		}
	}

	private void setreg(int regindex, int value) {
		if (regindex >= 0 && regindex < 8) {
			r[regindex].set(value);
		} else if (regindex == 8) {
			SP.set(value);
		} else if (regindex == 9) {
			SR.set(value);
		} else if (regindex == 10) {
			PC.set(value);
		}
	}

	public void stepPush() {
		if (stoprun && !halt) {
			step();
			update();

		}
	}

	private void centerMemoryScroll() {
		Adjustable sb = memscroll.getVerticalScrollBar();
		int tpos = ((MemoryUI) memory).getScrollPos(PC.get());
		int rows = memtable.getRowCount();
		if (rows > 0) {
			sb.setValue(Math.max(0,
					(sb.getMaximum() * tpos / rows)
							- (sb.getVisibleAmount() / 2)));
		}
	}

	public void runPush() {
		runPush(RunSpeed.NORMAL);
	}

	public void runPush(RunSpeed runspeed) {
		if (stoprun && !halt) {
			stoprun = false;
			this.runspeed = runspeed;
			switch (runspeed) {
			case FAST:
				delay = 0;
				fastupdate();
				break;
			case NORMAL:
				delay = 1;
				normalupdate();
				break;
			case SLOW:
				delay = 1000;
				update();
				break;
			}
			simworker = new SimWorker(this);
			simworker.execute();
		} else if (!halt) {
			this.runspeed = runspeed;
			switch (runspeed) {
			case FAST:
				delay = 0;
				break;
			case NORMAL:
				delay = 1;
				break;
			case SLOW:
				delay = 1000;
				break;
			}
		}

	}

	public void stopPush() {
		update();
		stoprun = true;

	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("Step")) {
			stepPush();
		} else if (a.getActionCommand().equals("Run")) {
			runPush(RunSpeed.NORMAL);
		} else if (a.getActionCommand().equals("Slow")) {
			runPush(RunSpeed.SLOW);
		} else if (a.getActionCommand().equals("Fast")) {
			runPush(RunSpeed.FAST);
		} else if (a.getActionCommand().equals("Stop")) {
			stopPush();
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent a) {
		D.p("keyTyped" + a);
		if (a.isControlDown() && a.getKeyChar() == 0x0016) { // hit control v
			String str;
			try {
				str = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
						.getData(DataFlavor.stringFlavor);
				terminalChar.append(str);
				if (echoInput) {
					terminalAppend(str);
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (!(a.isControlDown() && (a.getKeyChar() == 0x0014
				|| a.getKeyChar() == 0x0010 || a.getKeyChar() == 0x0002 || a
					.getKeyChar() == 0x0012))) {
			terminalChar.append(a.getKeyChar());
			if (echoInput) {
				terminalAppend(a.getKeyChar());
			}
		}

	}

	public void terminalAppend(int value) {
		String content = terminal.getText()
				+ String.format("%c", 0x000000FF & value);
		if (countlines(content) > TERMINALLINES) {
			content = content.substring(content.indexOf('\n') + 1);
		}
		if (content.length() - content.lastIndexOf('\n') > TERMINALWIDTH) {
			content = content.substring(0, content.length() - 1) + "\n"
					+ content.charAt(content.length() - 1);
		}

		terminal.setText(content);
	}

	public void terminalAppend(String str) {
		String content = terminal.getText() + "\n"
				+ wrapit(TERMINALWIDTH, TERMINALLINES, str);
		while (countlines(content) > TERMINALLINES) {
			content = content.substring(content.indexOf('\n') + 1);
		}
		terminal.setText(content);
	}

	private String wrapit(int w, int l, String str) {
		int pos = 0;
		StringBuffer res = new StringBuffer();
		if (str.length() > w * l) {
			pos = str.length() - w * l;
		}
		while (pos < str.length()) {
			res.append(str.substring(pos, Math.min(pos + w, str.length())));
			res.append("\n");
			pos += w;
		}
		return res.toString();
	}

	private int countlines(String content) {
		int count = 0;
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '\n')
				count++;
		}
		return count;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		D.p("gained focus");
		terminal.setBackground(new Color(0.8f, 1.0f, 0.95f));
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		D.p("lost focus");
		terminal.setBackground(Color.white);

	}

	public void toggleEcho() {
		echoInput = !echoInput;
	}

	public boolean getEchoInput() {
		return echoInput;
	}

	public void toggleProfile() {
		profile = !profile;
		if (profile) {
			memtable.addColumn(profileColumn);
			memtable.moveColumn(5, 1);
		} else {
			memtable.removeColumn(memtable.getColumnModel().getColumn(1));
		}
	}

	public boolean getProfile() {
		return profile;
	}

	@Override
	public void addLayoutComponent(String arg0, Component arg1) {
		// TODO Auto-generated method stub

	}

	// we need to place step, run, fast, slow,
	@Override
	public void layoutContainer(Container arg0) {
		Dimension size = this.getSize();
		Dimension rs = ((RegisterUI) SP).getPreferredSize();

		int r2x = size.width - rs.width;
		int r1x = size.width - 2 * rs.width;
		int ry = 0;
		int ytop = rs.height;

		int idx = 1;
		for (int i = 0; i < 8; i++)
			((RegisterUI) r[i]).setBounds(r2x, idx++ * rs.height, rs.width,
					rs.height);

		((RegisterUI) SP).setBounds(r2x, (idx++) * rs.height, rs.width,
				rs.height);
		((RegisterUI) IR).setBounds(r2x, (idx++) * rs.height, rs.width,
				rs.height);
		((RegisterUI) SR).setBounds(r2x, (idx++) * rs.height, rs.width,
				rs.height);
		((RegisterUI) PC).setBounds(r2x, (idx++) * rs.height, rs.width,
				rs.height);

		countLabel.setBounds(r2x, (idx++) * rs.height, rs.width, rs.height);
		chooseStyle.setBounds(r2x, (idx++) * rs.height, rs.width, rs.height);
		chooseStyle1.setBounds(r2x, (idx++) * rs.height, rs.width, rs.height);
		chooseStyle2.setBounds(r2x, (idx++) * rs.height, rs.width, rs.height);

		int i = 0;
		int bts = 5;
		int pos;
		step.setBounds(pos = (size.width * (i++) / bts), 0,
				(size.width * i / bts) - pos, rs.height);
		run.setBounds(pos = (size.width * (i++) / bts), 0,
				(size.width * i / bts) - pos, rs.height);
		fast.setBounds(pos = (size.width * (i++) / bts), 0,
				(size.width * i / bts) - pos, rs.height);
		slow.setBounds(pos = (size.width * (i++) / bts), 0,
				(size.width * i / bts) - pos, rs.height);
		stop.setBounds(pos = (size.width * (i++) / bts), 0,
				(size.width * i / bts) - pos, rs.height);

		int hei;
		int wid;

		if ((rs.height * (8 + 4 + 1) + 2 * Simulate.SCREENHEIGHT) < size.height
				&& 3 * Simulate.SCREENWIDTH < size.width) {
			hei = 2 * Simulate.SCREENHEIGHT;
			wid = 2 * Simulate.SCREENWIDTH;
		} else {
			hei = Simulate.SCREENHEIGHT;
			wid = Simulate.SCREENWIDTH;
		}

		terminal.setBounds(0, size.height - hei, size.width - wid, hei);
		screen.setBounds(size.width - wid, size.height - hei, wid, hei);
		memscroll.setBounds(0, ytop, size.width - rs.width, size.height - hei
				- ytop);
	}

	@Override
	public Dimension minimumLayoutSize(Container arg0) {
		Dimension rs = ((RegisterUI) SP).getMinimumSize();
		return new Dimension(Simulate.SCREENWIDTH * 3 / 2, rs.height
				* (8 + 4 + 1) + Simulate.SCREENHEIGHT);
	}

	@Override
	public Dimension preferredLayoutSize(Container arg0) {
		Dimension rs = ((RegisterUI) SP).getMinimumSize();
		return new Dimension(rs.width * 5, rs.height * (8 + 4 + 1) + 2
				* Simulate.SCREENHEIGHT);
	}

	@Override
	public void removeLayoutComponent(Component arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		D.p("mouse clicked " + e);

		if (SwingUtilities.isMiddleMouseButton(e)) {
			D.p(" middle buttle ");
			Clipboard cb = getToolkit().getSystemClipboard();
			Transferable trans = cb.getContents(this);

			try {
				String str = (String) trans
						.getTransferData(DataFlavor.stringFlavor);
				D.p("str:" + str);

				terminalChar.append(str);
				if (echoInput) {
					terminalAppend(str);
				}

			} catch (UnsupportedFlavorException | IOException ex) {
				D.p("problem " + ex);
			}

		}

		// isRightMouseButton(e)) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
