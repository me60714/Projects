# ETL Data Standardization and Analysis Pipeline 
I create an ETL pipeline to extract, transform, and load customer data from a CSV file into a database. This project simulates a common real-world scenario where need to process and store customer information.

Futhermore, I can analysis these data by using SQL queries or even using data visualization tools or libraries in Python, such as Matplotlib, Seaborn, or Plotly, to create charts and graphs that help visualize the data.

Steps how I Implement:

## Data Extraction (E):
1. Write a Python script named "fake_customer_generater.py" that generates a synthetic customer dataset and saves it to a CSV file. (No need in real situation)
   ```python
   import csv
   import random
   import faker

   # Create a Faker instance to generate synthetic data
   fake = faker.Faker()

   # Number of customer records to generate
   num_records = 200

   # Define CSV file path
   csv_file = 'customer_data.csv'

   # Create and open the CSV file
   with open(csv_file, 'w', newline='') as file:
     writer = csv.writer(file)

   # Write the header row
     writer.writerow(["CustomerID", "FirstName", "LastName", "Email", "PhoneNumber", "Birthday", "Address", "City", "State", "ZipCode"])

   # Generate and write customer data
    for customer_id in range(1, num_records + 1):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = fake.email()
        phone_number = fake.phone_number()
        birthday = fake.date_of_birth(tzinfo=None, minimum_age=10, maximum_age=85)
        address = fake.street_address()
        city = fake.city()
        state = fake.state_abbr()
        zip_code = fake.zipcode()

        writer.writerow([customer_id, first_name, last_name, email, phone_number, birthday, address, city, state, zip_code])

   print(f"Generated {num_records} customer records and saved to {csv_file}")
   ```
2. Write a Python script named "customer_data_loader.py" to read data from the CSV file.
  ```python
  import csv

  # Define the input CSV file paths
  csv_file = 'customer_data.csv'

  # Initialize an empty list to store the customer data
  customer_data = []
        
  # Read data from the CSV file
  with open(csv_file, 'r', newline='') as file:
      reader = csv.DictReader(file)
      for row in reader:
          customer_data.append(row)

  # Now, customer_data is a list of dictionaries where each dictionary represents a customer record.

  # Create and open the output CSV file
  with open(output_csv_file, 'w', newline='') as file:
      fieldnames = ["CustomerID", "FirstName", "LastName", "Email", "PhoneNumber", "Birthday", "Address", "City", "State", "ZipCode", "Age"]
      writer = csv.DictWriter(file, fieldnames=fieldnames)
    
      # Write the header row
      writer.writeheader()
    
      # Write the standardized data to the output CSV file
      writer.writerows(customer_data)

  print(f"Standardized customer data saved to {output_csv_file}")

  # Print the first 5 customer records. (just to comfirm to data)
  for i in range(5):
      print(f"Customer {i + 1}:")
      print("CustomerID:", customer_data[i]["CustomerID"])
      print("FirstName:", customer_data[i]["FirstName"])
      print("LastName:", customer_data[i]["LastName"])
      print("Email:", customer_data[i]["Email"])
      print("PhoneNumber:", customer_data[i]["PhoneNumber"])
      print("Birthday:", customer_data[i]["Birthday"])
      print("Address:", customer_data[i]["Address"])
      print("City:", customer_data[i]["City"])
      print("State:", customer_data[i]["State"])
      print("ZipCode:", customer_data[i]["ZipCode"])
      print()
  ```

