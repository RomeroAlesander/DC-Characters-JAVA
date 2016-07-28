import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thehowtotutorial.splashscreen.JSplash;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.sql.*;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;


public class main extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	boolean ImageSelected;
	DefaultListModel model = new DefaultListModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//Splash Screen displayed at the beggining of the app
				JSplash splash = new JSplash(main.class.getResource("splash.jpg"), true, true, false, "Tools For Travelers",Color.red);
				try
				{
				splash.splashOn();
				splash.setProgress(20, "Initializing...");
				Thread.sleep(2500);
				splash.setProgress(40, "Gathering Superheroes...");
				Thread.sleep(2500);
				splash.setProgress(60, "Gathering Villains...");
				Thread.sleep(2500);
				splash.setProgress(95, "Displaying Application");
				Thread.sleep(2000);
				splash.splashOff();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
		//Calling the main frame		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//Making a connection to the mySQL database
	static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/Superheroes";
		java.util.Properties prop = new java.util.Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		return DriverManager.getConnection(url, prop);
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public main() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 628, 629);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setForeground(Color.LIGHT_GRAY);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//Creating directory in C: to store the superhero pictures
		File Dir = new File("C:\\Superheroes");
		File DirPics = new File("C:\\Superheroes\\Pictures");

		if (!Dir.exists() || !DirPics.exists()) {
		    try{
		        Dir.mkdir();
		        DirPics.mkdir();
		    } 
		    catch(SecurityException se){
		    	JOptionPane.showMessageDialog(null, "Sorry, you don't have permissions to write in the C: drive");
		    }  
		}
		
		//Making the connection to the database
		Connection conn = null;
		try {
	
			String url = "jdbc:mysql://localhost:3306/Superheroes";
			String username = "root";
			String password = "root";
			
			
			conn = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
		
		System.out.println(conn);
		
		String path = "C://Superheroes";
        ArrayList<String> files = new ArrayList<String>();
        
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
            
                files.add(listOfFiles[i].getName()); 
            }
        }
		Statement statement = conn.createStatement();
		
		//Creating an arraylist with the result of the SELECT statement to generate the Jlist
		ArrayList<String> names = new ArrayList<String>();
	    try {
	       
	        ResultSet result = statement.executeQuery("SELECT name FROM superheroes");
	        while (result.next()) {
	        	model.addElement(result.getString(1));
	        }
	    }
	    catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	    
		JList lstChar = new JList(model);
		lstChar.setBackground(Color.GRAY);
		lstChar.setValueIsAdjusting(true);
		lstChar.setBounds(10, 88, 306, 82);
		//contentPane.add(lstChar);
	
        JScrollPane scrollpane;
        scrollpane = new JScrollPane(lstChar);
        scrollpane.setBounds(10, 88, 306, 82);
        contentPane.add(scrollpane);
        scrollpane.setVisible(true);
        
        
		
		JLabel lblPicture = new JLabel("");
		lblPicture.setIcon(null);
		lblPicture.setBounds(10, 181, 306, 398);
		contentPane.add(lblPicture);
		
		JLabel lblName = new JLabel("Choose a Supherhero or create one");
		lblName.setVerticalAlignment(SwingConstants.TOP);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblName.setBounds(344, 83, 258, 61);
		contentPane.add(lblName);
		

		
		
		JLabel lblDescription = new JLabel("");
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setVerticalAlignment(SwingConstants.TOP);
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDescription.setBackground(Color.LIGHT_GRAY);
		lblDescription.setBounds(344, 338, 247, 9);
		contentPane.add(lblDescription);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setBounds(344, 369, 68, 23);
		contentPane.add(lblName_1);
		
		txtName = new JTextField();
		txtName.setBounds(344, 397, 258, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblDescription_1 = new JLabel("Description:");
		lblDescription_1.setBounds(344, 423, 72, 23);
		contentPane.add(lblDescription_1);
		
		JButton txtSave = new JButton("Save");
		txtSave.setBounds(344, 556, 258, 23);
		contentPane.add(txtSave);
		
		JTextPane txtDescription = new JTextPane();
		txtDescription.setBounds(344, 454, 258, 50);
		contentPane.add(txtDescription);
		
		JButton btnRefresh = new JButton("Refresh List");
		btnRefresh.setBounds(10, 45, 160, 23);
		contentPane.add(btnRefresh);
		
		JButton btnAddPic = new JButton("Add Picture");
		btnAddPic.setBounds(344, 522, 258, 23);
		contentPane.add(btnAddPic);
		
        JLabel lblWelcome = new JLabel("Welcome to the DC Characters Encyclopedia");
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblWelcome.setBounds(152, 11, 309, 27);
        contentPane.add(lblWelcome);
		
		
		//Set focus on the first item of the List
        lstChar.setSelectedIndex(0);
		
		String selected = lstChar.getSelectedValue().toString();
	  	lblName.setText(selected);
	  	String picName = lblName.getText() + ".jpeg";
        
	  	//Set the picture according to the selected item on the list
        ImageIcon myPic = new ImageIcon("C:\\Superheroes\\Pictures\\" + picName);
        Image image = myPic.getImage(); 
        Image newimg = image.getScaledInstance(254, 396,  java.awt.Image.SCALE_SMOOTH);
        myPic = new ImageIcon(newimg);
        lblPicture.setIcon(myPic);
        
        //Add localization for English and Spanish
        JRadioButton rdbtnEnglish = new JRadioButton("English");
        rdbtnEnglish.setBackground(Color.GRAY);
        rdbtnEnglish.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		btnRefresh.setText("Refresh List");
        		lblName_1.setText("Name");
        		lblDescription_1.setText("Description");
        		btnAddPic.setText("Add Picture");
        		txtSave.setText("Save");
        		lblWelcome.setText("Welcome to the DC Characters Encyclopedia");
        	}
        });
        rdbtnEnglish.setBounds(222, 45, 68, 23);
        contentPane.add(rdbtnEnglish);
        
        //Add Localization for English and Spanish
        JRadioButton rdbtnSpanish = new JRadioButton("Spanish");
        rdbtnSpanish.setBackground(Color.GRAY);
        rdbtnSpanish.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		btnRefresh.setText("Refrescar la lista");
        		lblName_1.setText("Nombre");
        		lblDescription_1.setText("Descripcion");
        		btnAddPic.setText("Añadir Foto");
        		txtSave.setText("Guardar");
        		lblWelcome.setText("Bienvenido a la Enciclopedia DC");
        	}
        });
        rdbtnSpanish.setBounds(292, 45, 109, 23);
        contentPane.add(rdbtnSpanish);
        
        ButtonGroup language = new ButtonGroup();
        language.add(rdbtnEnglish);
        language.add(rdbtnSpanish);
        
        JTextArea txtDesc = new JTextArea();
        txtDesc.setBackground(Color.GRAY);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setFont(new Font("Monospaced", Font.BOLD, 14));
        txtDesc.setRows(20);
        txtDesc.setEditable(false);
        txtDesc.setBounds(344, 127, 258, 220);
        contentPane.add(txtDesc);

        //Set the description text based on the result of the SELECT statement from mySQL
        try {
			ResultSet description = statement.executeQuery("Select description from superheroes where name = '" + selected + "'" );
			if (description.next()){
				String finalDescription = description.getString(1);
				txtDesc.setText(finalDescription);
			}	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		//Add button click. Used to add new characters to the database and add the picture to the C: folder
        //specified at the top
		btnAddPic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!txtName.getText().equals("") || !txtDescription.getText().equals("")){
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(btnAddPic);
					 if (returnVal == JFileChooser.APPROVE_OPTION) {
						 
							 File file = fc.getSelectedFile();
						        String filePath = file.getAbsolutePath();
						        try {
						        	
						        	File target = new File("C:\\Superheroes\\Pictures", txtName.getText() + ".jpeg");
						            Files.copy(file.toPath(),target.toPath());
						            ImageSelected = true;
						        } catch (IOException ex) {
						            
						        }
				}	
				}
				else
					//More localization
					if (rdbtnSpanish.isSelected())
						{
						JOptionPane.showMessageDialog(null, "Introduce el nombre y la descripcion primero");
						}
					else
					{
						JOptionPane.showMessageDialog(null, "Enter name and description first");
					}
				} });
		
		//Refresh button to refresh the list when new characters are added to the database
		btnRefresh.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				
				model.clear();
				
				Connection conn = null;
				try {
			
					String url = "jdbc:mysql://localhost:3306/Superheroes";
					String username = "root";
					String password = "root";
					
					
					conn = DriverManager.getConnection(url, username, password);
				}
				catch (SQLException e1) {
					System.out.println(e1);
				}
				
				Statement statement = null;
				try {
					statement = conn.createStatement();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				
				ArrayList<String> names = new ArrayList<String>();
			    try {
			       
			        ResultSet result = statement.executeQuery("SELECT name FROM superheroes");
			        while (result.next()) {
			            //names.add(result.getString(1));
			        	model.addElement(result.getString(1));
			        }
			    }
			    catch (SQLException e1) {
			        System.out.println(e1.getMessage());
			    }
			
			}
			
		});
		
		//When a new character and its picture are selected, this button makes a connection to the database 
		//and inserts the new information to be displayed later
		txtSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				if(txtName.getText().equals("") || txtDescription.getText().equals("") || ImageSelected == false){
					if (rdbtnSpanish.isSelected())
					{
						JOptionPane.showMessageDialog(null, "Por favor introduce informacion o selecciona una foto");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please enter some information or select a picture");
					}
				}
				else{
					
					try(Connection conn = getConnection();
							Statement statement = conn.createStatement()) {

							int ret = statement.executeUpdate("INSERT INTO Superheroes (name, description) VALUES ('" + txtName.getText() + "','" + txtDescription.getText() + "')");
															  
							ImageSelected = false;
							System.out.println(ret);
							txtName.setText("");
							txtDescription.setText("");
						}
						catch(SQLException e1) {
							System.out.println(e1);
						}
					
				}
			}
		});
		
		//Code used to copy the picture file from its original location to the destination
		class CopyFile {
		public CopyFile(String srFile, String dtFile) {

	        try {
	            File f1 = new File(srFile);
	            File f2 = new File(dtFile);
	            InputStream in = new FileInputStream(f1);

	            OutputStream out = new FileOutputStream(f2);

	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            in.close();
	            out.close();
	            System.out.println("File copied.");
	        } catch (FileNotFoundException ex) {
	            System.out.println(ex.getMessage() + " in the specified directory.");
	            System.exit(0);
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    }};
		
	    //When a new item is clicked on the list, the picture, name, and description are updated in real time
		lstChar.addListSelectionListener(new ListSelectionListener()
		{
		  public void valueChanged(ListSelectionEvent ev)
		  {
			  	String selected = lstChar.getSelectedValue().toString();
			  	lblName.setText(selected);
			  	String picName = lblName.getText() + ".jpeg";
	            
	            ImageIcon myPic = new ImageIcon("C:\\Superheroes\\Pictures\\" + picName);
	            Image image = myPic.getImage(); 
	            Image newimg = image.getScaledInstance(254, 396,  java.awt.Image.SCALE_SMOOTH);
	            myPic = new ImageIcon(newimg);
	            lblPicture.setIcon(myPic);
	            
	          
	    
			
				try {
					ResultSet description = statement.executeQuery("Select description from superheroes where name = '" + selected + "'" );
					if (description.next()){
						String finalDescription = description.getString(1);
						txtDesc.setText(finalDescription);
					}
						
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  } 
		});
		
		

}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
