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

package fr.zcraft.quartzteams.guis.builder;

import com.google.common.collect.ImmutableList;
import fr.zcraft.quartzlib.components.gui.Gui;
import fr.zcraft.quartzlib.components.gui.GuiAction;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.quartzlib.tools.items.ColorableMaterial;
import fr.zcraft.quartzlib.tools.items.ItemStackBuilder;
import fr.zcraft.quartzlib.tools.items.ItemUtils;
import fr.zcraft.quartzlib.tools.runners.RunTask;
import fr.zcraft.quartzteams.colors.TeamColor;
import fr.zcraft.quartzteams.texts.TextUtils;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;


public class TeamBuilderStepColorGUI extends TeamBuilderBaseGUI {
    private final static Random randomSource = new Random();
    private final static List<Material> colorBlocks = ImmutableList.of(
            Material.WHITE_CONCRETE,
            Material.BLACK_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.LIME_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.PINK_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.RED_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE
    );

    private BukkitTask randomUpdate = null;


    @Override
    protected void onUpdate() {
        /// The title of the color selector GUI, in the create team GUIs
        setTitle(I.t("New team » {black}Color"));
        setSize(6 * 9);

        generateBreadcrumbs(BuildingStep.COLOR);

        insertColors(3);
    }

    protected void insertColors(final int firstLine) {
        final int offset = (firstLine - 1) * 9;

        /// The random color button in a colors selector GUI.
        action("random", offset + 4, new ItemStackBuilder(randomColorBlock()).title(I.t("{lightpurple}Random color")));

        insertColor(offset + 10, ChatColor.WHITE);
        insertColor(offset + 11, ChatColor.AQUA);
        insertColor(offset + 12, ChatColor.BLUE);
        insertColor(offset + 13, ChatColor.GREEN);
        insertColor(offset + 14, ChatColor.YELLOW);
        insertColor(offset + 15, ChatColor.GOLD);
        insertColor(offset + 16, ChatColor.LIGHT_PURPLE);

        insertColor(offset + 19, ChatColor.RED);
        insertColor(offset + 20, ChatColor.DARK_RED);
        insertColor(offset + 21, ChatColor.DARK_GREEN);
        insertColor(offset + 22, ChatColor.DARK_PURPLE);
        insertColor(offset + 23, ChatColor.DARK_BLUE);
        insertColor(offset + 24, ChatColor.DARK_AQUA);
        insertColor(offset + 25, ChatColor.BLACK);

        insertColor(offset + 29, ChatColor.GRAY);
        insertColor(offset + 33, ChatColor.DARK_GRAY);

        randomUpdate = RunTask.timer(() ->
        {
            ItemStack random = getInventory().getItem(offset + 4);
            if (random != null) {
                random.setType(randomColorBlock());
            }
        }, 15L, 15L);
    }

    private void insertColor(int slot, ChatColor color) {
        action(
                "",
                slot,
                new ItemStackBuilder(ItemUtils.colorize(ColorableMaterial.CONCRETE, color))
                        // The color is extracted from the title, so it has to be directly inside,
                        // not as a JSON attribute.
                        // See #unknown_action(String, int, ItemStack).
                        .title(color + TextUtils.friendlyEnumName(color))
                        .item()
        );
    }

    private Material randomColorBlock() {
        return colorBlocks.get(randomSource.nextInt(colorBlocks.size()));
    }


    @GuiAction("random")
    protected void random() {
        saveColor(TeamColor.RANDOM);
    }

    @Override
    protected void unknown_action(String name, int slot, ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            saveColor(TeamColor.fromChatColor(ChatColor.getByChar(
                    ChatColor.getLastColors(item.getItemMeta().getDisplayName().replace(ChatColor.RESET.toString(), ""))
                            .substring(1))));
        }
    }

    protected void saveColor(TeamColor color) {
        Gui.open(getPlayer(), new TeamBuilderStepNameGUI(color));
    }

    @Override
    protected void onClose() {
        if (randomUpdate != null) {
            randomUpdate.cancel();
            randomUpdate = null;
        }

        super.onClose();
    }

    @Override
    protected TeamColor getColor() {
        return null;
    }

    @Override
    protected String getName() {
        return null;
    }
}
