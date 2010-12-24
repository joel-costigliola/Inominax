package zorglux.inominax.client;

import org.junit.Before;
import org.junit.Test;

import zorglux.inominax.shared.TokenSet;

public class NameGeneratorTest {

   private NameGenerator nameGenerator;


   @Before
   public void setup() {
      nameGenerator = new NameGenerator();
      nameGenerator.setMinNumberOfTokensInName(2);
      nameGenerator.setMaxNumberOfTokensInName(3);
      // basic TokenSet
      TokenSet tokenSet = new TokenSet();
      tokenSet.addToken("lae", "il", "mar", "sel", "fel", "fin", "iel", "gad", "del", "sin", "rin");
      nameGenerator.useTokenSet(tokenSet);
   }

   @Test
   public void generateName() {
      for (int i = 0; i < 10; i++) {
         System.out.println("name : " + nameGenerator.generateName());
      }
   }
}
