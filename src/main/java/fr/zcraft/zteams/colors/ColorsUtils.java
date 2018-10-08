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
package fr.zcraft.zteams.colors;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;


public final class ColorsUtils
{
    private ColorsUtils() {}

    public static DyeColor chat2Dye(ChatColor chatColor)
    {
        switch (chatColor)
        {
            case BLACK:
                return DyeColor.BLACK;

            case BLUE:
            case DARK_BLUE:
                return DyeColor.BLUE;

            case DARK_GREEN:
                return DyeColor.GREEN;

            case DARK_AQUA:
                return DyeColor.CYAN;

            case DARK_RED:
                return DyeColor.RED;

            case DARK_PURPLE:
                return DyeColor.PURPLE;

            case GOLD:
            case YELLOW:
                return DyeColor.YELLOW;

            case GRAY:
            case DARK_GRAY:
                return DyeColor.SILVER;

            case GREEN:
                return DyeColor.LIME;

            case AQUA:
                return DyeColor.LIGHT_BLUE;

            case RED:
                return DyeColor.ORANGE;

            case LIGHT_PURPLE:
                return DyeColor.PINK;

            // White, reset & formatting
            default:
                return DyeColor.WHITE;
        }
    }
}
