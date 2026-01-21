import numpy as np
import pandas as pd

# Parameters
n_rows = 10_000_000

# 1. ATM Card Numbers
# BIN codes for major Vietnamese banks
bins = [
    '970436', # Vietcombank
    '970415', # VietinBank
    '970418', # BIDV
    '970405', # Agribank
    '970407', # Techcombank
    '970403', # Sacombank
    '970416', # ACB
    '970422', # MBBank
    '970432', # VPBank
    '970406', # DongA Bank
    '970431', # Eximbank
    '970423', # TPBank
    '970443', # SHB
    '970426', # MSB
    '970441', # VIB
    '970437'  # HDBank
]

# Generate random BIN choices
random_bins = np.random.choice(bins, n_rows)

# Generate remaining 10 digits for the 16-digit card number
# We generate them as strings to keep leading zeros if any (though usually not issue for mid-string)
# Using randint for the suffix
random_suffixes = np.random.randint(0, 1000000000, n_rows) # 9 digits? Wait. 16 - 6 = 10 digits needed.
# Max of 10 digits is 9,999,999,999.
# Let's generate two parts to ensure 10 digits including leading zeros or just format.
# Better way: Generate 10 digits as a string.
# Vectorized approach:
# We need 10 random digits.
suffix_part1 = np.random.randint(0, 100000, n_rows) # 5 digits
suffix_part2 = np.random.randint(0, 100000, n_rows) # 5 digits
# Format to string with padding
suffixes = [f"{p1:05d}{p2:05d}" for p1, p2 in zip(suffix_part1, suffix_part2)]

card_numbers = [f"{b}{s}" for b, s in zip(random_bins, suffixes)]

# 2. PINs
# 6 digits
pins = np.random.randint(0, 1000000, n_rows)
pins_str = [f"{p:06d}" for p in pins]

# 3. Balances
# Range 50,000 to 500,000,000
balances = np.random.randint(50000, 500000000, n_rows)

# 4. Names
surnames = ["Nguyen", "Tran", "Le", "Pham", "Hoang", "Huynh", "Phan", "Vu", "Vo", "Dang", "Bui", "Do", "Ho", "Ngo", "Duong", "Ly"]
middle_names = ["Van", "Thi", "Minh", "Duc", "Ngoc", "Thanh", "Quang", "Tuan", "Hoang", "Huu", "Kim", "Xuan", "Thu", "Phuong", "My"]
given_names = ["Anh", "Tuan", "Hung", "Dung", "Lan", "Hue", "Hoa", "Minh", "Hieu", "Quan", "Linh", "Trang", "Mai", "Huong", "Phuong", "Thao", "Vy", "Hang", "Cuong", "Nam", "Bac", "Son", "Dong"]

# Random selection
rand_surnames = np.random.choice(surnames, n_rows)
rand_middles = np.random.choice(middle_names, n_rows)
rand_givens = np.random.choice(given_names, n_rows)

# Combine names
full_names = [f"{s} {m} {g}" for s, m, g in zip(rand_surnames, rand_middles, rand_givens)]

# Create DataFrame
df = pd.DataFrame({
    'CardNumber': card_numbers,
    'PIN': pins_str,
    'Balance': balances,
    'AccountHolderName': full_names
})

# Save to accounts.txt, header=False to match the user's template (implied by "1111,..." example without header)
# User's example: 1111,123456,10300000,Nguyen Van A
# It looks like there is no header in the user's snippet.
df.to_csv('accounts.txt', index=False, header=False)

print(f"Generated {n_rows} records.")
print(df.head())