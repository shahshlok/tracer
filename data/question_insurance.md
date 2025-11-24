An insurance company computes the cost of vehicle insurance premiums based on the following rules:
(i) A driver who is under 18 or who had more than 2 accidents is ineligible for insurance.
(ii) The basic insurance charge is $600.
(iii) There is a surcharge of $200 if the driver is under 24 years old.
(iv) There is another surcharge: an extra 25 % of the **total premium** if the driver had any accidents.

**Examples:**

| Age | Accidents | Insurance | How insurance is calculated |
| :--- | :--- | :--- | :--- |
| 40 | 3 | Ineligible | Number of accidents is more than 2. |
| 40 | 0 | 600 | No surcharges. Only basic amount. |
| 40 | 1 | 750 | 600 (basic) + 25% of 600 (accidents surcharge) |
| 19 | 0 | 800 | 600 (basic) + 200 (age surcharge) |
| 19 | 2 | 1,000 | 600 (basic) + 200 (age) + 25% of 800 (accidents) |

**Write a Java program that works as following:**
a) Prompt the user for the age and number of accidents.
b) Display an error message if user enters a negative value for either age or number of accidents, then terminate the program. (Assume the user will only enter integers).
c) Calculate and display insurance amount only if rule (i) above is satisfied, otherwise display a message that the user is ineligible for insurance then terminate the program.
d) Prompt the user for an email address. If **valid**, inform the user that a quote will be sent to *his/her* email. Otherwise, display an error message that a quote cannot be sent. A **valid** email address is a string that includes the character @.

### Sample Runs

| Sample Run | Output |
| :--- | :--- |
| **Sample run \# 1:** invalid age | `Enter age and number of accidents: -19 2`<br>`Invalid age or number of accidents.` |
| **Sample run \# 2:** invalid number of accidents | `Enter age and number of accidents: 19 -2`<br>`Invalid age or number of accidents.` |
| **Sample run \# 3:** valid values, ineligible driver | `Enter age and number of accidents: 40 3`<br>`You are not eligible for insurance.` |
| **Sample run \# 4:** valid values, but invalid email | `Enter age and number of accidents: 40 1`<br>`Your insurance is 750.0`<br>`Enter your email: abcd`<br>`Invalid email. Can't send you the quote.` |
| **Sample run \# 5:** valid values and email | `Enter age and number of accidents: 40 1`<br>`Your insurance is 750.0`<br>`Enter your email: abcd@xyz`<br>`quote will be sent to abcd@xyz` |



