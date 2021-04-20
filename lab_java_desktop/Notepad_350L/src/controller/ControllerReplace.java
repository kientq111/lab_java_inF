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
import view.ReplaceDialog;
import view.View;

/**
 *
 * @author PHT
 */
public class ControllerReplace {

    ReplaceDialog replace;
    String textOutPut;
    JTextArea output;

    public ControllerReplace(JFrame parent, boolean check) {
        replace = new ReplaceDialog(parent, check);
        // get output textarea of notepad
        output = ((View) parent).getTxtOutPut();
        replace.setTitle("Replace");
        replace.setVisible(true);
        replace.setResizable(false);
        replace.setLocationRelativeTo(replace);
        action();
        
    }

    public void action() {
        replace.getjButtonFindNext().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindText();
            }
        });

        replace.getjButtonReplaceAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replaceAll();
            }
        });
        replace.getjButtonReplace().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replace();
            }
        });
        replace.getjButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replace.dispose();
            }
        });

    }

    // return string you want find
    public String getTextFind() {
        return replace.getjTextFieldFind().getText();
    }

    //check case true or false
    public String getTextReplace() {
        return replace.getjTextFieldReplace().getText();
    }

    //check around true or false
    public boolean checkCase() {
        return replace.getCheckCase().isSelected();
    }

    //check find with up or dow ( true is down or false id up )
    public boolean checkAround() {
        return replace.getCheckAround().isSelected();
    }

    // return index of string want find with index start
    public int index(int indexStart) {
        int pos;
        // find checkcase or not checkcase
        if (checkCase()) {
            pos = this.textOutPut.indexOf(getTextFind(), indexStart);
        } else {
            pos = this.textOutPut.toLowerCase().indexOf(getTextFind().toLowerCase(), indexStart);
        }
        return pos;
    }

    // logic fint string the same findcontroller
    public void FindText() {
        textOutPut = output.getText();
        //return point of mouse on string
        int indexStart = this.output.getCaretPosition();
        int pos = index(indexStart);
        // if you find string yes return index else return -1 
        // but if you select checkaround then you should check more than one String output with index = 0
        if (checkAround()) {
            if (pos == -1) {
                pos = index(0);
            }
        }
        //if index  = -1 then not found String in String Notepad
        if (pos == -1) {
            JOptionPane.showMessageDialog(replace, "Not found!");
            // dont find String on String notepad then set point of mouse with index befor find
            output.setCaretPosition(indexStart);
        } else {
            // if select sucsess then you should select with String you want fint
            // index point of mouse return befor String you want find then you should select from point + leng of 
            // String you find
            output.select(pos, pos + getTextFind().length());
        }
    }

    // replace all 
    public void replaceAll() {
        textOutPut = output.getText();
        // if user dont check in check case then replace all 
        if (checkCase()) {
            textOutPut = textOutPut.replaceAll(getTextFind(), getTextReplace());
        } else {
            //if user check replace with case so replace all char if up or lowercase
            textOutPut = textOutPut.replaceAll(getTextFind().toUpperCase(), getTextReplace());
            textOutPut = textOutPut.replaceAll(getTextFind().toLowerCase(), getTextReplace());
        }
        // return string after change
        output.setText(textOutPut);

    }

    //replace
    public void replace() {
        // setString in notepad
        textOutPut = output.getText();
        // get point of mouse in string notepad
        int indexStart = this.output.getCaretPosition();
        // if user dont select string in notepad so easy 
        // but user select string then point of mouse is indext last String select
        // so if you find befor than string select you should get index befor string user select
        if (output.getSelectedText() != null) {
            indexStart -= output.getSelectedText().length();
        }
        // you want cut string notepad then 2 sub string becase replace dont have index from
        String strFirst = textOutPut.substring(0, indexStart);
        String strLast = textOutPut.substring(indexStart, textOutPut.length());
        // if user check around you replace one more 
        // if user dont chekc around -> you should replace string after select then last char of string notepad
        // but if check around if index last string -> string want check repalce is full string of notepad
        if (checkAround()) {
            if (strFirst.equals(textOutPut)) {
                strLast = strFirst;
                strFirst = "";
                indexStart = 0;
            }
        }
        // check case then replace string first 
        if (checkCase()) {
            strLast = strLast.replaceFirst(getTextFind(), getTextReplace());
        } else {
            // if user dont choise case you should replace up or lower 
            // if relace one lower then dont replace upper 
            String strnew = strLast.replaceFirst(getTextFind().toUpperCase(), getTextReplace());
            if(strnew.equals(strLast)){
                strLast = strLast.replaceFirst(getTextFind().toLowerCase(), getTextReplace());
            }else{
                strLast = strnew;
            }
        }
        //concat string after replcae
        output.setText(strFirst.concat(strLast));
        //set index of point
        output.setCaretPosition(indexStart);
        //int text after 
        FindText();
    }

}
