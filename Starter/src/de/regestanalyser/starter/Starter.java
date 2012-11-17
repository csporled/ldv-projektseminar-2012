package de.regestanalyser.starter;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import de.regestanalyser.classtypes.Regest;
import de.regestanalyser.database.DBManager;

@SuppressWarnings("serial")
public class Starter extends JFrame {

	private static JPanel contentPane;
	private static JTextField toolsPathField;
	private static JTextField libPathField;
	private static JTextField dbPathField;
	private static JTextField regestField;
	private static JTextField lemmaField;
	private static JTextField fileField;
	
	private static JEditorPane outputPane;
	private static Document outputDoc;
	private static String appDir;
	private static String dbName;
	private static DBManager dbManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Starter frame = new Starter();
					frame.setVisible(true);
				} catch (Exception e) {
					error(e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 * @throws UnsupportedEncodingException 
	 */
	public Starter() {
		// set path fields		
		appDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if (appDir.endsWith(".jar"))
			try {
				appDir = URLDecoder.decode(new File(appDir).getParent(), "utf-8");
			} catch (Exception e) {
				error(e);
			}
		else
			appDir = ("C:/Users/nils/Documents/Projects/Java/Projektseminar LDV 2012/Code/RegestAnalyser").replace("/", File.separator);
		
		setTitle("Regest Analyser Start Tool");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 227, 450);
		contentPane.add(tabbedPane);
		
		JPanel toolPanel = new JPanel();
		tabbedPane.addTab("Tools", null, toolPanel, null);
		toolPanel.setLayout(null);
		
		JButton btnDatabaseInfo = new JButton("database info");
		btnDatabaseInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Starter.getDatabaseInfo();
				} catch (Exception e) {
					error(e);
				}
			}
		});
		btnDatabaseInfo.setBounds(10, 11, 202, 23);
		toolPanel.add(btnDatabaseInfo);
		
		JButton btnNewButton = new JButton("get Regest");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Starter.getRegest();
			}
		});
		btnNewButton.setBounds(10, 70, 202, 23);
		toolPanel.add(btnNewButton);
		
		JLabel lblRegest = new JLabel("Regest:");
		lblRegest.setBounds(10, 45, 54, 14);
		toolPanel.add(lblRegest);
		
		regestField = new JTextField();
		regestField.setBounds(92, 45, 120, 20);
		toolPanel.add(regestField);
		regestField.setColumns(10);
		
		JLabel lblLemmatize = new JLabel("Lemmatize:");
		lblLemmatize.setBounds(10, 104, 72, 14);
		toolPanel.add(lblLemmatize);
		
		lemmaField = new JTextField();
		lemmaField.setBounds(92, 104, 120, 20);
		toolPanel.add(lemmaField);
		lemmaField.setColumns(10);
		
		JButton btnGetLemma = new JButton("get Lemma");
		btnGetLemma.setBounds(10, 129, 202, 23);
		toolPanel.add(btnGetLemma);
		
		JLabel lblSyntaxtaggersyntaxparser = new JLabel("SyntaxTagger/SyntaxParser:");
		lblSyntaxtaggersyntaxparser.setBounds(10, 163, 202, 14);
		toolPanel.add(lblSyntaxtaggersyntaxparser);
		
		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(10, 185, 54, 14);
		toolPanel.add(lblFile);
		
		fileField = new JTextField();
		fileField.setBounds(49, 182, 120, 20);
		toolPanel.add(fileField);
		fileField.setColumns(10);
		
		JLabel lblText = new JLabel("Text:");
		lblText.setBounds(10, 210, 202, 14);
		toolPanel.add(lblText);
		
		JEditorPane textPane = new JEditorPane();
		textPane.setBounds(10, 227, 202, 150);
		toolPanel.add(textPane);
		
		JButton btnTagger = new JButton("tagger");
		btnTagger.setBounds(10, 388, 89, 23);
		toolPanel.add(btnTagger);
		
		JButton btnParser = new JButton("parser");
		btnParser.setBounds(109, 388, 89, 23);
		toolPanel.add(btnParser);
		
		JButton btnNewButton_2 = new JButton("...");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(appDir);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					toolsPathField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnNewButton_2.setBounds(179, 181, 33, 23);
		toolPanel.add(btnNewButton_2);
		
		JPanel pathPanel = new JPanel();
		tabbedPane.addTab("Paths", null, pathPanel, null);
		pathPanel.setLayout(null);
		
		JLabel lblTest = new JLabel("Path to tools:");
		lblTest.setBounds(10, 5, 202, 14);
		pathPanel.add(lblTest);
		
		toolsPathField = new JTextField();
		toolsPathField.setBounds(10, 20, 147, 20);
		pathPanel.add(toolsPathField);
		toolsPathField.setColumns(10);
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(appDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					toolsPathField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button.setBounds(167, 19, 45, 23);
		pathPanel.add(button);
		
		JLabel lblPathToLibraries = new JLabel("Path to libraries:");
		lblPathToLibraries.setBounds(10, 51, 202, 14);
		pathPanel.add(lblPathToLibraries);
		
		libPathField = new JTextField();
		libPathField.setBounds(10, 66, 147, 20);
		libPathField.setColumns(10);
		pathPanel.add(libPathField);
		
		JButton btnNewButton_1 = new JButton("...");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(appDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					libPathField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnNewButton_1.setBounds(167, 65, 45, 23);
		pathPanel.add(btnNewButton_1);
		
		JLabel lblPathToDatabase = new JLabel("Path to database:");
		lblPathToDatabase.setBounds(10, 99, 202, 14);
		pathPanel.add(lblPathToDatabase);
		
		dbPathField = new JTextField();
		dbPathField.setBounds(10, 114, 147, 20);
		dbPathField.setColumns(10);
		pathPanel.add(dbPathField);
		
		JButton button_1 = new JButton("...");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(appDir);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					libPathField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button_1.setBounds(167, 113, 45, 23);
		pathPanel.add(button_1);
		
		outputPane = new JEditorPane();
		outputDoc = outputPane.getDocument();
		outputPane.setBounds(247, 11, 337, 450);
		contentPane.add(outputPane);
		
		setOutput(appDir);
		
		if (appDir != null) {
			String[] databases = new File(appDir).list(new FilenameFilter() {

				@Override
				public boolean accept(File file, String name) {
					return name.toLowerCase().endsWith(".odb");
				}
				
			});
			if (databases.length > 0) {
				dbName = databases[0];
				dbPathField.setText(appDir+File.separator+dbName);
			}
		}

		toolsPathField.setText((appDir != null?appDir+"/tools/":"tools/").replace("/", File.separator));
		libPathField.setText((appDir != null?appDir+"/lib/":"lib/").replace("/", File.separator));
	}

	protected static void getRegest() {
		DBManager dbm = getDBManger();
		
		if (dbm != null) {
			String regest;
			if(!(regest = regestField.getText()).isEmpty()) {
				try {
					Regest r = dbm.getSingleResult("SELECT r FROM Regest r WHERE r.id LIKE '"+regest+"'", Regest.class);

					setOutput("display regest: "+r.getId());
					addOutput("Content:\n"+r.getContent());
				} catch (Exception e) {
					error(e);
				}
			}
			else
				setOutput("no regest for search is set.");
		}
	}

	protected static void getDatabaseInfo() {
		DBManager dbm = getDBManger();
		
		if (dbm != null) {
			long regestCount;
			try {
				regestCount = (long) dbm.getSingleResult("SELECT COUNT(r) FROM Regest r");
				setOutput("count of 'Regest': "+regestCount);
			} catch (Exception e) {
				error(e);
			}
		}
	}

	private static DBManager getDBManger() {
		if (dbManager != null)
			return dbManager;
		else {
			String dbName;
			if (!(dbName = dbPathField.getText()).isEmpty()) {
				try {
					dbManager = new DBManager(dbName);
				} catch (Exception e) {
					error(e);
				}
				return dbManager;
			}
			else {
				setOutput("no database is set.");
				return null;
			}
		}
	}

	private static void addOutput(String string) {
		try {
			outputDoc.insertString(outputDoc.getLength(), string, null);
		} catch (Exception e) {
			error(e);
		}
	}
	
	private static void setOutput(String string) {
		outputPane.setText(string);
	}
	
	protected static void error(Exception e) {
		try {
			setOutput(e.getMessage());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
