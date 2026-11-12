package it.chalmers.commit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
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

  private static final JavaClasses CLASSES =
      new ClassFileImporter()
          .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages(ROOT);

  /**
   * Covers both "no framework code in the model" and "dependencies point inwards": the model may
   * only use the JDK and itself, so it cannot reach Spring, JPA or any of our other packages.
   */
  @Test
  void modelDependsOnlyOnTheJdkAndItself() {
    classes()
        .that()
        .resideInAPackage(MODEL)
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(MODEL, "java..")
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
