import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

    BufferedReader br;
    PrintWriter pw;
    Socket socket;

    private JLabel heading=new JLabel("CLIENT SPACE");
    Icon icon=new ImageIcon("logo1.png",getName());
    // private JLabel logo=new JLabel("logo1.png");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput =new JTextField();
    private Font headFont=new Font("Roboto",Font.CENTER_BASELINE,25);
    private Font font=new Font("Roboto",Font.PLAIN,20);

    
    public Client()
    {
        try {
            // System.out.println("sending request to server");
            socket=new Socket("119.226.236.129", 7777 );
            // System.out.println("connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            pw=new PrintWriter(socket.getOutputStream(), true);

            createGui();
            handleEvents();
            startReading();
            // startWriting();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10)
                {
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me : "+contentToSend +"\n");
                    pw.println(contentToSend);
                    pw.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    if(contentToSend.equalsIgnoreCase("exit"))
                    {
                        try {
                            socket.close();
                            messageInput.setEnabled(false);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        
                    }
                    
                }
            }
            
        });
    }


    private void createGui() {
        this.setTitle("Client Messenger[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //coding for component
        heading.setFont(headFont);
        messageArea.setFont(font);
        messageInput.setFont(font);
        

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        // this.add(Icon ,icon,BorderLayout.NORTH);

        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }


    private void startWriting() {
        //one thread is for writing data
        Runnable r2=()->{
            System.out.println("writer started");
            try {
                while(!socket.isClosed())
                {
                    
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    pw.println(content);
                    pw.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                    
                }
            } 
            catch (Exception e) 
            {
                // TODO: handle exception
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }


    private void startReading() {
        //one thread is for reading data
        Runnable r1=()->{
            System.out.println("reader started");
            try {
                while(true)
                {
                    
                    String msg=br.readLine();
                    if(msg.equalsIgnoreCase("exit"))
                    {
                        // System.out.p rintln("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat!!");
                        messageInput.setEnabled(false); 
                        socket.close();
                        break;
                    }
                    // System.out.println("Server: "+ msg);
                    messageArea.append("Server : "+msg+"\n");
                }

            } 
            catch (Exception e) 
            {
                // TODO: handle exception
                e.printStackTrace();
             }
        };
        new Thread(r1).start();
    }
    public static void main(String[] args) {
        System.out.println("this is client ...");
        new Client();
    }
}
