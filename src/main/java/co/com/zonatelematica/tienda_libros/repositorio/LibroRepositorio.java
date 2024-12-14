package co.com.zonatelematica.tienda_libros.repositorio;

import co.com.zonatelematica.tienda_libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro , Integer> {
}
