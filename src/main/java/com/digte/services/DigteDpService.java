package com.digte.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Base64;

import javax.ws.rs.core.StreamingOutput;

import com.digte.RestConstant;
import com.digte.domain.FileInfo;
import com.digte.utils.StreamUtil;
import com.fluig.customappkey.Keyring;
import com.fluig.sdk.api.customappkey.KeyVO;

public class DigteDpService {
    private Long tenantId = 0L;
    private KeyVO key = null;
    private FileInfo fileInfo;

    private OauthService _oauthService() {
         return new OauthService();
    }

    private String serverURL() {
        String server = key.getDomainUrl();

        int ajustePorta = server.lastIndexOf("//");

        if (ajustePorta > 1){
            server = server.substring(ajustePorta+2,server.length());
        }

        ajustePorta = server.lastIndexOf(":");

        if (ajustePorta > 1){
            server = server.substring(0,ajustePorta -1);
        }

        return server;
    }

    private void validateURL(String urlDocument) throws Exception {
         if (urlDocument == null)   {
            String erro = "{\"content\":\"ERROR\",\"message\":{\"message\":\"Erro no dowload de conteúdo do documento : " + urlDocument
            +  "). \",\"detail\":\"Erro no dowload de conteúdo do documento.\",\"type\":\"ERROR\",\"param\":"
            + urlDocument + ",\"errorCode\":\"DigteDpfException\"}}";

            throw new Exception(erro);
        }

        if (   urlDocument.indexOf("/streamcontrol") == -1
            && urlDocument.indexOf("/stream")        == -1
            && urlDocument.indexOf("/webdesk")       == -1
            && urlDocument.indexOf("/volume")        == -1)
        {

            String erro = "{\"content\":\"ERROR\",\"message\":{\"message\":\"Erro no dowload de conteúdo do documento (ERRO: Url inválida) : " + urlDocument
            +  "). \",\"detail\":\"Erro no dowload de conteúdo do documento (ERRO: Url inválida).\",\"type\":\"ERROR\",\"param\":" + urlDocument
            + ",\"errorCode\":\"DigteDpfException\"}}";

            throw new Exception(erro);

        }

        String server = serverURL();

        if (urlDocument.indexOf(server) == -1)   {
            String erro = "{\"content\":\"ERROR\",\"message\":{\"message\":\"Erro no dowload de conteúdo do documento - Servidor incorreto : " + urlDocument
            +  "). \",\"detail\":\"Erro no dowload de conteúdo do documento - Servidor incorreto.\",\"type\":\"ERROR\",\"param\":" + urlDocument
            + ",\"errorCode\":\"DigteDpfException\"}}";

            throw new Exception(erro);

        }
    }

    private String mountUrl(Long _tenantId, String urlBase64) throws Exception {
        tenantId = _tenantId;

        key = Keyring.getKeys(tenantId, RestConstant.APP_KEY);

        if (urlBase64 == null)   {
            String erro = "{\"content\":\"ERROR\",\"message\":{\"message\":\"Erro no dowload de conteúdo do documento : " + urlBase64
            +  "). \",\"detail\":\"Erro no dowload de conteúdo do documento.\",\"type\":\"ERROR\",\"param\":"
            + urlBase64 + ",\"errorCode\":\"DigteDpfException\"}}";

            throw new Exception(erro);
        }

        String urlDocument = "";

        try{
            byte[] decodedBytes = Base64.getDecoder().decode(urlBase64);

            urlDocument = new String(decodedBytes);

            validateURL(urlDocument);

        } catch (Exception e) {
        	String erro = "{\"content\":\"ERROR\",\"message\":{\"message\":\"Problema no acesso a imagem.\",\"detail\":\"Problema no acesso a imagem.\",\"type\":\"ERROR\",\"param\":null,\"errorCode\":\"DigteDpfException\"}}";
            throw new Exception(erro);
        }

        return urlDocument;
    }

    public StreamingOutput documentStream(Long _tenantId, String urlBase64) throws Exception {
        String urlDocument = mountUrl(_tenantId, urlBase64);

        if (key.getDomainUrl().indexOf("-1") != -1) {
            String url_string = key.getDomainUrl().replace(":-1", "");
            key = new KeyVO(key.getUser(), key.getTenantId(), key.getConsumerKey(), key.getConsumerSecret(), key.getToken(), key.getTokenSecret(), url_string);
        }

        HttpURLConnection conn = _oauthService().GetFluig(key, urlDocument);

        try {
            conn.connect();

            String fileName = "";
            String disposition = conn.getHeaderField("Content-Disposition");
            String contentType = conn.getContentType();
            int contentLength = conn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = urlDocument.substring(urlDocument.lastIndexOf("/") + 1,
                urlDocument.length());
            }

            fileInfo = new FileInfo(fileName, disposition, contentType, contentLength);
            InputStream inputStream = conn.getInputStream();

            return StreamUtil.stream2file(inputStream, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        finally {
            conn.disconnect();
        }
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }
}
