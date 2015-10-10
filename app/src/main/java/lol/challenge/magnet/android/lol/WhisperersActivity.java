package lol.challenge.magnet.android.lol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.magnet.mmx.client.api.ListResult;
import com.magnet.mmx.client.api.MMX;
import com.magnet.mmx.client.api.MMXUser;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import lol.challenge.magnet.android.lol.adapter.UsersRecyclerViewAdapter;
import lol.challenge.magnet.android.lol.models.User;

public class WhisperersActivity extends AppCompatActivity {

    final String TAG = "WhisperersActivity";

    RecyclerView rvUsers;
    List<User> userlist;

    UsersRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whisperers);

        if (MMX.getCurrentUser() == null) {
            MMX.logout(null);
            Intent intent = new Intent(WhisperersActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);

        userlist = new ArrayList<>();
        adapter = new UsersRecyclerViewAdapter(this, userlist);
        rvUsers.setAdapter(new SlideInBottomAnimationAdapter(adapter));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUsers.setLayoutManager(layoutManager);

        updateViewState();
    }

    private void updateViewState() {
        // return all users
        MMXUser.findByName("%", 50, new MMXUser.OnFinishedListener<ListResult<MMXUser>>() {
            public void onSuccess(ListResult<MMXUser> users) {
                refreshListView(users.totalCount > 0 ? users.items : null);
            }

            public void onFailure(MMXUser.FailureCode failureCode, Throwable throwable) {
                Log.e(TAG, "MMXUser.findByName() error: " + failureCode, throwable);
                refreshListView(null);
            }
        });
    }

    protected void refreshListView(final List<MMXUser> users) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                if (users != null && users.size() > 0) {
                    adapter.addAll(users);
                    rvUsers.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(WhisperersActivity.this, "No users found.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whisperers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
//noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {
//            LoginManager.getInstance().logOut();
            MMX.logout(null);
            Intent intent = new Intent(WhisperersActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_refresh) {
            updateViewState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
