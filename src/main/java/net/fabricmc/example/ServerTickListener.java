package net.fabricmc.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.fabricmc.example.mixin.ServerStatHandlerMixin;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

public class ServerTickListener implements ServerTickEvents.EndTick{

    public long ticks = 0;
    @Override
    public void onEndTick(MinecraftServer minecraftServer){
        ticks++;
        if (ticks % 600 != 0) {return;}

        HashMap<String, String> data = new HashMap<>();

        for (ServerPlayerEntity player : PlayerLookup.all(minecraftServer)) {
            data.put(player.getUuidAsString(), ((ServerStatHandlerMixin)player.getStatHandler()).asString());
        }


        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(data);

        var request = HttpRequest
                .newBuilder()
                .uri(URI.create(ExampleMod.address + "/report"))
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofMillis(2000))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        System.out.println(ExampleMod.address + "/report");
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            return;
        } catch (InterruptedException e) {
            return;
        }
        System.out.println("Sent request probably successful");
    }
}