/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import model.WFile;
import view.View;

/**
 *
 * @author PHT
 */
public class Controller {

    View view;
    private JLabel statusLabel;
    String textOriginal = "";
    boolean isNewFile;
    String filePath = "";
    UndoManager um;
    Document doc;

    public Controller() {
        view = new View();
        //class undomanager is class convert action undo and redo
        this.um = new UndoManager();
        doc = view.getTxtOutPut().getDocument();
        isNewFile = true;
        view.setTitle("Notepad");
        view.setLocationRelativeTo(view);
        view.setVisible(true);
        action();
    }

    public void action() {
        doc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                um.addEdit(e.getEdit());
            }
        });
        view.getfNew().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });
        view.getfOpen().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        view.getfSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        view.getfSaveAs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAssFile();
            }
        });
        view.getfExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitsFile();
            }
        });
        view.getsUndo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btUndo();
            }
        });
        view.getsRedo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btRedo();
            }
        });

        view.getsSelectAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btselectAll();
            }
        });
        view.getsCut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btCut();
            }
        });
        view.getsCopy().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btCopy();
            }
        });
        view.getsPaste().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btPaste();
            }
        });
        view.getsDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btDelete();
            }
        });

        view.getsFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ControllerFind(view, false);
            }
        });
        view.getsReplace().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ControllerReplace(view, false);
            }
        });

        view.getsChangeFont().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ControllerChangeFont(view, false);
            }
        });
        view.getjMenu2().addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                setdefaultEdit();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
    }

    //set deault edit buttom in edit
    public void setdefaultEdit() {
        // if string in notepad have more char so find not enable
        boolean setFind = !"".equals(view.getTxtOutPut().getText());
//        boolean setUndo = !textOriginal.equals(view.getTxtOutPut().getText());
        // if string before and after the same so redo and undo setenabled
        view.getsRedo().setEnabled(um.canRedo());
        view.getsUndo().setEnabled(um.canUndo());
        view.getsFind().setEnabled(setFind);
        //if user select more char so copy , cut , delete is show
        boolean setEdit = !(view.getTxtOutPut().getSelectedText() == null);
        view.getsCopy().setEnabled(setEdit);
        view.getsCut().setEnabled(setEdit);
        view.getsDelete().setEnabled(setEdit);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        String str = null;
        try {
            str = (String) c.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            str = null;
        }
        view.getsPaste().setEnabled(str != null);
    }

    // select all string
    public void btselectAll() {
        view.getTxtOutPut().selectAll();
    }

    // cut string if user select
    public void btCut() {
        view.getTxtOutPut().cut();
    }

    // copy string if user select
    public void btCopy() {
        view.getTxtOutPut().copy();
    }

    // paste string 
    public void btPaste() {
        view.getTxtOutPut().paste();
    }

    // delete string if user cut
    public void btDelete() {
        view.getTxtOutPut().replaceSelection("");
    }

    // undo
    public void btUndo() {
        if (um.canUndo()) {
            um.undo();
        }
    }

    // redo
    public void btRedo() {
        if (um.canRedo()) {
            um.redo();
        }
    }

    // check String save or dont save befor do some thing
    private boolean convert_Save() {
        // if textdefulst not equale string in notepasd
        // program shoult show question save or dont save
        if (!textOriginal.equals(view.getTxtOutPut().getText())) {
            int result;
            Object[] options = {"Save", "Don't save", "Cancel"};
            result = JOptionPane.showOptionDialog(view,
                    "Do you want to save?", "Notepad",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            // if user choise save call method saveFile
            if (result == JOptionPane.YES_OPTION) {
                saveFile();
                // if user choise dont save
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        // if string equals or save file return true;
        return true;
    }

    // new file after check convert_save
    public void newFile() {
        if (!convert_Save()) {
            return;
        }
        isNewFile = true;
        textOriginal = "";
        view.getTxtOutPut().setText("");
    }

    // open file after check convert_save
    public void openFile() {
        if (!convert_Save()) {
            return;
        }
        JFileChooser open = new JFileChooser();
        // filter in typle file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT file", "txt");
        open.setFileFilter(filter);
        //show dialog open 
        int option = open.showOpenDialog(view);
        // if user choisr file option = 1  = APPROVE_OPTION
        if (option == JFileChooser.APPROVE_OPTION) {
            view.getTxtOutPut().setText("");
            try {
                //path is file 
                filePath = open.getSelectedFile().getPath();
                // scaner in file with path
                Scanner scan = new Scanner(new FileReader(filePath));
                // check nect line have many char if have is true or dont have is false
                while (scan.hasNext()) {
                    // read file and write
                    view.getTxtOutPut().append(scan.nextLine() + "\n");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Can't open file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // after open file set String default is file
        textOriginal = view.getTxtOutPut().getText();
        isNewFile = false;
        // set point of mouse
        view.getTxtOutPut().setCaretPosition(0);
    }

    // save file
    public void saveFile() {
        // before save file you should check file new or open file because
        // if new you should save new file 
        // or opent file you should save for file this
        if (!isNewFile) {
            try {
                // save file set string default string
                textOriginal = view.getTxtOutPut().getText();
                // call wire file in model
                if (textOriginal != null) {
                    WFile wf = new WFile(filePath, textOriginal);
                    wf.write();
                }
            } catch (Exception e) {
            }
        } else {
            saveAssFile();
        }
    }

    public void saveAssFile() {
        // save file new
        JFileChooser save = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt");
        save.setFileFilter(filter);
        //open dialog save
        int option = save.showSaveDialog(view);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                // save file set string default string
                textOriginal = view.getTxtOutPut().getText();
                // get path in dialog save
                filePath = save.getSelectedFile().getPath();
                // 2 case important
                // 1 user creat new file 
                // 2 choise file old
                // if choise file old  show path file have txt then dont concat string whith .txt
                // if new file so creart pasth have .txt
                if (!filePath.contains(".txt")) {
                    filePath += ".txt";
                }
                // call call wire file in model
                WFile wf = new WFile(filePath, textOriginal);
                wf.write();
                isNewFile = false;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // exit program
    public void exitsFile() {
        if (!convert_Save()) {
            return;
        }
        System.exit(0);
    }
}
