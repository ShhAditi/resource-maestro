import java.util.Arrays;

public class Bankers {
    private static final int NUM_PROCESSES = 5;
    private static final int NUM_RESOURCES = 4;

    private static int[] available = {6, 7, 12, 12};
    private static int[][] max = {
        {0, 0, 1, 2},
        {2, 7, 5, 0},
        {6, 6, 5, 6},
        {4, 3, 5, 6},
        {0, 6, 5, 2}
    };
    private static int[][] allocation = {
        {0, 0, 1, 2},
        {2, 0, 0, 0},
        {0, 0, 3, 4},
        {2, 3, 5, 4},
        {0, 3, 3, 2}
    };
    private static int[][] need = new int[NUM_PROCESSES][NUM_RESOURCES];
    private static boolean[] finish = new boolean[NUM_PROCESSES];

    private static void calculateNeedMatrix() {
        for (int i = 0; i < NUM_PROCESSES; ++i) {
            for (int j = 0; j < NUM_RESOURCES; ++j) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    private static boolean isSafeState() {
        int[] work = Arrays.copyOf(available, NUM_RESOURCES);
        Arrays.fill(finish, false);
        int count = 0;
        while (count < NUM_PROCESSES) {
            boolean found = false;
            for (int i = 0; i < NUM_PROCESSES; ++i) {
                if (!finish[i]) {
                    boolean canAllocate = true;
                    for (int j = 0; j < NUM_RESOURCES; ++j) {
                        if (need[i][j] > work[j]) {
                            canAllocate = false;
                            break;
                        }
                    }
                    if (canAllocate) {
                        for (int j = 0; j < NUM_RESOURCES; ++j) {
                            work[j] += allocation[i][j];
                        }
                        finish[i] = true;
                        found = true;
                        count++;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private static void requestResources(int process, int[] request) {
        for (int i = 0; i < NUM_RESOURCES; ++i) {
            if (request[i] > need[process][i] || request[i] > available[i]) {
                System.out.println("Request cannot be granted immediately.");
                return;
            }
        }
        for (int i = 0; i < NUM_RESOURCES; ++i) {
            available[i] -= request[i];
            allocation[process][i] += request[i];
            need[process][i] -= request[i];
        }
        if (isSafeState()) {
            System.out.println("Request granted.");
        } else {
            for (int i = 0; i < NUM_RESOURCES; ++i) {
                available[i] += request[i];
                allocation[process][i] -= request[i];
                need[process][i] += request[i];
            }
            System.out.println("Request cannot be granted immediately.");
        }
    }

    public static void main(String[] args) {
        calculateNeedMatrix();
        System.out.println("Need Matrix:");
        for (int i = 0; i < NUM_PROCESSES; ++i) {
            for (int j = 0; j < NUM_RESOURCES; ++j) {
                System.out.print(need[i][j] + " ");
            }
            System.out.println();
        }
        if (isSafeState()) {
            System.out.println("System is in a safe state.");
            System.out.println("Safe Sequence:");
            for (int i = 0; i < NUM_PROCESSES; ++i) {
                System.out.print((i + 1) + " ");
            }
            System.out.println();
            int process = 2;
            int[] request = {0, 1, 0, 0};
            requestResources(process, request);
        } else {
            System.out.println("System is in an unsafe state.");
        }
    }
}