## Data Transformation (T):
1. Perform data cleansing and transformation. Including tasks like handling missing values, standardizing formats, and generating derived features.
2. In the script "customer_data_loader.py", I add some code snippets to implement data cleansing like standardizing phone number formats and calculate customer's age.
   ```python
       # Standardize the phone number format
        phone_number = row["PhoneNumber"]
        # Remove non-numeric characters
        phone_number = re.sub(r'\D', '', phone_number)
        phone_number = f'({phone_number[:3]}) {phone_number[3:6]}-{phone_number[6:]}'
        # Update the phone number in the row
        row["PhoneNumber"] = phone_number
        
        # Calculate the customer's age based on their birthday
        birthday = datetime.strptime(row["Birthday"], "%Y-%m-%d")
        today = datetime.today()
        # Add the "Age" column
        age = today.year - birthday.year - ((today.month, today.day) < (birthday.month, birthday.day))
        row["Age"] = age
   ```
   and after adding the snippets and modify the script, it becomes like:
   ```python
   import csv
   import re
   from datetime import datetime


   # Define the input and output CSV file paths
   csv_file = 'customer_data.csv'
   output_csv_file = 'standardized_customer_data.csv'

   # Initialize an empty list to store the customer data
   customer_data = []
        
   # Read data from the CSV file
   with open(csv_file, 'r', newline='') as file:
   reader = csv.DictReader(file)
   for row in reader:
        # Standardize the phone number format
        phone_number = row["PhoneNumber"]
        # Remove non-numeric characters
        phone_number = re.sub(r'\D', '', phone_number)
        phone_number = f'({phone_number[:3]}) {phone_number[3:6]}-{phone_number[6:]}'
        # Update the phone number in the row
        row["PhoneNumber"] = phone_number
        
        # Calculate the customer's age based on their birthday
        birthday = datetime.strptime(row["Birthday"], "%Y-%m-%d")
        today = datetime.today()
        # Add the "Age" column
        age = today.year - birthday.year - ((today.month, today.day) < (birthday.month, birthday.day))
        row["Age"] = age

        customer_data.append(row)

   # Now, customer_data is a list of dictionaries where each dictionary represents a customer record, standardized phone numbers and the age.

   # Create and open the output CSV file
   with open(output_csv_file, 'w', newline='') as file:
    fieldnames = ["CustomerID", "FirstName", "LastName", "Email", "PhoneNumber", "Birthday", "Address", "City", "State", "ZipCode", "Age"]
    writer = csv.DictWriter(file, fieldnames=fieldnames)
    
    # Write the header row
    writer.writeheader()
    
    # Write the standardized data to the output CSV file
    writer.writerows(customer_data)

   print(f"Standardized customer data saved to {output_csv_file}")
   ```

## Data Loading (L):
1. Set up a PostgreSQL database named CustomerInsightsDB using Mac terminal by following steps:  
  1.`brew install postgresql`  
  2.`brew services start postgresql`  
  3.`createdb CustomerInsightsDB`  
  4.`psql -d CustomerInsightsDB` (this command is to make sure the database is created and access to it)
