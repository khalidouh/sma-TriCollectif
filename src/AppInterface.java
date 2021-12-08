import com.univlyon1.sma.model.Environement;
import com.univlyon1.sma.model.View;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author khalid Ouhmaid
 */
public class AppInterface extends JFrame {
    Environement environement;
    public AppInterface(Environement environement){
        this.environement = environement;
        this.setTitle("khalid OUHMAID & Med Amine ASRI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        View view;
        JPanel pano=new JPanel();
        pano.setLayout(new GridBagLayout());

        view=new View(environement);
        environement.setView(view);
        pano.add(view);
        this.setContentPane(pano);
        this.pack();
    }
}
