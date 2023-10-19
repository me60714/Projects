import csv
import re
from datetime import datetime
import psycopg2
import os
import logging

# Set environment variables for database credentials
os.environ["DB_USERNAME"] = "waynekao"
os.environ["DB_PASSWORD"] = "wayne2023"

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

# ...

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

