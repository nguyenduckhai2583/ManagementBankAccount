import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.SystemColor;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.UIManager;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import jxl.*;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.table.JTableHeader;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class BankAccountManagement extends JFrame implements MouseListener, ActionListener {

    private JPanel contentPane; 
    private JTable table;
    private JTextField txt_search;
    private JPanel btn_insert;
    private JPanel btn_add;
    private JPanel btn_deposit;
    private JPanel btn_withdraw;
    private JPanel btn_edit;
    private JPanel btn_delete;
    private JPanel btn_history;
    private JLabel useramount;
    private JLabel totalmoney;
    private JMenuItem saveitem;
    private JMenuItem exititem;
    private JMenuItem openitem;
    private JComboBox comboBox;
    private JMenuItem saveasitem;
    private JMenuItem LogOut;
    private JMenuItem aboutmeitem;
    
    private String  db_url = "jdbc:sqlserver://localhost:1433;" 
    					+ "databaseName=bankaccount";
    	//				+ "integratedSecurity=true";
    private String user_name = "sunshine";
    private String password = "uyenphan22061997";

    Vector vdata = new Vector<>();
    Vector vtitle = new Vector<>();
    Vector vhistory = new Vector();
    
    DefaultTableModel model;
    int selectview, selectmodel, indexsearch = -1;
        
    
	Connection con;
	Statement stm;
	ResultSet rs;
	ResultSetMetaData dataresult;
	private String csdl;
    
    String [] choose = {"", "ID", "Name"};
    String[] title = { "ID", "Name", "Balance", "Gender", "Date of Birth" };
    String[][] data = {

    };
    
    transfer t1 = new transfer();
    login user1;

    public static void main(String[] args) {
        try {      	      	
        	
        	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        	BankAccountManagement frame = new BankAccountManagement();
        	frame.login();
        	
            
   /*         BankAccountManagement frame = new BankAccountManagement();
            frame.setVisible(true);
            frame.setResizable(false); */
        } catch (Exception e) {
        	new error("Error");
            e.printStackTrace();
        }
    }
      
    public BankAccountManagement() {
    	
    	try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(db_url, user_name, password);
			stm = con.createStatement();
		} catch (ClassNotFoundException e1) {
			new error("Error");
			e1.printStackTrace();
		} catch (SQLException e1) {
			new error("Error");
			e1.printStackTrace();
		}
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 792, 541);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);
        
        openitem = new JMenuItem("Open Excel File");
        openitem.addActionListener(this);
        mnNewMenu.add(openitem);
        
        exititem = new JMenuItem("Exit");
        exititem.addActionListener(this);
        
        LogOut = new JMenuItem("Log out");
        LogOut.addActionListener(this);
        mnNewMenu.add(LogOut);
        mnNewMenu.add(exititem);
        
        JMenu mnStore = new JMenu("Store");
        menuBar.add(mnStore);
        
        saveitem = new JMenuItem("Save to Excel File");
        saveitem.addActionListener(this);
        mnStore.add(saveitem);
        
        saveasitem = new JMenuItem("Refresh");
        saveasitem.addActionListener(this);
        mnStore.add(saveasitem);
        
        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);
        
        aboutmeitem = new JMenuItem("About Me");
        aboutmeitem.addActionListener(this);
        mnHelp.add(aboutmeitem);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.text);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(-2, 0, 209, 479);
        panel.setBackground(Color.decode("#474747"));
        contentPane.add(panel);
        panel.setLayout(null);

        btn_insert = new JPanel();
        btn_insert.addMouseListener(this);
        btn_insert.setBounds(10, 191, 189, 30);
        btn_insert.setBackground(new Color(131, 175, 152));
        panel.add(btn_insert);
        btn_insert.setLayout(null);

        JLabel label = new JLabel("");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        Image imageinsert = new ImageIcon(this.getClass().getResource("insert.png")).getImage();
        label.setIcon(new ImageIcon(imageinsert));
        label.setBounds(10, 0, 38, 30);
        btn_insert.add(label);

        JLabel lblInsert = new JLabel("Insert");
        lblInsert.setHorizontalAlignment(SwingConstants.CENTER);
        lblInsert.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblInsert.setForeground(Color.WHITE);
        lblInsert.setBounds(42, 0, 97, 30);
        btn_insert.add(lblInsert);

        btn_add = new JPanel();
        btn_add.addMouseListener(this);
        btn_add.setBounds(10, 233, 189, 30);
        btn_add.setLayout(null);
        btn_add.setBackground(Color.decode("#83AF98"));
        panel.add(btn_add);

        JLabel label_1 = new JLabel("");
        Image imageadd = new ImageIcon(this.getClass().getResource("add.png")).getImage();
        label_1.setIcon(new ImageIcon(imageadd));
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setBounds(10, 0, 38, 30);
        btn_add.add(label_1);

        JLabel lblAdd = new JLabel("Add");
        lblAdd.setHorizontalAlignment(SwingConstants.CENTER);
        lblAdd.setForeground(Color.WHITE);
        lblAdd.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblAdd.setBounds(42, 0, 90, 30);
        btn_add.add(lblAdd);

        btn_deposit = new JPanel();
        btn_deposit.addMouseListener(this);
        btn_deposit.setBounds(10, 275, 189, 30);
        btn_deposit.setLayout(null);
        btn_deposit.setBackground(new Color(131, 175, 152));
        panel.add(btn_deposit);

        JLabel label_2 = new JLabel("");
        Image imagedeposit = new ImageIcon(this.getClass().getResource("deposit.png")).getImage();
        label_2.setIcon(new ImageIcon(imagedeposit));
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        label_2.setBounds(10, 0, 38, 30);
        btn_deposit.add(label_2);

        JLabel lblDeposit = new JLabel("Exchange");
        lblDeposit.setHorizontalAlignment(SwingConstants.CENTER);
        lblDeposit.setForeground(Color.WHITE);
        lblDeposit.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblDeposit.setBounds(42, 0, 120, 30);
        btn_deposit.add(lblDeposit);

        btn_withdraw = new JPanel();
        btn_withdraw.addMouseListener(this);
        btn_withdraw.setBounds(10, 317, 189, 30);
        btn_withdraw.setLayout(null);
        btn_withdraw.setBackground(new Color(131, 175, 152));
        panel.add(btn_withdraw);

        JLabel label_3 = new JLabel("");
        Image imagewithdraw = new ImageIcon(this.getClass().getResource("withdraw.png")).getImage();
        label_3.setIcon(new ImageIcon(imagewithdraw));
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setBounds(10, 0, 38, 30);
        btn_withdraw.add(label_3);

        JLabel lblWithdraw = new JLabel("Transfer");
        lblWithdraw.setHorizontalAlignment(SwingConstants.CENTER);
        lblWithdraw.setForeground(Color.WHITE);
        lblWithdraw.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblWithdraw.setBounds(42, 0, 120, 30);
        btn_withdraw.add(lblWithdraw);

        btn_edit = new JPanel();
        btn_edit.addMouseListener(this);
        btn_edit.setBounds(10, 359, 189, 30);
        btn_edit.setLayout(null);
        btn_edit.setBackground(new Color(131, 175, 152));
        panel.add(btn_edit);

        JLabel label_4 = new JLabel("");
        Image imageedit = new ImageIcon(this.getClass().getResource("edit.png")).getImage();
        label_4.setIcon(new ImageIcon(imageedit));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(10, 0, 38, 30);
        btn_edit.add(label_4);

        JLabel lblEdit = new JLabel("Edit");
        lblEdit.setHorizontalAlignment(SwingConstants.CENTER);
        lblEdit.setForeground(Color.WHITE);
        lblEdit.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblEdit.setBounds(42, 0, 91, 30);
        btn_edit.add(lblEdit);

        btn_delete = new JPanel();
        btn_delete.addMouseListener(this);
        btn_delete.setBounds(10, 401, 189, 30);
        btn_delete.setLayout(null);
        btn_delete.setBackground(new Color(131, 175, 152));
        panel.add(btn_delete);

        JLabel label_5 = new JLabel("");
        Image imagedelete = new ImageIcon(this.getClass().getResource("delete.png")).getImage();
        label_5.setIcon(new ImageIcon(imagedelete));
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setBounds(10, 0, 38, 30);
        btn_delete.add(label_5);

        JLabel lblDelete = new JLabel("Delete");
        lblDelete.setHorizontalAlignment(SwingConstants.CENTER);
        lblDelete.setForeground(Color.WHITE);
        lblDelete.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        lblDelete.setBounds(42, 0, 104, 30);
        btn_delete.add(lblDelete);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(6, 6, 193, 45);
        panel.add(panel_2);
        panel_2.setBackground(Color.decode("#EEB462"));
        panel_2.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Image imageuser = new ImageIcon(this.getClass().getResource("user.png")).getImage();
        lblNewLabel.setIcon(new ImageIcon(imageuser));
        lblNewLabel.setBounds(6, 0, 49, 45);
        panel_2.add(lblNewLabel);

        JLabel lblUser = new JLabel("User");
        lblUser.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        lblUser.setHorizontalAlignment(SwingConstants.CENTER);
        lblUser.setBounds(54, 24, 133, 21);
        panel_2.add(lblUser);

        useramount = new JLabel("0");
        useramount.setForeground(Color.WHITE);
        useramount.setFont(new Font("Consolas", Font.ITALIC, 18));
        useramount.setHorizontalAlignment(SwingConstants.CENTER);
        useramount.setBounds(54, 0, 139, 28);
        panel_2.add(useramount);

        JPanel panel_3 = new JPanel();
        panel_3.setBounds(6, 55, 193, 45);
        panel.add(panel_3);
        panel_3.setBackground(Color.decode("#EEB462"));
        panel_3.setLayout(null);

        JLabel label_8 = new JLabel("");
        Image imagetotal = new ImageIcon(this.getClass().getResource("totalmoney.png")).getImage();
        label_8.setIcon(new ImageIcon(imagetotal));
        label_8.setHorizontalAlignment(SwingConstants.CENTER);
        label_8.setBounds(0, 0, 55, 45);
        panel_3.add(label_8);

        JLabel lblTotalBalance = new JLabel("Total Balance");
        lblTotalBalance.setBounds(67, 23, 102, 16);
        panel_3.add(lblTotalBalance);
        lblTotalBalance.setHorizontalAlignment(SwingConstants.CENTER);
        lblTotalBalance.setForeground(Color.WHITE);
        lblTotalBalance.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        totalmoney = new JLabel("0");
        totalmoney.setHorizontalAlignment(SwingConstants.CENTER);
        totalmoney.setForeground(Color.WHITE);
        totalmoney.setFont(new Font("Consolas", Font.ITALIC, 18));
        totalmoney.setBounds(64, 0, 111, 28);
        panel_3.add(totalmoney);

        JLabel lblDesignBy = new JLabel("design by");
        lblDesignBy.setFont(new Font("Yu Gothic UI Semilight", Font.BOLD, 18));
        lblDesignBy.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesignBy.setForeground(Color.WHITE);
        lblDesignBy.setBounds(33, 112, 99, 30);
        panel.add(lblDesignBy);

        JLabel lblSunshine = new JLabel("Sunshine");
        lblSunshine.setFont(new Font("Segoe Script", Font.BOLD, 18));
        lblSunshine.setHorizontalAlignment(SwingConstants.CENTER);
        lblSunshine.setForeground(Color.WHITE);
        lblSunshine.setBounds(90, 141, 99, 37);
        panel.add(lblSunshine);
        
        btn_history = new JPanel();
        btn_history.setLayout(null);
        btn_history.setBackground(new Color(131, 175, 152));
        btn_history.setBounds(10, 443, 189, 30);
        btn_history.addMouseListener(this);
        panel.add(btn_history);
        
        JLabel label_6 = new JLabel("");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        Image imagehistory = new ImageIcon(this.getClass().getResource("history.png")).getImage();
        label_6.setIcon(new ImageIcon(imagehistory));
        label_6.setBounds(10, 0, 38, 30);
        btn_history.add(label_6);
        
        JLabel label_9 = new JLabel("History");
        label_9.setHorizontalAlignment(SwingConstants.CENTER);
        label_9.setForeground(Color.WHITE);
        label_9.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        label_9.setBounds(42, 0, 104, 30);
        btn_history.add(label_9);

        for (int i = 0; i < title.length; i++) {
            vtitle.add(title[i]);
        }

        model = new DefaultTableModel(vdata, vtitle) {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return Double.class;
                case 3:
                    return String.class;
                default:
                    return String.class;
                }
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);       

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(0, 204, 204));
        table.setShowGrid(false);
        table.setDefaultRenderer(String.class, center);
        table.setDefaultRenderer(Double.class, center);
        table.addMouseListener(this);

        table.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent arg0) {
                try {
					int total = 0;
					String count = Integer.toString(model.getRowCount());
					for (int i = 0; i < model.getRowCount(); i++) {
					    total += Integer.parseInt(model.getValueAt(i, 2).toString());
					}                
					useramount.setText(count);
					totalmoney.setText(Integer.toString(total));
				} catch (NumberFormatException e) {
					new error("Error");
					e.printStackTrace();
				}
            }
        });

        JTableHeader header = new JTableHeader();
        header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setDefaultRenderer(center);

        // Sort
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(rowSorter);
        rowSorter.setSortable(3, false);
        rowSorter.setSortable(4, false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(207, 176, 569, 268);
        scrollPane.setViewportBorder(null);
        contentPane.add(scrollPane);

        JLabel lblSearch = new JLabel("Search");
        lblSearch.setBounds(610, 450, 86, 23);
        lblSearch.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        Image imagesearch = new ImageIcon(this.getClass().getResource("search.png")).getImage();
        lblSearch.setIcon(new ImageIcon(imagesearch));
        contentPane.add(lblSearch);

        txt_search = new JTextField("Search..");
        txt_search.setBounds(469, 449, 129, 24);
        txt_search.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        txt_search.setHorizontalAlignment(SwingConstants.LEFT);
        contentPane.add(txt_search);
        txt_search.setColumns(10);
        
        txt_search.addFocusListener(new FocusListener() {
			
        	@Override
			public void focusLost(FocusEvent e) {
				if(txt_search.getText().equals("")) {
					txt_search.setText("Search..");
				}			
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(txt_search.getText().equals("Search..")) {
					txt_search.setText(null);
				}
				else {
					txt_search.setCaretPosition(txt_search.getText().length());
				}
				
				
			}
		});

        JLabel label_7 = new JLabel("");
        label_7.setBounds(-2, 0, 209, 148);
        contentPane.add(label_7);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(207, 0, 569, 177);
        contentPane.add(panel_1);
        panel_1.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("");
        Image image = new ImageIcon(this.getClass().getResource("wall.jpg")).getImage();
        lblNewLabel_1.setIcon(new ImageIcon(image));

        lblNewLabel_1.setBounds(0, 0, 569, 177);
        panel_1.add(lblNewLabel_1);
        
        comboBox = new JComboBox(choose);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboBox.setBounds(353, 450, 104, 24);
        comboBox.setBackground(Color.WHITE);
        contentPane.add(comboBox);
        comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					String text = txt_search.getText();
					if(text.trim().length() == 0 || text.equals("Search..")) {
						rowSorter.setRowFilter(null);
					}
					else if (comboBox.getSelectedIndex() == 0) {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					else
					{
						if(comboBox.getSelectedIndex() == 1) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 0));
						}
						else if (comboBox.getSelectedIndex() == 2) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 1));
						}
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}				
			}
		});

        txt_search.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
            	try {
					String text = txt_search.getText();
					if(text.trim().length() == 0 || text.equals("Search..")) {
						rowSorter.setRowFilter(null);
					}
					else if (comboBox.getSelectedIndex() == 0) {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					else
					{
						if(comboBox.getSelectedIndex() == 1) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 0));
						}
						else if (comboBox.getSelectedIndex() == 2) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 1));
						}
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}							
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
            	try {
					String text = txt_search.getText();
					if(text.trim().length() == 0 || text.equals("Search..")) {
						rowSorter.setRowFilter(null);
					}
					else if (comboBox.getSelectedIndex() == 0) {
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
					}
					else
					{
						if(comboBox.getSelectedIndex() == 1) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 0));
						}
						else if (comboBox.getSelectedIndex() == 2) {
							rowSorter.setRowFilter(RowFilter.regexFilter(text, 1));
						}
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}			
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }
    
    private void setdatabase(String database) {
    	this.csdl = database;
    }
    
    public void login() {    	
    	user1 = new login("My Program", this);
    	user1.setVisible(true);
    }
    
    public void createaccount(String username, String password) {
    	try {
    		
    	String sqlsearch = "select case when not EXISTS("
    			+ "			select *"
    			+ "			from useraccount"
    			+ "			where username = '"+username+"')"
    					+ "	then cast (1 as bit)"
    					+ "	else cast (0 as bit) end";
   // 	System.out.print(sqlsearch);
    	rs = this.stm.executeQuery(sqlsearch);
    	String check = "";
    	
    	if(rs.next()) {
    		check = rs.getString(1);
    	}
    	
    	if(check.equals("1")) {
    		
    		String sql = "insert into useraccount (username, password) values ('"+username+"', '"+password+"')";       	
			this.stm.executeUpdate(sql);   		
			
			String sqlcreate = "create table "+username+"("
					+ "ID varchar(10),"
					+ "Name varchar(40),"
					+ "Balance varchar(40),"
					+ "Gender varchar(10),"
					+ "DoB varchar(20))";
			this.stm.executeUpdate(sqlcreate);
    	}
    	
    	else {
    		new error("Username existed");
    	}
    		   	
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}
    	
    }
    
    public void start(String username, String password) {
    	    	
    	try {
    		
    		String sqlsearch = "select case when EXISTS("
        			+ "			select *"
        			+ "			from useraccount"
        			+ "			where username = '"+username+"' and password = '"+password+"')"
        					+ "	then cast (1 as bit)"
        					+ "	else cast (0 as bit) end";
       // 	System.out.print(sqlsearch);
        	rs = this.stm.executeQuery(sqlsearch);
        	String check = "";
        	
        	if(rs.next()) {
        		check = rs.getString(1);
       // 		System.out.print(check);
        	}
        	
        	if(check.equals("1")) {
//        		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");		
    			rs = stm.executeQuery("select * from "+username+"");
    			dataresult = rs.getMetaData();
    			int column = dataresult.getColumnCount();
    		//	for(int i = 1; i <= column; i++) {
    		//		System.out.print(data.getColumnLabel(i) + "\t");
    		//	}
    		//	System.out.println("");
    			vdata.clear();
    			while(rs.next()) {
    				Vector temp = new Vector(column);
    				for(int i = 1; i <= column; i++) {
//    					System.out.println(rs.getString(i));
    					temp.add(rs.getString(i));
    				}
    		//		System.out.println("");
    				vdata.add(temp);
    			}
    			
    			rs = stm.executeQuery("select * from history where Username = '"+username+"'");
    			dataresult = rs.getMetaData();
    			int columnhis = dataresult.getColumnCount();
    			vhistory.clear();
    			while(rs.next()) {
    				Vector temphis = new Vector(columnhis);
    				for(int i = 2; i <= columnhis; i++) {
    					temphis.add(rs.getString(i));
    				}
    				vhistory.add(temphis);
    			}
    			    			
    			rs.close();
    			model.fireTableDataChanged();	
    			this.setVisible(true);
    	    	this.setResizable(false);
    	    	this.setdatabase(username);
    	    	user1.stop();
        	}
        	else
        	{
        		new error("Wrong username or password");
        	}		
			
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}  	
    }
    
    public void stop() {
    	this.setVisible(false);
    	this.setResizable(false);
    }
    

    public void resetColor(JPanel pn) {
        pn.setBackground(new Color(71, 71, 71));
    }

    public void setColor(JPanel pn) {
        pn.setBackground(new Color(131, 175, 152));
    }

    public void inset(String _id, String _name, int _balance, String _gender, String _day, String _month,
            String _year) {
     try {
         Vector temp = new Vector<>();
         temp.add(_id);
         temp.add(_name);
         temp.add(_balance);
         temp.add(_gender);
         String date = _day + "/" + _month + "/" + _year;
         temp.add(date);
         
         Vector his = new Vector();
         bankaccount bank = new bankaccount(_id, _name, _balance, _gender, date);
         his.add(_id);
         his.add(_name);
         his.add(bank.actionadd());
         his.add(bank.contentadd(_id));
         his.add(bank.timer());
         vhistory.add(his);
         
         vdata.add(temp);
         model.fireTableDataChanged();
         
         String sqlhis =  "insert into history (Username, ID, Name, Action, ActionContent, Time) values ('"+csdl+"', '"+_id+"', '"+_name+"', '"+bank.actionadd()+"', '"+bank.contentadd(_id)+"', '"+bank.timer()+"')";
         this.stm.executeUpdate(sqlhis);         
         
         String sqladd = "insert into " +csdl+"(ID, Name, Balance, Gender, Dob) values ('"+_id+"', '"+_name+"', '"+_balance+"', '"+_gender+"', '"+date+"')";      
         this.stm.executeUpdate(sqladd);
		
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}       
    }

    public void edit(String _id, String _name, int _balance, String _gender, String _day, String _month,
            String _year) {
    
        try {
            selectview = table.getSelectedRow();
            selectmodel = table.convertRowIndexToModel(selectview);
            
            Vector temp = new Vector<>();
            temp.add(_id);
            temp.add(_name);
            temp.add(_balance);
            temp.add(_gender);
            String date = _day + "/" + _month + "/" + _year;       
            temp.add(date);
            
            Vector st = (Vector) vdata.elementAt(selectmodel);
            
            String sqledit = "update "+csdl+" set ID = '"+_id+"', Name = '"+_name+"', Balance = '"+_balance+"', Gender = '"+_gender+"',"
            		+ "DoB = '"+date+"' where ID = '"+st.elementAt(0).toString()+"'";    	
			this.stm.executeUpdate(sqledit);
                                 
            vdata.remove(selectmodel);
            vdata.add(selectmodel, temp);
            model.fireTableDataChanged();
			
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}       
    }

    public void deposit(int _balance) {
         	
		try {
			 selectview = table.getSelectedRow();
		        selectmodel = table.convertRowIndexToModel(selectview);
		        Vector st = (Vector) vdata.elementAt(selectmodel);
		        
		        Vector temp = new Vector<>();
		        temp.add(st.elementAt(0));
		        temp.add(st.elementAt(1));
		        temp.add(_balance);
		        temp.add(st.elementAt(3));
		        temp.add(st.elementAt(4));
		        		        
		        String sqledit = "update "+csdl+" set Balance = '"+_balance+"' where ID = '"+st.elementAt(0).toString()+"'";  			
		        this.stm.executeUpdate(sqledit);
			
			if(Integer.parseInt(st.elementAt(2).toString()) < _balance) {
	        	 Vector his = new Vector();
	        	 	      
	        	 int depositmoney = _balance -  Integer.parseInt(st.elementAt(2).toString());
	             his.add(st.elementAt(0).toString());
	             his.add(st.elementAt(1).toString());
	             his.add("Deposit");
	             his.add("Depost " + depositmoney);
	             his.add(this.gettime());
	             
	             String sqlhis =  "insert into history (Username, ID, Name, Action, ActionContent, Time) values "
	             		+ "('"+csdl+"', '"+st.elementAt(0)+"', '"+st.elementAt(1)+"', 'Deposit',"
	             		+ " 'Deposit "+depositmoney+"', '"+this.gettime()+"')";
	             this.stm.executeUpdate(sqlhis);
	             
	             
	             vhistory.add(his);
	        }
	        
	        if(Integer.parseInt(st.elementAt(2).toString()) > _balance) {
	       	 Vector his = new Vector();
	       	 int depositmoney = Integer.parseInt(st.elementAt(2).toString()) - _balance;
	            his.add(st.elementAt(0).toString());
	            his.add(st.elementAt(1).toString());
	            his.add("Withdraw");
	            his.add("Withdraw " + depositmoney);
	            his.add(this.gettime());
	            
	            String sqlhis =  "insert into history (Username, ID, Name, Action, ActionContent, Time) values "
	             		+ "('"+csdl+"', '"+st.elementAt(0)+"', '"+st.elementAt(1)+"', 'Withdraw',"
	             		+ " 'Withdraw "+depositmoney+"', '"+this.gettime()+"')";
	             this.stm.executeUpdate(sqlhis);
	            
	            vhistory.add(his);
	       }
	       	               
	        vdata.remove(selectmodel);
	        vdata.add(selectmodel, temp);
	        model.fireTableDataChanged();
	        
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}                
    }
    
    public void exchange1(int _balance) {
        			
        try {
        	
        	selectview = table.getSelectedRow();
            selectmodel = table.convertRowIndexToModel(selectview);
            Vector st = (Vector) vdata.elementAt(selectmodel);

            Vector temp = new Vector<>();
            temp.add(st.elementAt(0));
            temp.add(st.elementAt(1));
            temp.add(_balance);
            temp.add(st.elementAt(3));
            temp.add(st.elementAt(4));
                   
            String sqledit = "update "+csdl+" set Balance = '"+_balance+"' where ID = '"+st.elementAt(0).toString()+"'";          	
			this.stm.executeUpdate(sqledit);
			
			 vdata.remove(selectmodel);
		        vdata.add(selectmodel, temp);
		        model.fireTableDataChanged();
		        
		     	 Vector his = new Vector();
		       	 int depositmoney = Integer.parseInt(st.elementAt(2).toString()) - _balance;
		            his.add(st.elementAt(0).toString());
		            his.add(st.elementAt(1).toString());
		            his.add("Transfer");
		            his.add("Transfer " + depositmoney);
		            his.add(this.gettime());
		            
		            String sqlhis =  "insert into history (Username, ID, Name, Action, ActionContent, Time) values "
		             		+ "('"+csdl+"', '"+st.elementAt(0)+"', '"+st.elementAt(1)+"', 'Transfer',"
		             		+ " 'Transfer "+depositmoney+"', '"+this.gettime()+"')";
		             this.stm.executeUpdate(sqlhis);
		            
		            vhistory.add(his);
			
			
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}                                
    }

    public void exchange2(int _balance, int index) {
                	
		try {
			
			Vector st = (Vector) vdata.elementAt(index);

	        Vector temp = new Vector<>();
	        temp.add(st.elementAt(0));
	        temp.add(st.elementAt(1));
	        temp.add(_balance);
	        temp.add(st.elementAt(3));
	        temp.add(st.elementAt(4));
	        
	        
	        String sqledit = "update "+csdl+" set Balance = '"+_balance+"' where ID = '"+st.elementAt(0).toString()+"'";  
			this.stm.executeUpdate(sqledit);
			
			vdata.remove(index);
			vdata.add(index, temp);
			model.fireTableDataChanged();
		            
			Vector his = new Vector();
		   	int depositmoney = _balance - Integer.parseInt(st.elementAt(2).toString());
		   	his.add(st.elementAt(0).toString());
            his.add(st.elementAt(1).toString());
            his.add("Give");
            his.add("Give " + depositmoney);
            his.add(this.gettime());
            
            String sqlhis =  "insert into history (Username, ID, Name, Action, ActionContent, Time) values "
             		+ "('"+csdl+"', '"+st.elementAt(0)+"', '"+st.elementAt(1)+"', 'Give',"
             		+ " 'Give "+depositmoney+"', '"+this.gettime()+"')";
             this.stm.executeUpdate(sqlhis);
            
            vhistory.add(his); 		
            
		} catch (SQLException e) {
			new error("Error");
			e.printStackTrace();
		}                           
    }
    
    public void search(int index, String infosearch, int check)
    {
    	indexsearch = -1;
    	for(int i = 0; i < vdata.size(); i++)
    	{
    		if(infosearch.equals(model.getValueAt(i, index))) {
    			indexsearch = i;
    		}
    	}
    	
    	if(indexsearch == -1) {
    		if(index == 0) {
    			new error("No Id found");
    		}
    		if(index == 1) {
    			new error("No Name found");
    		}
    	}
    		
    	else if (indexsearch == check)
    		new error("Can not choose the same account");
    	else {
    			
    	Vector st = (Vector) vdata.elementAt(indexsearch);
    	t1.add2(st.elementAt(0).toString(), st.elementAt(1).toString(),
    			st.elementAt(2).toString(), st.elementAt(3).toString(), st.elementAt(4).toString(), indexsearch);
    		} 
    }
    
    public int checkid(String idsearch, int indexcheck) {
    	for(int i = 0; i < vdata.size(); i++) {
    		if(idsearch.equals(model.getValueAt(i, 0)) && i != indexcheck) {
    			return 1;
    		}
    	}
    	return 2;
    }
    
    public int getindex(String idsearch) {
    	for(int i = 0; i < vdata.size(); i++) {
    		if(idsearch.equals(model.getValueAt(i, 0)))
    			return i;
    	}
    	return -1;
    }
    
    public String gettime() {
        DateFormat datefomart = new SimpleDateFormat("yyyy//MM//dd HH:mm:ss");
	  	Calendar now = Calendar.getInstance();
	  	return datefomart.format(now.getTime());
    }
     
    public void importexcel(File file)
    {
    		Workbook book;
    	//	System.out.println(file);
    		try {		
    				book = Workbook.getWorkbook(file);
        			System.out.println("Connected");
    			
    			Sheet sheet = book.getSheet(0);
    			
    			vdata.clear();
    			
    			Vector temp = new Vector<>();
    			for(int i = 1; i < sheet.getRows(); i++) {	
    				for(int k = 0; k < sheet.getColumns(); k++) {
    					Cell data = sheet.getCell(k, i);
    					temp.add(data.getContents());
    				}
    				vdata.add(temp);			
    			}
    			model.fireTableDataChanged();
    			book.close();
    		} catch (Exception e) {
    			JOptionPane.showMessageDialog(null, "Error");
    			}	
    	}    	
    
    public void exportexcel(JTable table, File file) {
    	try {
    		WritableWorkbook workbook = Workbook.createWorkbook(file);
    		WritableSheet sheet = workbook.createSheet("sheet 0", 0);
    		    		
    		for(int i = 0; i < model.getColumnCount(); i++) {
    			Label column = new Label(i, 0, model.getColumnName(i));
    			sheet.addCell(column);
    		}
    		
    		for(int j = 0; j < model.getRowCount(); j++) {
    			for(int k = 0; k < model.getColumnCount(); k++) {
    				Label row = new Label(k, j + 1, model.getValueAt(j, k).toString());
    				sheet.addCell(row);
    			}
    		}
    		workbook.write();
    		workbook.close();
    	} catch (Exception e) {
    		new error("Error");
    	}
    }
     
    
    @Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveitem) {
			JFileChooser chooser = new JFileChooser();
			int check = chooser.showSaveDialog(this);
			if(check == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				System.out.print(file.toString());
				this.exportexcel(table, file);
			}
		}
		
		if(e.getSource() == saveasitem) {
			
			try {
				rs = stm.executeQuery("select * from "+csdl+"");
				dataresult = rs.getMetaData();
				int column = dataresult.getColumnCount();

				vdata.clear();
				while(rs.next()) {
					Vector temp = new Vector(column);
					for(int i = 1; i <= column; i++) {
						temp.add(rs.getString(i));
					}

					vdata.add(temp);
				}
				
				rs = stm.executeQuery("select * from history where Username = '"+csdl+"'");
				dataresult = rs.getMetaData();
				int columnhis = dataresult.getColumnCount();
				vhistory.clear();
				while(rs.next()) {
					Vector temphis = new Vector(columnhis);
					for(int i = 2; i <= columnhis; i++) {
						temphis.add(rs.getString(i));
					}
					vhistory.add(temphis);
				}
				    			
				rs.close();
				model.fireTableDataChanged();
			//	System.out.print("success");
			} catch (SQLException e1) {
				new error("Error");
				e1.printStackTrace();
			}	
			
		}
		
		if(e.getSource() == LogOut) {
			this.dispose();
			user1 = new login("My Program", this);
	    	user1.setVisible(true);
		}
		
		if(e.getSource() == exititem) {
			this.dispose();
		}
		
		if(e.getSource() == openitem) {
			JFileChooser chooser = new JFileChooser();
	    	int check = chooser.showOpenDialog(this);
	    	if(check == JFileChooser.APPROVE_OPTION) {
	    		File file = chooser.getSelectedFile();
	    		this.importexcel(file);
	    	}
		}	
		
		if(e.getSource() == aboutmeitem) {
			new aboutme();
		}
	}


	@Override
    public void mouseClicked(MouseEvent e) {
        selectview = table.getSelectedRow();
        if (e.getSource() == btn_insert) {
            setColor(btn_insert);
            new information("Insert form", this, "", "", "", "", "", "", "");
        }
        if (e.getSource() == btn_add) {
            setColor(btn_add);
            new information("Add form", this, "", "", "", "", "", "", "");
        }
        if (e.getSource() == btn_deposit) {
            try {
				setColor(btn_deposit);
				selectview = table.getSelectedRow();  
				if(selectview < 0) {
					new error("Please choose an account");
				}
				else {
				selectmodel = table.convertRowIndexToModel(selectview);
				Vector st = (Vector) vdata.elementAt(selectmodel);
				new exchange("Deposit form", this, st.elementAt(0).toString(), st.elementAt(1).toString(),
				        st.elementAt(2).toString(), st.elementAt(3).toString(), st.elementAt(4).toString());                                              
				}
			} catch (Exception e1) {
				new error("Error");
				e1.printStackTrace();
			}
        }
            
        if (e.getSource() == btn_withdraw) {
            try {
				setColor(btn_withdraw);
				selectview = table.getSelectedRow();
				if(selectview < 0) {
					new error("Please choose an account");
				}
				else {
				selectmodel = table.convertRowIndexToModel(selectview);
				Vector st = (Vector) vdata.elementAt(selectmodel);
				          
				t1.add1(this,st.elementAt(0).toString(), st.elementAt(1).toString(),
				        st.elementAt(2).toString(), st.elementAt(3).toString(), st.elementAt(4).toString(),
				        selectmodel);
				}
			} catch (Exception e1) {
				new error("Error");
				e1.printStackTrace();
			}
        }
        if (e.getSource() == btn_edit) {
            try {
				setColor(btn_edit);
				selectview = table.getSelectedRow();
				
				if(selectview < 0) {
					new error("Please choose an account");
				}
				else {
				selectmodel = table.convertRowIndexToModel(selectview);
				Vector st = (Vector) vdata.elementAt(selectmodel);
         
				String objTostr = st.elementAt(4).toString();
				String _day = Character.toString(objTostr.charAt(0)) + Character.toString(objTostr.charAt(1));
				String _month = Character.toString(objTostr.charAt(3)) + Character.toString(objTostr.charAt(4));
				String _year = Character.toString(objTostr.charAt(6)) + Character.toString(objTostr.charAt(7))
				        + Character.toString(objTostr.charAt(8)) + Character.toString(objTostr.charAt(9));
				
				new information("Edit form", this, st.elementAt(0).toString(), st.elementAt(1).toString(),
				        st.elementAt(2).toString(), st.elementAt(3).toString(), _day, _month, _year);
				}
			} catch (Exception e1) {
				new error("Error");
				e1.printStackTrace();
			}
        }
        if (e.getSource() == btn_delete) {
          
           	try {
           	  setColor(btn_delete);
              selectview = table.getSelectedRow();
                      
              if(selectview < 0) {
              	new error("Please choose an account");
              }
              
              else {
                      	
              	selectmodel = table.convertRowIndexToModel(selectview);
               	Vector st = (Vector) vdata.elementAt(selectmodel);
                                            
               	String sqldelete = "Delete from "+csdl+" where ID = '"+st.elementAt(0).toString()+"'";
				this.stm.executeUpdate(sqldelete);
					
				model.removeRow(selectmodel);
              }
					
			} catch (SQLException e1) {
				new error("Error");
				e1.printStackTrace();
			}                                      
        }
        
        if(e.getSource() == btn_history) {
        	setColor(btn_history);
        	history h1 = new history();
            
            for(int i = 0; i < vhistory.size(); i++) {
            	Vector v = (Vector) vhistory.elementAt(i);
            		h1.givedata(v);
            }
            h1.setVisible(true);              	
        }
        
        
        if (e.getClickCount() == 2) {
            try {
				JTable tagert = (JTable) e.getSource();
				selectview = tagert.getSelectedRow();
				selectmodel = table.convertRowIndexToModel(selectview);
				Vector st = (Vector) vdata.elementAt(selectmodel);

				String objTostr = st.elementAt(4).toString();
				String _day = Character.toString(objTostr.charAt(0)) + Character.toString(objTostr.charAt(1));
				String _month = Character.toString(objTostr.charAt(3)) + Character.toString(objTostr.charAt(4));
				String _year = Character.toString(objTostr.charAt(6)) + Character.toString(objTostr.charAt(7))
				        + Character.toString(objTostr.charAt(8)) + Character.toString(objTostr.charAt(9));

				new information("Edit form", this, st.elementAt(0).toString(), st.elementAt(1).toString(),
				        st.elementAt(2).toString(), st.elementAt(3).toString(), _day, _month, _year);
			} catch (Exception e1) {
				new error("Error");
				e1.printStackTrace();
			}
        }                    
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == btn_insert)
            resetColor(btn_insert);
        if (e.getSource() == btn_add)
            resetColor(btn_add);
        if (e.getSource() == btn_deposit)
            resetColor(btn_deposit);
        if (e.getSource() == btn_withdraw)
            resetColor(btn_withdraw);
        if (e.getSource() == btn_delete)
            resetColor(btn_delete);
        if (e.getSource() == btn_edit)
            resetColor(btn_edit);
        if(e.getSource() == btn_history)
        	resetColor(btn_history);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
}



