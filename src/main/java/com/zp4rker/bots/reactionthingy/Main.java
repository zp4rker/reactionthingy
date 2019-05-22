package com.zp4rker.bots.reactionthingy;

import com.zp4rker.core.discord.config.Config;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class Main {

    private static Config conf;

    public static void main(String[] args) throws Exception {

        conf = new Config("config.json");

        new JDABuilder(AccountType.BOT)
        .setToken(conf.data.getString("token"))
        .setEventManager(new AnnotatedEventManager())
        .addEventListeners(new Main())
        .build();

    }

    @SubscribeEvent
    public void onReaction(GuildMessageReactionAddEvent e) {
        if (e.getChannel().getIdLong() != conf.data.getLong("channel-to-receive")) return;
        if (!e.getReactionEmote().isEmote()) return;
        if (!e.getReactionEmote().getName().equals(conf.data.getString("emote"))) return;

        Message m = e.getChannel().getHistoryAround(e.getMessageId(), 1).complete().getMessageById(e.getMessageId());
        String content = m.getContentRaw();
        m.delete().queue();

        e.getGuild().getTextChannelById(conf.data.getLong("channel-to-send")).sendMessage(content).queue();
    }

}
