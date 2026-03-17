package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Multi-Class Classification
 */
public class App 
{
    public static void main( String[] args )
    {
        String filePath = "model.csv";
        FileReader filereader;
        List<String[]> allData;
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
            csvReader.close();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }

        final int NUM_CLASSES = 5;
        int count = 0;
        double sum_cce = 0.0;

        // confusion matrix: confMatrix[predicted][actual]
        int[][] confMatrix = new int[NUM_CLASSES][NUM_CLASSES];

        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0].trim()); // true value

            double[] y_predicted = new double[NUM_CLASSES];
            for (int i = 0; i < NUM_CLASSES; i++) {
                y_predicted[i] = Double.parseDouble(row[i + 1].trim());
            }

            // CCE: add log of the probability for the correct class
            sum_cce += Math.log(y_predicted[y_true - 1]);

            // predicted class = index of highest probability
            int y_pred_class = 0;
            for (int i = 1; i < NUM_CLASSES; i++) {
                if (y_predicted[i] > y_predicted[y_pred_class]) {
                    y_pred_class = i;
                }
            }

            // increment specific index
            confMatrix[y_pred_class][y_true - 1]++;

            count++;
        }

        double cce = -sum_cce / count;

        // print results
        System.out.println("for " + filePath);
        System.out.println("\tCCE =" + cce);

        System.out.println("\tConfusion Matrix");
        System.out.print("\t\t\t");
        for (int i = 1; i <= NUM_CLASSES; i++) System.out.print("y=" + i + "\t"); // actual axis
        System.out.println();
        for (int i = 0; i < NUM_CLASSES; i++) {
            System.out.print("\t\ty^=" + (i + 1) + "\t"); // predicted axis
            for (int j = 0; j < NUM_CLASSES; j++) {
                System.out.print(confMatrix[i][j] + "\t"); // numbers
            }
            System.out.println();
        }
    }
}