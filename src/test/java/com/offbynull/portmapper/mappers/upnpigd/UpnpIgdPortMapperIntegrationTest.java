package com.offbynull.portmapper.mappers.upnpigd;

import com.offbynull.portmapper.gateway.Bus;
import com.offbynull.portmapper.mapper.MappedPort;
import com.offbynull.portmapper.mapper.PortType;
import com.offbynull.portmapper.gateways.network.NetworkGateway;
import com.offbynull.portmapper.gateways.network.internalmessages.KillNetworkRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Ignore;

@Ignore("REQUIRES (modified) MINIUPNPD TO BE PROPERLY SET UP IN A VM ALONG WITH NO OTHER UPNP-ENABLED ROUTERS")
public class UpnpIgdPortMapperIntegrationTest {
    
    // FOR THIS CLASS TO WORK: miniupnpd needs to be modified to ignore errors on addpinhole/deletepinhole. The VM set has a modified
    // version of miniupnpd that allows this.

    private NetworkGateway network;
    private Bus networkBus;

    @Before
    public void before() {
        network = NetworkGateway.create();
        networkBus = network.getBus();
    }

    @After
    public void after() {
        networkBus.send(new KillNetworkRequest());
    }

    @Test
    public void mustFindAndControlPortMappers() throws Throwable {
        List<UpnpIgdPortMapper> mappers = UpnpIgdPortMapper.identify(networkBus);
        assertEquals(2, mappers.size());
        
        Set<Class<?>> expectedTypes = new HashSet<>();
        
        for (UpnpIgdPortMapper mapper : mappers) {
            expectedTypes.add(mapper.getClass());
        }
        
        for (UpnpIgdPortMapper mapper : mappers) {
            MappedPort tcpPort = mapper.mapPort(PortType.TCP, 12345, 12345, 999999999999999999L);
            assertEquals(PortType.TCP, tcpPort.getPortType());
            assertEquals(12345, tcpPort.getInternalPort());
            assertNotEquals(999999999999999999L, tcpPort.getLifetime());
            // external port/external ip/etc.. may all be different

            MappedPort udpPort = mapper.mapPort(PortType.UDP, 12345, 12345, 999999999999999999L);
            assertEquals(PortType.UDP, udpPort.getPortType());
            assertNotEquals(999999999999999999L, udpPort.getLifetime());
            // external port/external ip/etc.. may all be different




            tcpPort = mapper.refreshPort(tcpPort, 10000L);
            assertEquals(PortType.TCP, tcpPort.getPortType());
            assertEquals(12345, tcpPort.getInternalPort());
            assertEquals(10000L, tcpPort.getLifetime());
            // external port/external ip/etc.. may all be different

            udpPort = mapper.refreshPort(udpPort, 10000L);
            assertEquals(PortType.UDP, udpPort.getPortType());
            assertEquals(12345, udpPort.getInternalPort());
            assertEquals(10000L, udpPort.getLifetime());
            // external port/external ip/etc.. may all be different




            mapper.unmapPort(tcpPort);
            mapper.unmapPort(udpPort);
        }
        
        
        
        Set<Class<?>> actualTypes = new HashSet<>();
        actualTypes.add(FirewallUpnpIgdPortMapper.class);
        actualTypes.add(PortMapperUpnpIgdPortMapper.class);
        
        assertEquals(expectedTypes, actualTypes);
    }
}