2. Install psycopg2 library to interact with PostgreSQL from Python:  
`pip install psycopg2`
3. Write a Python script named "etl_to_postgres.py" to connect to the PostgreSQL database and insert the standardized data.
```python
import csv
import re
from datetime import datetime
import psycopg2

# Database connection parameters
db_params = {
    "dbname": "CustomerInsightsDB",
    "user": "yourusername",      # Replaced with the fake PostgreSQL username
    "password": "yourpassword",  # Replaced with the fake PostgreSQL password
    "host": "localhost",         # Use "localhost" to connect to the local PostgreSQL instance
}

# Define the input CSV file path
csv_file = 'standardized_customer_data.csv'

# Read data from the CSV file
with open(csv_file, 'r', newline='') as file:
    reader = csv.DictReader(file)
    customer_data = list(reader)

# Connect to the PostgreSQL database
conn = psycopg2.connect(**db_params)

# Create a cursor object
cur = conn.cursor()

# Insert data into the PostgreSQL database
for row in customer_data:
    cur.execute("""
        INSERT INTO customer_data (CustomerID, FirstName, LastName, Email, PhoneNumber, Birthday, Address, City, State, ZipCode, Age)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """, (
        row["CustomerID"], row["FirstName"], row["LastName"], row["Email"], row["PhoneNumber"],
        row["Birthday"], row["Address"], row["City"], row["State"], row["ZipCode"], row["Age"]
    ))

# Commit the changes and close the cursor and connection
conn.commit()
cur.close()
conn.close()

print(f"Data loaded into PostgreSQL database")
```
4. Base on the script above, I enhance its Security, Error Handling and Logging:  
   **Security:**  
   Storing sensitive information like database usernames and passwords in the script directly is not a good practice.
   Instead of hardcoding them in the script, it's better to use environment variables or a configuration file to store
   such information. This helps keep my credentials secure and separate from the code.  

   **Error Handling:**  
   It's a good practice to include error handling in the code. In a real-world scenario, things might not always go
   smoothly. Therefore, include try-except blocks to catch and handle exceptions that might occur during the database
   connection and data insertion processes is important.  

   **Logging:**  
   Consider implementing proper logging to record what's happening during the execution of my script. This can be
   helpful for debugging and monitoring.


   ```python
   import csv
   import re
   from datetime import datetime
   import psycopg2
   import os
   import logging

   # Set environment variables for database credentials
   os.environ["DB_USERNAME"] = "*********"  # Replace with the real username here
   os.environ["DB_PASSWORD"] = "*********"  # Replace with the real password here

   # Database connection parameters
   db_params = {
       "dbname": "CustomerInsightsDB",
       "user": os.environ.get("DB_USERNAME"),   
       "password": os.environ.get("DB_PASSWORD"),  
       "host": "localhost",      # Use "localhost" to connect to the local PostgreSQL instance
   }

   # Define the input CSV file path
   csv_file = 'standardized_customer_data.csv'

   # Configure logging
   logging.basicConfig(filename = 'database_insert.log', level = logging.INFO)

   try:
       # Read data from the CSV file
       with open(csv_file, 'r', newline='') as file:
           reader = csv.DictReader(file)
           customer_data = list(reader)

       # Connect to the PostgreSQL database
       conn = psycopg2.connect(**db_params)

       # Create a cursor object
       cur = conn.cursor()
   
       # Insert data into the PostgreSQL database
       for row in customer_data:
           cur.execute("""
               INSERT INTO customer_data (CustomerID, FirstName, LastName, Email, PhoneNumber, Birthday, Address, City, State, ZipCode, Age)
               VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """, (
               row["CustomerID"], row["FirstName"], row["LastName"], row["Email"], row["PhoneNumber"],
               row["Birthday"], row["Address"], row["City"], row["State"], row["ZipCode"], row["Age"]
           ))

       # Commit the changes and close the cursor and connection
       conn.commit()
       cur.close()
       conn.close()

       print(f"Data loaded into PostgreSQL database")
       logging.info("Data loaded into PostgreSQL database")

   except psycopg2.Error as e:
       logging.error(f"Database error: {e}")
       print(f"Error: {e}")
       
   except Exception as e:
       logging.error(f"An unexpected error occurred: {e}")
       print(f"An unexpected error occurred: {e}")
   ```
   
5. Create a table schema to store the customer data.
   Open the PostgreSQL shell by running the following command in the terminal:
   ```command
   psql -d CustomerInsightsDB
   ```
   Grant the necessary permissions to the username
   ```sql
   GRANT INSERT, SELECT ON TABLE customer_data TO *********;  # Replace with the real username here
   ```
   execute the SQL command to create the "customer_data" table:  
   ```sql
   CREATE TABLE customer_data (
    CustomerID SERIAL PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    Email VARCHAR(255),
    PhoneNumber VARCHAR(30),
    Birthday DATE,
    Address VARCHAR(255),
    City VARCHAR(255),
    State VARCHAR(2),
    ZipCode VARCHAR(10),
    Age INT
   );
   ```
6. Use the command `python etl_to_postgres.py` to load data into PosgreSQL database.

## Data Analysis:
