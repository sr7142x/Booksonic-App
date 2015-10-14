/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package github.popeen.booksonic.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import github.popeen.booksonic.domain.MusicDirectory;
import github.popeen.booksonic.domain.Playlist;
import github.popeen.booksonic.util.FileUtil;
import github.popeen.booksonic.util.Util;
import github.popeen.booksonic.dsub.R;

public class PlaylistSongView extends UpdateView2<Playlist, List<MusicDirectory.Entry>> {
	private static final String TAG = PlaylistSongView.class.getSimpleName();

	private TextView titleView;
	private TextView countView;
	private int count = 0;

	public PlaylistSongView(Context context) {
		super(context, false);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.basic_count_item, this, true);

		titleView = (TextView) findViewById(R.id.basic_count_name);
		countView = (TextView) findViewById(R.id.basic_count_count);
	}

	protected void setObjectImpl(Playlist playlist, List<MusicDirectory.Entry> songs) {
		count = 0;
		titleView.setText(playlist.getName());
		// Make sure to hide initially so it's not present briefly before update
		countView.setVisibility(GONE);
	}

	@Override
	protected void updateBackground() {
		// Make sure to reset when starting count
		count = 0;
		
		// Don't try to lookup playlist for Create New
		if(!"-1".equals(item.getId())) {
			MusicDirectory cache = FileUtil.deserialize(context, Util.getCacheName(context, "playlist", item.getId()), MusicDirectory.class);
			if(cache != null) {
				// Try to find song instances in the given playlists
				for(MusicDirectory.Entry song: item2) {
					if(cache.getChildren().contains(song)) {
						count++;
					}
				}
			}
		}
	}

	@Override
	protected void update() {
		// Update count display with appropriate information
		if(count <= 0) {
			countView.setVisibility(GONE);
		} else {
			String displayName;
			if(count < 10) {
				displayName = "0" + count;
			} else {
				displayName = "" + count;
			}

			countView.setText(displayName);
			countView.setVisibility(VISIBLE);
		}
	}
}