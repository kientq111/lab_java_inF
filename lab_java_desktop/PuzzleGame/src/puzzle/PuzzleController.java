/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class PuzzleController {

    PuzzleGUI puzzle;
    private int moveCount = 0;
    private Timer timer;
    private JButton[][] matrix;
    private boolean isGameStart = false;

    public PuzzleController(PuzzleGUI puzzle) {
        this.puzzle = puzzle;
    }

    public void init() {
        this.initMoveCount();
        this.initTimeCount();

        this.initMatrix();

        isGameStart = true;
    }

    public void initMoveCount() {
        moveCount = 0;
        this.puzzle.lblCountMove.setText("0");
    }

    public void initTimeCount() {
        this.puzzle.lblCountTime.setText("0");
        timer = new Timer(1000, new ActionListener() {
            int second = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                second++;
                puzzle.lblCountTime.setText(String.valueOf(second));
            }
        });
        timer.start();
    }

    public void initMatrix() {
        String txt = puzzle.cbxSize.getSelectedItem().toString();

        String[] tmp = txt.split("x");
        int size = Integer.parseInt(tmp[0]);

        puzzle.pnLayout.removeAll();
        puzzle.pnLayout.setLayout(new GridLayout(size, size, 10, 10));

        // get value it size  3x3 4x4 5x5
        matrix = new JButton[size][size];
        System.out.println(matrix.length);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton button = new JButton(i * size + j + 1 + "");
                matrix[i][j] = button;
                puzzle.pnLayout.add(button);
                // thêm sự kiện cho button
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isGameStart) {
                            // kiem tra button co move sang duoc o trong ko 
                            if (checkMove(button)) {
                                moveButton(button);

                                if (checkWin()) {
                                    isGameStart = false;
                                    JOptionPane.showMessageDialog(null, "You won!");
                                    timer.stop();
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Press New Game to start");
                        }
                    }
                });
            }
        }
        matrix[size - 1][size - 1].setText("");
        randomMatrix();
        setWindowsSize();
    }

  



    private void setWindowsSize() {// khoi tao giao dien 
        int heightButton = 60;
        int widthButton = 60;
        puzzle.pnLayout.setPreferredSize(new Dimension(widthButton * matrix.length, heightButton * matrix.length));
        puzzle.setResizable(false);
        puzzle.pack();//cho frame vừa đủ với nội dung của frame

    }

    public void randomMatrix1() {
        Random rd = new Random();

        for (int i = 0; i < 1000; i++) {

            // lay vi tri button rong
            Point p = getPositionOfEmptyButton();

            // random 
            int number = rd.nextInt(4);

            switch (number) {
                case 0: // tren
                    if (p.x > 0) {
                        matrix[p.x][p.y].setText(matrix[p.x - 1][p.y].getText());
                        matrix[p.x - 1][p.y].setText("");
                    }
                    break;
                case 1: // phai
                    if (p.y < matrix.length - 1) {
                        matrix[p.x][p.y].setText(matrix[p.x][p.y + 1].getText());
                        matrix[p.x][p.y + 1].setText("");
                    }
                    break;
                case 2: // duoi
                    if (p.x < matrix.length - 1) {
                        matrix[p.x][p.y].setText(matrix[p.x + 1][p.y].getText());
                        matrix[p.x + 1][p.y].setText("");
                    }
                    break;
                case 3: // trai
                    if (p.y > 0) {
                        matrix[p.x][p.y].setText(matrix[p.x][p.y - 1].getText());
                        matrix[p.x][p.y - 1].setText("");
                    }
                    break;
            }
        }

    }

    public void randomMatrix() {
        Random rd = new Random();
        for (int i = 0; i < 1000; i++) {
            Point p = getPositionOfEmptyButton();
            int number = rd.nextInt(4);
            switch (number) {
                case 0:
                    if (p.x > 0) {
                        matrix[p.x][p.y].setText(matrix[p.x - 1][p.y].getText());
                        matrix[p.x - 1][p.y].setText("");
                    }
                    break;
                case 1:
                    if (p.y > 0) {
                        matrix[p.x][p.y].setText(matrix[p.x][p.y - 1].getText());
                        matrix[p.x][p.y - 1].setText("");
                    }
                    break;
                case 2:
                    if (p.x < matrix.length - 1) {
                        matrix[p.x][p.y].setText(matrix[p.x + 1][p.y].getText());
                        matrix[p.x + 1][p.y].setText("");
                    }
                    break;
                case 3:
                    if (p.y < matrix.length - 1) {
                        matrix[p.x][p.y].setText(matrix[p.x][p.y + 1].getText());
                        matrix[p.x][p.y + 1].setText("");
                    }
                    break;
            }
        }
    }

    public boolean checkWin() {
        // check nut cuoi
        if (matrix[matrix.length - 1][matrix.length - 1].getText().equals("")) {

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {

                    if (i == matrix.length - 1 && j == matrix.length - 1) {
                        return true;
                    }

                    if (Integer.parseInt(matrix[i][j].getText()) != (i * matrix.length + j + 1)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void moveButton(JButton button) {
        Point p = getPositionOfEmptyButton();
        matrix[p.x][p.y].setText(button.getText());
        button.setText("");
        this.moveCount++;
        puzzle.lblCountMove.setText(String.valueOf(moveCount));
    }
    
    /*NEW */
    Point getClickPoint(JButton button) {
        Point p = getPositionOfEmptyButton();
        int size = matrix.length;

        if (p.y + 1 < size) {
            if (matrix[p.x][p.y + 1].getText().equals(button.getText())) {
                return new Point(p.x, p.y + 1);
            }
        }
        if (p.y - 1 >= 0) {
            if (matrix[p.x][p.y - 1].getText().equals(button.getText())) {
                return new Point(p.x, p.y - 1);
            }
        }

        if (p.x + 1 < size) {
            if (matrix[p.x + 1][p.y].getText().equals(button.getText())) {
                return new Point(p.x + 1, p.y);
            }
        }

        if (p.x - 1 >= 0) {
            if (matrix[p.x - 1][p.y].getText().equals(button.getText())) {
                return new Point(p.x - 1, p.y);
            }
        }
        return null;
    }

    public boolean checkMove(JButton button) {
        if (button.getText().equals("")) {
            return false;
        }
        Point p = getPositionOfEmptyButton();

        /*NEW*/
        Point clickedPoint = getClickPoint(button);

        try {

            if (p.x == clickedPoint.x && Math.abs(p.y - clickedPoint.y) == 1) {
                return true;
            }

            if (p.y == clickedPoint.y && Math.abs(p.x - clickedPoint.x) == 1) {

                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Move fail,Pls move again!");
        }
        return false;
    }

    public Point getPositionOfEmptyButton() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void newGame() {
        if (isGameStart) {
            timer.stop();
            int confirm = JOptionPane.showConfirmDialog(null, "Do you must"
                    + " be want to make new game?", "New Game", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                this.init();
            } else if (confirm == JOptionPane.NO_OPTION) {
                timer.start();
            }
        } else {
            this.init();
        }
    }
}
//    private void setWindowsSize(int num) {// khoi tao giao dien 
//        int heightButton = 60;
//        int widthButton = 60;
//        puzzle.pnLayout.setPreferredSize(new Dimension(widthButton * num, heightButton * num));
//        puzzle.setResizable(false);
//        puzzle.pack();//cho frame vừa đủ với nội dung của frame
//
//    }

