# UiBot - Selenium + Crawler

UiBot combines the power of **Web Crawler** and **Selenium**. With home URL as input UiBot can crawl into the static webpages in the site and do the Pre-configured validation using Selenium.

## Features

 - Autonomous website validation with a single URL as input
 - Robust Web Crawler Algorithm (Without crossing legal hit count/seconds of Web server).
 -  Support Text Validations, WebElement Presence Validation, Galen Spec Validation, W3C Standard validation, SSL validation.  
 - Validations can be Generic/Conditional
 - Robust page load wait to handle Angular/React Pages.
 - Elegant Report generated for every execution

## Overview

 UIBot uses Selenium to navigate through webpages and runs autonomous into the depths of web site with the help of Built-in Web Crawler. UiBot has UI to configure the Selenium Validations and Web Crawler settings.
### Selenium Validations
In UiBot, user can configure two types of settings to be validated in the crawled webpages.

 - Generic Validations - Set of validations that will be performed in all crawled/identified webpages. (**Ex:** Galen Spec Validation of Headers)
 - Conditional Validations - Set of validations that will be performed in a webpage only if the user configured conditions matches that webpage. (**Ex:** Presence of Login form If Title contains Login)

### Workflow

![UIBotFlow](https://github.com/rsangeethk/uibot/blob/master/images/UIBot-Flow.PNG)
