# IncrementMePlease
A simple but useful Burp extension to increment a parameter in each request, intended for use with Active Scan.

An example use case would be if you are active scanning a "create user" form, which would normally produce an error if you created two users with the same username. You can use the text `IncrementMePlease` for the username parameter parameter and it will replace it with `Incremented[RandomInt][Counter]`, so that you can successfully active scan this form.

The extension also supports `IntMePlease` and `FloatMePlease` that will replace the text with an integer or float starting from 1. If you need the counter to start at another number, append this to the end of the string such as `IntMePlease2` or `FloatMePlease10.0`. To reset or change the numberin again, you'll need to unload and then load the extension.

Lastly, the extension support a random GUID with the string `GUIDMePlease`

## Example

### `IncrementMePlease`
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

### `IntMePlease` and `FloatMePlease`
It will match:
```
{"name":"IntMePlease"}
```
And replace it with:
```
{"name":"1"}
...
{"name":"2"}
...
{"name":"3"}
```

or

It will match:
```
{"name":"IntMePlease5"}
```
And replace it with:
```
{"name":"6"}
...
{"name":"7"}
...
{"name":"8"}
```

### `GUIDMePlease`
It will match:
```
{"name":"GUIDMePlease"}
```
And replace it with:
```
{"name":"c2d733ef-dca0-468a-ad8e-3eb687e9a8a3"}
...
{"name":"606a6c73-f5e0-4049-9a18-7a1929029e27"}
...
{"name":"b9839038-a2a4-4ff1-b22c-e7213b292dff"}
```

## Releases
This is available in the BApp store as the "Token Incrementor" extension.

See the [Releases](https://github.com/alexlauerman/IncrementMePlease/releases) tab for a pre-built jar.

## Contributors
alexlauerman
xl-sec