class information extends JFrame implements MouseListener {
    private JPanel contentPane;
    private JTextField id;
    private JTextField name;
    private JTextField balance;
    private JLabel lblName;
    private JPanel save;
    private JPanel cancel;
    private JRadioButton male;
    private JRadioButton female;
    private ButtonGroup group;
    private JComboBox day;
    private JComboBox month;
    private JComboBox year;
    private JLabel labelcheck;
    private Image imageright;
    private Image imagefalse;
    private JLabel labelmoney;
    int checkaccount;
    String titleforcheck;
    BankAccountManagement temp;  

    private String[] ngay = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
    private String[] thang = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
    private String[] nam = { "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000",
            "2001", "2002", "2003", "2004", "2005" };

    public information(String title, BankAccountManagement _ui, String _id, String _name, String _balance,
            String _gender, String _day, String _month, String _year) {
        super(title);
        temp = _ui;
        titleforcheck = title;
        this.setVisible(true);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(200, 200, 315, 329);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(null);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#A8E6CE"));
        panel.setBounds(10, 65, 102, 23);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblId = new JLabel("ID");
        lblId.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblId.setHorizontalAlignment(SwingConstants.CENTER);
        lblId.setBounds(0, 0, 102, 23);
        panel.add(lblId);

        id = new JTextField(_id);
        id.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(153, 204, 255)));
        id.setBounds(117, 65, 145, 23);
        contentPane.add(id);
        id.setColumns(10);
        
        id.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					String idsearch = id.getText();
					
					if(title.equals("Edit form")) {
						int indexsearch = temp.getindex(_id);
						int check = temp.checkid(idsearch, indexsearch);
						if(check == 1) {
							labelcheck.setIcon(new ImageIcon(imagefalse));
							checkaccount = 1;
						}
						else if (check == 2) {
							labelcheck.setIcon(new ImageIcon(imageright));
							checkaccount = 2;
						}
					}
					else {
						int check = temp.checkid(idsearch, -1);
						if(check == 1) {
							labelcheck.setIcon(new ImageIcon(imagefalse));
							checkaccount = 1;
						}
						else if (check == 2) {
							labelcheck.setIcon(new ImageIcon(imageright));
							checkaccount = 2;
						}
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}								
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					String idsearch = id.getText();

					if(title.equals("Edit form")) {
						int indexsearch = temp.getindex(_id);
						int check = temp.checkid(idsearch, indexsearch);
						if(check == 1) {
							labelcheck.setIcon(new ImageIcon(imagefalse));
							checkaccount = 1;
						}
						else if (check == 2) {
							labelcheck.setIcon(new ImageIcon(imageright));
							checkaccount = 2;
						}
					}
					else {
						int check = temp.checkid(idsearch, -1);
						if(check == 1) {
							labelcheck.setIcon(new ImageIcon(imagefalse));
							checkaccount = 1;
						}
						else if (check == 2) {
							labelcheck.setIcon(new ImageIcon(imageright));
							checkaccount = 2;
						}
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				new error("Error");
				
			}
		});

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBackground(Color.decode("#A8E6CE"));
        panel_1.setBounds(10, 99, 102, 23);
        contentPane.add(panel_1);

        lblName = new JLabel("Name");
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblName.setBounds(0, 0, 102, 23);
        panel_1.add(lblName);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBackground(Color.decode("#A8E6CE"));
        panel_2.setBounds(10, 133, 102, 23);
        contentPane.add(panel_2);

        JLabel lblBalance = new JLabel("Balance");
        lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
        lblBalance.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblBalance.setBounds(0, 0, 102, 23);
        panel_2.add(lblBalance);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBackground(Color.decode("#A8E6CE"));
        panel_3.setBounds(10, 167, 102, 23);
        contentPane.add(panel_3);

        JLabel lblGender = new JLabel("Gender");
        lblGender.setHorizontalAlignment(SwingConstants.CENTER);
        lblGender.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblGender.setBounds(0, 0, 102, 23);
        panel_3.add(lblGender);

        JPanel panel_4 = new JPanel();
        panel_4.setLayout(null);
        panel_4.setBackground(Color.decode("#A8E6CE"));
        panel_4.setBounds(10, 201, 102, 23);
        contentPane.add(panel_4);

        JLabel lblDateOfBirth = new JLabel("Date of Birth");
        lblDateOfBirth.setHorizontalAlignment(SwingConstants.CENTER);
        lblDateOfBirth.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblDateOfBirth.setBounds(0, 0, 102, 23);
        panel_4.add(lblDateOfBirth);

        name = new JTextField(_name);
        name.setColumns(10);
        name.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(153, 204, 255)));
        name.setBounds(117, 99, 145, 23);
        contentPane.add(name);

        balance = new JTextField(_balance);
        balance.setColumns(10);
        balance.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(153, 204, 255)));
        balance.setBounds(117, 133, 145, 23);
        contentPane.add(balance);

        male = new JRadioButton("Male");
        male.setBackground(Color.WHITE);
        male.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        male.setHorizontalAlignment(SwingConstants.LEFT);
        male.setBounds(117, 167, 64, 23);
        contentPane.add(male);

        female = new JRadioButton("Female");
        female.setBackground(Color.WHITE);
        female.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        female.setHorizontalAlignment(SwingConstants.CENTER);
        female.setBounds(179, 167, 71, 23);
        contentPane.add(female);

        group = new ButtonGroup();
        group.add(male);
        group.add(female);

        if (_gender.equals("Male"))
            male.setSelected(true);
        if (_gender.equals("Female"))
            female.setSelected(true);

        day = new JComboBox(ngay);
        day.setBackground(Color.WHITE);
        day.setBounds(113, 201, 47, 23);
        contentPane.add(day);
        day.setSelectedItem(_day);

        month = new JComboBox(thang);
        month.setBackground(Color.WHITE);
        month.setBounds(164, 201, 47, 22);
        contentPane.add(month);
        month.setSelectedItem(_month);

        year = new JComboBox(nam);
        year.setBackground(Color.WHITE);
        year.setBounds(215, 201, 64, 23);
        contentPane.add(year);
        year.setSelectedItem(_year);

        JPanel panel_5 = new JPanel();
        panel_5.setBackground(Color.decode("#FFD3B5"));
        panel_5.setBounds(51, 11, 183, 40);
        contentPane.add(panel_5);
        panel_5.setLayout(null);

        JLabel lblNewLabel = new JLabel("Register");
        lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 0, 183, 40);
        panel_5.add(lblNewLabel);

        save = new JPanel();
        save.setBackground(Color.decode("#FFD3B5"));
        save.setBounds(68, 245, 71, 23);
        contentPane.add(save);
        save.setLayout(null);
        save.addMouseListener(this);

        JLabel lblSave = new JLabel("Save");
        lblSave.setHorizontalAlignment(SwingConstants.CENTER);
        lblSave.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblSave.setBounds(0, 0, 71, 23);
        save.add(lblSave);

        cancel = new JPanel();
        cancel.setBackground(Color.decode("#FFD3B5"));
        cancel.setBounds(160, 245, 71, 23);
        contentPane.add(cancel);
        cancel.setLayout(null);
        cancel.addMouseListener(this);

        JLabel lblCancel = new JLabel("Cancel");
        lblCancel.setHorizontalAlignment(SwingConstants.CENTER);
        lblCancel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lblCancel.setBounds(0, 0, 71, 23);
        cancel.add(lblCancel);
        
        labelcheck = new JLabel("");
		labelcheck.setHorizontalAlignment(SwingConstants.CENTER);
		imageright = new ImageIcon(getClass().getResource("right.png")).getImage();
		imagefalse = new ImageIcon(getClass().getResource("false.png")).getImage();
		labelcheck.setBounds(266, 65, 28, 28);
		contentPane.add(labelcheck);
		
		labelmoney = new JLabel("");
		labelmoney.setHorizontalAlignment(SwingConstants.CENTER);
		labelmoney.setBounds(252, 133, 23, 23);
		contentPane.add(labelmoney);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == save) {
        	setColor(save);
            insertdata();
        }
        if (e.getSource() == cancel) {
        	setColor(cancel);
            this.dispose();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    	if(e.getSource() == save) {
    		resetColor(save);
    	}
    	if(e.getSource() == cancel) {
    		resetColor(cancel);
    	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    
    public void resetColor(JPanel pn) {
        pn.setBackground(Color.WHITE);
    }

    public void setColor(JPanel pn) {
        pn.setBackground(Color.decode("#FFD3B5"));
    }

    public void insertdata() {
    	
    	try {
    		
			double checkmoney = Integer.parseInt(balance.getText());
			int idcheck = temp.checkid(id.getText(), -1);
			
			if (id.getText().equals("") || name.getText().equals("") || balance.getText().equals("")
			        || (male.isSelected() == false && female.isSelected() == false)) {
			    this.dispose();
			    new error("Invalid Information");
			} 
			
			else if(titleforcheck == "Insert form" && idcheck == 1) {
				new error("ID Exsisted, please choose another one");
			}
			
			else if(checkaccount == 1 ) {
				new error("ID Existed, please choose another one");
			}
			
			else if(checkmoney < 0) {
				new error("Balance must be positive");
			}
			
			else {
			    String _id = id.getText();
			    String _name = name.getText();
   //         double _balance = Double.parseDouble(balance.getText());
			    int _balance = Integer.parseInt(balance.getText());
			    String _gender = null;
			    if (male.isSelected())
			        _gender = "Male";
			    if (female.isSelected())
			        _gender = "Female";
			    String _day = day.getSelectedItem().toString();
			    String _month = month.getSelectedItem().toString();
			    String _year = year.getSelectedItem().toString();
			    
			              	
			    	if (this.getTitle().equals("Add form")) {
			            temp.inset(_id, _name, _balance, _gender, _day, _month, _year);      
			            this.dispose();
			        }
			        if (this.getTitle().equals("Edit form")) {
			            temp.edit(_id, _name, _balance, _gender, _day, _month, _year);
			            this.dispose();
			        }
			        if (this.getTitle().equals("Insert form")) {
			            temp.inset(_id, _name, _balance, _gender, _day, _month, _year);
			        }
     
			    
			}
		} catch (NumberFormatException e) {
			
		//	e.printStackTrace();
			new error("Input string");
		}
    }
}

class exchange extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JTextField exchange;
    private JButton confirm;
    private JButton cancel;
    private JComboBox status;
    JLabel realmoney;
    String[] choose = {"", "Deposit", "Withdraw" };
    BankAccountManagement myUI;
    int stamoney;

    public exchange(String title, BankAccountManagement _ui, String _id, String _name, String _balance, String _gender,
            String _dob) {

        super(title);
        myUI = _ui;
        this.setVisible(true);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(200, 150, 593, 320);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(71, 71, 71));
        panel.setBounds(0, 0, 246, 281);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblName = new JLabel("Name : ");
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        lblName.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblName.setForeground(SystemColor.activeCaptionBorder);
        lblName.setBounds(23, 38, 49, 22);
        panel.add(lblName);

        JLabel name = new JLabel(_name);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        name.setForeground(Color.WHITE);
        name.setBounds(61, 33, 156, 28);
        panel.add(name);

        JSeparator separator = new JSeparator();
        separator.setBounds(20, 71, 197, 2);
        panel.add(separator);

        JLabel lblId = new JLabel("Id :");
        lblId.setHorizontalAlignment(SwingConstants.LEFT);
        lblId.setForeground(SystemColor.activeCaptionBorder);
        lblId.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblId.setBounds(23, 89, 32, 22);
        panel.add(lblId);

        JLabel id = new JLabel(_id);
        id.setHorizontalAlignment(SwingConstants.CENTER);
        id.setForeground(Color.WHITE);
        id.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        id.setBounds(61, 84, 156, 28);
        panel.add(id);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(20, 122, 197, 2);
        panel.add(separator_1);

        JLabel dob = new JLabel(_dob);
        dob.setHorizontalAlignment(SwingConstants.CENTER);
        dob.setForeground(Color.WHITE);
        dob.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        dob.setBounds(61, 186, 156, 28);
        panel.add(dob);

        JLabel lblDob = new JLabel("DoB :");
        lblDob.setHorizontalAlignment(SwingConstants.LEFT);
        lblDob.setForeground(SystemColor.activeCaptionBorder);
        lblDob.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblDob.setBounds(20, 192, 52, 22);
        panel.add(lblDob);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(20, 173, 197, 2);
        panel.add(separator_2);

        JLabel lblGender = new JLabel("Gender :");
        lblGender.setHorizontalAlignment(SwingConstants.LEFT);
        lblGender.setForeground(SystemColor.activeCaptionBorder);
        lblGender.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblGender.setBounds(23, 140, 62, 22);
        panel.add(lblGender);

        JLabel gender = new JLabel(_gender);
        gender.setHorizontalAlignment(SwingConstants.CENTER);
        gender.setForeground(Color.WHITE);
        gender.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        gender.setBounds(61, 135, 156, 28);
        panel.add(gender);

        JLabel label_2 = new JLabel("");       
        Image imagedollar = new ImageIcon(this.getClass().getResource("dollar.png")).getImage();
        label_2.setIcon(new ImageIcon(imagedollar));
        label_2.setBounds(70, 235, 38, 29);
        panel.add(label_2);

        JLabel balance = new JLabel(_balance);
        balance.setHorizontalAlignment(SwingConstants.CENTER);
        balance.setForeground(Color.WHITE);
        balance.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        balance.setBounds(61, 235, 145, 28);
        panel.add(balance);

        JLabel label = new JLabel("");
        label.setHorizontalAlignment(SwingConstants.CENTER);        
        Image imageuserM = new ImageIcon(this.getClass().getResource("mainuser.png")).getImage();
        label.setIcon(new ImageIcon(imageuserM));
        label.setBounds(204, 33, 32, 28);
        panel.add(label);

        JLabel label_1 = new JLabel("");        
        Image imageid = new ImageIcon(this.getClass().getResource("id.png")).getImage();
        label_1.setIcon(new ImageIcon(imageid));
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setBounds(204, 83, 32, 28);
        panel.add(label_1);

        JLabel label_3 = new JLabel("");        
        Image imagemale = new ImageIcon(this.getClass().getResource("male.png")).getImage();
        Image imagefemale = new ImageIcon(this.getClass().getResource("female.png")).getImage();
        if(_gender == "Male")
        	label_3.setIcon(new ImageIcon(imagemale));
        if(_gender == "Female")
        	label_3.setIcon(new ImageIcon(imagefemale));
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setBounds(204, 134, 32, 28);
        panel.add(label_3);

        JLabel label_4 = new JLabel("");        
        Image imagebirth = new ImageIcon(this.getClass().getResource("birth.png")).getImage();
        label_4.setIcon(new ImageIcon(imagebirth));     
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(204, 186, 32, 28);
        panel.add(label_4);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(245, 0, 330, 281);
        contentPane.add(panel_1);
        panel_1.setLayout(null);      

        JLabel lblYourBalance = new JLabel("Your Balance");
        lblYourBalance.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        lblYourBalance.setHorizontalAlignment(SwingConstants.CENTER);
        lblYourBalance.setBounds(49, 90, 109, 27);
        panel_1.add(lblYourBalance);

        JLabel money = new JLabel(_balance);
        money.setHorizontalAlignment(SwingConstants.RIGHT);
        money.setForeground(Color.BLACK);
        money.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        money.setBounds(170, 71, 124, 60);
        panel_1.add(money);
        
        JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.decode("#851E3E"));
		panel_3.setBounds(88, 11, 172, 36);
		panel_1.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Exchange Form");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 172, 36);
		panel_3.add(lblNewLabel);

        status = new JComboBox(choose);
        status.setBackground(Color.WHITE);
        status.setBounds(68, 122, 92, 27);
        status.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				try {
					String text = exchange.getText();
					if((text.length() == 0 || status.getSelectedIndex() == 0)) {
						realmoney.setText(_balance);}
					else {
						if(status.getSelectedIndex() == 1) {
							stamoney = Integer.parseInt(_balance) + Integer.parseInt(exchange.getText());
							realmoney.setText(Double.toString(stamoney));
						}
						if(status.getSelectedIndex() == 2) {
							stamoney = Integer.parseInt(_balance) - Integer.parseInt(exchange.getText());
							realmoney.setText(Double.toString(stamoney));
						}
					}
				} catch (NumberFormatException e) {
					new error("Error");
			//		e.printStackTrace();
				}
			}
		}); 
        panel_1.add(status);

        exchange = new JTextField();
        exchange.setBorder(null);
        exchange.setHorizontalAlignment(SwingConstants.RIGHT);
        exchange.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        exchange.setBounds(202, 120, 92, 24);
        panel_1.add(exchange);
        exchange.setColumns(10);
        
        exchange.getDocument().addDocumentListener(new DocumentListener() {			
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					String text = exchange.getText();
					if((text.trim().length() == 0 && status.getSelectedIndex() == 1)  ||
							(text.trim().length() == 0 && status.getSelectedIndex() == 2)) {
						realmoney.setText(_balance);
					}	
					else {
					if(status.getSelectedIndex() == 1) {
						stamoney = Integer.parseInt(_balance) + Integer.parseInt(exchange.getText());
						realmoney.setText(Double.toString(stamoney));
						}
					if(status.getSelectedIndex() == 2) {
						stamoney = Integer.parseInt(_balance) - Integer.parseInt(exchange.getText());
						realmoney.setText(Double.toString(stamoney));
						}
					}
				} catch (NumberFormatException e1) {
					new error("Error");
			//		e1.printStackTrace();
				}	
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					String text = exchange.getText();
					if((text.trim().length() == 0 && status.getSelectedIndex() == 1)  ||
							(text.trim().length() == 0 && status.getSelectedIndex() == 2)) {
						realmoney.setText(_balance);
					}		
					else {
						
					if(status.getSelectedIndex() == 1) {
						stamoney = Integer.parseInt(_balance) + Integer.parseInt(exchange.getText());
						realmoney.setText(Double.toString(stamoney));
						}
					if(status.getSelectedIndex() == 2) {
						stamoney = Integer.parseInt(_balance) - Integer.parseInt(exchange.getText());
						realmoney.setText(Double.toString(stamoney));
						}
					}
				} catch (NumberFormatException e1) {
					new error("Error");
				//	e1.printStackTrace();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet.");	
			}
		}); 

        JSeparator separator_3 = new JSeparator();
        separator_3.setBounds(180, 147, 116, 2);
        panel_1.add(separator_3);

        realmoney = new JLabel(_balance);
        realmoney.setHorizontalAlignment(SwingConstants.RIGHT);
        realmoney.setForeground(Color.BLACK);
        realmoney.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        realmoney.setBounds(170, 158, 124, 52);
        panel_1.add(realmoney);

        confirm = new JButton("Confirm");
        confirm.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        confirm.setBackground(Color.decode("#009688"));
        confirm.setBounds(73, 231, 89, 23);
        confirm.addActionListener(this);
        panel_1.add(confirm);

        cancel = new JButton("Cancel");
        cancel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        cancel.setBackground(Color.decode("#009688"));
        cancel.setBounds(202, 231, 89, 23);
        cancel.addActionListener(this);
        panel_1.add(cancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirm) {
            this.exchange();
        } else
            this.dispose();
    }

    public void exchange() {
        if (exchange.getText().equals("") || status.getSelectedIndex() == 0) {
            this.dispose();
            new error("Invalid Information");
        }
        
        else if(stamoney < 0) {
        	new error("You do not have enough money");
        }      
        else {
        	myUI.deposit(stamoney);
        	this.dispose();
        }
    }   	
}

