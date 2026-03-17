package com.ontariotechu.sofe3980U;


import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single-variable Binary Regression
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		double[] funk_1 = funky_function("model_1.csv");
		double[] funk_2 = funky_function("model_2.csv");
		double[] funk_3 = funky_function("model_3.csv");
		for(int i = 0; i < 6; i++) {
			String best_model = "model_1.csv";
			if(i == 0) {
				double smallest = funk_1[i];
				
				if(funk_1[i] < smallest) {
					best_model = "model_1.csv";
					smallest = funk_1[i];
				}
				if(funk_2[i] < smallest) {
					best_model = "model_2.csv";
					smallest = funk_2[i];
				}
				if(funk_3[i] < smallest) {
					best_model = "model_3.csv";
					smallest = funk_3[i];
				}
				System.out.println("According to BCE, The best model is " + best_model);
			} else {
				double largest = funk_1[i];
				if(funk_1[i] > largest) {
					best_model = "model_1.csv";
					largest = funk_1[i];
				}
				if(funk_2[i] > largest) {
					best_model = "model_2.csv";
					largest = funk_2[i];
				}
				if(funk_3[i] > largest) {
					best_model = "model_3.csv";
					largest = funk_3[i];
				}
				if(i == 1) {
					System.out.println("According to Accuracy, The best model is " + best_model);
				} else if(i == 2) {
					System.out.println("According to Precision, The best model is " + best_model);
				} else if(i == 3) {
					System.out.println("According to Recall, The best model is " + best_model);
				} else if(i == 4) {
					System.out.println("According to F1 score, The best model is " + best_model);
				} else if(i == 5) {
					System.out.println("According to AUC ROC, The best model is " + best_model);
				} 
			}
			
			
		}
	}
	static double[] funky_function(String file_name) {
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(file_name); 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			allData = csvReader.readAll();
			csvReader.close();
		}
		catch(Exception e){
			System.out.println( "Error reading the CSV file" );
			return null;
		}
		
		int count=0;
		int tp = 0;
		int fp = 0;
		int tn = 0;
		int fn = 0;
		int n_positive = 0;
		int n_negative = 0;
		double sum_bce = 0.0;
		for (String[] row : allData) { 
			int y_true=Integer.parseInt(row[0]);
			double y_predicted = Double.parseDouble(row[1]);
			// System.out.print(y_true + "  \t  "+y_predicted); 
			// System.out.println(); 

			sum_bce += (y_true * Math.log(1 - y_predicted)) + ((1 - y_true) * Math.log(y_predicted)); // sum for bce

			if(y_true == 1) n_positive++;
			else if(y_true == 0) n_negative++;

			int y_pred_label = (y_predicted >= 0.5) ? 1 : 0;

			if(y_pred_label == 1 && y_true == 1) tp++;
			else if(y_pred_label == 1 && y_true == 0) fp++;
			else if(y_pred_label == 0 && y_true == 0) tn++;
			else if(y_pred_label == 0 && y_true == 1) fn++;

			count++;
		}
		double binary_cross_entropy = (sum_bce / count) * -1.0;
		double accuracy = (double) (tp + tn) / (tp + tn + fn + fp);
		double precision = (double) tp / (tp + fp);
		double recall = (double) tp / (tp + fn);
		double f1_score = 2.0 * (precision * recall) / (precision + recall);
		
        int steps = 101; // range
        double[] tpr = new double[steps]; // each step has a tpr and fpr
        double[] fpr = new double[steps];

        for (int i = 0; i < steps; i++) {
            double th = i / 100.0;
            int tp_step = 0;
            int fp_step = 0;

            for (String[] row : allData) {
                int y_true = Integer.parseInt(row[0].trim());
                double y_pred = Double.parseDouble(row[1].trim());
                int y_pred_label = (y_pred >= th) ? 1 : 0;
                if (y_true == 1 && y_pred_label == 1) tp_step++;
                if (y_true == 0 && y_pred_label == 1) fp_step++;
            }

            tpr[i] = (double) tp_step / n_positive;
            fpr[i] = (double) fp_step / n_negative;
        }

        double auc = 0.0;
		for (int i = 1; i < steps; i++) {
			auc += (((tpr[i - 1] + tpr[i]) * (Math.abs(fpr[i-1] - fpr[i]))) / 2);
		}

		// print results
        System.out.println("for " + file_name);
        System.out.println("\tBCE =" + binary_cross_entropy);
        System.out.println("\tConfusion matrix");
        System.out.println("\t\t\ty=1\t\ty=0");
        System.out.println("\t\ty^=1\t" + tp + "\t\t" + fp);
        System.out.println("\t\ty^=0\t" + fn + "\t\t" + tn);
        System.out.println("\tAccuracy =" + accuracy);
        System.out.println("\tPrecision =" + precision);
        System.out.println("\tRecall =" + recall);
        System.out.println("\tf1 score =" + f1_score);
        System.out.println("\tauc roc =" + auc);

		double[] results = {binary_cross_entropy, accuracy, precision, recall, f1_score, auc};

		return results;
	}
}
