package org.chesscorp.club.config;

import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.marshaller.optimized.OptimizedMarshaller;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class IgniteConfig {

    @Bean
    public IgniteSpringBean getIgniteGrid() {
        OptimizedMarshaller marshaller = new OptimizedMarshaller();
        marshaller.setRequireSerializable(false);

        List<String> addresses = new ArrayList<>();
        addresses.add("127.0.0.1:47500..47509");

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(addresses);

        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        spi.setIpFinder(ipFinder);

        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setPeerClassLoadingEnabled(true);
        configuration.setMarshaller(marshaller);
        configuration.setDiscoverySpi(spi);

        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        igniteSpringBean.setConfiguration(configuration);

        return igniteSpringBean;
    }
}