package com.ontariotechu.sofe3980U;


import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		float[] funk_1 = funky_function("model_1.csv");
		float[] funk_2 = funky_function("model_2.csv");
		float[] funk_3 = funky_function("model_3.csv");
		for(int i = 0; i < 3; i++) {
			float smallest = funk_1[i];
			String best_model = "model_1.csv";
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
			if(i == 0) {
				System.out.println("According to MSE, The best model is " + best_model);
			} else if(i == 1) {
				System.out.println("According to MAE, The best model is " + best_model);
			} else if(i == 2) {
				System.out.println("According to MARE, The best model is " + best_model);
			}
			
		}
    }

	static float[] funky_function(String file_name) {
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
		float sum_mse = 0.0f;
		float sum_mae = 0.0f;
		float sum_mare = 0.0f;
		float small_num = 0.000001f;
		for (String[] row : allData) { 
			float y_true=Float.parseFloat(row[0]);
			float y_predicted=Float.parseFloat(row[1]);
			// System.out.print(y_true + "  \t  "+y_predicted); 
			// System.out.println(); 
			sum_mse += (float) Math.pow(y_true - y_predicted, 2); // sum of squared differences between true and predicted
			sum_mae += Math.abs(y_true - y_predicted); // sum of absolute differences between true and predicted
			sum_mare += (Math.abs(y_true - y_predicted) / (Math.abs(y_true) + small_num)); // sum of absolute difference divided by true value to get average percentage
			count++;
		}
		float mean_squared_err = sum_mse / count;
		float mean_absolute_err = sum_mae / count;
		float mean_absolute_relative_err = (sum_mare / count) * 100;
		System.out.println("for " + file_name + "\n\tMSE =" + mean_squared_err + "\n\tMAE =" + mean_absolute_err + "\n\tMARE =" + mean_absolute_relative_err);
		float[] result = {mean_squared_err, mean_absolute_err, mean_absolute_relative_err};
		return result;
	}
}
