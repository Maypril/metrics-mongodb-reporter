package se.maypril.metrics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.runners.MockitoJUnitRunner;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBReporterTest {

    private final static long timestamp = 1000198;
    private final Clock clock = mock(Clock.class);
    private final MetricRegistry registry = mock(MetricRegistry.class);
    private final DB database = mock(DB.class);

    private final DBCollection gaugeCollection = mock(DBCollection.class);
    private final DBCollection counterCollection = mock(DBCollection.class);
    private final DBCollection histogramCollection = mock(DBCollection.class);
    private final DBCollection timerCollection = mock(DBCollection.class);
    private final DBCollection meteredCollection = mock(DBCollection.class);

    private final MongoDBReporter reporter = MongoDBReporter.forRegistry(registry).withClock(clock).prefixedWith("prefix").convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL).build(database);

    @Before
    public void setUp() throws Exception {
        when(clock.getTime()).thenReturn(timestamp * 1000);
        when(database.getCollection("gauge")).thenReturn(gaugeCollection);
        when(database.getCollection("counter")).thenReturn(counterCollection);
        when(database.getCollection("histogram")).thenReturn(histogramCollection);
        when(database.getCollection("metered")).thenReturn(meteredCollection);
        when(database.getCollection("timer")).thenReturn(timerCollection);

    }

    @Test
    public void doesNotReportStringGaugeValues() throws Exception {

        reporter.report(map("gauge", gauge("value")), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        final InOrder inOrder = inOrder(database);
        inOrder.verify(database).getCollection(eq("gauge"));

        verifyNoMoreInteractions(gaugeCollection);
    }

    @Test
    public void reportsByteGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge((byte) 1)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", (byte) 1, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));

        verifyNoMoreInteractions(gaugeCollection);
    }

    @Test
    public void reportsShortGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge((short) 1)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", (short) 1, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));

        verifyNoMoreInteractions(gaugeCollection);
    }

    @Test
    public void reportsIntegerGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge(1)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", 1, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));

        verifyNoMoreInteractions(gaugeCollection);

    }

    @Test
    public void reportsLongGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge(1L)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", 1L, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));
        System.out.println(value);
        verifyNoMoreInteractions(gaugeCollection);

    }

    @Test
    public void reportsFloatGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge(1.1f)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());
        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", 1.1f, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));
        verifyNoMoreInteractions(gaugeCollection);

    }

    @Test
    public void reportsDoubleGaugeValues() throws Exception {
        reporter.report(map("gauge", gauge(1.1)), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());
        verify(database).getCollection(eq("gauge"));
        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(gaugeCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save correct value", 1.1, value.get("value"));
        assertEquals("Does not save corret name", "prefix.gauge", value.get("name"));
        verifyNoMoreInteractions(gaugeCollection);

    }

    @Test
    public void reportsCounters() throws Exception {
        final Counter counter = mock(Counter.class);
        when(counter.getCount()).thenReturn(100L);

        reporter.report(this.<Gauge> map(), this.<Counter> map("counter", counter), this.<Histogram> map(), this.<Meter> map(), this.<Timer> map());

        verify(database).getCollection(eq("counter"));

        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(counterCollection).save(captor.capture());

        final DBObject value = captor.getValue();
        assertEquals("Does not save corret name", "prefix.counter.count", value.get("name"));
        assertEquals("Does not save correct count", 100L, value.get("count"));
    }

    @Test
    public void reportsHistograms() throws Exception {
        final Histogram histogram = mock(Histogram.class);
        when(histogram.getCount()).thenReturn(1L);

        final Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getMax()).thenReturn(2L);
        when(snapshot.getMean()).thenReturn(3.0);
        when(snapshot.getMin()).thenReturn(4L);
        when(snapshot.getStdDev()).thenReturn(5.0);
        when(snapshot.getMedian()).thenReturn(6.0);
        when(snapshot.get75thPercentile()).thenReturn(7.0);
        when(snapshot.get95thPercentile()).thenReturn(8.0);
        when(snapshot.get98thPercentile()).thenReturn(9.0);
        when(snapshot.get99thPercentile()).thenReturn(10.0);
        when(snapshot.get999thPercentile()).thenReturn(11.0);

        when(histogram.getSnapshot()).thenReturn(snapshot);

        reporter.report(this.<Gauge> map(), this.<Counter> map(), this.<Histogram> map("histogram", histogram), this.<Meter> map(), this.<Timer> map());
        verify(database).getCollection(eq("histogram"));

        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(histogramCollection).save(captor.capture());

        final DBObject value = captor.getValue();
        assertEquals("Does not save corret name", "prefix.histogram", value.get("name"));

        assertEquals("Does not have correct count", 1L, value.get("count"));
        assertEquals("Does not have correct max", 2L, value.get("max"));
        assertEquals("Does not have correct mean", 3.0, value.get("mean"));
        assertEquals("Does not have correct min", 4L, value.get("min"));
        assertEquals("Does not have correct stdDev", 5.0, value.get("stdDev"));
        assertEquals("Does not have correct median", 6.0, value.get("median"));
        assertEquals("Does not have correct p75", 7.0, value.get("p75"));
        assertEquals("Does not have correct p95", 8.0, value.get("p95"));
        assertEquals("Does not have correct p98", 9.0, value.get("p98"));
        assertEquals("Does not have correct p99", 10.0, value.get("p99"));
        assertEquals("Does not have correct p999", 11.0, value.get("p999"));

    }

    @Test
    public void reportsMeters() throws Exception {
        final Meter meter = mock(Meter.class);
        when(meter.getCount()).thenReturn(1L);
        when(meter.getOneMinuteRate()).thenReturn(2.0);
        when(meter.getFiveMinuteRate()).thenReturn(3.0);
        when(meter.getFifteenMinuteRate()).thenReturn(4.0);
        when(meter.getMeanRate()).thenReturn(5.0);

        reporter.report(this.<Gauge> map(), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map("meter", meter), this.<Timer> map());
        verify(database).getCollection(eq("metered"));

        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(meteredCollection).save(captor.capture());
        final DBObject value = captor.getValue();
        assertEquals("Does not save corret name", "prefix.meter", value.get("name"));

        assertEquals("Does not have correct count", 1L, value.get("count"));
        assertEquals("Does not have correct one minute rate", 2.0, value.get("m1Rate"));
        assertEquals("Does not have correct five minute rate", 3.0, value.get("m5Rate"));
        assertEquals("Does not have correct fifteen minute rate", 4.0, value.get("m15Rate"));
        assertEquals("Does not have correct mean rate", 5.0, value.get("meanRate"));

    }

    @Test
    public void reportsTimers() throws Exception {
        final Timer timer = mock(Timer.class);
        when(timer.getCount()).thenReturn(1L);
        when(timer.getMeanRate()).thenReturn(2.0);
        when(timer.getOneMinuteRate()).thenReturn(3.0);
        when(timer.getFiveMinuteRate()).thenReturn(4.0);
        when(timer.getFifteenMinuteRate()).thenReturn(5.0);

        final Snapshot snapshot = mock(Snapshot.class);
        when(snapshot.getMax()).thenReturn(TimeUnit.MILLISECONDS.toNanos(100));
        when(snapshot.getMean()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(200));
        when(snapshot.getMin()).thenReturn(TimeUnit.MILLISECONDS.toNanos(300));
        when(snapshot.getStdDev()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(400));
        when(snapshot.getMedian()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(500));
        when(snapshot.get75thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(600));
        when(snapshot.get95thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(700));
        when(snapshot.get98thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(800));
        when(snapshot.get99thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(900));
        when(snapshot.get999thPercentile()).thenReturn((double) TimeUnit.MILLISECONDS.toNanos(1000));

        when(timer.getSnapshot()).thenReturn(snapshot);

        reporter.report(this.<Gauge> map(), this.<Counter> map(), this.<Histogram> map(), this.<Meter> map(), map("timer", timer));

        verify(database).getCollection(eq("timer"));

        final ArgumentCaptor<DBObject> captor = ArgumentCaptor.forClass(DBObject.class);
        verify(timerCollection).save(captor.capture());
        final DBObject value = captor.getValue();

        assertEquals("Does not save corret name", "prefix.timer", value.get("name"));

        assertEquals("Does not have correct count", 1L, value.get("count"));
        assertEquals("Does not have correct one minute rate", 3.0, value.get("m1Rate"));
        assertEquals("Does not have correct five minute rate", 4.0, value.get("m5Rate"));
        assertEquals("Does not have correct fifteen minute rate", 5.0, value.get("m15Rate"));
        assertEquals("Does not have correct mean rate", 2.0, value.get("meanRate"));

        final DBObject reportedSnaphot = (DBObject) value.get("snapshot");

        assertEquals("Does not have correct max", 100000000L, reportedSnaphot.get("max"));
        assertEquals("Does not have correct mean", 2.0E8, reportedSnaphot.get("mean"));
        assertEquals("Does not have correct min", 300000000L, reportedSnaphot.get("min"));
        assertEquals("Does not have correct stdDev", 4.0E8, reportedSnaphot.get("stdDev"));
        assertEquals("Does not have correct median", 5.0E8, reportedSnaphot.get("median"));
        assertEquals("Does not have correct p75", 6.0E8, reportedSnaphot.get("p75"));
        assertEquals("Does not have correct p95", 7.0E8, reportedSnaphot.get("p95"));
        assertEquals("Does not have correct p98", 8.0E8, reportedSnaphot.get("p98"));
        assertEquals("Does not have correct p99", 9.0E8, reportedSnaphot.get("p99"));
        assertEquals("Does not have correct p999", 1.0E9, reportedSnaphot.get("p999"));

    }

    private <T> SortedMap<String, T> map() {
        return new TreeMap<String, T>();
    }

    private <T> SortedMap<String, T> map(final String name, final T metric) {
        final TreeMap<String, T> map = new TreeMap<String, T>();
        map.put(name, metric);
        return map;
    }

    private <T> Gauge gauge(final T value) {
        final Gauge gauge = mock(Gauge.class);
        when(gauge.getValue()).thenReturn(value);
        return gauge;
    }

}
