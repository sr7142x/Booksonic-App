/*
  This file is part of Subsonic.
	Subsonic is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	Subsonic is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	GNU General Public License for more details.
	You should have received a copy of the GNU General Public License
	along with Subsonic. If not, see <http://www.gnu.org/licenses/>.
	Copyright 2014 (C) Scott Jackson
*/

package github.popeen.booksonic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import github.popeen.booksonic.domain.User;
import github.popeen.booksonic.util.ImageLoader;
import github.popeen.booksonic.view.SettingView;
import github.popeen.booksonic.dsub.R;
import github.popeen.booksonic.view.UpdateView;

public class SettingsAdapter extends SectionAdapter<User.Setting> {
	public final int VIEW_TYPE_SETTING = 1;

	private final User user;
	private final boolean editable;
	private final ImageLoader imageLoader;

	public SettingsAdapter(Context context, User user, ImageLoader imageLoader, boolean editable) {
		super(context, user.getSettings(), imageLoader != null);
		this.user = user;
		this.imageLoader = imageLoader;
		this.editable = editable;

		List<User.Setting> settings = sections.get(0);
		for(User.Setting setting: settings) {
			if(setting.getValue()) {
				addSelected(setting);
			}
		}
	}

	public UpdateView.UpdateViewHolder onCreateHeaderHolder(ViewGroup parent) {
		View header = LayoutInflater.from(context).inflate(R.layout.user_header, parent, false);
		return new UpdateView.UpdateViewHolder(header, false);
	}
	public void onBindHeaderHolder(UpdateView.UpdateViewHolder holder, String description) {
		View header = holder.getView();

		ImageView coverArtView = (ImageView) header.findViewById(R.id.user_avatar);
		imageLoader.loadAvatar(context, coverArtView, user.getUsername());

		TextView usernameView = (TextView) header.findViewById(R.id.user_username);
		usernameView.setText(user.getUsername());

		final TextView emailView = (TextView) header.findViewById(R.id.user_email);
		if(user.getEmail() != null) {
			emailView.setText(user.getEmail());
		} else {
			emailView.setVisibility(View.GONE);
		}
	}

	@Override
	public UpdateView.UpdateViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
		return new UpdateView.UpdateViewHolder(new SettingView(context));
	}

	@Override
	public void onBindViewHolder(UpdateView.UpdateViewHolder holder, User.Setting item, int viewType) {
		holder.getUpdateView().setObject(item, editable);
	}

	@Override
	public int getItemViewType(User.Setting item) {
		return VIEW_TYPE_SETTING;
	}

	@Override
	public void setChecked(UpdateView updateView, boolean checked) {
		if(updateView instanceof SettingView) {
			((SettingView) updateView).setChecked(checked);
		}
	}
}