package com.mycompany.prototipogym;

import javax.swing.JOptionPane;

public class Seguridad {
    private final Login login;

    public Seguridad(Login login) {
        this.login = login; // Usa la instancia real de Login
    }

public void validarUsuario(String[] usuarios, String user, String pwd, int intentos) {
    if (user.isEmpty() || pwd.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Complete los datos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
        if (intentos >= 3) {
            JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        return;
    }

    for (int i = 0; i < usuarios.length; i++) {
        String[] credenciales = usuarios[i].split(",");
        if (credenciales.length >= 3 && credenciales[0].equals(user) && credenciales[1].equals(pwd)) {
            try {
                int nivelUsuario = Integer.parseInt(credenciales[2]);
                JOptionPane.showMessageDialog(null, "Bienvenido " + user, "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                login.setIntentos(0);
                Menu m = new Menu(nivelUsuario);
                m.setVisible(true);
                login.dispose();
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Nivel de usuario inválido para este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // Si no se encontró el usuario
    if (intentos >= 3) {
        JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } else {
        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



}


