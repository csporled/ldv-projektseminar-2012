package de.regestanalyser.starter;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import classtypes.Regest;
import de.regestanalyser.database.DBManager;

@SuppressWarnings("serial")
public class Starter extends JFrame {
	// while coding adjust this to test functionality
	private static final String PATH_TO_REGEST_ANALYSER_FOLDER = "C:/Users/nils/Documents/Projects/Java/Projektseminar LDV 2012/Code/RegestAnalyser";

	// global variables
	private static ThreadPool threadPool;
	private static Starter frame;

	private static JPanel contentPane;
	private static JTextField binPathField;
	private static JTextField libPathField;
	private static JTextField dbPathField;
	private static JTextField regestField;
	private static JTextField lemmaField;
	private static JTextField fileField;

	private static JEditorPane textPane;
	private static JEditorPane outputPane;
	private static JScrollPane outputScrollPane;
	private static Document outputDoc;
	private static String dbName;
	private static DBManager dbManager;

	private static WaitingLayer waitingLayer;

	public static String appDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		threadPool = new ThreadPool();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Starter();
					frame.setVisible(true);

					setWaitingLayer();
				} catch (Exception e) {
					error(e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws BadLocationException
	 * @throws UnsupportedEncodingException
	 */
	public Starter() {
		// set path to application directory
		appDir = getClass().getProtectionDomain().getCodeSource().getLocation()
				.getPath();
		if (appDir.endsWith(".jar"))
			try {
				appDir = URLDecoder.decode(new File(appDir).getParent(),
						"utf-8");
			} catch (Exception e) {
				error(e);
			}
		else
			appDir = PATH_TO_REGEST_ANALYSER_FOLDER;

		// set layout
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
		btnGetLemma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lemmatizer();
			}
		});
		btnGetLemma.setBounds(10, 129, 202, 23);
		toolPanel.add(btnGetLemma);

		JLabel lblSyntaxtaggersyntaxparser = new JLabel(
				"SyntaxTagger/SyntaxParser:");
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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 227, 202, 150);
		toolPanel.add(scrollPane);

		textPane = new JEditorPane();
		scrollPane.setViewportView(textPane);

		JButton btnTagger = new JButton("tagger");
		btnTagger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tagger();
			}
		});
		btnTagger.setBounds(10, 388, 89, 23);
		toolPanel.add(btnTagger);

		JButton btnParser = new JButton("parser");
		btnParser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parser();
			}
		});
		btnParser.setBounds(109, 388, 89, 23);
		toolPanel.add(btnParser);

		JButton btnNewButton_2 = new JButton("...");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(appDir);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					fileField.setText(fc.getSelectedFile().getAbsolutePath());
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

		binPathField = new JTextField();
		binPathField.setBounds(10, 20, 147, 20);
		pathPanel.add(binPathField);
		binPathField.setColumns(10);

		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(appDir);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fc.showOpenDialog(Starter.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					binPathField
							.setText(fc.getSelectedFile().getAbsolutePath());
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
					libPathField
							.setText(fc.getSelectedFile().getAbsolutePath());
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
					libPathField
							.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button_1.setBounds(167, 113, 45, 23);
		pathPanel.add(button_1);

		outputPane = new JEditorPane();
		outputPane.setText("Welcome to Regest Analyser Tools Starter\n");
		outputPane.setToolTipText("Program's output pane");
		outputPane.setEditable(false);
		outputPane.setBounds(247, 11, 337, 450);

		outputScrollPane = new JScrollPane();
		outputScrollPane.setViewportBorder(new TitledBorder(null, "",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// outputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outputScrollPane.setBounds(241, 11, 343, 450);

		contentPane.add(outputScrollPane);
		outputScrollPane.setViewportView(outputPane);
		outputDoc = outputPane.getDocument();

		// set path fields
		// path to database
		if (appDir != null) {
			String[] databases = new File(appDir).list(new FilenameFilter() {

				@Override
				public boolean accept(File file, String name) {
					return name.toLowerCase().endsWith(".odb");
				}

			});
			// if .odb file exists in application directory, set it as database
			if (databases.length > 0) {
				dbName = databases[0];
				dbPathField.setText((appDir + "/" + dbName).replace("/",
						File.separator));
			}
		}

		// path to tools
		binPathField.setText((appDir != null ? appDir + "/bin/" : "bin/")
				.replace("/", File.separator));

		// path to libraries
		libPathField.setText((appDir != null ? appDir + "/lib/" : "lib/")
				.replace("/", File.separator));
	}

	public static void toggleWaitingLayer() {
		waitingLayer.setVisible(waitingLayer.isVisible() ? false : true);
	}
	
	public static void setWaitinLayerVisibility(boolean bool) {
		waitingLayer.setVisible(bool);
	}

	protected static void parser() {
		List<String> stdout = new ArrayList<String>();
		String command = null;

		if (!fileField.getText().isEmpty() && !textPane.getText().isEmpty()) {
			setOutput("Both a file AND Text have been set for parsing.\nPlease use only one.");
			return;
		} else if (!fileField.getText().isEmpty()) {
			// invoke SyntaxParser with file path from fileField
			command = String.format(
					"java -jar SyntaxParser.jar -log ir -logDir \"../log/SyntaxParser/\" -file \"%s\"",
					fileField.getText());
			setOutput("Parsing file(s):");
			threadPool.submit(command);
		} else if (!textPane.getText().isEmpty()) {
			// invoke SyntaxParser with Text from textPane as stdout
			for (String line : textPane.getText().split("\n")) {
				if (!line.isEmpty())
					stdout.add(line);
			}
			command = "java -jar SyntaxParser.jar -log ir -logDir \"../log/SyntaxParser/\" -stdin";
			setOutput(String.format("Parsing %d sentence(s):", stdout.size()));
			threadPool.submit(command, stdout);
		} else {
			setOutput("No text or file has been set for parsing.");
			return;
		}

		threadPool.finishTasks();
	}

	protected static void tagger() {
		List<String> stdout = new ArrayList<String>();

		if (!fileField.getText().isEmpty() && !textPane.getText().isEmpty()) {
			setOutput("Both a file AND Text have been set for tagging.\nPlease use only one.");
			return;
		} else if (!fileField.getText().isEmpty()) {
			// open file and save content as list of strings (line-wise)
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(
								fileField.getText())));
				String line;
				while ((line = reader.readLine()) != null) {
					if (!line.isEmpty())
						stdout.add(line);
				}
				reader.close();
			} catch (Exception e) {
				error(e);
			}
		} else if (!textPane.getText().isEmpty()) {
			// 
			for (String line : textPane.getText().split("\n")) {
				if (!line.isEmpty())
					stdout.add(line);
			}
		} else {
			setOutput("No text or file has been set for tagging.");
			return;
		}

		// one thread with multiple lines as stdout
		// threadPool.submit("java -jar SyntaxTagger.jar", stdout);

		// multiple threads with one line as stdout
		for (String line : stdout)
			threadPool.submit(new ExecutingCallable(
					"java -jar SyntaxTagger.jar", line));

		setOutput(String.format("Tagging %d sentence(s):", stdout.size()));
		threadPool.finishTasks();
	}

	protected static void lemmatizer() {
		String word = lemmaField.getText();

		if (word != null) {
			threadPool
					.submit("java -cp Lemmatizer.jar;../lib/baseform-complete-client.jar module.Lemmatizer "
							+ word);

			setOutput("Lemmatize word: " + word);
			threadPool.finishTasks();
		} else
			setOutput("No word has been set for lemmatizing.");
	}

	protected static void getRegest() {
		DBManager dbm = getDBManger();

		if (dbm != null) {
			String regest;
			if (!(regest = regestField.getText()).isEmpty()) {
				try {
					Regest r = dbm.getSingleResult(
							"SELECT r FROM Regest r WHERE r.id LIKE '" + regest
									+ "'", Regest.class);

					setOutput(r.getId() + ":");
					addOutput("Content:\n" + r.getContent());
				} catch (Exception e) {
					error(e);
				}
			} else
				setOutput("no regest for search is set.");
		}
	}

	protected static void getDatabaseInfo() {
		DBManager dbm = getDBManger();

		if (dbm != null) {
			try {
				List<Regest> regestList = dbm.getResultList(
						"SELECT r FROM Regest r", Regest.class);
				setOutput("count of 'Regest': " + regestList.size());

				for (Regest regest : regestList) {
					addOutput("");
					addOutput(regest.getId() + ":");
					for (String line : regest.getContent()) {
						addOutput(line);
					}
				}
			} catch (Exception e) {
				error(e);
			}
		}
	}

	protected static synchronized void addOutput(String string) {
		try {
			outputDoc.insertString(outputDoc.getLength(), string + "\n", null);
		} catch (Exception e) {
			error(e);
		}
	}

	protected static synchronized void setOutput(String string) {
		// outputPane.setText(string + "\n");
		addOutput("\n====================\n");
		addOutput(string);
	}

	protected static String getBinPath() {
		return binPathField.getText();
	}

	protected static void error(Exception e) {
		try {
			setOutput(e.getStackTrace().toString());
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void setWaitingLayer() {
		waitingLayer = new WaitingLayer();
		waitingLayer.setLayout(new BorderLayout());

		frame.getLayeredPane().add(waitingLayer, JLayeredPane.MODAL_LAYER);
		frame.addComponentListener(waitingLayer);

		// center label
		JLabel waitingLabel = new JLabel("Please wait...");
		waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// change text style within label
		Font currFont = waitingLabel.getFont();
		waitingLabel.setFont(new Font(currFont.getName(), Font.BOLD
				+ Font.ITALIC, 25));

		// add label to layer
		waitingLayer.add(BorderLayout.CENTER, waitingLabel);

		// set layer invisible at startup
		waitingLayer.setVisible(false);

		// add toggle button
		JButton toggleWaiting = new JButton("toggle waiting layer");
		toggleWaiting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggleWaitingLayer();
			}
		});
		waitingLayer.add(BorderLayout.NORTH, toggleWaiting);
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
			} else {
				setOutput("no database is set.");
				return null;
			}
		}
	}
}
