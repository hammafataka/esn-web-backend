package dev.mfataka.esnzlin.service;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 21.08.2024 16:59
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerInfoService {
    private int serverPort;


    @EventListener
    public void onApplicationEvent(final WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    @SneakyThrows
    public String getServerAddress() {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public String getFullServerAddress() {
        return "http://" + getServerAddress() + ":" + serverPort;
    }
}
