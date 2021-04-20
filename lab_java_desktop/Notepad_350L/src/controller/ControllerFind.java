/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import view.FindDialog;
import view.View;

/**
 *
 * @author PHT
 */
public class ControllerFind {

    FindDialog find;
    String textOutPut;
    JTextArea output;

    public ControllerFind(JFrame parent, boolean check) {
        find = new FindDialog(parent, check);
        // get output textarea of notepad
        output = ((View) parent).getTxtOutPut();
        find.setTitle("Find");
        find.getRadioUp().setSelected(true);
        find.setVisible(true);
        find.setResizable(false);
        find.setLocationRelativeTo(find);
        action();
        
    }

    public void action() {
        find.getBtNext().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindText();
            }
        });
        find.getBtCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                find.dispose();
            }
        });
    }

    // return string you want find
    public String getValueTextFiled() {
        return find.getTxtOutPut().getText();
    }
    //check case true or false
    public boolean checkCase() {
        return find.getCheckCase().isSelected();
    }
    //check around true or false
    public boolean checkAround() {
        return find.getCheckAround().isSelected();
    }
    //check find with up or dow ( true is down or false id up )
    public boolean checkDown() {
        return find.getRadioDown().isSelected();
    }
    // return index with down of string want find with index start
    public int indexDown(int indexStart) {
        int pos;
        // find checkcase or not checkcase
        if (checkCase()) {
            pos = this.textOutPut.indexOf(getValueTextFiled(), indexStart);
        } else {
            pos = this.textOutPut.toLowerCase().indexOf(getValueTextFiled().toLowerCase(), indexStart);
        }
        return pos;
    }
    // return index with up of string want find with index start and string check
    public int indexUp(int indexStart) {
        int pos;
        // find checkcase or not checkcase
        if (checkCase()) {
            pos = this.textOutPut.lastIndexOf(getValueTextFiled(), indexStart);
        } else {
            pos = this.textOutPut.toLowerCase().lastIndexOf(getValueTextFiled().toLowerCase(), indexStart);
        }
        return pos;
    }
    // logic fint string
    public void FindText() {
        this.textOutPut = output.getText();
        //return point of mouse on string
        int indexStart = this.output.getCaretPosition();
        int pos = 0;
        // before check find you want check find down or find up
        if (checkDown()) {
            pos = indexDown(indexStart);
            // if you find string yes return index no return -1 
            // but if you select checkaround then you should check more than one String output with index = 0 
            if (checkAround()) {
                if (pos == -1) {
                    pos = indexDown(0);
                }
            }
            //if index  = -1 then not found String in String Notepad
            if (pos == -1) {
                JOptionPane.showMessageDialog(find, "Not found!");
                // dont find String on String notepad then set point of mouse with index befor find
                output.setCaretPosition(indexStart);
            } else {
                // if select sucsess then you should select with String you want fint
                // index point of mouse return befor String you want find then you should select from point + leng of 
                // String you find
                output.select(pos, pos + getValueTextFiled().length());
            }
            // if you want find up
            // logic find up than cut string from 0 to index point of mouse before find string
        } else {  
            // if user dont select string in notepad so easy 
            // but user select string then point of mouse is indext last String select
            // so if you find befor than string select you should get index befor string user select
            if (output.getSelectedText() != null) {
                indexStart -= output.getSelectedText().length();
            }
            // because func lastindex of find with last then - 1
            pos = indexUp(indexStart-1);
            System.out.println(pos);
            // check around 
            if (checkAround()) {
                // if pos == - 1 but checkAround true
                // then you should check one more with full String nodepad
                if (pos == -1) {
                    pos = indexUp(textOutPut.length());                }
            }
            // the same find down
            if (pos == -1) {
                JOptionPane.showMessageDialog(find, "Not found!");
                output.setCaretPosition(indexStart);
            } else {
                output.select(pos, pos + getValueTextFiled().length());
            }
        }
    }

}
