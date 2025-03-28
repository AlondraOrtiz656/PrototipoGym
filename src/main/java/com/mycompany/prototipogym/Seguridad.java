package com.mycompany.prototipogym;

import javax.swing.JOptionPane;

public class Seguridad {
    private final Login login;

    public Seguridad() {
        this.login = new Login();
    }

    public void validarUsuario(String[] usuarios, String user, String pwd, int intentos) {
        boolean encontrado = false;

        for (int i = 0; i < usuarios.length - 1; i++) {
            String[] credenciales = usuarios[i].split(",");
            if (credenciales.length == 2 && credenciales[0].equalsIgnoreCase(user) && credenciales[1].equals(pwd)) {
                JOptionPane.showMessageDialog(null, "Bienvenido " + user, "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                encontrado = true;
                login.setIntentos(0);
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);

        if (intentos >= 3) {
            JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }
}

