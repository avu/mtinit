package com.intellij.mobiusrss;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class ChooseSourceActivity extends Activity {

//  private List<RssFeedInfo> mySources;
//  private SourcesListAdapter myListViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_source);

    final ListView listView = (ListView) findViewById(R.id.sourcesListView);
    final List<String> sources = Arrays.asList(
        "http://news.google.ru/news?pz=1&cf=all&ned=ru_ru&hl=ru&output=rss",
        "https://developer.apple.com/news/rss/news.rss");
    listView.setAdapter(new ArrayAdapter<String>(
        this, android.R.layout.simple_list_item_1, sources));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String url = sources.get(position);
        startActivity(FeedActivity.createIntent(ChooseSourceActivity.this, url));
      }
    });
//    mySources = new ArrayList<RssFeedInfo>(PreferenceUtil.doLoadSources(this));
//    myListViewAdapter = new SourcesListAdapter(this, mySources);
//    listView.setAdapter(myListViewAdapter);
  }

  /*private void doAddSource(String url, RssFeed rssFeed) {
    mySources.add(new RssFeedInfo(url, rssFeed.getTitle(), rssFeed.getDescription()));
    PreferenceUtil.doSaveSources(this, mySources);
  }

  private void doAddSource(String url) {
    new MyRssFeedLoadingTask(url).execute();
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
            .setMessage("Cannot load RSS feed")
            .setNeutralButton(R.string.ok, null).create().show();
      }
    }
  }

  public static class AddSourceDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Activity activity = getActivity();
      @SuppressWarnings("ConstantConditions")
      final EditText input = new EditText(activity);
      input.setInputType(EditorInfo.TYPE_TEXT_VARIATION_URI);

      return new AlertDialog.Builder(activity).setMessage(getString(R.string.input_url))
          .setView(input)
          .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              final Editable text = input.getText();

              if (text != null) {
                final String url = text.toString();

                if (!url.isEmpty()) {
                  ((ChooseSourceActivity) activity).doAddSource(url);
                }
              }
            }
          })
          .setNegativeButton(getString(R.string.cancel), null)
          .create();
    }
  }*/
}
