package sa.m;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Simulation {

    Random random;
    
    int H, T;

    boolean serverIsAvailable = true;

    int L1, L2;

    Queue<Integer> Q;
    

    public static void main(String[] args) throws InterruptedException {
        Simulation simulation = new Simulation();
        simulation.runSimulation();
    }

    public void  runSimulation() throws InterruptedException{
        random = new Random();
        Q = new PriorityQueue<>();
        L1 = random.nextInt(50)+1;
        L2 = random.nextInt(50)+1;

        H = 501;
        System.out.println("---------------------------------------------------------------");
        System.out.println("| Event | Time | L1 | L2 | H | Server | Size | Queue|");
        System.out.println("---------------------------------------------------------------");
        printTable("Start");
        while (true) {
            if( T >= 500 ){
                break;
            }else{
                
                if(L1 < L2 && L1 < H  && L1!=L2){
                    T = L1;
                    L1 = T + random.nextInt(50);

                    if(serverIsAvailable){
                        H = T + random.nextInt(50);
                        printTable("L1");
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L1);
                        printTable("L1");
                        continue;
                    }
                }
                else if (L2 < L1 && L2 < H && L1!=L2) {
                    T = L2;
                    L2 = T + random.nextInt(50);

                    if(serverIsAvailable){
                        H = T + random.nextInt(50);
                        printTable("L2");
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("L2");
                        continue;
                    }
                }else if (H < L2 && H < L1 && L1!=L2){

                    T = H;
                    serverIsAvailable = true;
                    H = 501;

                    if(Q.isEmpty()){
                        continue;
                    }else{
                        int temp = Q.poll();
                        if(temp == L1){
                            H = T + random.nextInt(50);
                            printTable("QUEUE L1");
                            serverIsAvailable = false;
                            continue;
                        }else if (temp == L2){
                            H = T + random.nextInt(55);
                            serverIsAvailable = false;
                            printTable("QUEUE L2");
                            continue;
                        }
                    }
                }else if (L2 < H && L1 < H && L1==L2){
                    try {
                        System.out.println("dombal");
                        // Thread.sleep(1000);
                        T = L2;
                        L2 = T + random.nextInt(50);

                        if(serverIsAvailable){
                            H = T + random.nextInt(50);
                            printTable("EQUAL L2");
                            serverIsAvailable = false;
                            continue;
                        }else{
                            Q.add(L2);
                            printTable("EQUAL L2");
                            continue;
                        }
                        
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    // System.out.println("SICDIN");
                }
            }
        }
    }

    public void printTable(String event){
        if(event.contains("L1"))
        System.out.printf("| %s | %d | | %d | | -- | | %d | | %b | | %d | | %s |\n",event,T,L1,H,serverIsAvailable,Q.size(),Q);
        else if(event.contains("L2"))
        System.out.printf("| %s | %d | | -- | | %d | | %d | | %b | | %d | | %s |\n",event,T,L2,H,serverIsAvailable,Q.size(),Q);
        else
        System.out.printf("| %s | %d | | %d | | %d | | %d | | %b | | %d | | %s |\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q);

    }

}