import java.io.*;
import java.util.Arrays;

public class LottoMAX {

    public static final int MAX_NUMBER_OF_LOTTO = 50;
    public static final int NUMBERS_OF_LOTTO = 7;

    public static void main(String args[]) throws IOException {

        System.out.println("LottoMAX -- QUICK PICK");
        System.out.print("How many sets you want：");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int set = Integer.parseInt(br.readLine());

        for(int i = 1; i <= set; i++) {                                 // Generate how many set of lotto
            int[] lotto = new int[MAX_NUMBER_OF_LOTTO];                 // building Lotto MAX array
            for (int j = 1; j < MAX_NUMBER_OF_LOTTO; j++)               // Set the array value as 1 - 50
                lotto[j] = j;

            int count = 0;                                              // To recode how many numbers been generated
            int[] result = new int[NUMBERS_OF_LOTTO];

            do {
                int random = (int)(Math.random() * MAX_NUMBER_OF_LOTTO);

                if(lotto[random] == 0)                                   // if the random number is 0. it means that it has already been used
                    continue;                                            // Therefore, re-run this loop again to generate a new random number
                else {
                    result[count] = random;

                    lotto[random] = 0;                                   // Set the number to 0 in case of duplicate using
                    count++;
                }
            } while (count < NUMBERS_OF_LOTTO);                         // Stop after generating 7 numbers

            Arrays.sort(result);                                        // sort the result array in ascending order

            for (int number : result){
                System.out.print(number + "\t");                        // Print all the numbers in one set
            }

            System.out.print('\n');                                     // Change line after generating every set
        }

        System.out.println("\nYou can't buy or claim a ticket if you are under 18!!!");
    }
}