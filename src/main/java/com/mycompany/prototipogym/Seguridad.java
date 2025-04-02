package com.mycompany.prototipogym;

import javax.swing.JOptionPane;

public class Seguridad {
    private final Login login;

    public Seguridad(Login login) {
        this.login = login; // Usa la instancia real de Login
    }

public void validarUsuario(String[] usuarios, String user, String pwd, int intentos) {
    boolean encontrado = false;

    for (int i = 0; i < usuarios.length; i++) {
        String[] credenciales = usuarios[i].split(",");

        if (user.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Complete los datos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
            if (intentos >= 3) {
                JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
            return;
        } else if (credenciales.length >= 2 && credenciales[0].equals(user) && credenciales[1].equals(pwd)) {
            JOptionPane.showMessageDialog(null, "Bienvenido " + user, "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
            encontrado = true;
            login.setIntentos(0);  // Reiniciar intentos en la instancia de Login
            
            // Abre el menú y cierra la ventana de Login
            Menu m = new Menu();
            m.setVisible(true);
            login.dispose();
            return;
        }
    }


    // Verificar si se han alcanzado los 3 intentos
    if (intentos >= 3) {
        JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    } else {
        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


}


