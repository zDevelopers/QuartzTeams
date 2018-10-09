/*
 * Copyright or © or Copr. Amaury Carrade (2014 - 2016)
 *
 * http://amaury.carrade.eu
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.zcraft.zteams.guis;

import fr.zcraft.zlib.components.gui.ExplorerGui;
import fr.zcraft.zlib.components.gui.Gui;
import fr.zcraft.zlib.components.gui.GuiAction;
import fr.zcraft.zlib.components.gui.GuiUtils;
import fr.zcraft.zlib.components.gui.PromptGui;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import fr.zcraft.zteams.ZTeam;
import fr.zcraft.zteams.ZTeams;
import fr.zcraft.zteams.ZTeamsPermission;
import fr.zcraft.zteams.colors.ColorsUtils;
import fr.zcraft.zteams.guis.builder.TeamBuilderStepColorGUI;
import fr.zcraft.zteams.guis.editor.TeamEditGUI;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class TeamsSelectorGUI extends ExplorerGui<ZTeam>
{
    @Override
    protected void onUpdate()
    {
        /// The title of the teams selector GUI. {0} = teams count.
        setTitle(I.t("{black}Select a team {reset}({0})", ZTeams.get().countTeams()));
        setData(ZTeams.get().getTeamsArray());

        setMode(Mode.READONLY);
        setKeepHorizontalScrollingSpace(true);

        if (ZTeamsPermission.UPDATE_TEAM_NAME.grantedTo(getPlayer()))
        {
            int renameSlot = getPlayer().hasPermission("uh.team") ? getSize() - 6 : getSize() - 5;

            action("rename", renameSlot, GuiUtils.makeItem(
                    /// The title of a button to rename our team, in the selector GUI.
                    Material.BOOK_AND_QUILL, I.t("{white}Rename your team"),
                    /// Warning displayed in the "Rename your team" button, if the player is not in a team
                    ZTeams.get().getTeamForPlayer(getPlayer()) == null ? GuiUtils.generateLore(I.t("{gray}You have to be in a team")) : null
            ));
        }

        if (ZTeamsPermission.CREATE_TEAM.grantedTo(getPlayer()))
        {
            int newTeamSlot = getPlayer().hasPermission("uh.player.renameTeam") ? getSize() - 4 : getSize() - 5;

            /// The title of a button to create a new team, in the selector GUI.
            action("new", newTeamSlot, GuiUtils.makeItem(Material.EMERALD, I.t("{white}New team")));
        }
    }

    @Override
    protected ItemStack getViewItem(ZTeam team)
    {
        final boolean isPlayerInTeam = team.getPlayersUUID().contains(getPlayer().getUniqueId());


        // Lore
        final List<String> lore = new ArrayList<>();

        lore.add("");

        if (team.getSize() != 0)
        {
            /// The "Players" title in the selector GUI, on a team's tooltip
            lore.add(I.t("{blue}Players"));

            /// An item of the players list in the selector GUI, on a team's tooltip
            team.getPlayers().stream().map(player -> I.t("{darkgray}- {white}{0}", player.getName())).forEach(lore::add);

            lore.add("");
        }

        if (ZTeamsPermission.JOIN_TEAM.grantedTo(getPlayer()) && !isPlayerInTeam)
        {
            if (!team.isFull())
            {
                lore.add(I.t("{darkgray}» {white}Click {gray}to join this team"));
            }
            else
            {
                lore.add(I.t("{darkgray}» {red}This team is full"));
            }
        }
        else if (ZTeamsPermission.LEAVE_TEAM.grantedTo(getPlayer()) && isPlayerInTeam)
        {
            lore.add(I.t("{darkgray}» {white}Click {gray}to leave this team"));
        }

        if (hasManagementPermission())
        {
            lore.add(I.t("{darkgray}» {white}Right-click {gray}to manage this team"));
        }


        // Item
        final ItemStack item;
        final DyeColor dye = ColorsUtils.chat2Dye(team.getColorOrWhite().toChatColor());

        switch (ZTeams.settings().teamsGUIItemType())
        {
            case BANNER:
                item = team.getBanner();
                break;

            case CLAY:
                item = new ItemStack(Material.STAINED_CLAY, 1, dye.getWoolData());
                break;

            case GLASS:
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dye.getWoolData());
                break;

            case GLASS_PANE:
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dye.getWoolData());
                break;

            case DYE:
                item = new ItemStack(Material.INK_SACK, 1, dye.getDyeData());
                break;

            case WOOL:
            default:
                item = new ItemStack(Material.WOOL, 1, dye.getWoolData());
        }


        // Title
        final String title = ZTeams.settings().maxPlayersPerTeam() != 0
                /// Title of the team item in the teams selector GUI (with max). {0}: team display name. {1}: players count. {2}: max count.
                ? I.t("{white}Team {0} {gray}({1}/{2})", team.getDisplayName(), team.getSize(), ZTeams.settings().maxPlayersPerTeam())
                /// Title of the team item in the teams selector GUI (without max) {0}: team display name. {1}: players count.
                : I.tn("{white}Team {0} {gray}({1} player)", "{white}Team {0} {gray}({1} players)", team.getSize(), team.getDisplayName(), team.getSize());

        return new ItemStackBuilder(item)
                .title(title)
                .lore(lore)
                .glow(ZTeams.settings().teamsGUIGlowOnCurrentTeam() && isPlayerInTeam)
                .hideAttributes()
                .item();
    }

    @Override
    protected ItemStack getEmptyViewItem()
    {
        return new ItemStackBuilder(Material.BARRIER)
                .title(I.t("{red}No team created"))
                .lore(getPlayer().hasPermission("uh.team")
                        /// Subtitle of the item displayed in the teams selector GUI if there isn't anything to display.
                        ? GuiUtils.generateLore(I.t("{gray}Click the emerald button below to create one."))
                        /// Subtitle of the item displayed in the teams selector GUI if there isn't anything to display and the player cannot create a team.
                        : GuiUtils.generateLore(I.t("{gray}Wait for an administrator to create one.")))
                .hideAttributes()
                .item();
    }

    @Override
    protected ItemStack getPickedUpItem(ZTeam team)
    {
        final boolean playerInTeam = team.getPlayersUUID().contains(getPlayer().getUniqueId());

        if (ZTeamsPermission.JOIN_TEAM.grantedTo(getPlayer()) && !playerInTeam)
        {
            try
            {
                team.addPlayer(getPlayer());
            }
            catch (RuntimeException ignored) {} // team full, does nothing
        }
        else if (ZTeamsPermission.LEAVE_TEAM.grantedTo(getPlayer()) && playerInTeam)
        {
            team.removePlayer(getPlayer());
        }

        update();
        return null;
    }

    @Override
    protected void onRightClick(ZTeam team)
    {
        if (hasManagementPermission())
        {
            Gui.open(getPlayer(), new TeamEditGUI(team), this);
        }
        else
        {
            getPickedUpItem(team);
        }
    }

    @GuiAction ("rename")
    public void rename()
    {
        final ZTeam team = ZTeams.get().getTeamForPlayer(getPlayer());
        if (team == null) return;

        Gui.open(getPlayer(), new PromptGui(team::setName, team.getName()), this);
    }

    @GuiAction ("new")
    public void newTeam()
    {
        Gui.open(getPlayer(), new TeamBuilderStepColorGUI());
    }

    private boolean hasManagementPermission()
    {
        return ZTeamsPermission.UPDATE_TEAM_NAME.grantedTo(getPlayer())
                || ZTeamsPermission.UPDATE_TEAM_COLOR.grantedTo(getPlayer())
                || ZTeamsPermission.UPDATE_TEAM_BANNER.grantedTo(getPlayer());
    }
}
