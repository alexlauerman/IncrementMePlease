# IncrementMePlease
A simple but useful Burp extension to increment a parameter in each request, intended for use with Active Scan.

An example use case would be if  you are active scanning a "create user" form, which would normally produce an error if you created two users with the same username. You can use the text "IncrementMePlease" for the username parameter parameter and it will replace it with "Incremented[RandomInt][Counter]", so that you can successfully active scan this form.

## Example
It will match:
```
{"name":"IncrementMePlease"}
```
And replace it with:
```
{"name":"Incremented291706"}
...
{"name":"Incremented291707"}
...
{"name":"Incremented291708"}
```

## Releases
See the [Releases](https://github.com/alexlauerman/IncrementMePlease/releases) tab for a pre-built jar.
