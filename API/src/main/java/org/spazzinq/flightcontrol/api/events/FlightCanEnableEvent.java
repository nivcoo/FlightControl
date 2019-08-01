/*
 * This file is part of FlightControl-parent, which is licensed under the MIT License
 *
 * Copyright (c) 2019 Spazzinq
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
 */

package org.spazzinq.flightcontrol.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spazzinq.flightcontrol.api.events.interfaces.*;
import org.spazzinq.flightcontrol.api.objects.Sound;

public class FlightCanEnableEvent implements PlayerFlightEvent, LocationFlightEvent, MessageFlightEvent, SoundFlightEvent, Cancellable {
    @Getter private Player player;
    @Getter private Location location;
    @Getter @Setter private String message;
    @Getter @Setter private Sound sound;
    @Getter @Setter private boolean byActionbar;
    @Getter @Setter private boolean cancelled;

    public FlightCanEnableEvent(Player player, Location location, String message, Sound sound, boolean byActionbar) {
        this.player = player;
        this.location = location;
        this.message = message;
        this.sound = sound;
        this.byActionbar = byActionbar;
    }
}