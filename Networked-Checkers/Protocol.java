import java.net.*;
import java.io.*;

public class Protocol {
    private static final int WAITING = 0;
    private static final int IN_GAME = 1;
    private static final int DRAW = 50;
    
    private int state = WAITING;
    private int currentCount = 0;

    public String processInput(String theInput) {
        String theOutput = null;
       
        if(state == WAITING)
        {
            theOutput = "Please provide you name:";
            state = IN_GAME;
        }
        else if (state == IN_GAME)
        {
            if (theInput.equalsIgnoreCase("bye")) 
            {
                theOutput = "Bye.";
                state = WAITING;
            }
            else
            {
                if (currentCount == (DRAW))
                {
                    theOutput = "Bye.";
                    state = WAITING;
                }
                else
                {
                    currentCount++;
                
                    state = IN_GAME;
                }
            }
        }
        
        return theOutput;
    }
}
