package com.offbynull.portmapper.mappers.upnpigd.externalmessages;

import com.offbynull.portmapper.mapper.PortType;
import java.net.InetAddress;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DeletePortMappingUpnpIgdRequestTest {

    @Test
    public void mustGenerateTcpRequest() throws Exception {
        DeletePortMappingUpnpIgdRequest req = new DeletePortMappingUpnpIgdRequest("fake", "/controllink",
                "service:type", InetAddress.getByAddress(new byte[]{1, 2, 3, 4}), 15, PortType.TCP);
        String bufferText = new String(req.dump(), "US-ASCII");

        assertEquals("POST /controllink HTTP/1.1\r\n"
                + "Host: fake\r\n"
                + "Content-Type: text/xml\r\n"
                + "SOAPAction: service:type#DeletePortMapping\r\n"
                + "Connection: Close\r\n"
                + "Cache-Control: no-cache\r\n"
                + "Pragma: no-cache\r\n"
                + "Content-Length: 361\r\n"
                + "\r\n"
                + "<?xml version=\"1.0\"?>"
                + "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope/\" soap:encodingStyle=\"http://www.w3.org/2003/05/soap-encoding\">"
                + "<soap:Body>"
                + "<u:DeletePortMapping xmlns:u=\"service:type\">"
                + "<NewRemoteHost>1.2.3.4</NewRemoteHost>"
                + "<NewExternalPort>15</NewExternalPort>"
                + "<NewProtocol>TCP</NewProtocol>"
                + "</u:DeletePortMapping>"
                + "</soap:Body>"
                + "</soap:Envelope>",
                bufferText);
    }

    @Test
    public void mustGenerateUdpRequest() throws Exception {
        // NOTE: technically port mapping services should not be dealing with IPv6 (only the firewall supports IPv6) -- but allow it anyways
        // because some routers may not follow the spec
        DeletePortMappingUpnpIgdRequest req = new DeletePortMappingUpnpIgdRequest("fake", "/controllink",
                "service:type", InetAddress.getByAddress(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}), 20000,
                PortType.UDP);
        String bufferText = new String(req.dump(), "US-ASCII");

        assertEquals("POST /controllink HTTP/1.1\r\n"
                + "Host: fake\r\n"
                + "Content-Type: text/xml\r\n"
                + "SOAPAction: service:type#DeletePortMapping\r\n"
                + "Connection: Close\r\n"
                + "Cache-Control: no-cache\r\n"
                + "Pragma: no-cache\r\n"
                + "Content-Length: 388\r\n"
                + "\r\n"
                + "<?xml version=\"1.0\"?>"
                + "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope/\" soap:encodingStyle=\"http://www.w3.org/2003/05/soap-encoding\">"
                + "<soap:Body>"
                + "<u:DeletePortMapping xmlns:u=\"service:type\">"
                + "<NewRemoteHost>102:304:506:708:90a:b0c:d0e:f10</NewRemoteHost>"
                + "<NewExternalPort>20000</NewExternalPort>"
                + "<NewProtocol>UDP</NewProtocol>"
                + "</u:DeletePortMapping>"
                + "</soap:Body>"
                + "</soap:Envelope>",
                bufferText);
    }

    @Test
    public void mustGenerateRequestWithWildcardAddress() throws Exception {
        DeletePortMappingUpnpIgdRequest req = new DeletePortMappingUpnpIgdRequest("fake", "/controllink",
                "service:type", null, 15, PortType.TCP);
        String bufferText = new String(req.dump(), "US-ASCII");

        assertEquals("POST /controllink HTTP/1.1\r\n"
                + "Host: fake\r\n"
                + "Content-Type: text/xml\r\n"
                + "SOAPAction: service:type#DeletePortMapping\r\n"
                + "Connection: Close\r\n"
                + "Cache-Control: no-cache\r\n"
                + "Pragma: no-cache\r\n"
                + "Content-Length: 354\r\n"
                + "\r\n"
                + "<?xml version=\"1.0\"?>"
                + "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope/\" soap:encodingStyle=\"http://www.w3.org/2003/05/soap-encoding\">"
                + "<soap:Body>"
                + "<u:DeletePortMapping xmlns:u=\"service:type\">"
                + "<NewRemoteHost></NewRemoteHost>"
                + "<NewExternalPort>15</NewExternalPort>"
                + "<NewProtocol>TCP</NewProtocol>"
                + "</u:DeletePortMapping>"
                + "</soap:Body>"
                + "</soap:Envelope>",
                bufferText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mustFailToGenerateWhenPortIsOutOfRange() throws Exception {
        DeletePortMappingUpnpIgdRequest req = new DeletePortMappingUpnpIgdRequest("fake", "/controllink",
                "service:type", InetAddress.getByAddress(new byte[]{1, 2, 3, 4}), 5555555, PortType.UDP);
    }

}
