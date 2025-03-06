import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Simulation {

    Random random;

    int H, T;

    boolean serverIsAvailable = true;

    int L1, L2;

    Queue<Integer> Q;
    

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.runSimulation();
    }

    public void  runSimulation(){
        random = new Random();
        Q = new PriorityQueue<>();
        L1 = random.nextInt(20);
        L2 = random.nextInt(30);

        H = 501;
        System.out.println("---------------------------------------------------------------");
        System.out.println("| Event | Time | L1 | L2 | H | Server | Size | Queue|");
        System.out.println("---------------------------------------------------------------");
        while (true) {
            if( T >= 500 ){
                break;
            }else{
                
                if(L1 < L2 && L1 < H){
                    T = L1;
                    L1 = T + random.nextInt(20);

                    if(serverIsAvailable){
                        H = T + random.nextInt(10);
                        printTable("DALDAN");
                        serverIsAvailable = false;
                    }else{
                        Q.add(L1);
                    }
                }
                else if (L2 < L1 && L2 < H) {
                    T = L2;
                    L2 = T + random.nextInt(30);

                    if(serverIsAvailable){
                        H = T + random.nextInt(15);
                        printTable("DALDAN");
                        serverIsAvailable = false;
                    }else{
                        Q.add(L2);
                    }
                }else if (H < L2 && H < L1){

                    T = H;
                    serverIsAvailable = true;
                    H = 501;

                    if(Q.isEmpty()){
                        continue;
                    }else{
                        int temp = Q.poll();
                        if(temp == L1){
                            H = T + random.nextInt(10);
                            printTable("DALDAN");
                            serverIsAvailable = false;
                        }else if (temp == L2){
                            H = T + random.nextInt(15);
                            serverIsAvailable = false;
                            printTable("DALDAN");
                        }
                    }
                }
            }
        }
    }

    public void printTable(String event){
        System.out.printf("| %s | %d | | %d | | %d | | %d | | %b | | %d | | %s |\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q);
    }

}