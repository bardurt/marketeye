package com.zygne.zchart.volumeprofile;

import com.zygne.zchart.volumeprofile.model.chart.VolumeProfileBar;

public class ChartUtilities {


    public static void setColorScheme(VolumeProfileBar volumeProfileBar, double percent) {
        if (percent > 0.9) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.RED);
        } else if (percent > 0.75) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.ORANGE);
        } else if (percent > 0.55) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.YELLOW);
        } else if (percent > 0.48) {
            volumeProfileBar.setColorSchema(VolumeProfileBar.ColorSchema.GREEN);
        }
    }
}
