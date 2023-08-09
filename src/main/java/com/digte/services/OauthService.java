package com.digte.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fluig.oauth.Constants;
import com.fluig.sdk.api.customappkey.KeyVO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class OauthService {

    public Object config;

    private OAuthConsumer config(KeyVO key) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(key.getConsumerKey(), key.getConsumerSecret());
        consumer.setTokenWithSecret(key.getToken(), key.getTokenSecret());
        return consumer;
    }

    public HttpURLConnection GetFluig(KeyVO key, String urlStr) throws IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
        OAuthConsumer config = config(key);

        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(Constants.REQUEST_METHOD_GET);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        config(key).sign(conn);

        return conn;
    }
}
