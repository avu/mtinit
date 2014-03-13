package com.intellij.mobiusrss;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.matshofman.saxrssreader.RssFeed;

public class ChooseSourceActivity extends Activity {

  private List<RssFeedInfo> mySources;
  private BaseAdapter myListViewAdapter;
  private ListView myListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_source);

    myListView = (ListView) findViewById(R.id.sourcesListView);
    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final RssFeedInfo feedInfo = mySources.get(position);
        startActivity(FeedActivity.createIntent(ChooseSourceActivity.this, feedInfo.getUrl()));
      }
    });
    myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final boolean checked = !myListView.isItemChecked(position);
        myListView.setItemChecked(position, checked);

        if (checked) {
          view.setBackground(new ColorDrawable(Color.GRAY));
        }
        else {
          view.setBackground(null);
        }
        return true;
      }
    });
    mySources = new ArrayList<RssFeedInfo>(PreferenceUtil.doLoadSources(this));
    myListViewAdapter = new ArrayAdapter<RssFeedInfo>(this, android.R.layout.
        simple_list_item_1, mySources);

    myListView.setAdapter(myListViewAdapter);
    myListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.choose_source, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add:
        addItemSelected();
        return true;
      case R.id.remove:
        removeItemSelected();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void removeItemSelected() {
    final SparseBooleanArray positions = myListView.getCheckedItemPositions();

    if (positions != null && positions.size() > 0) {
      int i = 0;

      for (Iterator<RssFeedInfo> it = mySources.iterator(); it.hasNext(); ) {
        it.next();

        if (positions.get(i)) {
          positions.put(i, false);
          it.remove();
        }
        i++;
      }
      myListViewAdapter.notifyDataSetChanged();
      PreferenceUtil.doSaveSources(this, mySources);
    }
  }

  private void addItemSelected() {
    new AddSourceDialogFragment().show(getFragmentManager(), "add_source_dialog");
  }

  public void doAddSource(String url) {
    new MyRssFeedLoadingTask(url).execute();
  }

  private void doAddSource(String url, RssFeed rssFeed) {
    mySources.add(new RssFeedInfo(url, rssFeed.getTitle(), rssFeed.getDescription()));
    myListViewAdapter.notifyDataSetChanged();
    PreferenceUtil.doSaveSources(this, mySources);
  }

  public class MyRssFeedLoadingTask extends RssFeedLoadingTask {

    MyRssFeedLoadingTask(String url) {
      super(url);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(RssFeed rssFeed) {
      super.onPostExecute(rssFeed);

      if (rssFeed != null) {
        doAddSource(myUrl, rssFeed);
      } else {
        new AlertDialog.Builder(ChooseSourceActivity.this)
            .setMessage(getString(R.string.cannot_load_rss_feed))
            .setNeutralButton(R.string.ok, null).create().show();
      }
    }
  }

}