class transfer extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField txtsearch;
	private JTextField moneytransfer;
	private JComboBox comboBox;
	private JLabel name2;
	private JLabel dob2;
	private JLabel gender2;
	private JLabel id2;
	private JLabel aftermoney1;
	private JLabel aftermoney2;
	private JButton search;
	private JButton btnConfirm;
	private JButton btnCancel;
	private JLabel money2;
	
	int moneychoose1, moneychoose2, moneylabel2;
	int indexmoney2,indexmoney1;
	
	BankAccountManagement myUI;
	
	String []choose = {"ID", "Name"};
	
	public void add1(BankAccountManagement _ui, String _id, String _name, String _balance, String _gender,
            String _dob, int index) {
		
		this.setVisible(true);
		this.setResizable(false);
		myUI = _ui;
		indexmoney1 = index;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 517, 446);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 501, 106);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.decode("#A8E6CE"));
		panel_3.setBounds(0, 0, 95, 106);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblTransfer = new JLabel("Transfer");
		lblTransfer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransfer.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		lblTransfer.setBounds(0, 0, 95, 106);
		panel_3.add(lblTransfer);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setForeground(Color.GRAY);
		lblName.setBounds(105, 11, 37, 20);
		panel.add(lblName);
		
		JLabel lblId = new JLabel("Id");
		lblId.setForeground(Color.GRAY);
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblId.setBounds(371, 11, 37, 20);
		panel.add(lblId);
		
		JLabel lblDayOfBirth = new JLabel("Day of Birth");
		lblDayOfBirth.setForeground(Color.GRAY);
		lblDayOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDayOfBirth.setBounds(105, 42, 73, 20);
		panel.add(lblDayOfBirth);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setForeground(Color.GRAY);
		lblGender.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblGender.setBounds(325, 42, 45, 20);
		panel.add(lblGender);
		
		JLabel money1 = new JLabel(_balance);
		money1.setHorizontalAlignment(SwingConstants.CENTER);
		money1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		money1.setBounds(225, 61, 117, 34);
		panel.add(money1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(178, 61, 37, 34);
		panel.add(lblNewLabel);
		
		JLabel name1 = new JLabel(_name);
		name1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		name1.setBounds(147, 11, 157, 20);
		panel.add(name1);
		
		JLabel dob1 = new JLabel(_dob);
		dob1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		dob1.setBounds(178, 42, 124, 20);
		panel.add(dob1);
		
		JLabel gender1 = new JLabel(_gender);
		gender1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		gender1.setBounds(371, 42, 95, 20);
		panel.add(gender1);
		
		JLabel id1 = new JLabel(_id);
		id1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		id1.setBounds(391, 11, 100, 20);
		panel.add(id1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(245, 245, 220));
		panel_1.setBounds(0, 106, 501, 151);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.decode("#FFD3B5"));
		panel_4.setBounds(0, 0, 95, 151);
		panel_1.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblGiver = new JLabel("Giver");
		lblGiver.setHorizontalAlignment(SwingConstants.CENTER);
		lblGiver.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblGiver.setBounds(0, 0, 95, 151);
		panel_4.add(lblGiver);
		
		JLabel label = new JLabel("Name");
		label.setForeground(Color.GRAY);
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(105, 50, 37, 20);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("Day of Birth");
		label_1.setForeground(Color.GRAY);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(105, 81, 73, 20);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Id\r\n");
		label_2.setForeground(Color.GRAY);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(374, 50, 28, 20);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("Gender\r\n");
		label_3.setForeground(Color.GRAY);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(322, 81, 45, 20);
		panel_1.add(label_3);
		
		name2 = new JLabel("");
		name2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		name2.setBounds(152, 50, 157, 20);
		panel_1.add(name2);
		
		dob2 = new JLabel("");
		dob2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		dob2.setBounds(173, 81, 124, 20);
		panel_1.add(dob2);
		
		gender2 = new JLabel("");
		gender2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		gender2.setBounds(368, 81, 109, 20);
		panel_1.add(gender2);
		
		id2 = new JLabel("");
		id2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		id2.setBounds(396, 51, 95, 20);
		panel_1.add(id2);
		
		comboBox = new JComboBox(choose);
		comboBox.setBackground(Color.WHITE);
		comboBox.setBounds(140, 11, 73, 22);
		panel_1.add(comboBox);
		
		txtsearch = new JTextField();
		txtsearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtsearch.setHorizontalAlignment(SwingConstants.RIGHT);
		txtsearch.setBounds(223, 11, 144, 25);
		panel_1.add(txtsearch);
		txtsearch.setColumns(10);
		
		search = new JButton("Search");
		search.setBackground(Color.decode("#FFD3B5"));
		search.setBounds(374, 11, 89, 23);
		panel_1.add(search);
		search.addActionListener(this);
		
		money2 = new JLabel("");
		money2.setHorizontalAlignment(SwingConstants.CENTER);
		money2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		money2.setBounds(235, 106, 117, 34);
		panel_1.add(money2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(0, 257, 501, 150);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblMoneyTransfer = new JLabel("Money Transfer");
		lblMoneyTransfer.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblMoneyTransfer.setBounds(52, 11, 105, 26);
		panel_2.add(lblMoneyTransfer);
		
		moneytransfer = new JTextField();
		moneytransfer.setHorizontalAlignment(SwingConstants.RIGHT);
		moneytransfer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		moneytransfer.setBounds(191, 12, 157, 26);
		panel_2.add(moneytransfer);
		moneytransfer.setColumns(10);
		
		moneytransfer.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				try {
					String text = moneytransfer.getText();
					
					if(id2.getText().equals("")) {
						aftermoney1.setText(_balance);
						aftermoney2.setText("");
					}
					else {
						if(text.trim().length() == 0) {
							aftermoney1.setText(_balance);
							aftermoney2.setText(Integer.toString(moneylabel2));
						}
						else {
							moneychoose1 = moneylabel2 + Integer.parseInt(moneytransfer.getText());
							moneychoose2 = Integer.parseInt(_balance) - Integer.parseInt(moneytransfer.getText());
							aftermoney1.setText(Integer.toString(moneychoose2));
							aftermoney2.setText(Integer.toString(moneychoose1));
						}
					}
				} catch (NumberFormatException e) {
					new error("Error");
			//		e.printStackTrace();
				}								
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				try {
					String text = moneytransfer.getText();
					
					if(id2.getText().equals("")) {
						aftermoney1.setText(_balance);
						aftermoney2.setText("");
					}
					else {
						if(text.trim().length() == 0) {
							aftermoney1.setText(_balance);
							aftermoney2.setText(Integer.toString(moneylabel2));
						}
						else {
							moneychoose1 = moneylabel2 + Integer.parseInt(moneytransfer.getText());
							moneychoose2 = Integer.parseInt(_balance) - Integer.parseInt(moneytransfer.getText());
							aftermoney1.setText(Integer.toString(moneychoose2));
							aftermoney2.setText(Integer.toString(moneychoose1));
						}
					}
				} catch (NumberFormatException e) {
					new error("Error");
				//	e.printStackTrace();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				throw new UnsupportedOperationException("Not supported yet.");	
				
			}
		});
		
		aftermoney1 = new JLabel(_balance);
		aftermoney1.setHorizontalAlignment(SwingConstants.CENTER);
		aftermoney1.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		aftermoney1.setBounds(42, 105, 131, 34);
		panel_2.add(aftermoney1);
		
		aftermoney2 = new JLabel("");
		aftermoney2.setHorizontalAlignment(SwingConstants.CENTER);
		aftermoney2.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		aftermoney2.setBounds(231, 105, 117, 34);
		panel_2.add(aftermoney2);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.setBackground(new Color(143, 188, 143));
		btnConfirm.setBounds(394, 48, 89, 23);
		btnConfirm.addActionListener(this);
		panel_2.add(btnConfirm);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBackground(new Color(143, 188, 143));
		btnCancel.setBounds(394, 82, 89, 23);
		btnCancel.addActionListener(this);
		panel_2.add(btnCancel);
		
		JLabel lblGiverMoney = new JLabel("Giver Money");
		lblGiverMoney.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblGiverMoney.setHorizontalAlignment(SwingConstants.CENTER);
		lblGiverMoney.setBounds(231, 48, 117, 34);
		panel_2.add(lblGiverMoney);
		
		JLabel lblTransferMoney = new JLabel("Transfer Money");
		lblTransferMoney.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransferMoney.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		lblTransferMoney.setBounds(42, 48, 131, 34);
		panel_2.add(lblTransferMoney);
	}
	
	public void add2(String _id, String _name, String _balance, String _gender,
            String _dob, int index)
	{
		id2.setText(_id);
		name2.setText(_name);
		money2.setText(_balance);
		gender2.setText(_gender);
		dob2.setText(_dob);
		aftermoney2.setText(_balance);
		moneylabel2 = Integer.parseInt(_balance);
		indexmoney2 = index;
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            this.searchinfo();
        } 
        if(e.getSource() == btnConfirm) {
        	this.exchangemoney();
        }
        if(e.getSource() == btnCancel) {
        	this.dispose();
        }
    }
	
	public void searchinfo()
	{
		String infosearch = txtsearch.getText();
		int indexselect = comboBox.getSelectedIndex();
		myUI.search(indexselect, infosearch, indexmoney1);
	}
	
	public void exchangemoney() {
		if(moneytransfer.getText().equals("") || txtsearch.getText().equals(""))
		{
			this.dispose();
			new error("Invalid Information");
		}
		
		else if (moneychoose2 < 0) {
			new error("You do not have enoungh money");
		}
			
		else {
			myUI.exchange1(moneychoose2);
			myUI.exchange2(moneychoose1, indexmoney2);
			this.dispose();
		}
	}		
}

