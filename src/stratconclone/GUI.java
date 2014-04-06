/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
//import java.awt.font.*;

import javax.swing.*;

/**
 *
 * @author matthew
 */
public class GUI {

    Grid grid;
    GameEngine gameEngine;

    int screenWidth;
    int screenHeight;
    boolean maximiseGameWindow;

    /**
     * **************************
     * game window widgets
     */
    private JFrame f1;
    private JPanel p1;
    private JScrollBar hbar;
    private JScrollBar vbar;
    private JMenuBar m1;
    private JMenuBar menuBar;
    private JMenu menu1, menu2;
    private JMenuItem menu1_item1, menu1_item2;
    private JMenuItem menu2_item1, menu2_item2;
    private JToolBar toolBar;
    private JButton btnEndTurn1;
    private JButton btnEndTurn2;
    private JLabel label__game_day_number;

    /**
     * **************************
     * dialogue - human player, set city production
     */
    private JDialog diag_production;
    private JLabel diag_production_label1; // user message
    private ButtonGroup diag_production_button_group;
    private JRadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8; // radiobuttons
    private JButton btnCityProdSave;

    /**
     * **************************
     * dialogue - surrender
     */
    private JDialog diag_surrender;
    private JLabel diag_surrender_label1; // user message (do you accept my surrender?)
    private JButton btnSurrenderYes;
    private JButton btnSurrenderNo;

    /**
     * **************************
     * dialogue - game over, play again?
     */
    private JDialog diag_gameover;
    private JLabel diag_gameover_label1; // user message (game over, play again?)
    private JButton btnGameOverYesPlayAgain;
    private JButton btnGameOverNoQuit;

     /**
     * **************************
     * dialogue - bye, thanks for playing
     */
    private JDialog diag_bye;
    private JLabel diag_bye_label1;
    private JButton btnBye;
    
    // constructor
    public GUI(Grid _grid, GameEngine _gameEngine, boolean _maximiseGameWindow) {

        grid = _grid;
        gameEngine = _gameEngine;
        this.maximiseGameWindow = _maximiseGameWindow;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Insets ins = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        screenWidth = gc.getBounds().width - ins.left - ins.right;
        screenHeight = gc.getBounds().height - ins.top - ins.bottom;

        //f1 = new JFrame("Frame 1 (parent frame)");
        //f1.setBounds(32, 32, 300, 200);
        //f1.setSize(200, 200);
        //f1.addWindowListener(closeWindow);
        createAndShowGUI();
    }

    private static WindowListener closeWindow = new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    };

    public JPanel getPanelReference() {
        return p1;
    }

    public void showDialogueCityProduction() {
        System.out.println("in gui showDialogueCityProduction()");
        diag_production.setVisible(true);
    }
    
    public void setEndTurnButtonEnabled(boolean value) {
        btnEndTurn2.setEnabled(value);
    }
        
    public void showDialogueAiSurrender() {
        System.out.println("in gui showDialogueAiSurrender()");
        diag_surrender.setVisible(true);
    }

    public void showDialogueGameOverPlayAgain() {
        System.out.println("in gui showDialogueGameOverPlayAgain()");
        diag_gameover.setVisible(true);
    }

    public void showDialogueThanksForPlaying() {
        System.out.println("in gui showDialogueThanksForPlaying()");
        diag_bye.setVisible(true);
    }
    
    public void updateDayNumberLabel(int dayNumber) {
        label__game_day_number.setText(" Day " + Integer.toString(dayNumber) + " ");
    }

    private void createAndShowGUI() {

        build_frame1();
        //build_dialogue_city_production();
        build_dialogue_AI_surrender();
        build_dialogue_game_over();
        build_dialogue_bye();
    }

    public void quit() {

        System.exit(0);
    }
        
    /**
     * ****************************************
     * frame 1 - game window
     *
     */
    private void build_frame1() {

        //1. Create the frame.
        f1 = new JFrame("Strategic Conquest Clone");
        f1.setResizable(false);

        //2. Optional: What happens when the frame closes?
        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.addWindowListener(closeWindow);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        p1 = new JPanel();

        p1.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if( gameEngine.getCurrentPlayerId()==2) {
                        gameEngine.mouseClicked(e.getX(),e.getY());
                    }
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if( gameEngine.getCurrentPlayerId()==2) {
                        gameEngine.mousePressed(e.getX(),e.getY());
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if( gameEngine.getCurrentPlayerId()==2) {
                        gameEngine.mouseDragged(e.getX(),e.getY());
                    }
                }
            }
        });

        if (this.maximiseGameWindow) {
            // maximise window (this is not full screen)
            f1.setExtendedState(f1.getExtendedState() | f1.MAXIMIZED_BOTH);
            //p1.setPreferredSize(new Dimension(screenWidth, screenHeight)); // width, height
        } else {
            f1.setBounds((screenWidth / 2) - 250, (screenHeight / 2) - 250, 500, 500); // (int x, int y, int width, int height)
            p1.setPreferredSize(new Dimension(500, 500)); // width, height
        }

