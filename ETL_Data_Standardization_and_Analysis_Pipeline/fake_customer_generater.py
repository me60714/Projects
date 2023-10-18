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
