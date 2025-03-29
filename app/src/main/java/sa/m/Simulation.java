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
        
        // L1 = random.nextInt(90)+1;
        // L2 = random.nextInt(90)+1;
        L1 = (int) getExponentialRandom(1.5);
        L2 = (int) getExponentialRandom(4);

        H = 499;
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println("| Event | Time | L1 | L2 | H | Server | Size | Queue| Process Time ");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        printTable("Start",0);
        while (true) {
            // System.out.println("dombal");
            System.out.println("--------------------------------------------------------------------------------------------------");

            Thread.sleep(1000);
            if( T >= 500 ){
                break;
            }else{
                
                if(L1 < L2 && L1 < H){
                    T = L1;
                    L1 = T + (int) getExponentialRandom(1.5);

                    if(serverIsAvailable){
                        work = (int) getNormalRandom(2, 0.3);
                        H = T + work;
                        printTable("L1 process",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L1);
                        printTable("L1 queued",0);
                        continue;
                    }
                }
                else if (L2 < L1 && L2 < H) {
                    T = L2;
                    L2 = T + (int) getExponentialRandom(4);

                    if(serverIsAvailable){
                        work = (int) getNormalRandom(2, 0.3);
                        H = T + work;
                        printTable("L2 process",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("L2 queued",0);
                        continue;
                    }
                }else if (H < L2 && H < L1){

                    T = H;
                    serverIsAvailable = true;
                    // H = 501;
                    // System.out.println("DOMABAASSADSA");
                    if(Q.isEmpty()){
                        System.out.println("Empty QUEUE");
                        if (L2 < L1) {
                            T = L2;
                            L2 = T + (int) getExponentialRandom(4);

                            if(serverIsAvailable){
                                work = (int) getNormalRandom(2, 0.3);
                                H = T + work;
                                printTable("L2 process",work);
                                serverIsAvailable = false;
                                continue;
                            }else{
                                Q.add(L2);
                                printTable("L2 queued",0);
                                continue;
                            }
                        }else{
                            T = L1;
                            L1 = T + (int) getExponentialRandom(1.5);
        
                            if(serverIsAvailable){
                                work = (int) getNormalRandom(2, 0.3);
                                H = T + work;
                                printTable("L1 process",work);
                                serverIsAvailable = false;
                                continue;
                            }else{
                                Q.add(L1);
                                printTable("L1 queued",0);
                                continue;
                            }          
                        }
                    }else{
                        int temp = Q.pop();
                        // if(temp == L1){
                            work = (int) getNormalRandom(2, 0.3);
                            H = T + work;
                            printTable("Taken FROM QUEUE",work);
                            serverIsAvailable = false;
                            continue;
                        // }else if (temp == L2){
                        //     work = random.nextInt(55);
                        //     H = T + work;
                        //     serverIsAvailable = false;
                        //     printTable("L2 Taken FROM QUEUE ",work);
                        //     continue;
                        // }
                    }
                }else if (L2 < H && L1 < H && L1==L2){

                    System.out.println("dombal");
                    // Thread.sleep(1000);
                    T = L2;
                    L2 = T + (int) getExponentialRandom(4);

                    if(serverIsAvailable){
                        work = (int) getNormalRandom(2, 0.3);
                        H = T + work;
                        printTable("EQUAL L2",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("EQUAL L2",0);
                        continue;
                    }                        
                }else if (L2 < L1 && L2 == H){
                    T = L2;
                    L2 = T + (int) getExponentialRandom(4);

                    System.out.println("dombal e");
                    if(serverIsAvailable){
                        work = (int) getNormalRandom(2, 0.3);
                        H = T + work;
                        printTable("L2 process",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L2);
                        printTable("L2 queued",0);
                        continue;
                    }                    
                }else if (L1 < L2 && L1 == H){


                    T = L1;
                    L1 = T + (int) getExponentialRandom(1.5);
                    System.out.println("dombal a");

                    if(serverIsAvailable){
                        work = (int) getNormalRandom(2, 0.3);
                        H = T + work;
                        printTable("L1 process",work);
                        serverIsAvailable = false;
                        continue;
                    }else{
                        Q.add(L1);
                        printTable("L1 queued",0);
                        continue;
                    }
                }
            }
        }
    }

    public void printTable(String event, int work){
        if(event.contains("L1"))
        System.out.printf("| - %s | Time - %d | | L1 - %d | | L2 - -- | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L1,H,serverIsAvailable,Q.size(),Q,work);
        else if(event.contains("L2"))
        System.out.printf("| - %s | Time - %d | | L1 - -- | | L2 - %d | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.printf("| %s | %d | | -- | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L2,H,serverIsAvailable,Q.size(),Q,work);

        else
        System.out.printf("| - %s | Time - %d | | L1 - %d | | L2 - %d | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.printf("| %s | %d | | %d | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);
        System.out.println(getExponentialRandom(1.5));
        System.out.println(getExponentialRandom(4));
        System.out.println(getNormalRandom(2, 0.3));
        System.out.println(getNormalRandom(3, 0.5));
    }
    /**
     * Generates a random number following an exponential distribution.
     * 
     * @param lambda The rate parameter of the exponential distribution.
     *               Higher values lead to smaller random numbers on average.
     * @return A random number from the exponential distribution.
     */
    public double getExponentialRandom(double lambda) {
        // Generate a random number between 0 and 1 (excluding 0)
        double u = 0;
        while (u == 0) {
            u = random.nextDouble();  // Avoid u=0 since ln(0) is undefined
        }
        
        // Apply exponential distribution formula: X = -ln(U)/λ
        return  -Math.log(1-u) / lambda;
    }

    /**
     * Generates a random number following a normal (Gaussian) distribution.
     * 
     * @param mean The mean (average) of the distribution
     * @param stdDev The standard deviation of the distribution
     * @return A random number from the normal distribution
     */
    public double getNormalRandom(double mean, double stdDev) {
        // Box-Muller transform implementation
        
        // Generate two independent uniform random numbers between 0 and 1
        // (excluding 0 for the first one)
        double u1 = 0;
        while (u1 == 0) {
            u1 = random.nextDouble();
        }
        double u2 = random.nextDouble();
        
        // Apply Box-Muller transform to get standard normal distribution
        // This generates a random number with mean 0 and standard deviation 1
        int z0 = (int) Math.round(Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2));
        
        // Scale and shift to get the desired mean and standard deviation
        return Math.max(1,mean + stdDev * z0);
    }

}
