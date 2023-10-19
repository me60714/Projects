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

# Print the first 5 customer records. (just to comfirm to data)
# for i in range(5):
#     print(f"Customer {i + 1}:")
#     print("CustomerID:", customer_data[i]["CustomerID"])
#     print("FirstName:", customer_data[i]["FirstName"])
#     print("LastName:", customer_data[i]["LastName"])
#     print("Email:", customer_data[i]["Email"])
#     print("PhoneNumber:", customer_data[i]["PhoneNumber"])
#     print("Birthday:", customer_data[i]["Birthday"])
#     print("Address:", customer_data[i]["Address"])
#     print("City:", customer_data[i]["City"])
#     print("State:", customer_data[i]["State"])
#     print("ZipCode:", customer_data[i]["ZipCode"])
#     print("Age:", customer_data[i]["Age"])
#     print()
