$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("out/test/resources/search.feature");
formatter.feature({
  "line": 1,
  "name": "As a Amazon user I should be able to search product",
  "description": "",
  "id": "as-a-amazon-user-i-should-be-able-to-search-product",
  "keyword": "Feature"
});
formatter.before({
  "duration": 5022992987,
  "status": "passed"
});
formatter.scenario({
  "line": 4,
  "name": "Searching different products after login",
  "description": "",
  "id": "as-a-amazon-user-i-should-be-able-to-search-product;searching-different-products-after-login",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 3,
      "name": "@amazon1"
    }
  ]
});
formatter.step({
  "line": 5,
  "name": "I am on the Login page URL \"https://www.amazon.in/\"",
  "keyword": "Given "
});
formatter.step({
  "line": 6,
  "name": "I click on sign in button and wait for sign in page",
  "keyword": "Then "
});
formatter.step({
  "line": 7,
  "name": "I should see Sign In Page",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "https://www.amazon.in/",
      "offset": 28
    }
  ],
  "location": "loginLogoutPageStepDefinitions.i_am_on_the_Login_page_URL(String)"
});
formatter.result({
  "duration": 4748290413,
  "status": "passed"
});
formatter.match({
  "location": "loginLogoutPageStepDefinitions.i_click_on_sign_in_button_and_wait_for_sign_in_page()"
});
formatter.result({
  "duration": 2186889720,
  "status": "passed"
});
formatter.match({
  "location": "loginLogoutPageStepDefinitions.i_should_see_Sign_In_Page()"
});
formatter.result({
  "duration": 62263510,
  "status": "passed"
});
formatter.embedding("image/png", "embedded0.png");
formatter.after({
  "duration": 2613474516,
  "status": "passed"
});
});