package sa.m;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class Simulation {

    Random random;
    
    double H, T;

    boolean serverIsAvailable = true;

    double L1, L2;

    // Queue<Integer> Q;
    Stack<Double> Q;
    

    public static void main(String[] args) throws InterruptedException {
        Simulation simulation = new Simulation();
        simulation.runSimulation();
    }

    public void  runSimulation() throws InterruptedException{
        random = new Random();
        // Q = new PriorityQueue<>();
        Q = new Stack<>();

        double work = 0;
        
        // L1 = random.nextInt(90)+1;
        // L2 = random.nextInt(90)+1;
        L1 =  getExponentialRandom(1.5);
        L2 =  getExponentialRandom(4);

        H = 499;
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println("| Event | Time | L1 | L2 | H | Server | Size | Queue| Process Time ");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        printTable("Start",0);
        while (true) {
            System.out.println("--------------------------------------------------------------------------------------------------");

            Thread.sleep(1000);
            if( T >= 500 ){
                break;
            }else{
                
                if(L1 < L2 && L1 < H){
                    T = L1;
                    L1 = T +  getExponentialRandom(1.5);

                    if(serverIsAvailable){
                        work = nextNormal(2, 0.3);
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
                    L2 = T +  getExponentialRandom(4);

                    if(serverIsAvailable){
                        work = nextNormal(2, 0.3);
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
                    if(Q.isEmpty()){
                        System.out.println("Empty QUEUE");
                        if (L2 < L1) {
                            T = L2;
                            L2 = T + getExponentialRandom(4);

                            if(serverIsAvailable){
                                work = nextNormal(2, 0.3);
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
                            L1 = T + getExponentialRandom(1.5);
        
                            if(serverIsAvailable){
                                work = nextNormal(2, 0.3);
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
                        double temp = Q.pop();
                        // if(temp == L1){
                            work = nextNormal(2, 0.3);
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

                    // Thread.sleep(1000);
                    T = L2;
                    L2 = T + getExponentialRandom(4);

                    if(serverIsAvailable){
                        work = nextNormal(2, 0.3);
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
                    L2 = T + getExponentialRandom(4);

                    if(serverIsAvailable){
                        work = nextNormal(2, 0.3);
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
                    L1 = T + getExponentialRandom(1.5);

                    if(serverIsAvailable){
                        work = nextNormal(2, 0.3);
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

    public void printTable(String event, double work){

        StringBuilder sb = new StringBuilder();
        Iterator<Double> iterator = Q.iterator();

        int i = 0;
        while (iterator.hasNext() && i <5) {
            Double element = iterator.next();
            sb.append(String.format("%.2f", element));
            i++;
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        String queue = sb.toString();

        if(event.contains("L1"))
        System.out.printf("| - %s | Time - %.2f | | L1 - %.2f | | L2 - -- | | H - %.2f | | available - %b | | size - %d | | %s | -- %.2f\n",event,T,L1,H,serverIsAvailable,Q.size(),queue,work);
        else if(event.contains("L2"))
        System.out.printf("| - %s | Time - %.2f | | L1 - -- | | L2 - %.2f | | H - %.2f | | available - %b | | size - %d | | %s | -- %.2f\n",event,T,L2,H,serverIsAvailable,Q.size(),queue,work);
        else
        System.out.printf("| - %s | Time - %.2f | | L1 - -- | | L2 - %.2f | | H - %.2f | | available - %b | | size - %d | | %s | -- %.2f\n",event,T,L2,H,serverIsAvailable,Q.size(),queue,work);

        // // System.out.printf("| %s | %d | | -- | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L2,H,serverIsAvailable,Q.size(),Q,work);

        // else
        // System.out.printf("| - %s | Time - %d | | L1 - %d | | L2 - %d | | H - %d | | available - %b | | size - %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.printf("| %s | %d | | %d | | %d | | %d | | %b | | %d | | %s | -- %d\n",event,T,L1,L2,H,serverIsAvailable,Q.size(),Q,work);
        // System.out.println(getExponentialRandom(1.5));
        // System.out.println(getExponentialRandom(4));
        // System.out.println(getNormalRandom(2, 0.3));
        // System.out.println(getNormalRandom(3, 0.5));
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
        return -(1/lambda)*Math.log(1-u);
        // return  (-Math.log(1-u) / lambda)*10;
    }



    /**
     * Generates a random number following a normal (Gaussian) distribution.
     * 
     * @param mean The mean (average) of the distribution
     * @param stdDev The standard deviation of the distribution
     * @return A random number from the normal distribution
     */
    // public double getNormalRandom(double mean, double stdDev) {
        
    // }

    public double nextNormal(double mean, double stdDev) {
        if (stdDev < 0.0) {
            throw new IllegalArgumentException("Standard deviation cannot be negative. Received: " + stdDev);
        }
        // If stdDev is 0, the distribution is just the mean value
        if (stdDev == 0.0) {
            return mean;
        }

        // Get a value from the standard normal distribution N(0, 1)
        double standardNormalValue = nextStandardNormal();

        // Transform the standard normal value to N(mean, stdDev)
        // X = mean + stdDev * Z
        return mean + stdDev * standardNormalValue;
    }



    private double nextStandardNormal() {
        // If we have a cached value from the previous calculation, return it
        // if (hasCachedValue) {
        //     hasCachedValue = false; // Mark cache as consumed
        //     return cachedValue;
        // }

        // --- Box-Muller transform ---
        // Need two independent uniform random numbers in [0,1)
        // We must ensure u1 is not 0 because log(0) is undefined.
        double u1 = 0.0;
        while (u1 == 0.0) {
            u1 = Math.random(); // Generate number in [0.0, 1.0)
        }
        double u2 = Math.random(); // Generate number in [0.0, 1.0)

        // Calculate intermediate values R and theta
        double R = Math.sqrt(-2.0 * Math.log(u1)); // ln is natural log
        double theta = 2.0 * Math.PI * u2;

        // Calculate the two standard normal random variables
        double z0 = R * Math.cos(theta);
        double z1 = R * Math.sin(theta);

        // Cache z1 for the next call and return z0
        // this.cachedValue = z1;
        // this.hasCachedValue = true;
        return z0;
    }

}
