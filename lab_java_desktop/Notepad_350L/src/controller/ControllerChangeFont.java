/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.FontDialog;
import view.View;

/**
 *
 * @author dell
 */
public class ControllerChangeFont {

    FontDialog font;
    JTextArea textOutPut;
    Font f;

    public ControllerChangeFont(JFrame parent, Boolean check) {
        font = new FontDialog(parent, check);
        // get output textarea of notepad
        textOutPut = ((View) parent).getTxtOutPut();
        // add value for list font farmily,fontStyle,fontSize

        addListFont();
        addListSize();
        setFileText();

        //add action
        action();
        font.setVisible(true);
        font.setResizable(false);

    }

    // add FontName into ListFont
    public void addListFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontNames[] = ge.getAvailableFontFamilyNames();
        //String fo = ge.get
        DefaultListModel model = new DefaultListModel();
        // add element Font Family
        for (int i = 0; i < fontNames.length; i++) {
            model.addElement(fontNames[i]);
        }
        font.getjListFont().setModel(model);
    }

    //add size into ListSize
    public void addListSize() {
        DefaultListModel model = new DefaultListModel();
        // size (2;72)
        for (int i = 2; i < 72; i += 2) {
            model.addElement(i);
        }
        font.getjListSize().setModel(model);
    }

    //set file text FontView
    public void setFileText() {
        f = textOutPut.getFont();
        font.getTxtSample().setFont(f);
        // Font family
        font.getjListFont().setSelectedValue(f.getFamily(), true);
        font.getjTextFieldFont().setText(f.getFamily());
        // size
        font.getjListSize().setSelectedValue(f.getSize(), true);
        font.getjTextFieldSize().setText(Integer.toString(f.getSize()));
        //Font Style
        String[] style = {"Plain", "Bold", "Italic", "Bold Italic"};
        DefaultListModel model = new DefaultListModel();
        for (String s : style) {
            model.addElement(s);
        }
        font.getjListFontStyle().setModel(model);
        font.getjTextFieldFontStyle().setText(style[f.getStyle()]);
        font.getjListFontStyle().setSelectedIndex(f.getStyle());

    }

    public void setPreView() {
        Font current = font.getTxtSample().getFont();
        String fName = current.getFontName();
        int fStyle = current.getStyle();
        Integer size = 0;
        try {
            //get size from list size up textFild size
            size = Integer.valueOf(font.getjTextFieldSize().getText());
            // size in not (1; 100)
            if (size <= 0 || size >= 72) {
                return;
            }
        } catch (NumberFormatException ex) {
            size = 12;
        }
        // if not try catch then point mouse is the fist
        try {
            font.getjListSize().setSelectedValue(size, true);
        } catch (Exception ex) {
        }
        font.getTxtSample().setFont(new Font(fName, fStyle, size));
    }

    public void action() {
        font.getjButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.dispose();
            }
        });

        font.getjListFont().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectFontFamily();
            }
        });
        font.getjListFontStyle().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectFontStyle();
            }
        });
        font.getjListSize().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectFontSize();
            }
        });
        font.getjButtonOk().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = 0;
                try {
                    ////get size from list size up textFild size
                    size = Integer.valueOf(font.getjTextFieldSize().getText());
                    // size in not (1; 72)
                    if (size <= 0 || size >= 72) {
                        size = 12;
                    }
                } catch (NumberFormatException ex) {
                    size = 12;
                }
                Font current = font.getTxtSample().getFont();
                String fName = current.getFontName();
                int fStyle = current.getStyle();
                // set TxtOutput
                textOutPut.setFont(new Font(fName, fStyle, size));
                font.dispose();
            }
        });

        font.getjTextFieldSize().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setPreView();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setPreView();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setPreView();
            }
        });
    }

    //acction user select fontfamily
    // 3 task
    //1 select
    // 2 return value in preview fontfamily
    //3 setfont whith note preview
    public void selectFontFamily() {
        Font current = font.getTxtSample().getFont();
        int fStyle = current.getStyle();
        int fSize = current.getSize();
        // get font name
        String fName = font.getjListFont().getSelectedValue().toString();
        //set text Font
        font.getjTextFieldFont().setText(fName);
        //get Font
        Font f = new Font(fName, fStyle, fSize);
        //set txtView 
        font.getTxtSample().setFont(f);
    }

    //acction user select fontStyle
    // 3 task
    //1 select
    // 2 return value in preview fontStyle
    //3 setfont whith note preview 
    public void selectFontStyle() {
        Font cur = font.getTxtSample().getFont();
        String fName = cur.getFontName();
        int fSize = cur.getSize();
        // get 
        int fStyle = font.getjListFontStyle().getSelectedIndex();
        font.getjTextFieldFontStyle().setText(font.getjListFontStyle().getSelectedValue().toString());
        Font f = new Font(fName, fStyle, fSize);
        font.getTxtSample().setFont(f);
    }
    //acction user select size
    // 3 task
    //1 select
    // 2 return value in preview size
    //3 setfont whith note preview 

    public void selectFontSize() {
        Font current = font.getTxtSample().getFont();
        String fName = current.getFontName();
        int fStyle = current.getStyle();
        int fSize = (int) font.getjListSize().getSelectedValue();
        font.getjTextFieldSize().setText(font.getjListSize().getSelectedValue().toString());
        Font f = new Font(fName, fStyle, fSize);
        font.getTxtSample().setFont(f);
    }

}