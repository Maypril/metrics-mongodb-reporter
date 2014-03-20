package se.maypril.metrics.entity;

import com.codahale.metrics.Metered;

public class MeteredEntity extends CoreEntity {

    private Long count;
    private Double m1Rate;
    private Double m5Rate;
    private Double m15Rate;
    private Double meanRate;


    public MeteredEntity(final Metered meter) {
        count = meter.getCount();
        m1Rate = meter.getOneMinuteRate();
        m5Rate = meter.getFiveMinuteRate();
        m15Rate = meter.getFifteenMinuteRate();
        meanRate = meter.getMeanRate();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public Double getM1Rate() {
        return m1Rate;
    }

    public void setM1Rate(final Double m1Rate) {
        this.m1Rate = m1Rate;
    }

    public Double getM5Rate() {
        return m5Rate;
    }

    public void setM5Rate(final Double m5Rate) {
        this.m5Rate = m5Rate;
    }

    public Double getM15Rate() {
        return m15Rate;
    }

    public void setM15Rate(final Double m15Rate) {
        this.m15Rate = m15Rate;
    }

    public Double getMeanRate() {
        return meanRate;
    }

    public void setMeanRate(final Double meanRate) {
        this.meanRate = meanRate;
    }

}
