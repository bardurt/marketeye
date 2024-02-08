package com.zygne.chart.chart.model.chart;

import com.zygne.chart.chart.Canvas;

public class TextObject extends Object2d {

    private String color = "#FFFFFF";
    private boolean useColorScheme = false;

    public enum FontSize {
        SMALL_EXTRA,
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum Alignment {
        CENTER,
        LEFT,
        RIGHT
    }

    private int scalar;
    private FontSize fontSize = FontSize.SMALL;
    private Alignment alignment = Alignment.CENTER;
    private String text;
    private int pointSize;

    public TextObject() {
        this.x = 0;
        this.y = 0;
        this.width = 1;
        this.height = 1;
    }

    public TextObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private void calculateScalar() {
        switch (fontSize) {
            case SMALL_EXTRA -> {
                pointSize = 10;
                scalar = 6;
            }
            case SMALL -> {
                pointSize = 12;
                scalar = 8;
            }
            case MEDIUM -> {
                pointSize = 24;
                scalar = 9;
            }
            case LARGE -> {
                pointSize = 72;
                scalar = 36;
            }
        }
    }

    @Override
    public void setColorSchema(ColorSchema colorSchema) {
        super.setColorSchema(colorSchema);
        useColorScheme = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if(text == null){
            return;
        }
        calculateScalar();


        int textCenterX;
        int textCenterY;

        if (alignment == Alignment.LEFT) {
            textCenterX = x;
            textCenterY = y + height / 2 + (scalar / 2);
        } else if (alignment == Alignment.CENTER) {
            textCenterX = (x + width / 2) - ((text.length() / 2) * scalar);
            textCenterY = y + height / 2 + (scalar / 2);
        } else {
            textCenterX = (x + width) - ((text.length()) * scalar);
            textCenterY = y + height / 2 + (scalar / 2);
        }

        if(useColorScheme){
            switch (colorSchema) {
                case WHITE -> {
                    color = Colors.WHITE;
                }
                case RED -> {
                    color = Colors.RED;
                }
                case ORANGE -> {
                    color = Colors.ORANGE;
                }
                case YELLOW -> {
                    color = Colors.YELLOW;
                }
                case BLUE -> {
                    color = Colors.BLUE;
                }
                case GREEN -> {
                    color = Colors.GREEN;
                }
                case HIGHLIGHT -> {
                    color = "#B2E1BD";
                }
            }
        }
        canvas.setColor(color);

        if(text.contains("\n")){
            String[] parts = text.split("\n");
            String part1 = parts[0];
            String part2 = parts[1];
            canvas.drawString(part1, textCenterX, textCenterY-7, pointSize, Canvas.TextStyle.BOLD);
            canvas.drawString(part2, textCenterX, textCenterY+7, pointSize, Canvas.TextStyle.BOLD);
        } else {
            canvas.drawString(text, textCenterX, textCenterY, pointSize, Canvas.TextStyle.BOLD);
        }

    }
}
