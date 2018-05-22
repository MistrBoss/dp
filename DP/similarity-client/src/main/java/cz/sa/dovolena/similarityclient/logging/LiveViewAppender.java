package cz.sa.dovolena.similarityclient.logging;

import cz.sa.dovolena.similarityclient.Settings;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender, ktery uchovava definovany pocet poslednich logu.
 * Umoznuje pomoci metody getLogsFrom(Date time) ziskat seznam logu, ktere byly zalogovany
 * od daneho data.
 * Pocet uchovavanych logu se nastavuje bud v konstruktoru, nebo pomoci metody setCacheSize(int size).
 * Jakmile velikost cache presahne urcenou velikost, nejstarsi zprava se odstrani.
 *
 * @author Dobroslav Pelc
 */
public class LiveViewAppender extends AppenderSkeleton {

    private SortedMap<Long, String> messages;
    private long cacheSize = 1000;

    public LiveViewAppender(Layout layout, int cacheSize) {
        this(layout);
        this.cacheSize = cacheSize;
    }

    public LiveViewAppender(Layout layout) {
        this.messages = new TreeMap<Long, String>();
        this.layout = layout != null ? layout : getDefaultLayout();
    }

    @Override
    protected void append(LoggingEvent event) {
        long timestamp = event.timeStamp;//getTimeStamp();
        // zakladni radek
        String text = layout.format(event);
        // potreba pridat stacktrace
        if (layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;
                for (int i = 0; i < len; i++) {
                    text += s[i];
                    text += Layout.LINE_SEP;
                }
            }
        }

        messages.put(timestamp, text);

        // kontrola preteceni cache
        if (messages.size() > cacheSize) {
            long key = messages.firstKey();
            messages.remove(key);
        }
    }

    /**
     * Ziskani seznamu zprav, ktere byly zalogovany od urciteho data.
     * Zpravy se neodstrani, takze je mozno metodu provolavat paralelne z ruznych vlaken
     * bez vlivu na funkcnost.
     * Logy jsou serazeny od nejstarsiho po nejnovejsi.
     *
     * @param time Cas, od ktereho jsou logy pozadovany
     * @param toList Seznam, do ktereho se maji nove logy vlozit
     * @return Cas, od ktereho ma byt pristi synchronizace vyzadovana
     */
    synchronized public Date loadLogsFrom(Date time, List<String> toList) {
        long longtime = time.getTime();
        SortedMap<Long, String> tail = messages.tailMap(longtime);
        if (!tail.isEmpty()) {
            // pro priste maji byt vyzadovany logy o 1ms po soucasnem
            long nextTime = tail.lastKey() + 1;
            toList.addAll(tail.values());
            return new Date(nextTime);
        } else {
            return time;
        }
    }

    @Override
    public void close() {
        messages.clear();
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    private Layout getDefaultLayout() {
        return Settings.CONSOLE_PATTERN;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }
}
