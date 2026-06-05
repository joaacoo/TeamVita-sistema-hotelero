package com.teamvita.hotel.repo;
import com.teamvita.hotel.model.*;
import com.teamvita.hotel.model.habitacion.*;
import com.teamvita.hotel.model.reserva.*;
import com.teamvita.hotel.model.servicio.*;
import com.teamvita.hotel.model.fidelizacion.*;
import com.teamvita.hotel.model.facturacion.*;
import java.util.List;
import java.sql.*;

public class PagoDAO {
    public void registrarPago(Pago pago) {
        String sql = "INSERT INTO pagos (monto, fecha) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // TODO: Setear par�metros del statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
