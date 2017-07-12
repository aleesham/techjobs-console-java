package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();
        ArrayList<HashMap<String, String>> copyAllJobs = new
                ArrayList<>(allJobs);
        return copyAllJobs;
    }

    /*
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for (all lower case)
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();
        //force our search term value to ber lowercase, so we can force case-insensitivity;
        value = value.toLowerCase();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
            //We lowercase the values of the properties to force case insensitivity.
            if (aValue.toLowerCase().contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }
/*
     * Returns results of search the jobs data by using
     * inclusion of the search term.
     *
     * For example, searching for "web" will include results
     * with position type "Web - Front End" or name "Front end web dev."
     * Note that each job will only be included at the most one time.
     *
     * @param value Value of the field to search for (all lower case)
     * @return List of all jobs matching the criteria
     */

    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        // load data, if not already loaded
        loadData();
        //force our search term value to ber lowercase, so we can force case-insensitivity;
        value = value.toLowerCase();

        //create an empty ArrayList that we can add a job to provided one of the job properties contains the value (search term)
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        //here, we loop through all the jobs in the data so we can check one by one if a property contains the search term.
        for (HashMap<String, String> oneJob : allJobs) {
            //here, we loop through all the entries in the hashmap representing the job to see if the property contains the search term.
            for (Map.Entry<String, String> jobProps : oneJob.entrySet()) {
                //if we find the value in one of the properties, we add the job, then break out of the Map.Entry for-loop so we do not add the same thing
                // twice.
                //
                //Furthermore, we lowercase the values of the properties to force case insensitivity.
                if (jobProps.getValue().toLowerCase().contains(value)) {
                    jobs.add(oneJob);
                    break;
                }
                //if we do not find the search term, we do not add it; we do nothing and go to the next job in the arraylist.
            }
        }
        //When we finish looking through all the jobs in the data, we return the jobs that we find satisfy the condition.
        return jobs;
    }
    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
