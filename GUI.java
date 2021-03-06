package blayzer.logparser;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI extends JFrame {
	
	private BufferedReader StringFromFile;
	private FileInputStream SelectedFile;
	private String str;
	private String filename;
	
	JFileChooser fileopen = new JFileChooser();
	File file;
	
	public GUI() {
	super("MachineGuard Log Parser");
	setBounds(450, 250, 400, 400); //Добавить получение размера экрана и считать
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(3, 1));
	
	final JLabel label1 = new JLabel("Выберите лог-файл");
	label1.setHorizontalAlignment(SwingConstants.CENTER);
	panel.add(label1);
	
    JButton button1 = new JButton("Выбрать лог-файл");
    panel.add(button1);
    button1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
            int ret = fileopen.showDialog(null, "Открыть файл");                
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                try
                {
                	SelectedFile = new FileInputStream(new File(file.getPath()));
                	StringFromFile = new BufferedReader(new InputStreamReader(SelectedFile, "UTF-8"));
                }
                catch (IOException error)
                {
                	error.printStackTrace();
                }
                
                filename = file.getName();
                label1.setText("<html> Выбран файл: " + filename + "<br>" + "Нажмите: Преобразовать </html>");
            }
        }
    });
    
    JButton button2 = new JButton("Преобразовать");
    panel.add(button2);
    button2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	
        	String result = "";
        	
        	try {
				while ((str = StringFromFile.readLine()) != null){
					if(str.contains("Injected new Forge block")){
						str = str.replaceAll(".+(material)","-");
						str = str.replaceAll("(\\s\\with.*)","" );
						
						if (!str.isEmpty()){
						result += str+ "\n";
						//System.out.println(result);
						}
						try(FileWriter fw = new FileWriter(file.getPath() + ".mg.txt")) {
							fw.write(result);
						}
						catch(IOException error){
				             
				            System.out.println(error.getMessage());
				        }
					}
					
				}
				label1.setText("<html> Выбран файл: " + filename + "<br>" + "Готово. Создан файл " + filename + ".mg.txt. </html>");
			} catch (IOException error) {
				error.printStackTrace();
			}
        }
    });
	
    getContentPane().add(panel);
	}
	
	public static void main(String[] args){
		GUI app = new GUI();
		app.setVisible(true);
	}

}
