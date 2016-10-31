package burp;

import java.io.PrintWriter;
import java.util.Random;
import java.util.List;


public class BurpExtender implements burp.IBurpExtender, burp.IHttpListener
{
    private burp.IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;

    private int counter = 0;
    private String nextToken = "";
    private String nextTimestamp = "";
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

                int offset = reqBody.indexOf("IncrementMePlease");
                stdout.println(offset);
                reqBody = reqBody.replaceAll("IncrementMePlease", "Incremented" + String.valueOf(randomint) + String.valueOf(counter));
                counter++;
                updated = true;
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
