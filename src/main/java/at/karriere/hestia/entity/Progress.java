package at.karriere.hestia.entity;

import org.apache.log4j.Logger;

public class Progress {

    private final Logger LOGGER = Logger.getLogger(Progress.class);
    private Long dbsize;
    private Long scanned;
    private Integer db;
    private double lastProgress;
    private String host;

    public Progress(Long dbsize, Integer db, String host) {
        this.dbsize = dbsize;
        scanned = 0L;
        this.db = db;
        lastProgress = 0;
        this.host = host;
    }

    public void tick(Long count) {
        scanned += count;
        double progress = scanned / (double) dbsize;
        progress = progress * 100;
        progress = Math.round(progress * 100.0) / 100.0;
        if (progress > 100)
            progress = 100;
        if (lastProgress == 0 || (progress - lastProgress) > 10 || progress == 100) {
            lastProgress = progress;
            LOGGER.info("Scanned " + progress + "% of db" + db + " of server " + host);
        }
    }
}
