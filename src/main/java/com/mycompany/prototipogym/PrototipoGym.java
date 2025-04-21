package com.mycompany.prototipogym;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import com.formdev.flatlaf.FlatDarkLaf;

public class PrototipoGym {

    public static void setUIFont(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Cargar fuente desde archivo
            Font oswaldFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/OpenSans-VariableFont_wdth,wght.ttf")).deriveFont(Font.PLAIN, 12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(oswaldFont);

            // Aplicar fuente globalmente
            setUIFont(oswaldFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}

