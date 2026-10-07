import unittest
import os
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


FRONTEND_URL = os.environ.get("FRONTEND_URL", "http://localhost:5173")
BACKEND_URL = os.environ.get("BACKEND_URL", "http://localhost:8080")


class TestBackendAPI(unittest.TestCase):
    """Verify the backend REST API is reachable via a headless browser."""

    def setUp(self):
        options = webdriver.ChromeOptions()
        options.add_argument("--headless")
        options.add_argument("--no-sandbox")
        options.add_argument("--disable-dev-shm-usage")
        self.driver = webdriver.Chrome(options=options)
        self.driver.implicitly_wait(10)

    def test_dashboard_summary_api(self):
        """Load /api/dashboard/summary and confirm JSON with totalServers."""
        self.driver.get(f"{BACKEND_URL}/api/dashboard/summary")
        body = self.driver.find_element(By.TAG_NAME, "body").text
        self.assertIn("totalServers", body)

    def test_servers_api(self):
        """Load /api/servers and confirm JSON array is returned."""
        self.driver.get(f"{BACKEND_URL}/api/servers")
        body = self.driver.find_element(By.TAG_NAME, "body").text
        # The response is a JSON array; it must contain at least a bracket
        self.assertTrue(body.startswith("["), "Expected JSON array from /api/servers")

    def test_alerts_api(self):
        """Load /api/alerts and confirm JSON array is returned."""
        self.driver.get(f"{BACKEND_URL}/api/alerts")
        body = self.driver.find_element(By.TAG_NAME, "body").text
        self.assertTrue(body.startswith("["), "Expected JSON array from /api/alerts")

    def tearDown(self):
        self.driver.quit()


class TestFrontendUI(unittest.TestCase):
    """Verify the React frontend loads correctly (requires frontend running)."""

    def setUp(self):
        options = webdriver.ChromeOptions()
        options.add_argument("--headless")
        options.add_argument("--no-sandbox")
        options.add_argument("--disable-dev-shm-usage")
        self.driver = webdriver.Chrome(options=options)
        self.driver.implicitly_wait(10)

    def test_dashboard_loads(self):
        self.driver.get(FRONTEND_URL)
        title = WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.CLASS_NAME, "page-title"))
        )
        self.assertEqual("Dashboard", title.text)

    def test_navigation_to_servers(self):
        self.driver.get(FRONTEND_URL)
        servers_link = self.driver.find_element(By.LINK_TEXT, "Servers")
        servers_link.click()
        title = WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.CLASS_NAME, "page-title"))
        )
        self.assertEqual("Servers", title.text)

    def tearDown(self):
        self.driver.quit()


if __name__ == "__main__":
    unittest.main()
