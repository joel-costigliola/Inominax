package zorglux.inominax.client;

import static java.lang.Math.random;
import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import zorglux.inominax.shared.TokenSet;

public class NameGenerator {

   private int minNumberOfTokensInName = 2;
   private int maxNumberOfTokensInName = 5;
   private List<String> tokens;

   public NameGenerator(int minNumberOfTokensInName, int maxNumberOfTokensInName, List<String> tokens) {
      super();
      this.minNumberOfTokensInName = minNumberOfTokensInName;
      this.maxNumberOfTokensInName = maxNumberOfTokensInName;
      this.tokens = tokens;
   }

   public NameGenerator() {
   }

   public String generateName() {
      long numberOfTokensInName = randomNumberOfTokens();
      String generatedName = "";
      for (int i = 0; i < numberOfTokensInName; i++) {
         generatedName += randomToken();
      }
      return uppercaseFirstLetter(generatedName);
   }

   public Set<String> generateNames(int numberOfNames) {
      Set<String> generatedNames = new TreeSet<String>();
      for (int i = 0; i < numberOfNames; i++) {
         generatedNames.add(generateName());
      }
      return generatedNames;
   }

   private String uppercaseFirstLetter(String generatedName) {
      return generatedName.substring(0, 1).toUpperCase() + generatedName.substring(1);
   }

   public void setMaxNumberOfTokensInName(int maxNumberOfTokensInName) {
      this.maxNumberOfTokensInName = maxNumberOfTokensInName;
   }

   public void setMinNumberOfTokensInName(int minNumberOfTokensInName) {
      this.minNumberOfTokensInName = minNumberOfTokensInName;
   }

   private int randomNumberOfTokens() {
      return (int) round(random() * (maxNumberOfTokensInName - minNumberOfTokensInName) + minNumberOfTokensInName);
   }

   private String randomToken() {
      int randomIndex = (int) round(random() * (tokens.size() - 1));
      return tokens.get(randomIndex);
   }

   public void useTokenSet(TokenSet tokenSet) {
      tokens = new ArrayList<String>(tokenSet.getTokens());
   }
}
