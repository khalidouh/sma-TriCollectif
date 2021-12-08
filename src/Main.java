import com.univlyon1.sma.model.Environement;
import com.univlyon1.sma.model.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Environement environement =new Environement(50,50, 100, 100,1);
       //Environement environement =new Environement(20,20, 10, 50,1);

        System.out.println(environement);
        //environement.pushLeft();
        AppInterface f1;
        f1= new AppInterface(environement);
        f1.setVisible(true);

        for(int i = 0; i<100000; i++){
            List<Integer> range= IntStream.range(0, environement.getAgentsList().size()).boxed().collect(Collectors.toList());

            ArrayList<Agent> agents = environement.getAgentsList();
            for(int j = 0; j< environement.getNumberOfAgents(); j++) {
                int alea = (int) (Math.random() * range.size());

                environement.getAgentsList().get(range.get(alea)).perception();
                environement.getAgentsList().get(range.get(alea)).action();

                range.remove(alea);
            }
            environement.notifyView();
            try {
                 //Thread.sleep(1000);
                Thread.sleep(20);
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
