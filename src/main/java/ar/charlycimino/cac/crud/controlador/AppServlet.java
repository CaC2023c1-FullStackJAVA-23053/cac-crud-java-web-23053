package ar.charlycimino.cac.crud.controlador;

import ar.charlycimino.cac.crud.modelo.Alumno;
import ar.charlycimino.cac.crud.modelo.Modelo;
import ar.charlycimino.cac.crud.modelo.ModeloHC;
import ar.charlycimino.cac.crud.modelo.ModeloMySQL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/app"})
public class AppServlet extends HttpServlet {

    private Modelo model;
    private final String URI_LIST = "WEB-INF/pages/alumnos/listadoAlumnos.jsp";
    private final String URI_EDIT = "WEB-INF/pages/alumnos/editarAlumno.jsp";

    @Override
    public void init() throws ServletException {
        model = new ModeloMySQL();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        accion = accion == null ? "" : accion;
        switch (accion) {
            case "editar":
                int id = Integer.parseInt(req.getParameter("id"));
                Alumno alu = model.getAlumno(id);
                req.setAttribute("alumnoAEditar", alu);
                req.setAttribute("yaTieneFoto", !alu.getFoto().contains("no-face"));
                req.getRequestDispatcher(URI_EDIT).forward(req, resp);
                break;
            default:
                req.setAttribute("listaAlumnos", model.getAlumnos());
                req.getRequestDispatcher(URI_LIST).forward(req, resp);
        }

    }

    /* TODO: Desarrollar Servlet */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Alumno alu;
        String accion = req.getParameter("accion");
        accion = accion == null ? "" : accion;
        switch (accion) {
            case "insert":
                alu = getAlumnoSegunParams(req);
                model.addAlumno(alu);
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("id"));
                alu = getAlumnoSegunParams(req);
                alu.setId(id);
                model.updateAlumno(alu);
                break;
        }
        resp.sendRedirect(getServletContext().getContextPath() + "/app");
    }

    private Alumno getAlumnoSegunParams(HttpServletRequest req) {
        Alumno alu = new Alumno();
        alu.setNombre(req.getParameter("nombre"));
        alu.setApellido(req.getParameter("apellido"));
        alu.setMail(req.getParameter("mail"));
        alu.setFechaNacimiento(req.getParameter("fechaNac"));
        alu.setFoto(req.getParameter("fotoBase64"));
        return alu;
    }
}
