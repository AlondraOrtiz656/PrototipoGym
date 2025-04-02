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
        // Divide la línea en partes separadas por coma
        String[] credenciales = usuarios[i].split(",");
        
        // Verifica si la línea tiene al menos dos valores (usuario y contraseña)
        if (user.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complete los datos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
                                
                return;
            }else if (credenciales.length >= 2 && credenciales[0].equals(user) && credenciales[1].equals(pwd)) {
            JOptionPane.showMessageDialog(null, "Bienvenido " + user, "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
            encontrado = true;
            login.setIntentos(0);

            // Abre el nuevo menú y cierra el Login actual
            Menu m = new Menu();
            m.setVisible(true);
            login.dispose(); // Cierra la ventana del Login

            return;
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Intento " + intentos + "/3.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    }}

    // Si no se encontró el usuario y la contraseña
    

    // Si ya se han alcanzado 3 intentos fallidos, cierra la aplicación
    if (intentos >= 3) {
        JOptionPane.showMessageDialog(null, "Tres intentos fallidos. La aplicación se cerrará.", "Error", JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    }
}

}


