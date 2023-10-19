I create an ETL pipeline to extract, transform, and load customer data from a CSV file into a database. This project simulates a common real-world scenario where need to process and store customer information.

Steps to Implement:

Data Extraction (E):
1. Write a Python script named "fake_customer_generater.py" that generates a synthetic customer dataset and saves it to a CSV file. (No need in real situation)
   ```ruby
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
  ```ruby
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


Data Transformation (T):
1. Perform data cleansing and transformation. Including tasks like handling missing values, standardizing formats, and generating derived features.
2. In the script "customer_data_loader.py", I add some code snippets to implement data cleansing like standardizing phone number formats and calculate customer's age.
   ```ruby
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
   ```ruby
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

Data Loading (L):
1. Set up a PostgreSQL database named CustomerInsightsDB using Mac terminal by following steps:  
  1.`brew install postgresql`  
  2.`brew services start postgresql`  
  3.`createdb CustomerInsightsDB`  
  4.`psql -d CustomerInsightsDB` (this command is to make sure the database is created and access to it)
2. Install psycopg2 library to interact with PostgreSQL from Python:  
`pip install psycopg2`
3. Write a Python script named "etl_to_postgres.py" to connect to the PostgreSQL database and insert the standardized data.
4. Create a table schema to store the customer data.
5. Write a Python script to insert the transformed data into the database.

Data Analysis and Visualization: