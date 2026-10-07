#!/usr/bin/env python3
"""
Health Check & Recovery Script  (Week 14)
==========================================
Checks if the backend API is healthy.
If unhealthy, restarts the Docker containers and re-checks.

Usage:
    python scripts/health_check.py
"""
import subprocess
import sys
import time
import urllib.request
import urllib.error

BACKEND_URL = "http://localhost:8080/api/dashboard/summary"
MAX_RETRIES = 5
RETRY_DELAY = 10  # seconds


def check_health():
    """Return True if the backend API responds with HTTP 200."""
    try:
        req = urllib.request.urlopen(BACKEND_URL, timeout=10)
        if req.getcode() == 200:
            return True
    except (urllib.error.URLError, urllib.error.HTTPError, OSError):
        pass
    return False


def restart_containers():
    """Restart Docker Compose containers."""
    print("[RECOVERY] Restarting containers via docker compose...")
    subprocess.run(["docker", "compose", "restart"], check=True)
    print("[RECOVERY] Waiting 30 seconds for containers to start...")
    time.sleep(30)


def main():
    print(f"[HEALTH] Checking backend at {BACKEND_URL} ...")

    if check_health():
        print("[HEALTH] OK: Backend is healthy (HTTP 200)")
        sys.exit(0)

    print("[HEALTH] FAIL: Backend is NOT healthy. Starting recovery...")
    restart_containers()

    for attempt in range(1, MAX_RETRIES + 1):
        print(f"[HEALTH] Re-check attempt {attempt}/{MAX_RETRIES} ...")
        if check_health():
            print("[HEALTH] OK: Backend recovered successfully (HTTP 200)")
            sys.exit(0)
        time.sleep(RETRY_DELAY)

    print("[HEALTH] FAIL: Backend did NOT recover after restart.")
    sys.exit(1)


if __name__ == "__main__":
    main()