class error extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JButton btnOk;
	private JLabel text;
	
	public error(String _text) {
		
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 250, 321, 173);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel text = new JLabel(_text);
		text.setForeground(Color.RED);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		text.setBounds(10, 50, 300, 39);
		contentPane.add(text);
		
		btnOk = new JButton("OK");
		btnOk.setBackground(new Color(255, 153, 153));
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnOk.setBounds(109, 100, 89, 23);
		btnOk.addActionListener(this);
		contentPane.add(btnOk);
		
		JLabel icon = new JLabel("");
		icon.setBounds(140, 11, 48, 39);
		Image imageerror = new ImageIcon(this.getClass().getResource("error.png")).getImage();
		icon.setIcon(new ImageIcon(imageerror));
		contentPane.add(icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnOk) {
			this.dispose();
		}
	}		
}

class login extends JFrame implements ActionListener, MouseListener{

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnLogin;
	private JButton btnExit;
	private JLabel createaccount;
	
	BankAccountManagement myUI;

	public login(String title, BankAccountManagement _ui) {
		
		super(title);
		myUI = _ui;
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 298, 459);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 25, 262, 56);
		contentPane.add(lblNewLabel);
		
		JLabel lblWellcomeToBank = new JLabel("Wellcome to Bank Account Management");
		lblWellcomeToBank.setForeground(Color.WHITE);
		lblWellcomeToBank.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		lblWellcomeToBank.setHorizontalAlignment(SwingConstants.CENTER);
		lblWellcomeToBank.setBounds(10, 75, 262, 23);
		contentPane.add(lblWellcomeToBank);
		
		textField = new JTextField();
		textField.setBorder(null);
		textField.setBounds(31, 280, 193, 23);
		textField.setOpaque(false);
		textField.setForeground(Color.WHITE);
		textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		textField.setBackground(new Color(0, 0, 0, 0));
		textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		contentPane.add(textField);
		textField.setColumns(10);
	//	textField.getInputMap().setParent(null);
	/*	textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textField.getText().equals("")) {
					textField.setText("Username");
				}			
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(textField.getText().equals("Username")) {
					textField.setText(null);
				}
				else {
					textField.setCaretPosition(textField.getText().length());
				}
				
				
			}
		}); */
		
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setBounds(230, 280, 27, 23);
		Image imageusername = new ImageIcon(this.getClass().getResource("mainuser.png")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(imageusername));
		contentPane.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(29, 304, 226, 2);
		contentPane.add(separator);
		
		passwordField = new JPasswordField();
		passwordField.setBorder(null);
		passwordField.setBounds(31, 327, 193, 23);
		passwordField.setOpaque(false);
		passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
		passwordField.setBackground(new Color(0, 0, 0, 0));
		passwordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		passwordField.setForeground(Color.WHITE);
