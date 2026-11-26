package it.chalmers.commit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/** Enforces the package rules described in ARCHITECTURE.md. */
class ArchitectureTests {

  private static final String ROOT = "it.chalmers.commit";
  private static final String MODEL = ROOT + ".model..";
  private static final String VIEW = ROOT + ".view..";
  private static final String CONTROLLER = ROOT + ".controller..";

  private static final JavaClasses CLASSES =
      new ClassFileImporter()
          .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages(ROOT);

  /** The model can use any library, but must not know about how it is shown or requested. */
  @Test
  void modelDoesNotDependOnViewOrController() {
    noClasses()
        .that()
        .resideInAPackage(MODEL)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(VIEW, CONTROLLER)
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  @Test
  void viewDoesNotDependOnController() {
    noClasses()
        .that()
        .resideInAPackage(VIEW)
        .should()
        .dependOnClassesThat()
        .resideInAPackage(CONTROLLER)
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  /** Database code belongs in the model, so controllers and views never touch Hibernate or JDBC. */
  @Test
  void onlyTheModelUsesTheDatabase() {
    noClasses()
        .that()
        .resideInAnyPackage(VIEW, CONTROLLER)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "org.hibernate..", "jakarta.persistence..", "java.sql..", "org.flywaydb..")
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  @Test
  void packagesAreFreeOfCycles() {
    slices()
        .matching(ROOT + ".(*)..")
        .should()
        .beFreeOfCycles()
        .allowEmptyShould(true)
        .check(CLASSES);
  }

  @Test
  void modelHasNoPublicSetters() {
    noMethods()
        .that()
        .areDeclaredInClassesThat()
        .resideInAPackage(MODEL)
        .and()
        .arePublic()
        .should()
        .haveNameMatching("set[A-Z].*")
        .allowEmptyShould(true)
        .check(CLASSES);
  }
}
