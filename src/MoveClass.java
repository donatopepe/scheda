
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author donato
 */
public class MoveClass extends Thread {
    
    private final Path source;
    private final Path destination;
    private final JTextArea textlog;

    @Override
    public void run() {
                            SchedaClass scheda = new SchedaClass(destination,textlog,Thread.interrupted());
                            
                            //scheda.destination = destination;

                            try {
                                Files.walkFileTree(source, scheda);
                                //this.jTextLog.add(scheda.log, this);
                                textlog.setText("!!!!THE END!!!!!\n"+scheda.log);
                                System.out.println("!!!!THE END!!!!!");
                            } catch (IOException e) {
                                //System.out.println("Exception: " + e);
                                textlog.setText("Exception: " + e);

                            }
        //System.out.println("Exception: " + e);

    }

    public MoveClass(Path source,Path destination, JTextArea textlog)
    {
        this.source=source;
        this.destination=destination;
        this.textlog=textlog;   
        
    }
    
    
    
}
