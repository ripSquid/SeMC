package net.fabricmc.example.mixin;

import net.minecraft.stat.ServerStatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerStatHandler.class)
public interface ServerStatHandlerMixin {
    @Invoker("asString")
    public String asString();
}
