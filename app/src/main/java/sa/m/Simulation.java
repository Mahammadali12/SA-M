package sa.m;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class Simulation {

    Random random;

    PrintWriter csvWriter;

    double H, T;

    boolean serverIsAvailable = true;

    double areaQueue = 0.0;
    double lastEventTime = 0.0;

    double totalIdleTime = 0.0;
    double idleStartTime = -1;
    final double simulationEndTime = 500;

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

        try {
            csvWriter = new PrintWriter("simulation_events_nums.csv");
            writeCSVHeader();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

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

            logEvent(null, work);
            
            System.out.println("--------------------------------------------------------------------------------------------------");

            // Thread.sleep(1000);
            if( T >= simulationEndTime ){

                areaQueue += Q.size() * (simulationEndTime - lastEventTime);

                if (idleStartTime != -1) {
                    totalIdleTime = totalIdleTime + (simulationEndTime - idleStartTime);
                }
                break;
            }
            
            double previousT = T;
            
            if(L1 < L2 && L1 < H){
                T = L1;
                L1 = T +  getExponentialRandom(1.5);

                areaQueue += Q.size() * (T - previousT);
                

                if(serverIsAvailable){
                    if (idleStartTime != -1) {
                        totalIdleTime += (T - idleStartTime);
                        idleStartTime = -1;
                    }
                    work = generateNormal(2, 0.3);
                    H = T + work;
                    printTable("L1 process",work);
                    logEvent("L1 Process", work);
                    serverIsAvailable = false;
                    continue;
                }else{
                    Q.add(L1);
                    printTable("L1 queued",0);
                    logEvent("L1 queued",0);
                    continue;
                }
            }
            else if (L2 < L1 && L2 < H) {
                T = L2;
                L2 = T +  getExponentialRandom(4);

                areaQueue += Q.size() * (T - previousT);

                if(serverIsAvailable){
                    if (idleStartTime != -1) {
                        totalIdleTime += (T-idleStartTime);
                        idleStartTime = -1;
                    }

                    work = generateNormal(2, 0.3);
                    H = T + work;
                    printTable("L2 process",work);
                    logEvent("L2 process",work);
                    serverIsAvailable = false;
                    continue;
                }else{
                    Q.add(L2);
                    printTable("L2 queued",0);
                    logEvent("L2 queued",0);
                    continue;
                }
            }else if (H < L2 && H < L1){
                T = H;
                areaQueue += Q.size() * (T - previousT);

                serverIsAvailable = true;

                if(Q.isEmpty()){
                    System.out.println("Empty QUEUE");
                    if (idleStartTime == -1) {
                        idleStartTime = T;
                    }
                    if (L2 < L1) {
                        T = L2;
                        L2 = T + getExponentialRandom(4);

                        if (idleStartTime == -1) {
                            idleStartTime = T;
                        }

                        if(serverIsAvailable){
                            work = generateNormal(2, 0.3);
                            H = T + work;
                            printTable("L2 process",work);
                            logEvent("L2 process",work);
                            serverIsAvailable = false;
                            continue;
                        }else{
                            Q.add(L2);
                            printTable("L2 queued",0);
                            logEvent("L2 queued",0);
                            continue;
                        }
                    }else{
                        T = L1;
                        L1 = T + getExponentialRandom(1.5);
                        
                        if (idleStartTime == -1) {
                            idleStartTime = T;
                        }
    
                        if(serverIsAvailable){
                            work = generateNormal(2, 0.3);
                            H = T + work;
                            printTable("L1 process",work);
                            logEvent("L1 process",work);

                            serverIsAvailable = false;
                            lastEventTime = T;

                            continue;
                        }else{
                            Q.add(L1);
                            printTable("L1 queued",0);
                            logEvent("L1 queued",0);
                            continue;
                        }          

                    }
                }else{
                    double temp = Q.pop();
                    // if(temp == L1){
                        work = generateNormal(2, 0.3);
                        H = T + work;
                        lastEventTime = T;
                        printTable("Taken FROM QUEUE",work);
                        logEvent("Taken FROM QUEUE",work);
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
                areaQueue += Q.size() * (T - previousT);

                if(serverIsAvailable){
                    if (idleStartTime != -1) {
                        totalIdleTime += (T - idleStartTime);
                        idleStartTime = -1;
                    }

                    work = generateNormal(2, 0.3);
                    H = T + work;
                    printTable("EQUAL L2",work);
                    logEvent("EQUAL L2",work);
                    serverIsAvailable = false;
                    continue;
                }else{
                    Q.add(L2);
                    printTable("EQUAL L2",0);
                    logEvent("EQUAL L2",0);
                    continue;
                }                        
            }else if (L2 < L1 && L2 == H){
                T = L2;
                L2 = T + getExponentialRandom(4);
                areaQueue += Q.size() * (T - previousT);

                if(serverIsAvailable){
                    if (idleStartTime != -1) {
                        totalIdleTime += (T-idleStartTime);
                        idleStartTime = -1;
                    }
                    work = generateNormal(2, 0.3);
                    H = T + work;
                    printTable("L2 process",work);
                    logEvent("L2 process",work);
                    serverIsAvailable = false;
                    continue;
                }else{
                    Q.add(L2);
                    printTable("L2 queued",0);
                    logEvent("L2 queued",0);
                    continue;
                }                    
            }else if (L1 < L2 && L1 == H){
                T = L1;
                L1 = T + getExponentialRandom(1.5);
                areaQueue += Q.size() * (T - previousT);

                if(serverIsAvailable){
                    if (idleStartTime != -1) {
                        totalIdleTime += (T-idleStartTime);
                        idleStartTime = -1;
                    }
                    work = generateNormal(2, 0.3);
                    H = T + work;
                    printTable("L1 process",work);
                    logEvent("L1 process",work);
                    serverIsAvailable = false;
                    continue;
                }else{
                    Q.add(L1);
                    printTable("L1 queued",0);
                    logEvent("L1 queued",0);
                    continue;
                }
            }

            lastEventTime = T;
            
        }

        double downtimeFactor = totalIdleTime / simulationEndTime;
        System.out.printf("Total Idle Time: %.7f minutes\n", totalIdleTime);
        System.out.printf("Downtime Factor: %.7f\n", downtimeFactor);

        double averageQueueLength = areaQueue/ simulationEndTime;
        System.out.printf("Total Queue Area: %.2f\n", areaQueue);
        System.out.printf("Average Queue Length: %.2f\n", averageQueueLength);

        // csvWriter.printf("Final Stats, , , , , , , ,\n");
        // csvWriter.printf("Total Idle Time,%.4f\n", totalIdleTime);
        // csvWriter.printf("Downtime Factor,%.4f\n", downtimeFactor);
        // csvWriter.printf("Total Queue Area,%.2f\n", areaQueue);
        // csvWriter.printf("Average Queue Length,%.2f\n", averageQueueLength);

        csvWriter.close();
    }

    public void writeCSVHeader(){
        // csvWriter.printf("Event,Time,L1,L2,H,Server Available,Queue Size,Process Time,Queue Contents\n");
        csvWriter.printf("L1,L2,H,Process Time\n");
    }

    public void logEvent(String event, double work) {
        StringBuilder sb = new StringBuilder();
        // Prepare CSV line (fields delimited by commas)
        // sb.append(event).append(",");
        // sb.append(String.format("%.2f", T)).append(",");
        sb.append(String.format("%.5f", getExponentialRandom(1.5))).append(",");
        sb.append(String.format("%.5f", getExponentialRandom(4))).append(",");
        // sb.append(String.format("%.2f", nextNormal(2, 0.3))).append(",");
        sb.append(String.format("%.5f", generateNormal(2, 0.3))).append(",");
        sb.append(String.format("%.5f", generateNormal(3, 0.5)));
        // sb.append(String.format("%.2f", nextNormal(3, 0.5))).append(",");
        // sb.append(String.format("%.2f", nextNormal(3, 0.5)));
        // sb.append(serverIsAvailable).append(",");
        // sb.append(Q.size()).append(",");
        // sb.append(String.format("%.2f", work)).append(",");

        // Append up to first 5 elements from the queue as a string
        StringBuilder queueContents = new StringBuilder();
        Iterator<Double> iterator = Q.iterator();
        int i = 0;
        while (iterator.hasNext() && i < 5) {
            queueContents.append(String.format("%.2f", iterator.next()));
            i++;
            if (iterator.hasNext() && i < 5) {
                queueContents.append(" | ");
            }
        }
        // sb.append("\"").append(queueContents.toString()).append("\"");
        
        // Write the line into the CSV
        csvWriter.println(sb.toString());
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
    
    public double getExponentialRandom(double lambda) {
        // Generate a random number between 0 and 1 (excluding 0)
        double u = 0;
        while (u == 0) {
            u = Math.random();  // Avoid u=0 since ln(0) is undefined
        }
        
        // Apply exponential distribution formula: X = -ln(U)/λ
        return -(1/lambda)*Math.log(1-u);
        // return  (-Math.log(1-u) / lambda)*10;
    }

    public double generateNormal(double mean, double stdDev) {
    // If standard deviation is 0, return the mean.
    if (stdDev == 0.0) {
        return mean;
    }

    // Generate two independent uniformly distributed numbers in (0, 1)
    double u1, u2;
    do {
        u1 = random.nextDouble();
    } while (u1 == 0.0);  // Ensure non-zero to avoid log(0).
    u2 = random.nextDouble();

    // Box-Muller transform:
    // Z0 = sqrt(-2 * ln(u1)) * cos(2 * PI * u2)
    double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);

    // Transform to N(mean, stdDev^2)
    return mean + stdDev * z0;
}

}
