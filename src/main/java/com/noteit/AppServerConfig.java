package com.noteit;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Getter
public class AppServerConfig
{
    private static String scheme;
    private static String host;
    private static String port;
    private static String context;
    private static String url;
    private static final Logger logger = Logger.getLogger(AppServerConfig.class.getName());

    @Autowired
    private Environment env;

    public String getServerUrl()
    {
        if (url != null)
        {
            return url;
        }

        scheme = env.getProperty("app.server.scheme");
        host = env.getProperty("app.server.name");
        port = env.getProperty("server.port");
        context = null;
        url = scheme + "://" + host;
        if (port != null && !port.equals("80"))
        {
            url += ":" + port;
        }
        if (context != null)
        {
            url += "/" + context;
        }
        return url;
    }
}
