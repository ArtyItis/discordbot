package com.github.artyitis.discordbot.command.music;

import com.github.artyitis.discordbot.STATICS;
import com.github.artyitis.discordbot.music.GuildMusicManager;
import com.github.artyitis.discordbot.utilities.MessageHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Queue extends CommandMusic {

    public Queue() {
        super(STATICS.QUEUE[0], STATICS.QUEUE[0]);
        SubcommandData clear = new SubcommandData(STATICS.QUEUE_SUB_CLEAR[0], STATICS.QUEUE_SUB_CLEAR[1]);
        SubcommandData show = new SubcommandData(STATICS.QUEUE_SUB_SHOW[0], STATICS.QUEUE_SUB_SHOW[1]);
        SubcommandData shuffle = new SubcommandData(STATICS.QUEUE_SUB_SHUFFLE[0], STATICS.QUEUE_SUB_SHUFFLE[1]);
        SubcommandData size = new SubcommandData(STATICS.QUEUE_SUB_SIZE[0], STATICS.QUEUE_SUB_SIZE[1]);
        commandData.addSubcommands(show, clear, shuffle, size);
    }

    @Override
    public void eventHandler(SlashCommandInteractionEvent event) throws Exception {
        String subcommandName = event.getSubcommandName();
        Guild guild = event.getGuild();
        ReplyCallbackAction replyAction = event.deferReply();
        Member member = event.getMember();
        final GuildMusicManager musicManager = musicHelper.getGuildAudioPlayer(guild);
        String commandAction = "";
        if (subcommandName.equals(STATICS.QUEUE_SUB_CLEAR[0])) {
            musicManager.scheduler.clearQueue();
            commandAction = "the queue has been cleared by " + member.getAsMention();
        } else if (subcommandName.equals(STATICS.QUEUE_SUB_SHOW[0])) {
            MessageChannel memberPrivateChanel = member.getUser().openPrivateChannel().complete();
            String trackNames = "";
            final java.util.Queue<AudioTrack> queue = musicManager.scheduler.getQueue();
            if (queue.isEmpty())
                commandAction = "queue is empty";
            else {
                int tracknumber = 1;
                for (final AudioTrack track : queue) {
                    String title = track.getInfo().title.replaceAll("||", "");
                    trackNames += "**" + tracknumber + ".** " + title + "\n";
                    tracknumber++;
                    if (trackNames.length() > 1000) {
                        MessageHelper.sendMessage(memberPrivateChanel, trackNames);
                        trackNames = "";
                    }
                }
                if (!trackNames.isBlank())
                    MessageHelper.sendMessage(memberPrivateChanel, trackNames);

                commandAction = "the queue will be listed in your DM's";
            }
        } else if (subcommandName.equals(STATICS.QUEUE_SUB_SHUFFLE[0])) {
            if (musicManager.scheduler.getQueue().isEmpty()) {
                commandAction = "shuffled an empty queue\npog you";
            } else {
                musicManager.scheduler.shuffle();
                commandAction = ":twisted_rightwards_arrows: the queue was shuffled";
            }
        } else if (subcommandName.equals(STATICS.QUEUE_SUB_SIZE[0])) {
            int size = musicManager.scheduler.getQueue().size();
            commandAction = "There are " + size + " tracks in the queue";
        }
        MessageHelper.replyMusic(member, replyAction, name + " " + subcommandName, commandAction);
    }
}