//        hbar = new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 300); // value, extent, min, max
//        vbar = new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 300); // value, extent, min, max
//        hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, grid.getCols()/10, 0, grid.getCols()); // value, extent, min, max
//        vbar = new JScrollBar(JScrollBar.VERTICAL, 0, grid.getRows()/10, 0, grid.getRows()); // value, extent, min, max
//        hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 10, 0, grid.getCols()); // value, extent, min, max
//        vbar = new JScrollBar(JScrollBar.VERTICAL, 0, 10, 0, grid.getRows()); // value, extent, min, max
        hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 10, 0, grid.getCols() / 2); // value, extent, min, max
        vbar = new JScrollBar(JScrollBar.VERTICAL, 0, 10, 0, grid.getRows() / 2); // value, extent, min, max

        hbar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                grid.scrollEvent(hbar.getValue(), vbar.getValue());
                //System.out.println("Horozontal: " + hbar.getValue() + " ,Vertical: " + vbar.getValue());
            }
        });

        vbar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                grid.scrollEvent(hbar.getValue(), vbar.getValue());
                //System.out.println("Horozontal: " + vbar.getValue() + " ,Vertical: " + vbar.getValue());
            }
        });

        build_frame1_menu();
        build_frame1_tool_bar();
        //p1.setOpaque(true); //content panes must be opaque
        //f1.setContentPane(demo);
        f1.getContentPane().add(menuBar, BorderLayout.NORTH);
        //f1.getContentPane().add(toolBar, BorderLayout.NORTH);
        f1.getContentPane().add(p1, BorderLayout.CENTER);
        f1.getContentPane().add(hbar, BorderLayout.SOUTH);
        f1.getContentPane().add(vbar, BorderLayout.EAST);

        //4. Size the frame.
        f1.pack();

        //5. Show it.
        f1.setVisible(true);
    }

    /**
     * *****************************************
     * build the menu bar for frame 1
     *
     * creating the visible menu:
     * http://docs.oracle.com/javase/tutorial/uiswing/components/menu.html#create
     *
     * linking up actions:
     * http://docs.oracle.com/javase/tutorial/uiswing/misc/action.html
     */
    private void build_frame1_menu() {

        menuBar = new JMenuBar();

        //Build the menu ONE
        menu1 = new JMenu("Menu One");
        menu1.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu1);

        // items for menu ONE
        menu1_item1 = new JMenuItem("one-one", KeyEvent.VK_B);
        menu1_item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("menu bar; menu1; item1...clicked");
            }
        });
        menu1.add(menu1_item1);

        menu1_item2 = new JMenuItem("one-two", KeyEvent.VK_C);
        menu1.add(menu1_item2);

        //Build the menu TWO
        menu2 = new JMenu("Menu Two");
        menu2.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu2);

        // items for menu TWO
        menu2_item1 = new JMenuItem("two-one", KeyEvent.VK_E);
        menu2.add(menu2_item1);

        menu2_item2 = new JMenuItem("two-two", KeyEvent.VK_F);
        menu2.add(menu2_item2);

        //Build the menuEndTurn
        btnEndTurn2 = new JButton("End Turn");
        btnEndTurn2.setEnabled(false);
        btnEndTurn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("menu bar; Button menuEndTurn2...clicked");
                gameEngine.EndPlayerTurn();
            }
        });
        menuBar.add(btnEndTurn2);

        label__game_day_number = new JLabel();
        menuBar.add(label__game_day_number);

    }

    /**
     * *****************************************
     * build the menu bar for frame 1
     *
     * creating a toolBar:
     * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/misc/ActionDemoProject/src/misc/ActionDemo.java
     */
    private void build_frame1_tool_bar() {

        toolBar = new JToolBar();

        // add a buton
        btnEndTurn1 = new JButton();
        btnEndTurn1.setText("End Turn");
        btnEndTurn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("tool bar; btnEndTurn1...clicked");
            }
        });
        toolBar.add(btnEndTurn1);

    }

    /**
     * ****************************************
     * dialogue 1 - human player, set city production
     *
     */
    public void build_dialogue_city_production(boolean isPortCity) {

        //1. Create the dialogue (referencing the parent frame).
        diag_production = new JDialog(f1, "Select City Production", true);
        //diag_production.setBounds(200, 200, 400, 300);
        diag_production.setBounds((screenWidth / 2) - 200, (screenHeight / 2) - 150, 400, 300);
        diag_production.setPreferredSize(new Dimension(400, 300)); // width, height

        //2. Optional: What happens when the dialogue closes?
        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diag_production.addWindowListener(closeWindow);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //diag_production_label1 = new JLabel("Hello World!");
        //diag_production.add(diag_production_label1, BorderLayout.NORTH);
        //diag_production.add(p1, BorderLayout.CENTER);
        btnCityProdSave = new JButton("Save");
        btnCityProdSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for (Enumeration<AbstractButton> buttons = diag_production_button_group.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();

                    if (button.isSelected()) {
                        System.out.println("City Production; Button Save...clicked " + button.getText());
                    }
                }

                //System.out.println("City Production; Button Save...clicked");
                diag_production.setVisible(false);
            }
        });
        diag_production.add(btnCityProdSave, BorderLayout.SOUTH);

        Container diag_production_content_pane = diag_production.getContentPane();
        diag_production_content_pane.setLayout(new BorderLayout());
        JPanel diag_production_pane = new JPanel();
        diag_production_pane.setLayout(new FlowLayout());
        diag_production_pane.add(btnCityProdSave);
        diag_production_content_pane.add(diag_production_pane, BorderLayout.SOUTH);

        diag_production_label1 = new JLabel("Hello World!");
        diag_production_label1.setHorizontalAlignment(SwingConstants.CENTER);
        //diag_production_content_pane.add(diag_production_label1, BorderLayout.CENTER);

        diag_production_button_group = new ButtonGroup();
        //
        rb1 = new JRadioButton("Tank", true);
        rb2 = new JRadioButton("Fighter");
        rb3 = new JRadioButton("Battleship");
        rb4 = new JRadioButton("Bomber");
        rb5 = new JRadioButton("Carrier");
        rb6 = new JRadioButton("Destroyer");
        rb7 = new JRadioButton("Transport");
        rb8 = new JRadioButton("Submarine");
        //
        // if not a port city, disable certain unit types
        if (!isPortCity) {
            rb3.setEnabled(false);
            rb5.setEnabled(false);
            rb6.setEnabled(false);
            rb7.setEnabled(false);
            rb8.setEnabled(false);
        }
        //
        diag_production_button_group.add(rb1);
        diag_production_button_group.add(rb2);
        diag_production_button_group.add(rb3);
        diag_production_button_group.add(rb4);
        diag_production_button_group.add(rb5);
        diag_production_button_group.add(rb6);
        diag_production_button_group.add(rb7);
        diag_production_button_group.add(rb8);

        // Add everything to a container.
        Box diag_production_box = Box.createVerticalBox();
        //diag_production_box.add(diag_production_label1);
        diag_production_box.add(rb1);
        diag_production_box.add(rb2);
        diag_production_box.add(rb3);
        diag_production_box.add(rb4);
        diag_production_box.add(rb5);
        diag_production_box.add(rb6);
        diag_production_box.add(rb7);
        diag_production_box.add(rb8);
        //diag_production_box.add(Box.createVerticalStrut(5)); // spacer
        // Add some breathing room.
        diag_production_box.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        //
        diag_production_content_pane.add(diag_production_box, BorderLayout.CENTER);

        //4. Size the frame.
        diag_production.pack();

        //5. Show it.
        diag_production.setVisible(false);
    }

    /**
     * ****************************************
     * dialogue 2 - AI surrender
     *
     */
    public void build_dialogue_AI_surrender() {

        //1. Create the dialogue (referencing the parent frame).
        diag_surrender = new JDialog(f1, "", true);
        //diag_surrender.setBounds(200, 200, 400, 200);
        diag_surrender.setBounds((screenWidth / 2) - 200, (screenHeight / 2) - 100, 400, 200);
        diag_surrender.setPreferredSize(new Dimension(400, 200)); // width, height

        //2. Optional: What happens when the dialogue closes?
        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diag_surrender.addWindowListener(closeWindow);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //diag_production.add(p1, BorderLayout.CENTER);
        btnSurrenderYes = new JButton("Yes");
        btnSurrenderYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Accept My Surrender; Button Yes...clicked ");
                diag_surrender.setVisible(false);
                gameEngine.userActionPlayerAcceptedSurrenderYes();
            }
        });

        //diag_surrender.add(btnSurrenderYes, BorderLayout.SOUTH);
        btnSurrenderYes.setHorizontalAlignment(SwingConstants.CENTER);
        diag_surrender.add(btnSurrenderYes, BorderLayout.SOUTH);

        btnSurrenderNo = new JButton("No");
        btnSurrenderNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Accept My Surrender; Button No...clicked");
                diag_surrender.setVisible(false);
                gameEngine.userActionPlayerAcceptedSurrenderNo();
            }
        });
        //diag_surrender.add(btnSurrenderNo, BorderLayout.SOUTH);
        btnSurrenderNo.setHorizontalAlignment(SwingConstants.CENTER);
        diag_surrender.add(btnSurrenderNo, BorderLayout.SOUTH);

        Container diag_surrender_content_pane = diag_surrender.getContentPane();
        diag_surrender_content_pane.setLayout(new BorderLayout());
        JPanel diag_surrender_pane = new JPanel();
        diag_surrender_pane.setLayout(new FlowLayout());
        diag_surrender_pane.add(btnSurrenderYes);
        diag_surrender_pane.add(btnSurrenderNo);
        diag_surrender_content_pane.add(diag_surrender_pane, BorderLayout.SOUTH);

        diag_surrender_label1 = new JLabel("General, Do You Accept My Surrender?");
        diag_surrender_label1.setHorizontalAlignment(SwingConstants.CENTER);
        diag_surrender_content_pane.add(diag_surrender_label1, BorderLayout.CENTER);

        //4. Size the frame.
        diag_surrender.pack();

        //5. Show it.
        diag_surrender.setVisible(false);
    }

    /**
     * ****************************************
     * dialogue - game over, play again?
     *
     */
    public void build_dialogue_game_over() {

        //1. Create the dialogue (referencing the parent frame).
        diag_gameover = new JDialog(f1, "Game Over", true);
        //diag_gameover.setBounds(200, 200, 400, 200);
        diag_gameover.setBounds((screenWidth / 2) - 200, (screenHeight / 2) - 100, 400, 200);
        diag_gameover.setPreferredSize(new Dimension(400, 200)); // width, height

        //2. Optional: What happens when the dialogue closes?
        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diag_gameover.addWindowListener(closeWindow);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //diag_production.add(p1, BorderLayout.CENTER);
        btnGameOverYesPlayAgain = new JButton("Yes");
        btnGameOverYesPlayAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GameOver; Button Yes (Play Again)...clicked");
                diag_gameover.setVisible(false);
                gameEngine.userActionGameOverPlayAgainYes(grid);
            }
        });
        btnGameOverYesPlayAgain.setHorizontalAlignment(SwingConstants.CENTER);
        diag_gameover.add(btnGameOverYesPlayAgain, BorderLayout.SOUTH);

        btnGameOverNoQuit = new JButton("No");
        btnGameOverNoQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GameOver; Button No (Quit)...clicked");
                diag_gameover.setVisible(false);
                gameEngine.userActionGameOverPlayAgainNo();
            }
        });
        btnGameOverNoQuit.setHorizontalAlignment(SwingConstants.CENTER);
        diag_gameover.add(btnGameOverNoQuit, BorderLayout.SOUTH);

        Container diag_gameover_content_pane = diag_gameover.getContentPane();
        diag_gameover_content_pane.setLayout(new BorderLayout());
        JPanel diag_gameover_pane = new JPanel();
        diag_gameover_pane.setLayout(new FlowLayout());
        diag_gameover_pane.add(btnGameOverYesPlayAgain);
        diag_gameover_pane.add(btnGameOverNoQuit);
        diag_gameover_content_pane.add(diag_gameover_pane, BorderLayout.SOUTH);

        diag_gameover_label1 = new JLabel("Play Again?");
        diag_gameover_label1.setHorizontalAlignment(SwingConstants.CENTER);
        diag_gameover_content_pane.add(diag_gameover_label1, BorderLayout.CENTER);

        //4. Size the frame.
        diag_gameover.pack();

        //5. Show it.
        diag_gameover.setVisible(false);
    }

    
    /**
     * ****************************************
     * dialogue - bye, thanks for playing
     *
     */
    public void build_dialogue_bye() {

        //1. Create the dialogue (referencing the parent frame).
        diag_bye = new JDialog(f1, "", true);
        //diag_gameover.setBounds(200, 200, 400, 200);
        diag_bye.setBounds((screenWidth / 2) - 200, (screenHeight / 2) - 100, 400, 200);
        diag_bye.setPreferredSize(new Dimension(400, 200)); // width, height

        //2. Optional: What happens when the dialogue closes?
        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diag_bye.addWindowListener(closeWindow);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //diag_production.add(p1, BorderLayout.CENTER);
        btnBye = new JButton("Quit");
        btnBye.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bye; Button Quit...clicked");
                diag_bye.setVisible(false);
                gameEngine.userActionQuit();
            }
        });
        btnBye.setHorizontalAlignment(SwingConstants.CENTER);
        diag_bye.add(btnBye, BorderLayout.SOUTH);

       
        Container diag_bye_content_pane = diag_bye.getContentPane();
        diag_bye_content_pane.setLayout(new BorderLayout());
        JPanel diag_bye_pane = new JPanel();
        diag_bye_pane.setLayout(new FlowLayout());
        diag_bye_pane.add(btnBye);
        diag_bye_content_pane.add(diag_bye_pane, BorderLayout.SOUTH);

        diag_bye_label1 = new JLabel("Thank you for playing!");
        diag_bye_label1.setHorizontalAlignment(SwingConstants.CENTER);
        diag_bye_content_pane.add(diag_bye_label1, BorderLayout.CENTER);

        //4. Size the frame.
        diag_bye.pack();

        //5. Show it.
        diag_bye.setVisible(false);
    }

}
