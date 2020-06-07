
# ![Logo](https://github.com/rsangeethk/uibot/blob/master/src/favicon-32x32.png)  UiBot - Selenium + Crawler

UiBot combines the power of **Web Crawler** and **Selenium**. With home URL as input UiBot can crawl into the static webpages in the site and do the Pre-configured validation using Selenium.

## Features

 - Autonomous website validation with a single URL as input
 - Robust Web Crawler Algorithm (Without crossing legal hit count/seconds of Web server).
 -  Support Text Validations, WebElement Presence Validation, Galen Spec Validation, W3C Standard validation, SSL validation.  
 - Validations can be Global/Conditional
 - Robust page load wait to handle Angular/React Pages.
 - Elegant Report for every execution
 - Execution can be done in Browsers, Devices and Cloud labs

## Overview

 UIBot uses Selenium to navigate through webpages and runs autonomous into the depths of web site with the help of Built-in Web Crawler. UiBot has UI to configure the Selenium Validations and Web Crawler settings.
### Selenium Validations
In UiBot, user can configure two types of settings to be validated in the crawled webpages.

 - Global Validations - Set of validations that will be performed in all crawled/identified webpages. (**Ex:** Galen Spec Validation of Headers in all crawled webpages)
 - Conditional Validations - Set of validations that will be performed in a webpage only if the user configured conditions matches that webpage. (**Ex:** Presence of Login form If Title contains Login)

### Workflow

![UIBotFlow](https://github.com/rsangeethk/uibot/blob/master/images/UIBot-Flow.PNG)

### Getting Started
Download and extract the package from [Latest Release](https://github.com/rsangeethk/uibot/releases/tag) 

 1. Open the uibot.jar file
 2. Select File > Open Folder > Select any empty folder
 3. Select File > New. UiBot Setting windows will appear as below.
 4. Configure the Website details and Browser settings in the appropriate fields and Click Add. New File will be created with file extension ``.uibot``
 5.  Select Validations Global Validations and the popup will be shown as below.
 6. Select Text Availability. Click Add button and Enter text value in to validate and Click Add Validation button
 7. Click Run. The Execution will be started for configured text validation as per Configured Website and browser settings.
