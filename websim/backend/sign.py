
from pathlib import Path
import re
import json
from typing import Any, Dict, List

SCRIPT_DIR = Path(__file__).parent.absolute()
SIGNS_DIR = Path(SCRIPT_DIR.parent, "signs")

class Sign:
    def __init__(self, name: str, *, authenticated: bool = False):
        if not re.match("^[a-zA-Z0-9\-]+$", name):
            raise ValueError("Invalid sign name")
        
        sign_dir = Path(SIGNS_DIR, name)
        if not sign_dir.relative_to(SIGNS_DIR):
            raise ValueError("Path traversal attempt detected.")

        if not sign_dir.exists() or not sign_dir.is_dir():
            raise ValueError("Sign does not exist")

        self.sign_dir = sign_dir

        if not authenticated and not self.public:
            raise PermissionError("Sign is private")

    def _get_meta(self):
        meta_path = Path(self.sign_dir, "meta.json")
        with meta_path.open('rb') as meta_f:
            meta = json.load(meta_f)  # type: dict
        
        # Always set public to False if it does not exist or is not a boolean
        if not isinstance(meta.get("public", None), bool):
            meta["public"] = False
        return meta
    
    @property
    def public(self) -> bool:
        meta = self._get_meta()
        # Default to false if the key is not set AND enforce it to be a boolean True exactly
        return meta.get("public", False) is True

    @property
    def scene_def(self) -> Path:
        return Path(self.sign_dir, "scene.json")

    @property
    def assets_dir(self) -> Path:
        return Path(self.sign_dir, "assets")

    def get_asset(self, name: str) -> Path:
        if name.startswith("."):
            raise ValueError("Asset can not start with a '.' character.")
        
        asset_path = Path(self.assets_dir, name)
        if not asset_path.relative_to(self.assets_dir):
            raise ValueError("Path traversal attempt detected.")
        return asset_path
    
    @property
    def pgm_dir(self) -> Path:
        return Path(self.sign_dir, "pgms")

    def list_pgms(self) -> List[Path]:
        out = []

        for f in self.pgm_dir.glob("*.pgm"):
            out.append(f)


        return sorted(out)

    def get_pgm(self, name: str) -> Path:
        if name.startswith("."):
            raise ValueError("PGM can not start with a '.' character.")
        if not name.endswith(".pgm"):
            raise ValueError("PGM must end with '.pgm'.")
        pgm_path = Path(self.pgm_dir, name)
        if not pgm_path.relative_to(self.pgm_dir):
            raise ValueError("Path traversal attempt detected.")
        
        return pgm_path

    def list_playlists(self) -> List[Dict[str, Any]]:
        out = []

        for f in self.pgm_dir.glob("*.txt"):
            playlist = {
                "name": f.name,
                "pgms": []
            }

            for pgm in f.read_text().splitlines():
                playlist["pgms"].append(pgm.strip())

            out.append(playlist)

        return sorted(out, key=lambda x: x["name"])