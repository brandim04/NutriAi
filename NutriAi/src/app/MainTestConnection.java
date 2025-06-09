package app;

import controller.AppController;
import util.ConnectionFactory; // CORREÇÃO: 'util' com 'u' minúsculo

public class MainTestConnection {

    public static void main(String[] args) {
        ConnectionFactory.getInstance().getConnection(); 

        java.awt.EventQueue.invokeLater(() -> {
            AppController.getInstance();
        });
    }
}