//		passwordField.getInputMap().setParent(null);
/*		passwordField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(passwordField.getText().equals("")) {
					passwordField.setText("Password");
				}				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				passwordField.setText(null);
				
			}
		}); 
		
		*/
		
		contentPane.add(passwordField);
		
		JLabel label = new JLabel("");
		label.setBounds(230, 327, 27, 23);
		Image imagepassword = new ImageIcon(this.getClass().getResource("password.png")).getImage();
		label.setIcon(new ImageIcon(imagepassword));
		contentPane.add(label);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(31, 350, 226, 2);
		contentPane.add(separator_1);
		
		btnLogin = new JButton("Sign In");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLogin.setBounds(31, 374, 104, 23);
		btnLogin.setOpaque(false);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.WHITE));
		btnLogin.addActionListener(this);
		contentPane.add(btnLogin);
		
		btnExit = new JButton("Exit");
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExit.setBounds(154, 374, 98, 23);
		btnExit.setOpaque(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.WHITE)); 
		btnExit.addActionListener(this);
		contentPane.add(btnExit);
		
		createaccount = new JLabel("Create a new account");
		createaccount.addMouseListener(this);
		createaccount.setHorizontalAlignment(SwingConstants.CENTER);
		createaccount.setForeground(Color.WHITE);
		createaccount.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
		createaccount.setBounds(10, 100, 262, 23);
		contentPane.add(createaccount);
		
		JLabel label_1 = new JLabel();
		Image imagebackground = new ImageIcon(this.getClass().getResource("background.jpg")).getImage();
		label_1.setIcon(new ImageIcon(imagebackground));
		label_1.setBounds(0, 0, 282, 420);
		contentPane.add(label_1);
			
	}
	
	public void getuser(String username, String password) {
		myUI.createaccount(username, password);
	}
	
	public void stop() {
		this.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == createaccount) {
			new createaccount(this);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogin) {
			String taikhoan = textField.getText();
			char [] mk = passwordField.getPassword();
			String matkhau = new String(mk);
		//	if(taikhoan.equals("sunshine") && matkhau.equals("neyu")) {
				myUI.start(taikhoan, matkhau);
				
		//	}
		}
		if(e.getSource() == btnExit) {
			this.dispose();
			}		
	}	
}


