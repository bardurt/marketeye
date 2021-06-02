package com.zygne.zchart.volumeprofile.util;

public class GroupingGenerator {

    private static final double[] groups = {
            0.05d,
            0.1d,
            0.25d,
            0.5d,
            1d,
            2d,
            5d,
            10d,
            50d,
            100d,
            500d,
            1000d};

    public static double generateGrouping(double grouping) {

        boolean set = false;
        for (int i = 0; i < groups.length; i++) {
            if (grouping <= groups[i]) {
                grouping = groups[i];
                set = true;
                break;
            }
        }

        if (!set) {
            grouping = groups[groups.length - 1];
        }

        return grouping;
    }

    public static double increment(double grouping) {

        grouping = generateGrouping(grouping);

        for (int i = 0; i < groups.length; i++) {
            if (grouping == groups[i]) {
                if (i < groups.length - 1) {
                    grouping = groups[i + 1];
                    break;
                } else {
                    grouping = groups[groups.length - 1];
                }
            }
        }

        return grouping;
    }

    public static double decrement(double grouping) {
        grouping = generateGrouping(grouping);

        for (int i = 0; i < groups.length; i++) {
            if (grouping == groups[i]) {
                if (i > 1) {
                    grouping = groups[i - 1];
                    break;
                } else {
                    grouping = groups[0];
                }
            }
        }

        return grouping;
    }

    public static void main(String[] args) {
        double group = 0.001;

        System.out.println(decrement(group));

        group = 0.05;

        System.out.println(decrement(group));

        group = 25;

        System.out.println(decrement(group));

        group = 2000;

        System.out.println(decrement(group));
    }
}
