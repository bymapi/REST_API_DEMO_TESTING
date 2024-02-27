package com.example.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.security.entities.OurUser;
import com.example.security.repository.OurUserRepository;

import lombok.RequiredArgsConstructor;

// Static import
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * Segun el enfoque: Una prueba unitaria se divide en tres partes
 *
 * 1. Arrange: Setting up the data that is required for this test case
 * 2. Act: Calling a method or Unit that is being tested.
 * 3. Assert: Verify that the expected result is right or wrong.
 *
 * Segun el enfoque BDD (Behaviour Driven Development).
 * 'Given-When-Then' como lenguaje comun con BDD
 * 
 * Para definir los casos BDD para una historia de usuario
 * se deben definir bajo el patrón "Given-When-Then",
 * que se define como sigue:
 *
 * 1. given (dado) : Se especifica el escenario, las precondiciones.
 * 2. when (cuando) : Las condiciones de las acciones que se van a ejecutar.
 * 3. then (entonces) : El resultado esperado, las validaciones a realizar.
 *
 * Un ejemplo practico seria:
 *
 * Given: Dado que el usuario no ha introducido ningun dato en el formulario.
 * When: Cuando se hace click en el boton de enviar.
 * Then: Se deben de mostrar los mensajes de validación apropiados.
 *
 * "Role-Feature-Reason" como lenguaje común con BDD
 *
 * Este patrón se utiliza en BDD para ayudar a la creación de historias de
 * usuarios. Este se define como:
 *
 * As a "Como" : Se especifica el tipo de usuario.
 * I want "Deseo" : Las necesidades que tiene.
 * So that "Para que" : Las caracteristicas para cumplir el objetivo.
 *
 * Un ejemplo práctico de historia de usuario sería: Como cliente interesado,
 * deseo ponerme en contacto mediante formulario,
 * para que atiendan mis necesidades.
 *
 * Parece que BDD y TDD son la misma cosa, pero la principal diferencia entre
 * ambas esta en el alcance. TDD es una practica de desarrollo
 * (se enfoca en como escribir el codigo y como deberia trabajar ese codigo)
 * mientras que BDD es una metodologia de equipo (Se enfoca
 * en porque debes escribir ese codigo y como se deberia de comportar ese
 * codigo)
 *
 * En TDD el desarrollador escribe los tests mientras que en BDD el usuario
 * final (o PO o analista) en conjunto con los testers escriben
 * los tests (y los Devs solo generan el codigo necesario para ejecutar dichos
 * tests)
 *
 * Tambien existe ATDD (Acceptance Test Driven Development), que es mas cercana
 * a BDD ya que no es una practica,
 * sino una metodologia de trabajo, pero la diferencia esta nuevamente en el
 * alcance, a diferencia de BDD, ATDD se extiende aun
 * mas en profundizar la búsqueda de que lo que se esta haciendo no solo se hace
 * de forma correcta, sino que tambien
 * es lo correcto a hacer.
 *
 */

// Esta anotación es para probar las entidades
// Y la interfaz JpaRepository
// NO es para services, ni controllers, ni repository
@DataJpaTest
// Por defecto intentaría usar una base de datos H2DB
// Tenemos MYSQL así que hay que usar la anotación AutoCofigureTestDatabase
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class UserDaoTests {
    @Autowired
    private OurUserRepository ourUserRepository;

    private OurUser ourPermanentUser;

    @BeforeEach
    void setUp() {
        ourPermanentUser = OurUser.builder()
                .email("PermanentUser@gmail.com")
                .password("1234")
                .role("ADMIN")
                .build();
    }

    // Test para agregar un user
    @DisplayName("***ADD USER***")
    @Test
    public void testAddUser() {

        // Given:
        // A brand new user

        OurUser ourUser = OurUser.builder()
                .email("dbmapi@gmail.com")
                .password("1234")
                .role("ADMIN")
                .build();

        // When:
        // the user is saved

        OurUser ourUserGuardado = ourUserRepository.save(ourUser);

        // Then:
        // * User added must be != null
        // * User's id must be > 0

        assertThat(ourUserGuardado).isNotNull();
        assertThat(ourUserGuardado.getId()).isGreaterThan(0);

    }

    // Test Find All Users
    @DisplayName("***TEST FIND ALL USERS***")
    @Test
    public void testFindAllUsers() {

        // GIVEN:
        // the saved users

        OurUser ourUser1 = OurUser.builder()
                .email("dbmapi@gmail.com")
                .password("1234")
                .role("USER")
                .build();

        ourUserRepository.save(ourPermanentUser);
        ourUserRepository.save(ourUser1);

        // WHEN

        List<OurUser> userList = ourUserRepository.findAll();

        // THEN

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);

    }

    // Test Find User By Id
    @Test
    @DisplayName("***TEST FIND USER BY ID***")
    public void findUserById() {

        // GIVEN:

        ourUserRepository.save(ourPermanentUser);

        // WHEN:

        OurUser foundUser = ourUserRepository.findById(ourPermanentUser.getId()).get();

        // THEN:

        assertThat(foundUser.getId()).isNotEqualTo(0);

    }


    // Test Update User.
    @DisplayName("***TEST UPDATE USER***")
    @Test
    public void testUpdateUser(){

        // GIVEN

        ourUserRepository.save(ourPermanentUser);

        // WHEN

        OurUser ourSavedUser = ourUserRepository.findByEmail(ourPermanentUser.getEmail()).get();

        ourSavedUser.setEmail("mynewmail@gmail.com");
        ourSavedUser.setPassword("5678");
        ourSavedUser.setRole("USER");

        OurUser userUpdated = ourUserRepository.save(ourSavedUser);

        // THEN

        assertThat(userUpdated.getEmail()).isEqualTo("mynewmail@gmail.com");
        assertThat(userUpdated.getRole()).isEqualTo("USER");
    }

    // Test Delete User.
    @DisplayName("***TEST DELETE USER***")
    @Test
    public void testDeleteUser(){

        // GIVEN

        ourUserRepository.save(ourPermanentUser);

        // WHEN

        ourUserRepository.delete(ourPermanentUser);
        

        // THEN

    }
}
