package com.zygne.zchart.volumeprofile.model.chart;

public class VolumeProfileBar extends Object2d {

    private TextObject textObject = new TextObject();
    private boolean printLabel = false;
    private String text;
    private long volume;
    private double percentage;
    private ColorSchema initialColorSchema = ColorSchema.BLUE;
    private ColorSchema colorSchema = ColorSchema.BLUE;
    private ColorSchema highlightSchema = ColorSchema.HIGHLIGHT;

    private String boxColorHex;
    private String outlineColorHex;

    @Override
    public void draw(Canvas canvas) {
        int posX = x;
        int posY = y;
        int boxWith = (int) width;
        int boxHeight = (int) height;
        this.textObject.setX(x-40);
        this.textObject.setY(y);
        this.textObject.setWidth(40);
        this.textObject.setHeight(height);

        prepareColors();

        canvas.setColor(boxColorHex);
        canvas.drawRectangle(posX, posY, boxWith, boxHeight, Canvas.Fill.SOLID);

        canvas.setColor(outlineColorHex);
        canvas.drawRectangle(posX, posY, boxWith, boxHeight, Canvas.Fill.OUTLINE);

        if (printLabel) {
            textObject.draw(canvas);
        }
    }

    private void prepareColors() {
        if (colorSchema == ColorSchema.BLUE) {
            boxColorHex = "#0093FF";
            outlineColorHex = "#00C5FF";
        } else if (colorSchema == ColorSchema.RED) {
            boxColorHex = "#FF0000";
            outlineColorHex = "#FF4900";
        } else if (colorSchema == ColorSchema.ORANGE) {
            boxColorHex = "#FF9E00";
            outlineColorHex = "#FFBD00";
        } else if (colorSchema == ColorSchema.YELLOW) {
            boxColorHex = "#FFE800";
            outlineColorHex = "#FDFF66";
        } else if (colorSchema == ColorSchema.GREEN) {
            boxColorHex = "#23FF00";
            outlineColorHex = "#86FFB0";
        } else if (colorSchema == ColorSchema.HIGHLIGHT) {
            boxColorHex = "#FFFFFF";
            outlineColorHex = "#FFFFFF";
        }

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.textObject.setText(text);
        this.textObject.setFontSize(TextObject.FontSize.SMALL);
    }


    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        this.initialColorSchema = colorSchema;
        this.colorSchema = initialColorSchema;
    }

    public boolean isPrintLabel() {
        return printLabel;
    }

    public void setPrintLabel(boolean printLabel) {
        this.printLabel = printLabel;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public long getVolume() {
        return volume;
    }

    public String getVolumeTest() {
        return String.format("%,d", volume);
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public void highLight(boolean b) {
        if (b) {
            this.colorSchema = highlightSchema;
        } else {
            this.colorSchema = initialColorSchema;
        }
    }
}