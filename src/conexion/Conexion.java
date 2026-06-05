package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // ⚠️ REPLICACIÓN MASTER-MASTER: 
    // Si corres el sistema en TU compu, déjalo en "localhost". 
    // Si vas a apuntar a la máquina de tu compa, cambia "localhost" por su IP (ej: 192.168.1.75)
    private static final String URL = "jdbc:mariadb://localhost:3307/liga_futbol?useSSL=false&serverTimezone=UTC";
    
    // 🔑 CREDENCIALES DE REPLICACIÓN DE MAÑANA
    // Compu A (Tú) usa: "replica1" | Compu B (Tu compañero) usa: "replica2"
    private static final String USER = "replica1"; 
    private static final String PASS = "1234"; 

    // Cambiado a 'conectar' y estático para que funcione directo con tu ventana
    public static Connection conectar() {
        Connection con = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("⚡ ¡Conexión Exitosa con usuario de Réplica! Datos sincronizados.");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: ¡Falta el Driver JDBC en Libraries!");
        } catch (SQLException e) {
            System.out.println("❌ Error de acceso en MariaDB: " + e.getMessage());
        }
        return con;
    }
}