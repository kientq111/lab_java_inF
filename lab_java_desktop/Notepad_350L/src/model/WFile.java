/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PHT
 */
public class WFile {
    
    // path of file 
    private String path;
    // content of file
    private String content;

    public WFile() {
    }

    
    
    public WFile(String path, String content) {
        this.path = path;
        this.content = content;
    }
    
    // write text then file
    public void write(){
           FileWriter fw = null;
           String s[] = content.split("\n");
        try {
            fw = new FileWriter(path);
            for (int i = 0; i < s.length; i++) {
                String string = s[i];
                fw.write(string + "\r\n");
            }
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(WFile.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
}
