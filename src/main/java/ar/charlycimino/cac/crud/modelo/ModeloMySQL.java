package ar.charlycimino.cac.crud.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
public class ModeloMySQL implements Modelo {
    
    private static final String GET_ALUMNOS_QUERY = "SELECT * FROM alumnos";
    private static final String GET_ALUMNO_BY_ID_QUERY = "SELECT * FROM alumnos WHERE id = ?";
    private static final String ADD_ALUMNO_QUERY = "INSERT INTO alumnos VALUES (null, ?, ?, ?, ?, ?)";
    private static final String EDIT_ALUMNO_BY_ID_QUERY = "UPDATE alumnos SET nombre=?, apellido=?, mail=?, fechaNac=?, fotoBase64=? WHERE id=?";
    private static final String REMOVE_ALUMNO_BY_ID_QUERY = "DELETE FROM alumnos WHERE id = ?";

    @Override
    public List<Alumno> getAlumnos() {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALUMNOS_QUERY);
             ResultSet rs = ps.executeQuery();) {            
            ArrayList<Alumno> alumnos = new ArrayList<>();            
            while (rs.next()) {
                alumnos.add(rsToAlumno(rs));
            }
            return alumnos;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener alumnos en la BD", ex);
        }
    }

    @Override
    public Alumno getAlumno(int id) {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALUMNO_BY_ID_QUERY); ) {
            Alumno alu = null;            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                rs.next();
                alu = rsToAlumno(rs);
            }   
            return alu;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener alumno con el ID " + id + " en la BD", ex);
        }
    }

    @Override
    public int addAlumno(Alumno alumno) {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(ADD_ALUMNO_QUERY); ) {
            int cantRegsAfectados;
            fillPreparedStatement(ps, alumno);
            cantRegsAfectados = ps.executeUpdate();
            return cantRegsAfectados;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar alumno a la BD", ex);
        }
    }

    @Override
    public int updateAlumno(Alumno alumno) {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(EDIT_ALUMNO_BY_ID_QUERY); ) {
            int cantRegsAfectados;
            fillPreparedStatement(ps, alumno);            
            ps.setInt(6, alumno.getId());
            cantRegsAfectados = ps.executeUpdate();
            return cantRegsAfectados;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al editar alumno a la BD", ex);
        }
    }

    @Override
    public int removeAlumno(int id) {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(REMOVE_ALUMNO_BY_ID_QUERY); ) {
            int cantRegsAfectados;            
            ps.setInt(1, id);
            cantRegsAfectados = ps.executeUpdate();
            return cantRegsAfectados;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al borrar alumno de la BD", ex);
        }
    }
    
    private void fillPreparedStatement(PreparedStatement ps, Alumno alu) throws SQLException {
        ps.setString(1, alu.getNombre());
        ps.setString(2, alu.getApellido());
        ps.setString(3, alu.getMail());
        ps.setString(4, alu.getFechaNacimiento());
        ps.setString(5, alu.getFoto());        
    }

    private Alumno rsToAlumno(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String mail = rs.getString("mail");
        String fechaNac = rs.getString("fechaNac");
        String fotoBase64 = rs.getString("fotoBase64");
        return new Alumno(id, nombre, apellido, mail, fechaNac, fotoBase64);
    }

}
