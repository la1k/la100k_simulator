from pathlib import Path
from . import ws
from ..frame_rate import FrameRateController
from ..pgm import PGMReader
import subprocess
import time
import os



SIGN="la100k"
SCRIPTS_BASE_DIR=Path(Path.cwd(), "generator/src/kotlin/darkness/generator/scripts/")
OUTPUT_DIR=Path(Path.cwd(), "generator/output/")
SCRIPTS_DIR=Path(SCRIPTS_BASE_DIR, SIGN)
STARTUP_DELAY=5


ws.start()


frc = FrameRateController(20)
last_script_selected = None
while True:
    # Send a blank frame to make the sign go dark
    ws.broadcast(bytes(512))

    # If the script has been deleted remove the reference
    if last_script_selected and not last_script_selected.exists():
        last_script_selected = None

    scripts = sorted([s for s in SCRIPTS_DIR.glob('*.kt') if s.name != 'BaseScript.kt'])
    print("Select script to run or action:")
    for i, script in enumerate(scripts):
        print(f"[{i: >2}] {script.stem}")

    print("[ R] refresh")
    print("[ D] set startup delay")
    print("[ Q] exit")

    print("Enter number/action", end="")
    if last_script_selected:
        print(f" or press enter to run the \"{last_script_selected.stem}\" script again", end="")

    cmd = input(": ").strip().upper()
    if cmd == "R":
        continue
    elif cmd == "D":
        stime = input("How many seconds should we wait for? ").strip()
        try:
            STARTUP_DELAY = int(stime)
            print(f"Startup delay set to {STARTUP_DELAY} seconds")
        except:
            print("Invalid startup delay.")
        continue
    elif cmd == "Q":
        print("Exiting...")
        exit(0)

    run_script = None
    if cmd == "":
        if last_script_selected is None:
            print("Invalid option")
            continue
        run_script = last_script_selected
    elif cmd.isnumeric():
        script_num = int(cmd)
        if script_num < 0 or script_num >= len(scripts):
            print("Script index out of range")
            continue
        run_script = scripts[script_num]

    if not run_script:
        print("No script selected...")
        continue

    last_script_selected = run_script
    script_name = run_script.stem
    print(f"Selected script \"{script_name}\"")
    print("Building...")

    # Spawn the gradle build process
    gradlew = "./gradlew"
    if os.name == 'nt':
        # Override to bat file if windows
        gradlew = "gradlew.bat"

    # illegal-access=permit thingy is a workaround for openjdk 16/17 for kotlin
    res = subprocess.run(f"{gradlew} -Dkotlin.daemon.jvm.options=--illegal-access=permit :generator:run --args=\"{SIGN} '{script_name}' output\"", shell=True)
    if res.returncode != 0:
        print("Build failed! check compile log for errors")
        input("press enter to continue:")
        continue

    print("Build completed!")
    print(f"Starting in {STARTUP_DELAY} seconds")
    time.sleep(STARTUP_DELAY)
    print("Starting script")

    pgm_path = Path(OUTPUT_DIR, f"{script_name}.pgm")
    if not pgm_path.exists():
        print("For some reason the generated PGM file does not exist. Error!")
        input("press enter to continue:")
        continue


    pgm = PGMReader(pgm_path)
    frc.reset()
    for frame_index, frame in pgm.frames():
        ws.broadcast(frame)
        frc.next_frame()