class history extends JFrame implements MouseListener{
		
	private JPanel contentPane;
	private JTable table;
	private JTextField idsearch;
	private JTextField namesearch;
	private JLabel realmoney;
	private JTextArea id;
	private JTextArea name;
	private JTextArea action;
	private JTextArea content;
	private JComboBox comboBox;
		
	String [] title = {"ID", "Name", "Action", "Content", "Time"};
	String [] choosesearchaction = {"","Add", "Deposit", "Withdraw", "Transfer", "Give"};
	
	Vector vtitle = new Vector();
	Vector datahis = new Vector();
	DefaultTableModel model;
	int selectview, selectmodel;
	
	public history() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		setBounds(100, 100, 706, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(102, 153, 255));
		panel.setBounds(0, 0, 693, 72);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("History Transaction");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		lblNewLabel.setBounds(0, 0, 607, 72);
		panel.add(lblNewLabel);
		
		for(int i = 0; i < title.length; i++) {
			vtitle.add(title[i]);
		}

		model = new DefaultTableModel(datahis, vtitle) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
		}
		};
		

		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setBackground(Color.WHITE);
		table.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		table.setSelectionBackground(new Color(0, 204, 204));
		table.setShowGrid(false);
		table.setBounds(0, 208, 693, 219);
		contentPane.add(table);
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				selectview = table.getSelectedRow();
				selectmodel = table.convertRowIndexToModel(selectview);
				Vector temp = (Vector) datahis.elementAt(selectmodel);
				id.setText(temp.elementAt(0).toString());
				name.setText(temp.elementAt(1).toString());
				action.setText(temp.elementAt(2).toString());
				content.setText(temp.elementAt(3).toString());
				
				super.mouseClicked(e);
			}
			
		});

		table.getModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(125);
		table.getColumnModel().getColumn(3).setPreferredWidth(180);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		
		
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(rowSorter);
    /*    rowSorter.setSortable(0, false);
        rowSorter.setSortable(1, false);
        rowSorter.setSortable(2, false);
        rowSorter.setSortable(3, false); */
        

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 208, 693, 219);
		scrollPane.setViewportBorder(null);
		contentPane.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(0, 71, 693, 138);
		contentPane.add(panel_1);
		
		JLabel label_1 = new JLabel("Name");
		label_1.setForeground(Color.GRAY);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(227, 11, 37, 20);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Id");
		label_2.setForeground(Color.GRAY);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(449, 11, 37, 20);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("Action");
		label_3.setForeground(Color.GRAY);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(226, 54, 73, 20);
		panel_1.add(label_3);
		
		JLabel label_4 = new JLabel("Content");
		label_4.setForeground(Color.GRAY);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(441, 54, 45, 20);
		panel_1.add(label_4);
		
		realmoney = new JLabel("");
		realmoney.setHorizontalAlignment(SwingConstants.CENTER);
		realmoney.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		realmoney.setBounds(369, 93, 117, 34);
		panel_1.add(realmoney);
		
		JLabel label_6 = new JLabel("");
		label_6.setBounds(178, 61, 37, 34);
		panel_1.add(label_6);
		
		name = new JTextArea();
		name.setFont(new Font("Monospaced", Font.PLAIN, 15));
		name.setForeground(Color.WHITE);
		name.setBackground(new Color(0, 153, 255));
		name.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		name.setBounds(274, 10, 130, 21);
		name.setEditable(false);
		panel_1.add(name);
		
		action = new JTextArea();
		action.setForeground(Color.WHITE);
		action.setFont(new Font("Monospaced", Font.PLAIN, 15));
		action.setEditable(false);
		action.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		action.setBackground(new Color(0, 153, 255));
		action.setBounds(297, 53, 130, 21);
		panel_1.add(action);
		
		content = new JTextArea();
		content.setForeground(Color.WHITE);
		content.setFont(new Font("Monospaced", Font.PLAIN, 15));
		content.setEditable(false);
		content.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		content.setBackground(new Color(0, 153, 255));
		content.setBounds(485, 53, 130, 21);
		panel_1.add(content);
		
		id = new JTextArea();
		id.setForeground(Color.WHITE);
		id.setFont(new Font("Monospaced", Font.PLAIN, 15));
		id.setEditable(false);
		id.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		id.setBackground(new Color(0, 153, 255));
		id.setBounds(475, 10, 103, 21);
		panel_1.add(id);
		
		comboBox = new JComboBox(choosesearchaction);
		comboBox.setBounds(10, 25, 124, 22);
		comboBox.setBackground(Color.WHITE);
		panel_1.add(comboBox);
		
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				try {
					if(comboBox.getSelectedItem().equals("")) {
						rowSorter.setRowFilter(null);
					}
					else {
						rowSorter.setRowFilter(RowFilter.regexFilter(comboBox.getSelectedItem().toString(), 2));
					}
				} catch (Exception e1) {
					new error("Error");
					e1.printStackTrace();
				}									
			}
		});	
			
		idsearch = new JTextField();
		idsearch.setBounds(10, 61, 124, 22);
		panel_1.add(idsearch);
		idsearch.setColumns(10);
		
		idsearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				rowSorter.setRowFilter(RowFilter.regexFilter(idsearch.getText(), 0));		
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				rowSorter.setRowFilter(RowFilter.regexFilter(idsearch.getText(), 0));			
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				new error("Error");		
			}
		});
				
		namesearch = new JTextField();
		namesearch.setColumns(10);
		namesearch.setBounds(10, 93, 124, 22);
		panel_1.add(namesearch);
		
		namesearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				rowSorter.setRowFilter(RowFilter.regexFilter(namesearch.getText(), 1));			
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				rowSorter.setRowFilter(RowFilter.regexFilter(namesearch.getText(), 1));			
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				new error("Error");	
			}
		});
		
		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setBounds(144, 58, 27, 27);
		panel_1.add(lblId);
		
		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(144, 93, 45, 27);
		panel_1.add(lblName);
	}
	
	public void givedata(Vector v) {
		datahis.add(v);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		selectview = table.getSelectedRow();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
		
}


