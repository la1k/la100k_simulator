from hashlib import sha256
from . import auth


def main():
    password = input("Type in password:\n")
    secret = auth.hash_password(password)
    print("hashed secret is: {}".format(secret))

if __name__ == "__main__":
    main()