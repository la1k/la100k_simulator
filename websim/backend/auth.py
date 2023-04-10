from hashlib import sha256
from datetime import datetime, timedelta
import jwt
import logging

logger = logging.getLogger(__name__)

# Salt is usually just needed for a multi user system to make it more
# computional heavy to figure out if multiple users have the same password.
# It is also used to make it harder to check for common passwords using precomputed hashes 
# Thus it does not really add any actual protection in this case, but it is a good practice.
SALT = "robbing employed professor opposite venomous carry"

# Secret is the sha256 hash of the password (stripped of spaces), 
# prefixed with the salt and then hashed again
SECRET = "d7f444f1ccbbf1aec031ffbb6fa186c115e702fc520bfe609576add89317b911"

def hash_password(password: str) -> str:
    """
    Hashes a given password using our hash + salt scheme
    """
    # Remove spaces in password and strip it on both sides
    password = password.replace(" ", "").strip()

    hashed_password = sha256(password.encode("utf-8")).hexdigest()
    salted_password = "{}{}".format(SALT, hashed_password)
    return sha256(salted_password.encode("utf-8")).hexdigest()

def check_password(password) -> bool:
    test = hash_password(password)
    return test == SECRET
    

def generate_auth_token() -> str:
    """
    Generates a new auth token.
    ONLY use this after verifying the password
    or when reissuing an existing verified token!
    """
    claims = {
        "iat": int(datetime.now().timestamp())
    }
    return jwt.encode(claims, SECRET, algorithm="HS256")

def decode_auth_token(auth_token: str) -> dict:
    """
    Decodes and verifies an auth token.
    Will throw an exception if verification fails.
    Returns the claims in the token if HMAC verification succeeds.
    """
    # This will throw an exception if the HMAC does not match
    return jwt.decode(auth_token, SECRET, algorithms=["HS256"])

def is_authenticated(auth_token: str) -> bool:
    """
    Checks if a given JWT token is trusted.
    The JWT uses HMAC-SHA256 for authenticating the payload.
    It is safe to use the same secret here as the hashed password.
    """
    if not auth_token:
        return False
    try:
        claims = decode_auth_token(auth_token)
        iat = datetime.fromtimestamp(claims.get("iat", 0))
        if datetime.now() - iat > timedelta(days=1):
            # Token is only valid for a day
            logger.warn("Authentication failure: token is too old")
            return False
    except jwt.DecodeError as e:
        logger.warn("Authentication failure: {}".format(e))
        return False
    
    return True