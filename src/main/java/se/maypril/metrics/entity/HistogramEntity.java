package se.maypril.metrics.entity;

import com.codahale.metrics.Snapshot;

public class HistogramEntity extends CoreEntity {

    private Long count;

    private Long max;

    private Double mean;

    private Long min;

    private Double stdDev;

    private Double median;

    private Double p75;

    private Double p95;

    private Double p98;

    private Double p99;

    private Double p999;


    public HistogramEntity(final Snapshot snapshot) {
        max = snapshot.getMax();
        mean = snapshot.getMean();
        min = snapshot.getMin();
        stdDev = snapshot.getStdDev();
        median = snapshot.getMedian();
        p75 = snapshot.get75thPercentile();
        p95 = snapshot.get95thPercentile();
        p98 = snapshot.get98thPercentile();
        p99 = snapshot.get99thPercentile();
        p999 = snapshot.get999thPercentile();
    }


    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(final Long max) {
        this.max = max;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(final Double mean) {
        this.mean = mean;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(final Long min) {
        this.min = min;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(final Double stdDev) {
        this.stdDev = stdDev;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(final Double median) {
        this.median = median;
    }

    public Double getP75() {
        return p75;
    }

    public void setP75(final Double p75) {
        this.p75 = p75;
    }

    public Double getP95() {
        return p95;
    }

    public void setP95(final Double p95) {
        this.p95 = p95;
    }

    public Double getP98() {
        return p98;
    }

    public void setP98(final Double p98) {
        this.p98 = p98;
    }

    public Double getP99() {
        return p99;
    }

    public void setP99(final Double p99) {
        this.p99 = p99;
    }

    public Double getP999() {
        return p999;
    }

    public void setP999(final Double p999) {
        this.p999 = p999;
    }

}
