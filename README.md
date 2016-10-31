# IncrementMePlease
Burp extension to increment a parameter in each active scan request.

An example use case would be if  you are active scanning a "create user" form, which would normally produce an error if you created two users with the same username. You can use the text "IncrementMePlease" for the username parameter parameter and it will replace it with "Incremented[RandomInt][Counter]", so that you can successfully active scan this form.
