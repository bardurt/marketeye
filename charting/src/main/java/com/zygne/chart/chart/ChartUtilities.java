package com.zygne.chart.chart;

import com.zygne.chart.chart.model.chart.VolumeProfileBar;

public class ChartUtilities {


    public static void setColorScheme(VolumeProfileBar volumeProfileBar, double percent) {
        if (percent > 0.95) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.RED);
        } else if (percent > 0.80) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.ORANGE);
        } else if (percent > 0.70) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.YELLOW);
        } else if (percent > 0.55) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.GREEN);
        }
    }

    public static void setColorSchemeTop2(VolumeProfileBar volumeProfileBar, double percent) {
        if (percent > 0.98) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.RED);
        } else if (percent > 0.95) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.ORANGE);
        }
    }
}
