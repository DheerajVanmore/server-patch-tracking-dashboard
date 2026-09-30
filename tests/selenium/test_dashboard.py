import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class TestDashboard(unittest.TestCase):
    def setUp(self):
        options = webdriver.ChromeOptions()
        options.add_argument('--headless')
        options.add_argument('--no-sandbox')
        options.add_argument('--disable-dev-shm-usage')
        self.driver = webdriver.Chrome(options=options)
        self.driver.implicitly_wait(10)
        self.base_url = "http://localhost:5173"

    def test_dashboard_loads(self):
        self.driver.get(self.base_url)
        # Wait for the dashboard title to load
        title = WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.CLASS_NAME, "page-title"))
        )
        self.assertEqual("Dashboard Summary", title.text)

    def test_navigation_to_servers(self):
        self.driver.get(self.base_url)
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
