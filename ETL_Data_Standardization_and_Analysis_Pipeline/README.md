I create an ETL pipeline to extract, transform, and load customer data from a CSV file into a database. This project simulates a common real-world scenario where need to process and store customer information.

Steps to Implement:

Data Extraction (E):
1. Write a Python script named "fake_customer_generater.py" that generates a synthetic customer dataset and saves it to a CSV file. (No need in real situation)
2. Write a Python script named "customer_data_loader.py" to read data from the CSV file.

Data Transformation (T):
1. Perform data cleansing and transformation. Including tasks like handling missing values, standardizing formats, and generating derived features.
2. In the script "customer_data_loader.py", I add some code snippets to implement data cleansing like standardizing phone number formats and calculate customer's age.

Data Loading (L):
1. Set up a PostgreSQL database named CustomerInsightsDB using Mac terminal by following steps:  
  1.`brew install postgresql`  
  2.`brew services start postgresql`  
  3.`createdb CustomerInsightsDB`  
  4.`psql -d CustomerInsightsDB` (this command is to make sure the database is created and access to it)  
3. Create a table schema to store the customer data.
4. Write a Python script to insert the transformed data into the database.

Data Analysis and Visualization:
