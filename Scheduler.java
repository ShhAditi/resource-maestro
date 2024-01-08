import java.util.*;

class Process {
    char id;
    int arrivalTime;
    int burstTime;
    
    Process(){}

    Process(char id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
}

public class Scheduler {
    static void fcfs(Process[] processes) {
        int n = processes.length;
        int waitingTime = 0;
        int turnaroundTime = 0;
        System.out.println("Gantt Chart (FCFS) :");
        System.out.println(" Process\tStart Time\tEnd Time");
        for (int i = 0; i < n; i++) {
            System.out.printf(" %c\t\t%d\t\t%d\n", processes[i].id, waitingTime, waitingTime + processes[i].burstTime);
            turnaroundTime += waitingTime + processes[i].burstTime - processes[i].arrivalTime;
            waitingTime += processes[i].burstTime;
        }
        float avgTurnaroundTime = (float)turnaroundTime / n;
        float avgWaitingTime = (float)waitingTime / n;
        float avgResponseTime = avgWaitingTime; // For FCFS, response time is the same as waiting time
        System.out.printf("\nAverage Turnaround Time: %.2f ms\n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f ms\n", avgWaitingTime);
        System.out.printf("Average Response Time: %.2f ms\n", avgResponseTime);
    }

    static void roundRobin(Process[] processes, int timeQuantum) {
        int n = processes.length;
        int waitingTime = 0;
        int turnaroundTime = 0;
        int[] responseTime = new int[n];
        int[] reScheduleringBurstTime = new int[n];
        Arrays.fill(responseTime, -1); // Initialize response time to -1
        for (int i = 0; i < n; i++) {
            reScheduleringBurstTime[i] = processes[i].burstTime;
        }
        System.out.println("Gantt Chart (Round Robin):");
        System.out.println(" Time\t\tProcess");
        int time = 0;
        int completed = 0;
        while (completed < n) {
            for (int i = 0; i < n; i++) {
                if (reScheduleringBurstTime[i] > 0) {
                    if (responseTime[i] == -1) {
                        responseTime[i] = time;
                    }
                    int executeTime = Math.min(reScheduleringBurstTime[i], timeQuantum);
                    System.out.printf(" %d-%d\t\t%c\n", time, time + executeTime, processes[i].id);
                    reScheduleringBurstTime[i] -= executeTime;
                    time += executeTime;
                    if (reScheduleringBurstTime[i] == 0) {
                        completed++;
                        turnaroundTime += time - processes[i].arrivalTime;
                        waitingTime += time - processes[i].arrivalTime - processes[i].burstTime;
                    }
                }
            }
        }
        float avgTurnaroundTime = (float)turnaroundTime / n;
        float avgWaitingTime = (float)waitingTime / n;
        float avgResponseTime = 0;
        for (int i = 0; i < n; i++) {
            avgResponseTime += responseTime[i];
        }
        avgResponseTime /= n;
        System.out.printf("\nAverage Turnaround Time: %.2f ms\n", avgTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f ms\n", avgWaitingTime);
        System.out.printf("Average Response Time: %.2f ms\n", avgResponseTime);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Process[] processes = {
            new Process('1', 0, 10),
            new Process('2', 1, 1),
            new Process('3', 2, 2),
            new Process('4', 3, 1),
            new Process('5', 6, 5)
        };
        
        while(true){
            System.out.println("Enter a choice:\n1.FCFS\n2.Round Robin\n3.EXIT:");
            int choice = sc.nextInt(); // Replace with user input
            switch (choice) {
                case 1:
                    fcfs(processes);
                    break;
                case 2:
                    int timeQuantum = 4;
                    roundRobin(processes, timeQuantum);
                    break;
                case 3:
                    System.out.println("Exiting the program!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}