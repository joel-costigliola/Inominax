package zorglux.inominax.client;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import zorglux.inominax.shared.TokenSet;

public class NameGeneratorTest {

   private NameGenerator nameGenerator;


   @Before
   public void setup() {
      // basic TokenSet
      TokenSet tokenSet = new TokenSet();
      tokenSet.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin");
      nameGenerator = new NameGenerator();
      nameGenerator.useTokenSet(tokenSet);
      nameGenerator.setMinNumberOfTokensInName(2);
      nameGenerator.setMaxNumberOfTokensInName(3);
   }

   @Test
   public void generateName() {
      for (int i = 0; i < 10; i++) {
         System.out.println("name : " + nameGenerator.generateName());
      }
   }

   @Test
   public void generateNameStartingWith() {
      nameGenerator.generatedNameMustStartsWith("toto");
      for (int i = 0; i < 100; i++) {
         assertThat(nameGenerator.generateName()).startsWith("Toto");
      }
   }

   @Test
   public void generateNameEndingWith() {
      nameGenerator.generatedNameMustEndWith("oto");
      for (int i = 0; i < 100; i++) {
         assertThat(nameGenerator.generateName()).endsWith("oto");
      }
   }

   @Test
   public void generateNameContainingString() {
      nameGenerator.generatedNameMustContain("ootoo");
      for (int i = 0; i < 100; i++) {
         assertThat(nameGenerator.generateName()).containsIgnoringCase("ootoo");
      }
   }

   @Test
   public void generateNameMultipleConstraints() {
      String startOfName = "aa";
      nameGenerator.generatedNameMustStartsWith(startOfName);
      nameGenerator.generatedNameMustContain("mm");
      String endOfName = "zz";
      nameGenerator.generatedNameMustEndWith(endOfName);
      nameGenerator.setMaxNumberOfTokensInName(2);
      for (int i = 0; i < 100; i++) {
         String generatedName = nameGenerator.generateName();
         assertThat(generatedName).startsWith("Aa");
         if (generatedName.length() > startOfName.length() + endOfName.length()) {
            assertThat(generatedName).containsIgnoringCase("mm");
         }
         assertThat(generatedName).endsWith(endOfName);
      }
   }
}
