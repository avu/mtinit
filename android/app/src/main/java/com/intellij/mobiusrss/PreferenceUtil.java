package com.intellij.mobiusrss;

import android.app.Activity;
import android.content.Context;

import java.util.Collections;
import java.util.List;

/**
 * @author Eugene.Kudelevsky
 */
public class PreferenceUtil {
  static final String SOURCES_PREFERENCE_KEY = "sources";

  public static List<RssFeedInfo> doLoadSources(Activity activity) {
    final String sourcesStr = activity.getPreferences(Context.MODE_PRIVATE).getString(SOURCES_PREFERENCE_KEY, "");
    List<RssFeedInfo> infos;

    if (!sourcesStr.isEmpty()) {
      infos = RssFeedInfo.readListFrom(sourcesStr);
    }
    else {
      infos = Collections.emptyList();
    }
    return infos;
  }

  public static void doSaveSources(Activity activity, List<RssFeedInfo> sources) {
    final String s = RssFeedInfo.writeListTo(sources);
    activity.getPreferences(Context.MODE_PRIVATE).edit().putString(SOURCES_PREFERENCE_KEY, s).commit();
  }
}