class createaccount extends JFrame implements ActionListener{
	
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField passwordField;
	private JPasswordField passwordField_check;
	private JTextField checknumber_field;
	private JButton btnCreate;
	private JButton btnCancel;
	private JCheckBox checkaccept;
	
	login user;	
	
	int num;
	
	public createaccount(login user1) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		user = user1;
		this.setVisible(true);
		this.setResizable(false);
		setBounds(100, 100, 300, 304);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sign Up");
		lblNewLabel.setForeground(new Color(204, 153, 102));
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 264, 42);
		contentPane.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(10, 64, 90, 24);
		contentPane.add(lblUsername);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(102, 153, 153));
		panel.setBounds(10, 64, 90, 24);
		contentPane.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(102, 153, 153));
		panel_1.setBounds(10, 99, 90, 24);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblPassword.setBounds(0, 0, 90, 24);
		panel_1.add(lblPassword);
		
		username = new JTextField();
		username.setBounds(110, 64, 164, 24);
		contentPane.add(username);
		username.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(110, 99, 164, 24);
		contentPane.add(passwordField);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(102, 153, 153));
		panel_2.setBounds(10, 134, 90, 24);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblConfirmPw = new JLabel("Confirm PW");
		lblConfirmPw.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfirmPw.setForeground(Color.WHITE);
		lblConfirmPw.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblConfirmPw.setBounds(0, 0, 90, 24);
		panel_2.add(lblConfirmPw);
		
		passwordField_check = new JPasswordField();
		passwordField_check.setBounds(110, 134, 164, 24);
		contentPane.add(passwordField_check);
				
		Random rand = new Random();
		num =rand.nextInt(1000);
		
		JLabel numbercheck = new JLabel();
		numbercheck.setText(String.valueOf(num));
		numbercheck.setForeground(Color.WHITE);
		numbercheck.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		numbercheck.setHorizontalAlignment(SwingConstants.CENTER);
		numbercheck.setBounds(10, 169, 90, 24);
		contentPane.add(numbercheck);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(102, 153, 153));
		panel_3.setBounds(10, 169, 90, 24);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
				
		checknumber_field = new JTextField();
		checknumber_field.setColumns(10);
		checknumber_field.setBounds(110, 169, 164, 24);
		contentPane.add(checknumber_field);
		
		btnCreate = new JButton("Create");
		btnCreate.setForeground(Color.WHITE);
		btnCreate.setBackground(new Color(255, 153, 102));
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCreate.addActionListener(this);
	
		btnCreate.setBounds(38, 236, 89, 23);
		contentPane.add(btnCreate);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setBackground(new Color(255, 153, 102));
		btnCancel.setBounds(153, 236, 89, 23);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);
		
		checkaccept = new JCheckBox("Accept term");
		checkaccept.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		checkaccept.setBounds(81, 205, 115, 18);
		contentPane.add(checkaccept);
	}
	
	public void create() {
		
		try {
			char [] mk = passwordField.getPassword();
			String matkhau = new String(mk);
			char [] mkcheck = passwordField_check.getPassword();
			String matkhaucheck = new String(mkcheck);
			
//	System.out.print(String.valueOf(num));
			
			if(username.getText().equals("") || passwordField.getText().equals("")) {
				new error("Invalid information");
			}
			else if(!matkhau.equals(matkhaucheck)) {
				new error("Wrong type again password");
			}
			else if (!checknumber_field.getText().equals(String.valueOf(num))) {
				new error("Wrong check number");
			}
			else if(!checkaccept.isSelected()) {
				new error("You must accept the terms");
			}
			else {
				String taikoan = username.getText();
				user.getuser(taikoan, matkhau);		
				this.dispose();
			}
		} catch (Exception e) {
			new error("Error");
			e.printStackTrace();
		}		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCancel) {
			this.dispose();
		}
		
		if(e.getSource() == btnCreate) {
			create();
		}		
	}	
}


