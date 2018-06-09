import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.sun.javafx.fxml.expression.Expression.add;
/**
 * Created by wang on 2018/3/5.
 */
public class labone {

    public static void main(String[] args) {

        JFrame jFrame = new JFrame();
       jFrame.setSize(300,300);
       jFrame.setLocation(200,200);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel menuPanel = new JPanel();

        JMenuBar jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        jMenuBar.add(fileMenu);

        JMenu ToolMenu = new JMenu("Tools");
        JMenuItem CountWorld = new JMenuItem("Countword");
        ToolMenu.add(CountWorld);
        jMenuBar.add(ToolMenu);

        menuPanel.add(jMenuBar);
        mainPanel.add(menuPanel,BorderLayout.PAGE_START);
        JTextPane Textpanel = new JTextPane();

        Checkbox boldcheckbox = new Checkbox("bold");
        mainPanel.add(boldcheckbox,BorderLayout.PAGE_END);
        jFrame.add(mainPanel);


        boldcheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    attributes.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
                    Textpanel.setCharacterAttributes(attributes,true);

                }else {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    attributes.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
                    Textpanel.setCharacterAttributes(attributes,true);

                }

            }

        });

        mainPanel.add(Textpanel);

        jFrame.getContentPane().add(mainPanel);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // add a listener to menuitem count words
        CountWorld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int word_number =0;
                String text = Textpanel.getText();
                System.out.println(text);
                String [] words = text.split("\\s+");
                if(text==null || text.isEmpty() ){word_number=0;}
                else word_number =words.length;
                JOptionPane.showMessageDialog(null,"world count:"+word_number);
            }
        });

        jFrame.setVisible(true);

    }

}
