package com.yasinvolved.supdater.client;

import com.google.gson.*;
import com.yasinvolved.supdater.utils.ExpandVars;
import com.yasinvolved.supdater.utils.STxtFilter;
import net.fabricmc.api.ClientModInitializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class SUpdaterClient implements ClientModInitializer {
    public static Logger LOGGER = LogManager.getLogger("SUpdater");
    public static final String MOD_ID = "supdater";
    private static final String URL = "https://api.github.com/repos/YasInvolved/SosnowieckiTXT/releases/latest";

    @Override
    public void onInitializeClient() {
        try {
            removeOldRelease();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(URL);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            String jsonString = EntityUtils.toString(result.getEntity(), "UTF-8");

            JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonArray assets = jsonObject.getAsJsonArray("assets");
            String url = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
            String filename = assets.get(0).getAsJsonObject().get("name").getAsString();

            URL parsedUrl = new URL(url);
            String fullpath = String.format("${appdata}\\.minecraft\\resourcepacks\\%s", filename);
            ReadableByteChannel rbc = Channels.newChannel(parsedUrl.openStream());
            FileOutputStream fos = new FileOutputStream(ExpandVars.expandvars(fullpath));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage());
        }
    }

    private void removeOldRelease() {
        File folder = new File(ExpandVars.expandvars("${appdata}\\.minecraft\\resourcepacks"));
        File[] listOfFiles = folder.listFiles(new STxtFilter());
        for (File oldTxt : listOfFiles) {
            oldTxt.delete();
        }
    }
}