class aboutme extends JFrame {
	
	public aboutme () {
		
		this.setVisible(true);
		this.setResizable(false);
		JPanel contentPane;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 631, 442);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		Image imageavatar = new ImageIcon(this.getClass().getResource("avatar.png")).getImage();		
		lblNewLabel.setIcon(new ImageIcon(imageavatar));
		lblNewLabel.setBounds(0, 11, 605, 140);
		contentPane.add(lblNewLabel);
		
		JLabel lblAB = new JLabel("- About Me -");
		lblAB.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		lblAB.setHorizontalAlignment(SwingConstants.CENTER);
		lblAB.setBounds(10, 147, 615, 97);
		contentPane.add(lblAB);
		
		JLabel lblManagementBankAccount = new JLabel("Management Bank Account Sofware");
		lblManagementBankAccount.setFont(new Font("SketchFlow Print", Font.PLAIN, 18));
		lblManagementBankAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblManagementBankAccount.setBounds(10, 218, 595, 35);
		contentPane.add(lblManagementBankAccount);
		
		JTextArea txtrContactMe = new JTextArea();
		txtrContactMe.setText("Contact me");
		txtrContactMe.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtrContactMe.setBorder(null);
		txtrContactMe.setBounds(20, 330, 94, 22);
		contentPane.add(txtrContactMe);
		
		JTextArea txtrAsd = new JTextArea();
		txtrAsd.setBorder(null);
		txtrAsd.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtrAsd.setText("This is official verision\r\nThis project was written in Java Language, mostly Java Swing Library, \r\nused WindowBuilder to design GUI and SQL Server as database.\r\n\r\n");
		txtrAsd.setBounds(20, 267, 555, 63);
		contentPane.add(txtrAsd);
		
		JLabel fb = new JLabel("");
		fb.setHorizontalAlignment(SwingConstants.CENTER);
		Image imagefb = new ImageIcon(this.getClass().getResource("fb.png")).getImage();
		fb.setIcon(new ImageIcon(imagefb));
		fb.setBounds(45, 363, 42, 29);
		contentPane.add(fb);
		
		JLabel lblFb = new JLabel("fb.com/youaremysunshine2583");
		lblFb.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
		lblFb.setHorizontalAlignment(SwingConstants.CENTER);
		lblFb.setBounds(97, 362, 179, 30);
		contentPane.add(lblFb);
		
		JLabel mail = new JLabel("");
		mail.setHorizontalAlignment(SwingConstants.CENTER);
		Image imagemail = new ImageIcon(this.getClass().getResource("mail.png")).getImage();
		mail.setIcon(new ImageIcon(imagemail));
		mail.setBounds(320, 363, 42, 29);
		contentPane.add(mail);
		
		JLabel lblNguyenduckhaigmailcom = new JLabel("nguyenduckhai91@gmail.com");
		lblNguyenduckhaigmailcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblNguyenduckhaigmailcom.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
		lblNguyenduckhaigmailcom.setBounds(372, 363, 179, 30);
		contentPane.add(lblNguyenduckhaigmailcom);
	}
	
	
}

class bankaccount
{
	protected String id, name, dob, gender;
	protected double balance;
	
	bankaccount()
	{
		id = name = dob = gender = "";
		balance = 0.0;
	}
	
	bankaccount(String _id, String _name, double _balance, String _gender, String _dob )
	{
		this.id = _id;
		this.name = _name;
		this.balance = _balance;
		this.gender = _gender;
		this.dob = _dob;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDob() {
		return dob;
	}

	public String getGender() {
		return gender;
	}

	public double getBalance() {
		return balance;
	}	
	
	public String actiondeposit() {
		return "Deposit";
	}
	
	public String actionwithdraw() {
		return "withdraw";
	}
	
	public String actiontransfer() {
		return "Deposit";
	}
	
	public String actionadd() {
		return "Add";
	}
	
	public String contentdeposit(double money) {
		return "Deposit " + money; 
	}
	
	public String contentwithdraw(double money) {
		return "Withdraw" + money;
	}
	
	public String contenttransfer(double money, String idgiver) {
		return "Transfer " + money + " to " + idgiver;
	}
	
	public String contentadd(String idadd) {
		return "Added " +idadd;
	}
	
	public String timer() {
		DateFormat datefomart = new SimpleDateFormat("yyyy//MM//dd HH:mm:ss");
    	Calendar now = Calendar.getInstance();
    	return datefomart.format(now.getTime());
	}
	
}
