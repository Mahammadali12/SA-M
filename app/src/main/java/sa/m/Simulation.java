package sa.m;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Simulation {

    Random random;
    
    int H, T;

    boolean serverIsAvailable = true;

    int L1, L2;

    // Queue<Integer> Q;
    Stack<Integer> Q;
    

    public static void main(String[] args) throws InterruptedException {
        Simulation simulation = new Simulation();
        simulation.runSimulation();
    }

    public void  runSimulation() throws InterruptedException{
        random = new Random();
        // Q = new PriorityQueue<>();
        Q = new Stack<>();

        int work = 0;
        
        L1 = random.nextInt(50)+1;
        L2 = random.nextInt(50)+1;

        H = 499;
        System.out.println("---------------------------------------------------------------");
        System.out.println("| Event | Time | L1 | L2 | H | Server | Size | Queue| Process Time ");
        System.out.println("---------------------------------------------------------------");
        printTable("Start",0);
        while (true) {
            // System.out.println("dombal");
            System.out.println("---------------------------------------------------------------");
            Thread.sleep(1000);
            if( T >= 500 ){
                break;
            }else{
                
                if(L1 < L2 && L1 < H  && L1!=L2 && H < 500){
                    T = L1;
                    L1 = T + random.nextInt(50);

                    if(serverIsAvailable){
                        work = random.nextInt(50);
                        H = T + work;
                        printTable("L1",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L1);
                        printTable("L1",0);
                        continue;
                    }
                }
                else if (L2 < L1 && L2 < H && L1!=L2 && H < 500) {
                    T = L2;
                    L2 = T + random.nextInt(50);

                    if(serverIsAvailable){
                        work = random.nextInt(50);
                        H = T + work;
                        printTable("L2",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("L2",0);
                        continue;
                    }
                }else if (H < L2 && H < L1 && L1!=L2){

                    T = H;
                    serverIsAvailable = true;
                    H = 501;

                    if(Q.isEmpty()){
                        continue;
                    }else{
                        int temp = Q.pop();
                        if(temp == L1){
                            work = random.nextInt(50);
                            H = T + work;
                            printTable("QUEUE L1",work);
                            serverIsAvailable = false;
                            continue;
                        }else if (temp == L2){
                            work = random.nextInt(55);
                            H = T + work;
                            serverIsAvailable = false;
                            printTable("QUEUE L2",work);
                            continue;
                        }
                    }
                }else if (L2 < H && L1 < H && L1==L2 && H < 500){
                    System.out.println("dombal");
                    // Thread.sleep(1000);
                    T = L2;
                    L2 = T + random.nextInt(50);

                    if(serverIsAvailable){
                        work = random.nextInt(50);
                        H = T + work;
                        printTable("EQUAL L2",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("EQUAL L2",0);
                        continue;
                    }                        
                }
            }
        }
    }

    public void printTable(String event, int work){
        if(event.contains("L1"))
        System.out.printf("| Event- %s | Time - %d | | L1 - %d | | L2 - -- | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L1,H,serverIsAvailable,Q.size(),Q,work);
        else if(event.contains("L2"))
        System.out.printf("| Event- %s | Time - %d | | L1 - -- | | L2 - %d | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.printf("| %s | %d | | -- | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L2,H,serverIsAvailable,Q.size(),Q,work);

        else
        System.out.printf("| Event- %s | Time - %d | | L1 - %d | | L2 - %d | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.printf("| %s | %d | | %d | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);

    }

}