package lol.challenge.magnet.android.lol.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.readystatesoftware.viewbadger.BadgeView;

import de.hdodenhof.circleimageview.CircleImageView;
import lol.challenge.magnet.android.lol.R;

/**
 * This adapter I created just to show how the
 * layout would look like.
 * I used a library here Badge view on github
 * https://github.com/jgilfelt/android-viewbadger
 *
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;

    public ChatAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.chat_list,viewGroup,false);
        View invisibleView = (View) view.findViewById(R.id.invisible_for_badge_view);
        BadgeView badgeView = new BadgeView(context,invisibleView);
        badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
        badgeView.setText("f");
        badgeView.setTextSize(12);
        badgeView.setBackgroundColor(view.getResources().getColor(R.color.com_facebook_button_background_color));
        badgeView.show();
        return view;
    }
}
