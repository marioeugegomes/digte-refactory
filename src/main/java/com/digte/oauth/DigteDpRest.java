package com.digte.oauth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.digte.domain.FileInfo;
import com.digte.domain.Whatsapp;
import com.digte.enums.WhatsappEnum;
import com.digte.services.DigteDpService;
import com.digte.services.WhatsappService;

@Path("/digte")
public class DigteDpRest {
    @GET
    @Path("/proxyDownload/{tenantId}/{urlDocumentBase64}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response proxyDownload (@PathParam("tenantId") Long tenantId, @PathParam("urlDocumentBase64") String urlDocumentBase64) throws Exception {
        try {
            DigteDpService _service  = getService();

            StreamingOutput out =_service.documentStream(tenantId, urlDocumentBase64);
            FileInfo info = _service.getFileInfo();
            String contentType = info.getContentType();
            String fileName = info.getFileName();

            return Response.ok(out)
            .type(contentType)
            .header("Content-Disposition", "attachment; filename=" + fileName)
            .build();

        } catch(Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorStatus(ex)).build();
        }
    }

    @POST
    @Path("/sendMessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage (final Whatsapp input) throws Exception {
        WhatsappService _service = getWhatsappService();

        try {
            _service.sendMessage(WhatsappEnum.TWILLIO, input.to, input.from, input.body);

            return Response.ok(input)
            .build();

        } catch(Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorStatus(ex)).build();
        }

    }

    private WhatsappService getWhatsappService() {
        return new WhatsappService();
    }

    private DigteDpService getService() {
        return new DigteDpService();
    }
}