/*
 * MIT License
 *
 * Copyright (c) 2017 Frederik Ar. Mikkelsen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package fredboat.command.admin;

import fredboat.command.info.HelpCommand;
import fredboat.commandmeta.abs.JCommand;
import fredboat.commandmeta.abs.CommandContext;
import fredboat.commandmeta.abs.ICommandRestricted;
import fredboat.definitions.PermissionLevel;
import fredboat.main.Launcher;
import fredboat.messaging.internal.Context;
import fredboat.util.DiscordUtil;

import javax.annotation.Nonnull;

/**
 *
 * @author frederik
 */
public class ReviveCommand extends JCommand implements ICommandRestricted {

    public ReviveCommand(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void onInvoke(@Nonnull CommandContext context) {

        int shardId;

        if (!context.hasArguments()) {
            HelpCommand.sendFormattedCommandHelp(context);
            return;
        }

        try {
            if (context.getArgs().length > 1 && context.getArgs()[0].equals("guild")) {
                long guildId = Long.valueOf(context.getArgs()[1]);
                shardId = DiscordUtil.getShardId(guildId, Launcher.getBotController().getCredentials());
            } else {
                shardId = Integer.parseInt(context.getArgs()[0]);
            }
        } catch (NumberFormatException e) {
            HelpCommand.sendFormattedCommandHelp(context);
            return;
        }

        context.replyWithName("Queued shard revive for shard " + shardId);
        // TODO Launcher.getBotController().getShardManager().restart(shardId); // If not found it will just function like #start()
    }

    @Nonnull
    @Override
    public String help(@Nonnull Context context) {
        return "{0}{1} <shardId> OR {0}{1} guild <guildId>\n#Revive the specified shard, or the shard of the specified guild.";
    }

    @Nonnull
    @Override
    public PermissionLevel getMinimumPerms() {
        return PermissionLevel.BOT_ADMIN;
    }
}
