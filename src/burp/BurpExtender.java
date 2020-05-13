package burp;

import java.io.PrintWriter;
import java.util.Random;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BurpExtender implements burp.IBurpExtender, burp.IHttpListener
{
    private burp.IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;

    private int counter = 0;
    private int counterInt = 0;
    private float counterFloat = 0;
    private String nextToken = "";
    private String nextTimestamp = "";
    private Boolean foundInt = false;
    private Boolean foundFloat = false;
    private Random rand = new Random();
    int randomint = rand.nextInt(999);


    //
    // implement IBurpExtender
    //
    @Override
    public void registerExtenderCallbacks(burp.IBurpExtenderCallbacks callbacks)
    {
        // obtain an extension helpers object
        helpers = callbacks.getHelpers();
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(),true);

        // set our extension name
        callbacks.setExtensionName("IncrementMePlease");

        // register ourselves as an HTTP listener
        callbacks.registerHttpListener(this);
    }

    //
    // implement IHttpListener
    //
    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, burp.IHttpRequestResponse messageInfo)
    {
        boolean updated = false;

        // only process requests
        if (messageIsRequest) {
            // get the HTTP service for the request
            burp.IHttpService httpService = messageInfo.getHttpService();
            burp.IRequestInfo iRequest = helpers.analyzeRequest(messageInfo);

            String request = new String(messageInfo.getRequest());

            List<String> headers = iRequest.getHeaders();
            // get the request body
            String reqBody = request.substring(iRequest.getBodyOffset());

            if (reqBody.contains("IncrementMePlease")) {
                // int offset = reqBody.indexOf("IncrementMePlease");
                // stdout.println(offset);
                counter++;
                reqBody = reqBody.replaceAll("IncrementMePlease", "Incremented" + String.valueOf(randomint) + String.valueOf(counter));
                updated = true;
            }

            if (reqBody.contains("IntMePlease")) {
                // int offset = reqBody.indexOf("IntMePlease");
                // stdout.println(offset);
                if (!foundInt) {
                    Pattern pattern = Pattern.compile(".*IntMePlease(\\d*).*");
                    Matcher matcher = pattern.matcher(reqBody);
                    if (matcher.find()){
                        int counterIntFound = Integer.parseInt(matcher.group(1));
                        // System.out.println(counterIntFound);
                        counterInt = counterIntFound;
                        foundInt = true;
                    }
                }

                counterInt++;
                reqBody = reqBody.replaceAll("IntMePlease\\d*", String.valueOf(counterInt));
                updated = true;
            }

            if (reqBody.contains("FloatMePlease")) {
                // int offset = reqBody.indexOf("FloatMePlease");
                // stdout.println(offset);
                if (!foundFloat) {
                    Pattern pattern = Pattern.compile(".*FloatMePlease(\\d*\\.\\d*).*");
                    Matcher matcher = pattern.matcher(reqBody);
                    if (matcher.find()){
                        float counterFloutFound = Float.parseFloat(matcher.group(1));
                        // System.out.println(counterFloutFound);
                        counterFloat = counterFloutFound;
                        foundFloat = true;
                    }
                }

                counterFloat++;
                reqBody = reqBody.replaceAll("FloatMePlease(\\d*\\.\\d*)?", String.valueOf(counterFloat));
                updated = true;
            }

            if (reqBody.contains("GUIDMePlease")) {
                // int offset = reqBody.indexOf("GUIDMePlease");
                // stdout.println(offset);
                reqBody = reqBody.replaceAll("GUIDMePlease", String.valueOf(UUID.randomUUID()));
                updated = true;
            }

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                if (header.contains("IncrementMePlease")) {
                    header = header.replaceAll("IncrementMePlease", "Incremented" + String.valueOf(randomint) + String.valueOf(counter));
                    updated = true;
                }
                if (header.contains("IntMePlease")) {
                    header = header.replaceAll("IntMePlease\\d*", String.valueOf(counterInt));
                    updated = true;
                }
                if (header.contains("FloatMePlease")) {
                    header = header.replaceAll("FloatMePlease(\\d*\\.\\d*)?", String.valueOf(counterFloat));
                    updated = true;
                }
                if (header.contains("GUIDMePlease")) {
                    header = header.replaceAll("GUIDMePlease", String.valueOf(UUID.randomUUID()));
                    updated = true;
                }
                headers.set(i, header);
            }

            if (updated) {
                stdout.println("-----Request Before Plugin Update-------");
                stdout.println(helpers.bytesToString(messageInfo.getRequest()));
                stdout.println("-----end output-------");

                byte[] message = helpers.buildHttpMessage(headers, reqBody.getBytes());
                messageInfo.setRequest(message);

                stdout.println("-----Request After Plugin Update-------");
                stdout.println(helpers.bytesToString(messageInfo.getRequest()));
                stdout.println("-----end output-------");
            }
        }
    }
}